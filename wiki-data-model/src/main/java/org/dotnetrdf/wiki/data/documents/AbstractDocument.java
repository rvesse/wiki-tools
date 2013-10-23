/**
 * Copyright (c) 2013 dotNetRDF Project (dotnetrdf-develop@lists.sf.net)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */
package org.dotnetrdf.wiki.data.documents;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.dotnetrdf.wiki.data.documents.formats.Format;
import org.dotnetrdf.wiki.data.links.BasicLink;
import org.dotnetrdf.wiki.data.links.Link;

/**
 * Abstract implementation of the {@link Document} interface which leaves the
 * choice of link type up to derived implementations
 * 
 * @author rvesse
 * @param <T>
 *            Link type
 * 
 */
public abstract class AbstractDocument<T extends BasicLink> implements Document<T> {

    private String path;
    private String filename;
    protected Set<T> links = new HashSet<T>();
    protected Set<T> inboundLinks = new HashSet<T>();
    protected Format format;

    /**
     * Creates a document
     * 
     * @param path
     *            Path to the document
     * @param format
     *            Format of the document
     */
    public AbstractDocument(String path, Format format) {
        this.path = path;
        this.filename = path.replace('/', File.separatorChar);
        this.format = format;

        // Strip extension if present from path for wiki formats only
        if (this.format.isWiki() && this.path.contains(".")) {
            this.path = this.path.substring(0, this.path.lastIndexOf('.'));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dotnetrdf.wiki.data.documents.Document#getPath()
     */
    @Override
    public String getPath() {
        return this.path;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dotnetrdf.wiki.data.documents.Document#getFilename()
     */
    @Override
    public String getFilename() {
        return this.filename;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dotnetrdf.wiki.data.documents.Document#getFormat()
     */
    @Override
    public Format getFormat() {
        return this.format;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dotnetrdf.wiki.data.documents.Document#isTopLevel()
     */
    @Override
    public boolean isTopLevel() {
        return !this.path.contains("/");
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.dotnetrdf.wiki.data.documents.Document#addOutboundLink(org.dotnetrdf
     * .wiki.data.links.Link)
     */
    @Override
    public void addOutboundLink(T link) {
        this.links.add(link);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dotnetrdf.wiki.data.documents.Document#getOutboundLinks()
     */
    @Override
    public Iterator<T> getOutboundLinks() {
        return this.links.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.dotnetrdf.wiki.data.documents.Document#addInboundLink(org.dotnetrdf
     * .wiki.data.links.Link)
     */
    @Override
    public void addInboundLink(T link) {
        this.inboundLinks.add(link);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dotnetrdf.wiki.data.documents.Document#getInboundLinks()
     */
    @Override
    public Iterator<T> getInboundLinks() {
        return this.inboundLinks.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dotnetrdf.wiki.data.documents.Document#getOutboundLinkCount()
     */
    @Override
    public int getOutboundLinkCount() {
        return this.links.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.dotnetrdf.wiki.data.documents.Document#getOutboundWikiLinkCount()
     */
    @Override
    public int getOutboundWikiLinkCount() {
        int count = 0;
        for (Link l : this.links) {
            if (l.isWikiLink())
                count++;
        }
        return count;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.dotnetrdf.wiki.data.documents.Document#getOutboundExternalLinkCount()
     */
    @Override
    public int getOutboundExternalLinkCount() {
        int count = 0;
        for (Link l : this.links) {
            if (!l.isWikiLink())
                count++;
        }
        return count;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dotnetrdf.wiki.data.documents.Document#getInboundLinkCount()
     */
    @Override
    public int getInboundLinkCount() {
        return this.inboundLinks.size();
    }

    @Override
    public String toString() {
        return this.getPath() + " (Format: " + this.format.toString() + ")";
    }
}
