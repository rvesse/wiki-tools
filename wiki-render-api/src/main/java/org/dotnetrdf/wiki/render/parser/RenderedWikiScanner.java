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

package org.dotnetrdf.wiki.render.parser;

import java.io.File;

import org.dotnetrdf.wiki.data.documents.formats.DocumentFormatRegistry;
import org.dotnetrdf.wiki.data.links.Link;
import org.dotnetrdf.wiki.parser.AbstractWikiScanner;
import org.dotnetrdf.wiki.render.data.RenderedWiki;
import org.dotnetrdf.wiki.render.data.documents.BasicRenderedDocument;
import org.dotnetrdf.wiki.render.data.documents.RenderedDocument;

/**
 * Scans a directory to populate a {@link RenderedWiki}
 * 
 * @author rvesse
 * 
 * @param <TLink>
 * @param <TDoc>
 */
public class RenderedWikiScanner<TLink extends Link, TDoc extends RenderedDocument<TLink>> extends
        AbstractWikiScanner<TLink, TDoc> {

    @SuppressWarnings("unchecked")
    @Override
    protected TDoc createDocument(String wikiPath, File f) {
        // Unchecked warning is kind of pointless because the type restriction
        // guarantees that TDoc will implement RenderedDocument so casting
        // BasicRenderedDocument to TDoc should always work
        return (TDoc) new BasicRenderedDocument(wikiPath, f, DocumentFormatRegistry.getFormat(f.getName()));
    }

}
