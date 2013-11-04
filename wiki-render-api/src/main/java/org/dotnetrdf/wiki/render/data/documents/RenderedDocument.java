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
import java.util.Iterator;

import org.dotnetrdf.wiki.data.documents.Document;
import org.dotnetrdf.wiki.data.issues.Issue;
import org.dotnetrdf.wiki.data.links.Link;

/**
 * Interface for documents that may be rendered
 * 
 * @author rvesse
 * 
 * @param <TLink>
 */
public interface RenderedDocument<TLink extends Link> extends Document<TLink> {

    /**
     * Gets whether the document has been rendered
     * 
     * @return True if it has been rendered, false otherwise
     */
    public boolean hasBeenRendered();

    /**
     * Sets whether the document has been rendered
     * 
     * @param rendered
     *            Rendered status
     */
    public void setRendered(boolean rendered);

    /**
     * Gets the rendered document file
     * 
     * @return Rendered document file, may be null if the document has not been
     *         rendered or there was a problem rendering it
     */
    public abstract File getRenderedFile();

    /**
     * Sets the rendered document file
     * 
     * @param f
     *            Rendered document file
     */
    public void setRenderedFile(File f);

    /**
     * Adds a rendering issue to the document
     * 
     * @param issue
     *            Rendering issue
     */
    public void addRenderingIssue(Issue issue);

    /**
     * Gets an iterator over the rendering issues
     * 
     * @return Iterator of rendering issues
     */
    public Iterator<Issue> getRenderingIssues();

    /**
     * Gets whether the document has rendering issues
     * 
     * @return True if there were any rendering issues, false otherwise
     */
    public boolean hasRenderingIssues();

    /**
     * Gets the number of rendering issues for the document
     * 
     * @return Number of rendering issues
     */
    public int getRenderingIssueCount();
}
