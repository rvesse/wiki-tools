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
import java.util.Iterator;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * @throws IOException 
     */
    public void run() throws IOException {
        // First scan for pages
        tracker.scan(this.baseDir);
        
        // Prepare regex
        Pattern linkRegex = Pattern.compile("\\[\\[[^\\]]+\\]\\]");
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
            if (lines <= 10) {
                page.addIssue(new Issue("Page has only " + lines + " Lines of content, this page may be an incomplete/stub page"));
            }
            
            // 2 - Detect Links
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
                    
                    page.addLink(new Link(linkPath, linkFriendlyText, line, col));
                } else {
                    page.addLink(new Link(linkText, line, col));
                }
            }
            
            // 3 - Check Links
            Iterator<Link> links = page.getLinks();
            while (links.hasNext()) {
                Link link = links.next();
                
                // TODO: Add remote link validation
                if (!link.isWikiLink()) continue;
                
                String linkPath = link.getPath();
                if (this.tracker.hasPage(linkPath)) {
                    page.addIssue(new Issue("Broken Link - " + link.toString(), true));
                }
            }
            
            // Finally mark as checked
            page.setChecked(true);
        }
    }

    /**
     * Gets the text of a page
     * @param page Page
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
            if (count + len >= index) return line + 1;
            count += len + 1; // The +1 is for the \n
        }
        return line + 1;
    }
    
    private int calculateColumn(String[] lines, int index) {
        int line = 0;
        int count = 0;
        while (count < index) {
            int len = lines[line].length();
            if (count + len >= index) return index - count;
            count += len + 1; // The +1 is for the \n
        }
        return 1;
    }
}
