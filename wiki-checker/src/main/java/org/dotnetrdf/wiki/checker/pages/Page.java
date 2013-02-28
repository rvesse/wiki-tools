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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dotnetrdf.wiki.checker.issues.Issue;
import org.dotnetrdf.wiki.checker.links.Link;
import org.dotnetrdf.wiki.checker.pages.formats.Format;

/**
 * Represents a page in the wiki
 * @author rvesse
 *
 */
public class Page {

    private String path;
    private String filename;
    private boolean checked = false;
    private List<Issue> issues = new ArrayList<Issue>();
    private Set<Link> links = new HashSet<Link>();
    private Set<Link> inboundLinks = new HashSet<Link>();
    private Format format;
    
    /**
     * Creates a page
     * @param path Path to the page
     * @param format Format of the page
     */
    public Page(String path, Format format) {
        this.path = path;
        this.filename = path.replace('/', File.separatorChar);
        this.format = format;
        
        // Strip extension if present from path
        if (this.path.contains(".")) {
            this.path = this.path.substring(0, this.path.lastIndexOf('.'));
        }
    }
        
    /**
     * Gets the path of the page
     * @return Path
     */
    public String getPath() {
        return this.path;
    }
    
    /**
     * Gets the filename of the page
     * @return Filename
     */
    public String getFilename() {
        return this.filename;
    }
    
    /**
     * Gets the format of the page
     * @return Format
     */
    public Format getFormat() {
        return this.format;
    }
    
    /**
     * Gets whether the page has been checked
     * @return True if it has been checked
     */
    public boolean getChecked() {
        return this.checked;
    }
    
    /**
     * Gets whether the page is a top level page
     * @return True if a top level page, false otherwise
     */
    public boolean isTopLevel() {
        return !this.path.contains("/");
    }
    
    /**
     * Sets whether the page has been checked
     * @param checked Check status of the page
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    
    /**
     * Adds an issue to the page
     * @param issue Issue
     */
    public void addIssue(Issue issue) {
        this.issues.add(issue);
    }
    
    /**
     * Gets whether the page has any issues (errors/warnings)
     * @return True if there are any issues, false otherwise
     */
    public boolean hasIssues() {
        return this.issues.size() > 0;
    }
    
    /**
     * Gets whether the page has any error issues
     * @return True if there are errors, false otherwise
     */
    public boolean hasErrors() {
        for (Issue issue : this.issues) {
            if (issue.isError()) return true;
        }
        return false;
    }
    
    /**
     * Gets the issues for the page
     * @return Issues
     */
    public Iterator<Issue> getIssues() {
        return this.issues.iterator();
    }
    
    /**
     * Adds an outbound link to the page
     * @param link Outbound Link
     */
    public void addOutboundLink(Link link) {
        this.links.add(link);
    }
        
    /**
     * Gets the outbound links for the page
     * @return Outbound Links
     */
    public Iterator<Link> getOutboundLinks() {
        return this.links.iterator();
    }
    
    /**
     * Adds an inbound link to the page
     * @param link Inbound Link
     */
    public void addInboundLink(Link link) {
        this.inboundLinks.add(link);
    }
    
    /**
     * Gets the inbound links for the page
     * @return Inbound Links
     */
    public Iterator<Link> getInboundLinks() {
        return this.inboundLinks.iterator();
    }
    
    @Override
    public String toString() {
        if (this.checked) {
            return this.path + " (Format: " + this.format.toString() + " with " + this.links.size() + " Outbound Link(s) with " + this.issues.size() + " Issue(s) and " + this.inboundLinks.size() + " Inbound Link(s))";
        } else {
            return this.path + " (Format: " + this.format.toString() + " Unchecked)";
        }
    }

    /**
     * Gets the number of links on the page
     * @return Number of links
     */
    public int getOutboundLinkCount() {
        return this.links.size();
    }
    
    /**
     * Gets the number of internal wiki links on the page
     * @return Number of internal links
     */
    public int getOutboundWikiLinkCount() {
        int count = 0;
        for (Link l : this.links) {
            if (l.isWikiLink()) count++;
        }
        return count;
    }
    
    /**
     * Gets the number of external links on the page
     * @return Number of external links
     */
    public int getOutboundExternalLinkCount() {
        int count = 0;
        for (Link l : this.links) {
            if (!l.isWikiLink()) count++;
        }
        return count;
    }
    
    /**
     * Gets the number of inbound links to the page
     * @return Number of inbound links
     */
    public int getInboundLinkCount() {
        return this.inboundLinks.size();
    }
    
    /**
     * Gets the number of issues identified
     * @return Number of issues
     */
    public int getIssueCount() {
        return this.issues.size();
    }
    
    /**
     * Gets the number of errors identified
     * @return Number of errors
     */
    public int getErrorCount() {
        int count = 0;
        for (Issue i : this.issues) {
            if (i.isError()) count++;
        }
        return count;
    }
    
    /**
     * Gets the number of warnings identified
     * @return Number of warnings
     */
    public int getWarningCount() {
        int count = 0;
        for (Issue i : this.issues) {
            if (!i.isError()) count++;
        }
        return count;
    }
    
}
