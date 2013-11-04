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

import org.dotnetrdf.wiki.data.documents.formats.Format;
import org.dotnetrdf.wiki.data.links.BasicLink;

/**
 * Basic implementation of a rendered documents
 * 
 * @author rvesse
 * 
 */
public class BasicRenderedDocument extends AbstractRenderedDocument<BasicLink> {

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
    public BasicRenderedDocument(String wikiPath, File f, Format format) {
        super(wikiPath, f, format);
        // TODO Auto-generated constructor stub
    }

    @Override
    public BasicLink createLink(String path, String text, int line, int column) {
        return new BasicLink(path, text, line, column);
    }

    @Override
    public BasicLink createLink(String path, int line, int column) {
        return new BasicLink(path, line, column);
    }

}
