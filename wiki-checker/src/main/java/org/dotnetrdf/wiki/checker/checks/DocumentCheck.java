/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package org.dotnetrdf.wiki.checker.checks;

import org.dotnetrdf.wiki.checker.data.CheckedWiki;
import org.dotnetrdf.wiki.checker.data.documents.CheckedDocument;

/**
 * Interface for document checks
 * 
 * @author rvesse
 * 
 * @param <T>
 *            Checked document type
 */
public interface DocumentCheck<T extends CheckedDocument> {

    /**
     * Carries out a check on the document
     * 
     * @param document
     *            Document
     * @param text
     *            Document text, null for non-text documents
     * @param wiki
     *            Wiki
     */
    public void check(T document, String text, CheckedWiki<T> wiki);
}
