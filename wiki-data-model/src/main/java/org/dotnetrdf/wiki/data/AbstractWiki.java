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

package org.dotnetrdf.wiki.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dotnetrdf.wiki.data.documents.Document;
import org.dotnetrdf.wiki.data.links.Link;

/**
 * A basic representation of a wiki
 * 
 * @author rvesse
 * @param <TLink>
 *            Link type
 * 
 * @param <TDoc>
 *            Document type
 */
public abstract class AbstractWiki<TLink extends Link, TDoc extends Document<TLink>> implements Wiki<TLink, TDoc> {

    /**
     * Map of wiki paths to documents
     */
    protected Map<String, TDoc> documents = new HashMap<String, TDoc>();

    @Override
    public void addDocument(TDoc document) {
        this.documents.put(document.getPath(), document);
    }

    @Override
    public boolean hasDocument(String path) {
        return this.documents.containsKey(path);
    }

    @Override
    public TDoc getDocument(String linkPath) {
        return this.documents.get(linkPath);
    }

    @Override
    public int getDocumentCount() {
        return this.documents.size();
    }

    @Override
    public Iterator<TDoc> getDocuments() {
        return this.documents.values().iterator();
    }

    @Override
    public int getTotalDocuments() {
        return this.documents.size();
    }

    @Override
    public int getTotalLinks() {
        int count = 0;
        for (TDoc document : this.documents.values()) {
            count += document.getOutboundLinkCount();
        }
        return count;
    }

    @Override
    public int getTotalWikiLinks() {
        int count = 0;
        for (TDoc document : this.documents.values()) {
            count += document.getOutboundWikiLinkCount();
        }
        return count;
    }

    @Override
    public int getTotalExternalLinks() {
        int count = 0;
        for (TDoc document : this.documents.values()) {
            count += document.getOutboundExternalLinkCount();
        }
        return count;
    }
}
