/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package org.dotnetrdf.wiki.checker.checks;

import org.dotnetrdf.wiki.checker.data.CheckedWiki;
import org.dotnetrdf.wiki.checker.data.documents.CheckedDocument;
import org.dotnetrdf.wiki.data.links.Link;

/**
 * Interface for document checks
 * 
 * @author rvesse
 * @param <TLink>
 *            Checked link type
 * @param <TDoc>
 *            Checked document type
 */
public interface DocumentCheck<TLink extends Link, TDoc extends CheckedDocument<TLink>> {

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
    public void check(TDoc document, String text, CheckedWiki<TLink, TDoc> wiki);
}
