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
 * @param <TLink> Link type
 * 
 * @param <TDoc> Document type
 */
public class BasicWiki<TLink extends Link, TDoc extends Document<TLink>> implements Wiki<TLink, TDoc> {

    /**
     * Map of wiki paths to documents
     */
    protected Map<String, TDoc> documents = new HashMap<String, TDoc>();

    /**
     * Adds a document
     * 
     * @param document
     *            Document
     */
    public void addDocument(TDoc document) {
        this.documents.put(document.getPath(), document);
    }

    /**
     * Gets whether a document with the given path exists
     * 
     * @param path
     *            Path
     * @return True if the document exists, false otherwise
     */
    public boolean hasDocument(String path) {
        return this.documents.containsKey(path);
    }
    
    /**
     * Gets the document associated with the given wiki path
     * 
     * @param linkPath
     *            Wiki Link Path
     * @return Page or null if no such document
     */
    public TDoc getDocument(String linkPath) {
        return this.documents.get(linkPath);
    }

    /**
     * Gets the documents that are known to exist
     * 
     * @return Documents
     */
    public Iterator<TDoc> getDocuments() {
        return this.documents.values().iterator();
    }

    /**
     * Gets the document count
     * 
     * @return Total documents
     */
    public int getTotalDocuments() {
        return this.documents.size();
    }

    /**
     * Gets the total number of links
     * 
     * @return Total Links
     */
    public int getTotalLinks() {
        int count = 0;
        for (TDoc document : this.documents.values()) {
            count += document.getOutboundLinkCount();
        }
        return count;
    }

    /**
     * Gets the total number of wiki links
     * 
     * @return Total wiki links
     */
    public int getTotalWikiLinks() {
        int count = 0;
        for (TDoc document : this.documents.values()) {
            count += document.getOutboundWikiLinkCount();
        }
        return count;
    }

    /**
     * Gets the total number of external links
     * 
     * @return Total external links
     */
    public int getTotalExternalLinks() {
        int count = 0;
        for (TDoc document : this.documents.values()) {
            count += document.getOutboundExternalLinkCount();
        }
        return count;
    }
}
