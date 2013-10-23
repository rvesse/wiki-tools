/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package org.dotnetrdf.wiki.checker;

import java.io.IOException;

import org.dotnetrdf.wiki.checker.data.CheckedWiki;
import org.dotnetrdf.wiki.checker.data.documents.CheckedDocument;
import org.dotnetrdf.wiki.checker.data.links.CheckedLink;

/**
 * Interface for wiki checkers
 * 
 * @author rvesse
 * 
 * @param <TLink>
 *            Checked link type
 * @param <TDoc>
 *            Checked document type
 */
public interface WikiChecker<TLink extends CheckedLink, TDoc extends CheckedDocument<TLink>> {

    /**
     * Gets the wiki
     * 
     * @return Wiki
     */
    public abstract CheckedWiki<TLink, TDoc> getWiki();

    // TODO Base Directory can likely be safely removed if we add a
    // getInputStream() method to Document

    /**
     * Gets the base directory on disk where the wiki is located
     * 
     * @return Base Directory
     */
    public abstract String getBaseDirectory();
    
    /**
     * Runs the wiki checker
     * @throws IOException
     */
    public abstract void run() throws IOException;

    /**
     * Run the wiki checker
     * 
     * @param recheck
     *            Whether previously checked documents should be rechecked, true
     *            forces rechecks, false (default) causes them to be skipped
     * 
     * @throws IOException
     */
    public abstract void run(boolean recheck) throws IOException;

}