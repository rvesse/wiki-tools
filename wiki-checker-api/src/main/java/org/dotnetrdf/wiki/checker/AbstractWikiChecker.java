/*
Copyright (c) 2013 dotNetRDF Project (dotnetrdf-develop@lists.sf.net)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is furnished
to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 */

package org.dotnetrdf.wiki.checker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dotnetrdf.wiki.checker.checks.DocumentCheck;
import org.dotnetrdf.wiki.checker.checks.LinkCheck;
import org.dotnetrdf.wiki.checker.data.CheckedWiki;
import org.dotnetrdf.wiki.checker.data.documents.CheckedDocument;
import org.dotnetrdf.wiki.checker.data.links.CheckedLink;
import org.dotnetrdf.wiki.checker.issues.Issue;
import org.dotnetrdf.wiki.checker.parser.CheckedWikiScanner;
import org.dotnetrdf.wiki.parser.links.LinkDetector;
import org.dotnetrdf.wiki.parser.links.LinkDetectorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract implementation of a wiki checker.
 * <p>
 * The implementation first detects all links within each document and applies
 * registered link checks to the detected links. After all documents have had
 * their links detected and check it will then go ahead and run the registered
 * document checks. This allows for document checks which rely on the whole of
 * the wiki link structure having been validated first to run.
 * </p>
 * 
 * @author rvesse
 * 
 * @param <TLink>
 *            Checked link type
 * @param <TDoc>
 *            Checked document type
 */
public class AbstractWikiChecker<TLink extends CheckedLink, TDoc extends CheckedDocument<TLink>> implements
        WikiChecker<TLink, TDoc> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWikiChecker.class);
    private CheckedWiki<TLink, TDoc> wiki;
    private String baseDir;
    private List<DocumentCheck> documentChecks = new ArrayList<DocumentCheck>();
    private List<LinkCheck> linkChecks = new ArrayList<LinkCheck>();

    /**
     * Creates a new document checker
     * 
     * @param wiki
     *            Wiki which is presumed to have already been populated by an
     *            appropriate {@link CheckedWikiScanner}
     * @param dir
     *            Base Directory
     */
    public AbstractWikiChecker(CheckedWiki<TLink, TDoc> wiki, String dir) {
        this.wiki = wiki;
        this.baseDir = dir;
        if (!this.baseDir.endsWith(File.separator))
            this.baseDir += File.separator;
    }

    @Override
    public final CheckedWiki<TLink, TDoc> getWiki() {
        return this.wiki;
    }

    @Override
    public final String getBaseDirectory() {
        return this.baseDir;
    }

    @Override
    public final void run() throws IOException {
        this.run(false);
    }

    @Override
    public final void run(boolean recheck) throws IOException {
        Iterator<TDoc> documents = this.wiki.getDocuments();
        
        // For each document detect and check links
        LOGGER.info("Checking links in documents");
        while (documents.hasNext()) {
            TDoc document = documents.next();

            if (document.hasBeenChecked() && !recheck)
                continue;

            if (document.hasBeenChecked() && recheck) {
                // TODO Reset links and issues
            }

            LOGGER.debug("Checking links in document " + document.getPath());

            // Firstly we need to read in the document text
            String text = document.getText();
            
            // Detect Links
            LinkDetector detector = LinkDetectorRegistry.getLinkDetector(document.getFormat());
            if (detector == null) {
                // Issue a warning when no link detector available
                document.addIssue(new Issue("Page has format " + document.getFormat().toString()
                        + " which does not have a link detector, no links can be detected for this document", false));
            } else {
                // Detect links
                detector.findLinks(document, text);
            }

            // Check Links
            
            // Apply link checks
            Iterator<TLink> links = document.getOutboundLinks();
            while (links.hasNext()) {
                TLink link = links.next();

                Iterator<LinkCheck> linkChecks = this.getLinkChecks();
                while (linkChecks.hasNext()) {
                    LinkCheck check = linkChecks.next();
                    check.check(document, link, this.wiki);
                }
            }

            LOGGER.debug("Finished checking links in document " + document.getPath());
        }
        LOGGER.info("Finished checking links in documents");

        // Now carry out document checks
        documents = this.wiki.getDocuments();
        LOGGER.info("Checking documents");
        while (documents.hasNext()) {
            TDoc document = documents.next();
            
            LOGGER.debug("Checking document " + document.getPath());
            
            Iterator<DocumentCheck> docChecks = this.getDocumentChecks();
            while (docChecks.hasNext()) {
                DocumentCheck check = docChecks.next();
                
                check.check(document, document.getText(), this.wiki);
            }
            
            // Finally mark as checked
            document.setChecked(true);
        }
        LOGGER.info("Finished checking documents");
        
        // Finally carry out wiki checks
    }

    @Override
    public final void addDocumentCheck(DocumentCheck check) {
        if (check != null)
            this.documentChecks.add(check);
    }

    @Override
    public Iterator<DocumentCheck> getDocumentChecks() {
        return this.documentChecks.iterator();
    }

    @Override
    public void addLinkCheck(LinkCheck check) {
        if (check != null)
            this.linkChecks.add(check);
    }

    @Override
    public Iterator<LinkCheck> getLinkChecks() {
        return this.linkChecks.iterator();
    }

}