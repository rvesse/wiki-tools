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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.dotnetrdf.wiki.data.documents.formats.Format;
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
public abstract class AbstractDocument<T extends Link> implements Document<T> {

    private File file;
    private String path;
    protected Set<T> links = new HashSet<T>();
    protected Set<T> inboundLinks = new HashSet<T>();
    protected Format format;

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
    public AbstractDocument(String wikiPath, File f, Format format) {
        if (wikiPath == null) throw new NullPointerException("Wiki path cannot be null");
        this.path = wikiPath;
        this.file = f;
        this.format = format == null ? Format.UNKNOWN : format;

        // Strip extension if present from path for wiki formats only
        if (this.format.isWiki() && this.path.contains(".")) {
            this.path = this.path.substring(0, this.path.lastIndexOf('.'));
        }
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public File getFile() {
        return this.file;
    }

    @Override
    public Format getFormat() {
        return this.format;
    }

    @Override
    public boolean isTopLevel() {
        return !this.path.contains("/");
    }
    
    @Override
    public String getText() throws IOException {
        if (!this.format.isText()) return null;
        if (this.file == null) return null;
        
        if (!this.file.exists()) throw new FileNotFoundException("The on disk file for the document " + this.getPath() + " cannot be found");
        
        FileReader reader = new FileReader(this.file);

        StringWriter sw = new StringWriter(8192);
        char buff[] = new char[8192];
        for (;;) {
            int l = reader.read(buff);
            if (l < 0)
                break;
            sw.write(buff, 0, l);
        }
        reader.close();
        sw.close();
        return sw.toString();
    }

    @Override
    public void addOutboundLink(T link) {
        this.links.add(link);
    }

    @Override
    public Iterator<T> getOutboundLinks() {
        return this.links.iterator();
    }

    @Override
    public void addInboundLink(T link) {
        this.inboundLinks.add(link);
    }

    @Override
    public Iterator<T> getInboundLinks() {
        return this.inboundLinks.iterator();
    }

    @Override
    public int getOutboundLinkCount() {
        return this.links.size();
    }

    @Override
    public int getOutboundWikiLinkCount() {
        int count = 0;
        for (Link l : this.links) {
            if (l.isWikiLink())
                count++;
        }
        return count;
    }

    @Override
    public int getOutboundExternalLinkCount() {
        int count = 0;
        for (Link l : this.links) {
            if (!l.isWikiLink())
                count++;
        }
        return count;
    }

    @Override
    public int getInboundLinkCount() {
        return this.inboundLinks.size();
    }

    @Override
    public String toString() {
        return this.getPath() + " (Format: " + this.format.toString() + ")";
    }
}
