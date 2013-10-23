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

package org.dotnetrdf.wiki.checker.data.documents;

import java.util.Iterator;

import org.dotnetrdf.wiki.checker.data.links.CheckedLink;
import org.dotnetrdf.wiki.checker.issues.Issue;
import org.dotnetrdf.wiki.data.documents.Document;

/**
 * Interface for documents that may be checked for issues
 * 
 * @author rvesse
 * 
 * @param <TLink>
 *            Link type
 */
public interface CheckedDocument<TLink extends CheckedLink> extends Document<TLink> {

    /**
     * Gets whether the document has been checked
     * 
     * @return True if it has been checked
     */
    public abstract boolean hasBeenChecked();

    /**
     * Adds an issue to the document
     * 
     * @param issue
     *            Issue
     */
    public abstract void addIssue(Issue issue);

    /**
     * Gets whether the document has any issues (errors/warnings)
     * 
     * @return True if there are any issues, false otherwise
     */
    public abstract boolean hasIssues();

    /**
     * Gets whether the document has any error issues
     * 
     * @return True if there are errors, false otherwise
     */
    public abstract boolean hasErrors();

    /**
     * Gets the issues for the document
     * 
     * @return Issues
     */
    public abstract Iterator<Issue> getIssues();

    /**
     * Gets the number of issues identified
     * 
     * @return Number of issues
     */
    public abstract int getIssueCount();

    /**
     * Gets the number of errors identified
     * 
     * @return Number of errors
     */
    public abstract int getErrorCount();

    /**
     * Gets the number of warnings identified
     * 
     * @return Number of warnings
     */
    public abstract int getWarningCount();

    /**
     * Sets whether the document has been checked
     * 
     * @param checked
     *            Check status of the document
     */
    public abstract void setChecked(boolean checked);

}