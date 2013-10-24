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

import org.dotnetrdf.wiki.checker.data.links.CheckedLink;
import org.dotnetrdf.wiki.checker.issues.Issue;
import org.dotnetrdf.wiki.data.documents.AbstractDocument;
import org.dotnetrdf.wiki.data.documents.formats.Format;

/**
 * Abstract implementation of a checked document
 * 
 * @author rvesse
 * @param <TLink>
 *            Link type
 * 
 */
public abstract class AbstractCheckedDocument<TLink extends CheckedLink> extends AbstractDocument<TLink> implements
        CheckedDocument<TLink> {
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
    public AbstractCheckedDocument(String path, Format format) {
        super(path, format);
    }

    @Override
    public boolean hasBeenChecked() {
        return this.checked;
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public void addIssue(Issue issue) {
        this.issues.add(issue);
    }

    @Override
    public boolean hasIssues() {
        return this.issues.size() > 0;
    }

    @Override
    public boolean hasErrors() {
        for (Issue issue : this.issues) {
            if (issue.isError())
                return true;
        }
        return false;
    }

    @Override
    public Iterator<Issue> getIssues() {
        return this.issues.iterator();
    }

    @Override
    public int getIssueCount() {
        return this.issues.size();
    }

    @Override
    public int getErrorCount() {
        int count = 0;
        for (Issue i : this.issues) {
            if (i.isError())
                count++;
        }
        return count;
    }

    @Override
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
