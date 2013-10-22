/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
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