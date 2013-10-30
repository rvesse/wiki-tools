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

package org.dotnetrdf.wiki.data.documents;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.dotnetrdf.wiki.data.documents.formats.Format;
import org.dotnetrdf.wiki.data.links.Link;

/**
 * Interface for representing wiki documents
 * 
 * @author rvesse
 * @param <T>
 *            Link type
 * 
 */
public interface Document<T extends Link> {

    /**
     * Gets the path of the document relative to the wiki root
     * 
     * @return Path
     */
    public abstract String getPath();

    /**
     * Gets the file system location of the document, may be null if the
     * document does not exist on disk
     * 
     * @return File system location, null if the document does not exist on disk
     */
    public abstract File getFile();

    /**
     * Gets the format of the document
     * 
     * @return Format
     */
    public abstract Format getFormat();

    /**
     * Gets the text of the document assuming the document is a textual document
     * and its on disk location is known
     * <p>
     * Implementations are free to decide whether they cache the document text
     * or not
     * </p>
     * 
     * @return Document text if a textual document whose on disk location is
     *         known, null otherwise
     * @throws IOException
     *             Thrown if there is a problem reading in the document text
     */
    public abstract String getText() throws IOException;

    /**
     * Gets whether the document is a top level document
     * 
     * @return True if a top level document, false otherwise
     */
    public abstract boolean isTopLevel();

    /**
     * Creates a link
     * 
     * @param path
     *            Link Path
     * @param text
     *            Friendly text for the link
     * @param line
     *            Line the link occurs at
     * @param column
     *            Column the link occurs at
     * @return Link
     */
    public abstract T createLink(String path, String text, int line, int column);

    /**
     * Creates a link
     * 
     * @param path
     *            Link path
     * @param line
     *            Line the link occurs at
     * @param column
     *            Column the link occurs at
     * @return Link
     */
    public abstract T createLink(String path, int line, int column);

    /**
     * Adds an outbound link to the document
     * 
     * @param link
     *            Outbound Link
     */
    public abstract void addOutboundLink(T link);

    /**
     * Gets the outbound links for the document
     * 
     * @return Outbound Links
     */
    public abstract Iterator<T> getOutboundLinks();

    /**
     * Adds an inbound link to the document
     * 
     * @param link
     *            Inbound Link
     */
    public abstract void addInboundLink(T link);

    /**
     * Gets the inbound links for the document
     * 
     * @return Inbound Links
     */
    public abstract Iterator<T> getInboundLinks();

    /**
     * Gets the number of links on the document
     * 
     * @return Number of links
     */
    public abstract int getOutboundLinkCount();

    /**
     * Gets the number of internal wiki links on the document
     * 
     * @return Number of internal links
     */
    public abstract int getOutboundWikiLinkCount();

    /**
     * Gets the number of external links on the document
     * 
     * @return Number of external links
     */
    public abstract int getOutboundExternalLinkCount();

    /**
     * Gets the number of inbound links to the document
     * 
     * @return Number of inbound links
     */
    public abstract int getInboundLinkCount();

}