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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dotnetrdf.wiki.checker.issues.Issue;

/**
 * Represents a page in the wiki
 * @author rvesse
 *
 */
public class Page {

    private String path;
    private boolean checked = false;
    private List<Issue> issues = new ArrayList<Issue>();
    private Set<Link> links = new HashSet<Link>();
    
    /**
     * Creates a page
     * @param path Wiki path to the page
     */
    public Page(String path) {
        this.path = path;
    }
        
    /**
     * Gets the path of the page
     * @return Path
     */
    public String getPath() {
        return this.path;
    }
    
    /**
     * Gets whether the page has been checked
     * @return True if it has been checked
     */
    public boolean getChecked() {
        return this.checked;
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
     * Gets whether the page has any issues
     * @return True if there are any issues, false otherwise
     */
    public boolean hasIssues() {
        return this.issues.size() > 0;
    }
    
    /**
     * Gets the issues for the page
     * @return Issues
     */
    public Iterator<Issue> getIssues() {
        return this.issues.iterator();
    }
    
    /**
     * Adds a link to the page
     * @param link Link
     */
    public void addLink(Link link) {
        this.links.add(link);
    }
    
    /**
     * Gets the links for the page
     * @return Links
     */
    public Iterator<Link> getLinks() {
        return this.links.iterator();
    }
    
    @Override
    public String toString() {
        if (this.checked) {
            return this.path + " ( " + this.links.size() + " Link(s) with " + this.issues.size() + " Issue(s))";
        } else {
            return this.path + " (Unchecked)";
        }
    }
    
}
