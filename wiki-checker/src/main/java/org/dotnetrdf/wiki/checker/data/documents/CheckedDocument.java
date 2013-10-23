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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dotnetrdf.wiki.checker.issues.Issue;
import org.dotnetrdf.wiki.data.documents.AbstractDocument;
import org.dotnetrdf.wiki.data.documents.formats.Format;
import org.dotnetrdf.wiki.data.links.BasicLink;

/**
 * A document which can be checked and have issues logged against it
 * 
 * @author rvesse
 * 
 */
public class CheckedDocument extends AbstractDocument<BasicLink> {
    // TODO Should really add a CheckedLink type
    
    private boolean checked = false;
    private List<Issue> issues = new ArrayList<Issue>();

    /**
     * Creates a document
     * 
     * @param path
     *            Path to the document
     * @param format
     *            Format of the document
     */
    public CheckedDocument(String path, Format format) {
        super(path, format);
    }

    /**
     * Gets whether the document has been checked
     * 
     * @return True if it has been checked
     */
    public boolean hasBeenChecked() {
        return this.checked;
    }

    /**
     * Sets whether the document has been checked
     * 
     * @param checked
     *            Check status of the document
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    /**
     * Adds an issue to the document
     * 
     * @param issue
     *            Issue
     */
    public void addIssue(Issue issue) {
        this.issues.add(issue);
    }

    /**
     * Gets whether the document has any issues (errors/warnings)
     * 
     * @return True if there are any issues, false otherwise
     */
    public boolean hasIssues() {
        return this.issues.size() > 0;
    }

    /**
     * Gets whether the document has any error issues
     * 
     * @return True if there are errors, false otherwise
     */
    public boolean hasErrors() {
        for (Issue issue : this.issues) {
            if (issue.isError())
                return true;
        }
        return false;
    }

    /**
     * Gets the issues for the document
     * 
     * @return Issues
     */
    public Iterator<Issue> getIssues() {
        return this.issues.iterator();
    }

    /**
     * Gets the number of issues identified
     * 
     * @return Number of issues
     */
    public int getIssueCount() {
        return this.issues.size();
    }

    /**
     * Gets the number of errors identified
     * 
     * @return Number of errors
     */
    public int getErrorCount() {
        int count = 0;
        for (Issue i : this.issues) {
            if (i.isError())
                count++;
        }
        return count;
    }

    /**
     * Gets the number of warnings identified
     * 
     * @return Number of warnings
     */
    public int getWarningCount() {
        int count = 0;
        for (Issue i : this.issues) {
            if (!i.isError())
                count++;
        }
        return count;
    }

    @Override
    public String toString() {
        if (this.checked) {
            return this.getPath() + " (Format: " + this.format.toString() + " with " + this.links.size()
                    + " Outbound Link(s) with " + this.issues.size() + " Issue(s) and " + this.inboundLinks.size()
                    + " Inbound Link(s))";
        } else {
            return this.getPath() + " (Format: " + this.format.toString() + " Unchecked)";
        }
    }
}
