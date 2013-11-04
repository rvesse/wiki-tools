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

package org.dotnetrdf.wiki.checker.data;

import java.util.Iterator;

import org.dotnetrdf.wiki.checker.data.documents.CheckedDocument;
import org.dotnetrdf.wiki.checker.data.links.CheckedLink;
import org.dotnetrdf.wiki.data.Wiki;
import org.dotnetrdf.wiki.data.issues.Issue;

/**
 * Interface for wikis consisting of checked documents
 * 
 * @author rvesse
 * 
 * @param <TLink>
 *            Checked link type
 * @param <TDoc>
 *            Checked document type
 */
public interface CheckedWiki<TLink extends CheckedLink, TDoc extends CheckedDocument<TLink>> extends Wiki<TLink, TDoc> {

    /**
     * Gets the total number of errors including those in checked documents and
     * global wiki level errors
     * 
     * @return Total errors
     */
    public abstract int getTotalErrorCount();

    /**
     * Gets the total number of warnings including those in checked documents
     * and global wiki level warnings
     * 
     * @return Total warnings
     */
    public abstract int getTotalWarningCount();

    /**
     * Gets whether the wiki has been checked
     * 
     * @return True if it has been checked
     */
    public abstract boolean hasBeenChecked();

    /**
     * Adds a global issue to the wiki
     * 
     * @param issue
     *            Issue
     */
    public abstract void addGlobalIssue(Issue issue);

    /**
     * Gets whether the wiki has any global issues (errors/warnings)
     * 
     * @return True if there are any global issues, false otherwise
     */
    public abstract boolean hasGlobalIssues();

    /**
     * Gets whether the wiki has any global errors
     * 
     * @return True if there are errors, false otherwise
     */
    public abstract boolean hasGlobalErrors();

    /**
     * Gets the global issues for the wiki
     * 
     * @return Global issues
     */
    public abstract Iterator<Issue> getGlobalIssues();

    /**
     * Gets the number of global issues identified
     * 
     * @return Number of global issues
     */
    public abstract int getGlobalIssueCount();

    /**
     * Gets the number of global errors identified
     * 
     * @return Number of global errors
     */
    public abstract int getGlobalErrorCount();

    /**
     * Gets the number of global warnings identified
     * 
     * @return Number of global warnings
     */
    public abstract int getGlobalWarningCount();

    /**
     * Sets whether the wiki has been checked
     * 
     * @param checked
     *            Check status of the wiki
     */
    public abstract void setChecked(boolean checked);

}