/**
 * Copyright (c) 2013 dotNetRDF Project (dotnetrdf-developer@lists.sf.net)
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

package org.dotnetrdf.wiki.checker.pages;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.jena.iri.IRI;
import org.apache.jena.iri.IRIFactory;
import org.apache.jena.iri.Violation;
import org.dotnetrdf.wiki.checker.issues.Issue;

/**
 * The page checker is responsible for carrying out actual checks on pages
 * 
 * @author rvesse
 * 
 */
public class PageChecker {

    private PageTracker tracker;
    private String baseDir;
    private Map<String, Boolean> externalUris = new HashMap<String, Boolean>();
    private Map<String, Integer> httpStatuses = new HashMap<String, Integer>();
    private HttpClient httpClient = new DefaultHttpClient();

    /**
     * Creates a new page checker
     * 
     * @param tracker
     *            Page Tracker
     * @param dir
     *            Base Directory
     */
    public PageChecker(PageTracker tracker, String dir) {
        this.tracker = tracker;
        this.baseDir = dir;
        if (!this.baseDir.endsWith(File.separator))
            this.baseDir += File.separator;
    }

    /**
     * Gets the page tracker
     * 
     * @return Page Tracker
     */
    public PageTracker getPageTracker() {
        return this.tracker;
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
     * Run the page checker
     * 
     * @param quiet
     *            Quite Mode
     * @throws IOException
     */
    public void run(boolean quiet) throws IOException {
        // First scan for pages
        tracker.scan(this.baseDir, quiet);

        // Prepare regex
        Pattern linkRegex = Pattern.compile("\\[\\[[^\\]]+\\]\\]");
        if (!quiet)
            System.out.println("Using Regex " + linkRegex.toString() + " to search for links");

        // Then start checking pages
        Iterator<Page> pages = tracker.getPages();
        while (pages.hasNext()) {
            Page page = pages.next();

            if (page.getChecked())
                continue;

            // Firstly we need to read in the page text
            String text = this.getText(page);
            String[] lineData = text.split("\n");

            // Then we can start checking it
            // 1 - Warn about short pages
            int lines = lineData.length;
            if (lines <= 5) {
                page.addIssue(new Issue("Page has only " + lines + " Lines of content, this page may be an incomplete/stub page"));
            }
            
            // 2 - Warn for <<toc>> macro used on non-top level page
            if (!page.isTopLevel() && text.contains("<<toc")) {
                page.addIssue(new Issue("Non-top level page uses the TOC macro, if this is used to list directory contents the generated links will be incorrect, see BitBucket Issue #2224", false));
            }

            // 3 - Detect Links
            Matcher linkMatcher = linkRegex.matcher(text);
            while (linkMatcher.find()) {
                MatchResult linkMatch = linkMatcher.toMatchResult();

                // Find position
                int line = this.calculateLine(lineData, linkMatch.start());
                int col = this.calculateColumn(lineData, linkMatch.start());

                // Find link information and track as a link
                String linkText = linkMatch.group().toString();
                linkText = linkText.substring(2, linkText.length() - 2);
                if (linkText.contains("|")) {
                    String linkPath = linkText.substring(0, linkText.lastIndexOf('|'));
                    String linkFriendlyText = linkText.substring(linkText.lastIndexOf('|') + 1);

                    page.addOutboundLink(new Link(linkPath, linkFriendlyText, line, col));
                } else {
                    page.addOutboundLink(new Link(linkText, line, col));
                }
            }

            // 4 - Check Links
            Iterator<Link> links = page.getOutboundLinks();
            while (links.hasNext()) {
                Link link = links.next();
                
                // Issue warnings for links without friendly text
                if (!link.hasFriendlyText() && (!link.isWikiLink() || link.getPath().contains("/"))) {
                    page.addIssue(new Issue("Link does not have friendly text - " + link.toString(), false));
                }

                // Determine how to validate the link
                if (link.isWikiLink()) {
                    // Wiki Link Validation
                    String linkPath = link.getPath();
                    Page target = this.tracker.getPage(linkPath);
                    if (target == null) {
                        // Mark as Broken
                        page.addIssue(new Issue("Broken Wiki Link - " + link.toString(), true));
                    } else {
                        // Mark as Inbound Link on target Page
                        target.addInboundLink(link);
                    }
                } else if (link.isMailLink()) {
                    // Warn on mail links
                    page.addIssue(new Issue("Email Links expose email address " + link.getPath().substring(8) + " publicly", false));
                } else {
                    // External Link Validation
                    if (this.externalUris.get(link.getPath()) != null) {
                        // Already validated, report broken link if necessary
                        if (this.externalUris.get(link.getPath()) != true) {
                            page.addIssue(new Issue("Broken External Link (HTTP Status " + this.httpStatuses.get(link.getPath())
                                    + ") - " + link.toString(), true));
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
                                    page.addIssue(new Issue("External Link " + link.toString() + " violates the URI specification - " + violation.getLongMessage(), true));
                                } else {
                                    page.addIssue(new Issue("External Link " + link.toString() + " has a warning against the URI specification - " + violation.getShortMessage(), false));
                                }
                            }
                            
                            // Skip further validation if has errors
                            if (iriErrors) continue;
                        }
                        
                        // Try a HTTP HEAD request first then fallback to a HTTP GET
                        // when necessary
                        HttpHead head = null;
                        HttpGet get = null;
                        try {
                            head = new HttpHead(link.getPath());
                            head.setHeader("Accept", "*/*");
                            if (!quiet)
                                System.out.println("Validating External Link " + link.getPath());
                            HttpResponse resp = this.httpClient.execute(head);

                            if (resp.getStatusLine().getStatusCode() >= 200 && resp.getStatusLine().getStatusCode() < 400) {
                                // Valid
                                this.externalUris.put(link.getPath(), true);
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
                                } else {
                                    // Invalid
                                    this.externalUris.put(link.getPath(), false);
                                    this.httpStatuses.put(link.getPath(), resp.getStatusLine().getStatusCode());
                                    page.addIssue(new Issue("Broken External Link (HTTP Status "
                                            + resp.getStatusLine().getStatusCode() + ") - " + link.toString(), true));
                                }
                            }

                        } catch (IllegalArgumentException e) {
                            this.externalUris.put(link.getPath(), false);
                            page.addIssue(new Issue("Invalid External Link URI - " + link.toString(), true));
                        } catch (UnknownHostException e) {
                            this.externalUris.put(link.getPath(), false);
                            page.addIssue(new Issue("Invalid External Link URI - " + link.toString(), true));
                        } catch (Throwable e) {
                            this.externalUris.put(link.getPath(), false);
                            page.addIssue(new Issue("Unexpected Error with External Link URI - " + link.toString(), true));
                            e.printStackTrace(System.out);
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
            page.setChecked(true);
        }
        
        // After initial check of pages need to do a subsequent check
        // for things that require information for all pages
        
        // Check for orphaned/poorly linked pages
        pages = this.tracker.getPages();
        while (pages.hasNext()) {
            Page page = pages.next();
            if (page.getInboundLinkCount() == 0) {
                page.addIssue(new Issue("Page is isolated,  no inbound links to this page were found", true));
            } else if (page.getInboundLinkCount() == 1) {
                page.addIssue(new Issue("Page has only a single inbound link", false));
            }
        }
    }

    /**
     * Gets the text of a page
     * 
     * @param page
     *            Page
     * @return Text
     * @throws IOException
     */
    private String getText(Page page) throws IOException {
        String pageFile = this.baseDir + page.getPath() + ".wiki";
        FileReader reader = new FileReader(pageFile);

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

    private int calculateLine(String[] lines, int index) {
        int line = 0;
        int count = 0;
        while (count < index) {
            int len = lines[line].length();
            if (count + len >= index)
                return line + 1;
            count += len + 1; // The +1 is for the \n
            line++;
        }
        return line + 1;
    }

    private int calculateColumn(String[] lines, int index) {
        int line = 0;
        int count = 0;
        while (count < index) {
            int len = lines[line].length();
            if (count + len >= index)
                return index - count;
            count += len + 1; // The +1 is for the \n
        }
        return 1;
    }
}
