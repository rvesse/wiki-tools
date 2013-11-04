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

package org.dotnetrdf.wiki.render.data.documents;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dotnetrdf.wiki.data.documents.AbstractDocument;
import org.dotnetrdf.wiki.data.documents.formats.Format;
import org.dotnetrdf.wiki.data.issues.Issue;
import org.dotnetrdf.wiki.data.links.Link;

/**
 * Abstract implementation of a rendered document
 * 
 * @author rvesse
 * @param <TLink>
 *            Link type
 * 
 */
public abstract class AbstractRenderedDocument<TLink extends Link> extends AbstractDocument<TLink> implements
        RenderedDocument<TLink> {

    private List<Issue> renderingIssues = new ArrayList<Issue>();
    private boolean rendered = false;
    private File renderedFile;

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
    public AbstractRenderedDocument(String wikiPath, File f, Format format) {
        super(wikiPath, f, format);
    }

    @Override
    public boolean hasBeenRendered() {
        return this.rendered;
    }

    @Override
    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    @Override
    public File getRenderedFile() {
        return this.renderedFile;
    }

    @Override
    public void setRenderedFile(File f) {
        this.renderedFile = f;
    }

    @Override
    public void addRenderingIssue(Issue issue) {
        if (issue != null)
            this.renderingIssues.add(issue);
    }

    @Override
    public Iterator<Issue> getRenderingIssues() {
        return this.renderingIssues.iterator();
    }

    @Override
    public boolean hasRenderingIssues() {
        return this.renderingIssues.size() > 0;
    }

    @Override
    public int getRenderingIssueCount() {
        return this.renderingIssues.size();
    }

}
