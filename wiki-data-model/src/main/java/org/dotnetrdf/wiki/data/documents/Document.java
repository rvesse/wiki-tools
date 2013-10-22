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

import java.util.Iterator;

import org.dotnetrdf.wiki.data.documents.formats.Format;
import org.dotnetrdf.wiki.data.links.Link;

/**
 * Interface for representing wiki documents
 * 
 * @author rvesse
 * 
 */
public interface Document {

    /**
     * Gets the path of the page
     * 
     * @return Path
     */
    public abstract String getPath();

    /**
     * Gets the filename of the page
     * 
     * @return Filename
     */
    public abstract String getFilename();

    /**
     * Gets the format of the page
     * 
     * @return Format
     */
    public abstract Format getFormat();

    /**
     * Gets whether the page is a top level page
     * 
     * @return True if a top level page, false otherwise
     */
    public abstract boolean isTopLevel();

    /**
     * Adds an outbound link to the page
     * 
     * @param link
     *            Outbound Link
     */
    public abstract void addOutboundLink(Link link);

    /**
     * Gets the outbound links for the page
     * 
     * @return Outbound Links
     */
    public abstract Iterator<Link> getOutboundLinks();

    /**
     * Adds an inbound link to the page
     * 
     * @param link
     *            Inbound Link
     */
    public abstract void addInboundLink(Link link);

    /**
     * Gets the inbound links for the page
     * 
     * @return Inbound Links
     */
    public abstract Iterator<Link> getInboundLinks();

    /**
     * Gets the number of links on the page
     * 
     * @return Number of links
     */
    public abstract int getOutboundLinkCount();

    /**
     * Gets the number of internal wiki links on the page
     * 
     * @return Number of internal links
     */
    public abstract int getOutboundWikiLinkCount();

    /**
     * Gets the number of external links on the page
     * 
     * @return Number of external links
     */
    public abstract int getOutboundExternalLinkCount();

    /**
     * Gets the number of inbound links to the page
     * 
     * @return Number of inbound links
     */
    public abstract int getInboundLinkCount();

}