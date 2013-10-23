/**
 * Copyright (c) 2013 dotNetRDF Project (dotnetrdf-develop@lists.sf.net)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */

package org.dotnetrdf.wiki.checker;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.jena.iri.IRI;
import org.apache.jena.iri.IRIFactory;
import org.apache.jena.iri.Violation;
import org.dotnetrdf.wiki.checker.data.CheckedWiki;
import org.dotnetrdf.wiki.checker.data.documents.CheckedDocument;
import org.dotnetrdf.wiki.checker.issues.Issue;
import org.dotnetrdf.wiki.checker.parser.CheckedWikiScanner;
import org.dotnetrdf.wiki.data.documents.Document;
import org.dotnetrdf.wiki.data.documents.formats.DataFormat;
import org.dotnetrdf.wiki.data.links.BasicLink;
import org.dotnetrdf.wiki.data.links.Link;
import org.dotnetrdf.wiki.parser.links.LinkDetector;
import org.dotnetrdf.wiki.parser.links.LinkDetectorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The document checker is responsible for carrying out actual checks on
 * documents
 * 
 * @author rvesse
 * @param <TLink>
 *            Checked link type
 * @param <TDoc>
 *            Checked document type
 * 
 */
public class DocumentChecker<TLink extends Link, TDoc extends CheckedDocument<TLink>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentChecker.class);

    private CheckedWiki<TLink, TDoc> wiki;
    private String baseDir;
    private Map<String, Boolean> externalUris = new HashMap<String, Boolean>();
    private Map<String, Integer> httpStatuses = new HashMap<String, Integer>();
    private HttpClient httpClient = new DefaultHttpClient();

    /**
     * Creates a new document checker
     * 
     * @param wiki
     *            Wiki which is presumed to have already been populated by an
     *            appropriate {@link CheckedWikiScanner}
     * @param dir
     *            Base Directory
     */
    public DocumentChecker(CheckedWiki<TLink, TDoc> wiki, String dir) {
        this.wiki = wiki;
        this.baseDir = dir;
        if (!this.baseDir.endsWith(File.separator))
            this.baseDir += File.separator;
    }

    /**
     * Gets the wiki
     * 
     * @return Wiki
     */
    public CheckedWiki<TLink, TDoc> getWiki() {
        return this.wiki;
    }

    /**
     * Gets the base directory
     * 
     * @return Base Directory
     */
    public String getBaseDirectory() {
        return this.baseDir;
    }

    /**
     * Run the document checker
     * 
     * @throws IOException
     */
    public void run() throws IOException {
        LOGGER.info("Checking for per-document issues");

        // Start checking documents
        Iterator<TDoc> documents = this.wiki.getDocuments();
        while (documents.hasNext()) {
            TDoc document = documents.next();

            if (document.hasBeenChecked())
                continue;

            LOGGER.debug("Checking document " + document.getPath() + document.getFilename());

            // Firstly we need to read in the document text
            String text = this.getText(document);
            String[] lineData = text == null ? new String[0] : text.split("\n");

            // Then we can start checking it
            // 1 - Warn about short documents
            int lines = lineData.length;
            if (document.getFormat().isText() && lines <= 5) {
                document.addIssue(new Issue("Page has only " + lines
                        + " Lines of content, this document may be an incomplete/stub document"));
            }

            // 2 - Warn for <<toc>> macro used on non-top level document
            if (!document.isTopLevel() && document.getFormat().getDataFormat().equals(DataFormat.CREOLE)
                    && text.contains("<<toc")) {
                document.addIssue(new Issue(
                        "Non-top level document uses the TOC macro, if this is used to list directory contents the generated links will be incorrect, see BitBucket Issue #2224",
                        false));
            }

            // 3 - Detect Links
            LinkDetector detector = LinkDetectorRegistry.getLinkDetector(document.getFormat());
            if (detector == null) {
                // Issue a warning when no link detector available
                document.addIssue(new Issue("Page has format " + document.getFormat().toString()
                        + " which does not have a link detector, no links can be detected for this document", false));
            } else {
                // Detect links
                detector.findLinks(document, text);
            }

            // 4 - Check Links
            Iterator<TLink> links = document.getOutboundLinks();
            while (links.hasNext()) {
                TLink link = links.next();

                // Issue warnings for links without friendly text
                if (!link.hasFriendlyText() && (!link.isWikiLink() || link.getPath().contains("/"))) {
                    document.addIssue(new Issue("Link does not have friendly text - " + link.toString(), false));
                    LOGGER.warn("Link does not have friendly text - " + link.toString());
                }

                // Determine how to validate the link
                if (link.isWikiLink()) {
                    // Wiki Link Validation
                    String linkPath = link.getPath();
                    TDoc target = this.wiki.getDocument(linkPath);
                    if (target == null) {
                        // Mark as Broken
                        document.addIssue(new Issue("Broken Wiki Link - " + link.toString(), true));
                        LOGGER.error("Broken wiki link " + link.toString());
                    } else {
                        // Mark as Inbound Link on target Page
                        // Don't count self referential links in inbound links
                        if (!document.getPath().equals(link.getPath())) {
                            target.addInboundLink(link);
                        }
                    }
                } else if (link.isMailLink()) {
                    // Warn on mail links
                    document.addIssue(new Issue("Email Links expose email address " + link.getPath().substring(7) + " publicly",
                            false));
                    LOGGER.warn("Email link exposes email address publicly - " + link.getPath().substring(7));
                } else {
                    // External Link Validation

                    // TODO Externalize this code

                    // TODO Strip off #fragment and ?querystrings to speed this
                    // stage up

                    if (this.externalUris.get(link.getPath()) != null) {
                        // Already validated, report broken link if necessary
                        if (this.externalUris.get(link.getPath()) != true) {
                            document.addIssue(new Issue("Broken External Link (HTTP Status "
                                    + this.httpStatuses.get(link.getPath()) + ") - " + link.toString(), true));
                        }
                    } else {
                        // Validate the external link

                        // Firstly look for obvious issues with the IRI
                        IRI iri = IRIFactory.uriImplementation().create(link.getPath());
                        if (iri.hasViolation(true)) {
                            Iterator<Violation> violations = iri.violations(true);
                            boolean iriErrors = false;
                            while (violations.hasNext()) {
                                Violation violation = violations.next();
                                if (violation.isError()) {
                                    iriErrors = true;
                                    document.addIssue(new Issue("External Link " + link.toString()
                                            + " violates the IRI specification - " + violation.getLongMessage(), true));
                                    LOGGER.error("Link " + link.toString() + " is not a valid IRI - "
                                            + violation.getLongMessage());
                                } else {
                                    document.addIssue(new Issue("External Link " + link.toString()
                                            + " has a warning against the IRI specification - " + violation.getShortMessage(),
                                            false));
                                    LOGGER.warn("Link " + link.toString() + " has an IRI warning - " + violation.getLongMessage());
                                }
                            }

                            // Skip further validation if has errors
                            if (iriErrors)
                                continue;
                        }

                        // Try a HTTP HEAD request first then fall back to a
                        // HTTP GET when necessary
                        HttpHead head = null;
                        HttpGet get = null;
                        try {
                            head = new HttpHead(link.getPath());
                            head.setHeader("Accept", "*/*");
                            LOGGER.debug("Validating External Link " + link.getPath());
                            HttpResponse resp = this.httpClient.execute(head);

                            if (resp.getStatusLine().getStatusCode() >= 200 && resp.getStatusLine().getStatusCode() < 400) {
                                // Valid
                                this.externalUris.put(link.getPath(), true);
                                LOGGER.debug("External Link " + link.getPath() + " is valid");
                            } else {
                                // Try a GET instead, in case the server doesn't
                                // support HEAD nicely
                                head.releaseConnection();
                                head.reset();
                                head = null;
                                get = new HttpGet(link.getPath());
                                get.setHeader("Accept", "*/*");
                                resp = this.httpClient.execute(get);

                                if (resp.getStatusLine().getStatusCode() >= 200 && resp.getStatusLine().getStatusCode() < 400) {
                                    // Valid
                                    this.externalUris.put(link.getPath(), true);
                                    LOGGER.debug("External Link " + link.getPath() + " is valid");
                                } else {
                                    // Invalid
                                    this.externalUris.put(link.getPath(), false);
                                    this.httpStatuses.put(link.getPath(), resp.getStatusLine().getStatusCode());
                                    document.addIssue(new Issue("Broken External Link (HTTP Status "
                                            + resp.getStatusLine().getStatusCode() + ") - " + link.toString(), true));
                                    LOGGER.error("External Link " + link.getPath() + " is invalid");
                                }
                            }

                        } catch (IllegalArgumentException e) {
                            this.externalUris.put(link.getPath(), false);
                            document.addIssue(new Issue("Invalid External Link URI - " + link.toString(), true));
                            LOGGER.debug("External Link " + link.getPath() + " is invalid", e);
                        } catch (UnknownHostException e) {
                            this.externalUris.put(link.getPath(), false);
                            document.addIssue(new Issue("Invalid External Link URI - " + link.toString(), true));
                            LOGGER.debug("External Link " + link.getPath() + " is invalid", e);
                        } catch (Throwable e) {
                            this.externalUris.put(link.getPath(), false);
                            document.addIssue(new Issue("Unexpected Error with External Link URI - " + link.toString(), true));
                            LOGGER.debug("External Link " + link.getPath() + " is invalid", e);
                        } finally {
                            if (head != null) {
                                head.releaseConnection();
                                head.reset();
                            }
                            if (get != null) {
                                get.releaseConnection();
                                get.reset();
                            }
                        }
                    }
                }
            }

            // Finally mark as checked
            document.setChecked(true);

            LOGGER.debug("Finished checking document " + document.getPath() + document.getFilename());
        }
        LOGGER.info("Finished checking for per-document wiki issues");

        // After initial check of documents need to do a subsequent check
        // for things that require information for all documents to have been
        // gathered
        LOGGER.info("Checking for global wiki issues");

        // Check for orphaned/poorly linked documents
        documents = this.wiki.getDocuments();
        while (documents.hasNext()) {
            TDoc document = documents.next();
            if (document.getFormat().isWiki()) {
                if (document.getInboundLinkCount() == 0) {
                    document.addIssue(new Issue("Page is isolated,  no inbound links to this document were found", true));
                } else if (document.getInboundLinkCount() == 1) {
                    document.addIssue(new Issue("Page has only a single inbound link", false));
                }
            }
        }

        LOGGER.info("Finished checking for global wiki issues");
    }

    /**
     * Gets the text of a document
     * 
     * @param document
     *            Page
     * @return Text of the document, null for non-text formats
     * @throws IOException
     */
    private String getText(Document<TLink> document) throws IOException {
        if (!document.getFormat().isText())
            return null;

        String documentFile = this.baseDir + document.getFilename();
        FileReader reader = new FileReader(documentFile);

        StringWriter sw = new StringWriter(8192);
        char buff[] = new char[8192];
        for (;;) {
            int l = reader.read(buff);
            if (l < 0)
                break;
            sw.write(buff, 0, l);
        }
        reader.close();
        sw.close();
        return sw.toString();

    }
}
