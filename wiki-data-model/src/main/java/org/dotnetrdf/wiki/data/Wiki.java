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

import java.util.Iterator;

import org.dotnetrdf.wiki.data.documents.Document;

/**
 * Represents a wiki which is a collection of documents
 * 
 * @author rvesse
 * @param <T>
 *            Document type
 * 
 */
public interface Wiki<T extends Document> {

    /**
     * Adds a document
     * 
     * @param document
     *            Document
     */
    public void addDocument(T document);

    /**
     * Gets whether a document with the given path exists
     * 
     * @param path
     *            Path
     * @return True if the document exists, false otherwise
     */
    public boolean hasDocument(String path);

    /**
     * Gets the document associated with the given wiki path
     * 
     * @param linkPath
     *            Wiki Link Path
     * @return Page or null if no such document
     */
    public T getDocument(String linkPath);

    /**
     * Gets the documents that are known to exist
     * 
     * @return Documents
     */
    public Iterator<T> getDocuments();

    /**
     * Gets the document count
     * 
     * @return Total documents
     */
    public int getTotalDocuments();

    /**
     * Gets the total number of links
     * 
     * @return Total Links
     */
    public int getTotalLinks();

    /**
     * Gets the total number of wiki links
     * 
     * @return Total wiki links
     */
    public int getTotalWikiLinks();

    /**
     * Gets the total number of external links
     * 
     * @return Total external links
     */
    public int getTotalExternalLinks();
}
