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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dotnetrdf.wiki.checker.data.links.BasicCheckedLink;
import org.dotnetrdf.wiki.data.documents.formats.Format;
import org.dotnetrdf.wiki.data.issues.Issue;
import org.dotnetrdf.wiki.data.issues.AbstractIssue;

/**
 * A document which can be checked and have issues logged against it
 * 
 * @author rvesse
 * 
 */
public class BasicCheckedDocument extends AbstractCheckedDocument<BasicCheckedLink> {
    // TODO Should really add a CheckedLink type

    private boolean checked = false;
    private List<AbstractIssue> issues = new ArrayList<AbstractIssue>();

    /**
     * Creates a document
     * 
     * @param wikiPath
     *            Wiki path to the document
     * @param f
     *            Disk file for the document
     * @param format
     *            Format of the document
     */
    public BasicCheckedDocument(String wikiPath, File f, Format format) {
        super(wikiPath, f, format);
    }

    @Override
    public BasicCheckedLink createLink(String path, String text, int line, int column) {
        return new BasicCheckedLink(path, text, line, column);
    }

    @Override
    public BasicCheckedLink createLink(String path, int line, int column) {
        return new BasicCheckedLink(path, line, column);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.dotnetrdf.wiki.checker.data.documents.ICheckedDocument#hasBeenChecked
     * ()
     */
    @Override
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.dotnetrdf.wiki.checker.data.documents.ICheckedDocument#addIssue(org
     * .dotnetrdf.wiki.checker.issues.Issue)
     */
    @Override
    public void addIssue(AbstractIssue issue) {
        this.issues.add(issue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.dotnetrdf.wiki.checker.data.documents.ICheckedDocument#hasIssues()
     */
    @Override
    public boolean hasIssues() {
        return this.issues.size() > 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.dotnetrdf.wiki.checker.data.documents.ICheckedDocument#hasErrors()
     */
    @Override
    public boolean hasErrors() {
        for (Issue issue : this.issues) {
            if (issue.isError())
                return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.dotnetrdf.wiki.checker.data.documents.ICheckedDocument#getIssues()
     */
    @Override
    public Iterator<AbstractIssue> getIssues() {
        return this.issues.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.dotnetrdf.wiki.checker.data.documents.ICheckedDocument#getIssueCount
     * ()
     */
    @Override
    public int getIssueCount() {
        return this.issues.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.dotnetrdf.wiki.checker.data.documents.ICheckedDocument#getErrorCount
     * ()
     */
    @Override
    public int getErrorCount() {
        int count = 0;
        for (Issue i : this.issues) {
            if (i.isError())
                count++;
        }
        return count;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.dotnetrdf.wiki.checker.data.documents.ICheckedDocument#getWarningCount
     * ()
     */
    @Override
    public int getWarningCount() {
        int count = 0;
        for (Issue i : this.issues) {
            if (!i.isError())
                count++;
        }
        return count;
    }
}
