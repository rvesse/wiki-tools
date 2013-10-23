/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package org.dotnetrdf.wiki.data.links;

/**
 * Interface for links
 * 
 * @author rvesse
 * 
 */
public interface Link {

    /**
     * Gets the path i.e. URL of the link, may be absolute or relative
     * 
     * @return Path
     */
    public abstract String getPath();

    /**
     * Gets whether this is a wiki link
     * 
     * @return True if a wiki link, false otherwise
     */
    public abstract boolean isWikiLink();

    /**
     * Gets whether this is an email link
     * 
     * @return True if an email link, false otherwise
     */
    public abstract boolean isMailLink();

    /**
     * Gets the display text of the link
     * 
     * @return Display Text
     */
    public abstract String getText();

    /**
     * Does the link have friendly text?
     * 
     * @return Returns true if the display text for the link differs from the
     *         actual link, false otherwise
     */
    public abstract boolean hasFriendlyText();

    /**
     * Gets the line the link occurs on
     * 
     * @return Line
     */
    public abstract int getLine();

    /**
     * Gets the column the link occurs at
     * 
     * @return Column
     */
    public abstract int getColumn();

}