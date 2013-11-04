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

package org.dotnetrdf.wiki.render.renderer;

import java.io.File;

import org.dotnetrdf.wiki.data.links.Link;
import org.dotnetrdf.wiki.render.data.RenderedWiki;
import org.dotnetrdf.wiki.render.data.documents.RenderedDocument;

/**
 * Interface for document renderers
 * 
 * @author rvesse
 * 
 */
public interface DocumentRenderer {

    /**
     * Renders a document
     * 
     * @param document
     *            Document
     * @param outputBase
     *            Output base directory i.e. the directory in which the file
     *            resulting from the rendering of the document will be stored
     *            under subject to its relative wiki path
     * @param wiki
     *            Wiki
     */
    public <TLink extends Link, TDoc extends RenderedDocument<TLink>> void render(TDoc document, File outputBase,
            RenderedWiki<TLink, RenderedDocument<TLink>> wiki);
}
