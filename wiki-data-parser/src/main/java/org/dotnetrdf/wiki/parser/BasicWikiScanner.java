/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package org.dotnetrdf.wiki.parser;

import java.io.File;

import org.dotnetrdf.wiki.data.documents.BasicDocument;
import org.dotnetrdf.wiki.data.documents.formats.DocumentFormatRegistry;

/**
 * A wiki scanner that populates a basic wiki
 * 
 * @author rvesse
 * 
 * @param <T>
 *            Document type
 */
public class BasicWikiScanner<T extends BasicDocument> extends AbstractWikiScanner<T> {

    @SuppressWarnings("unchecked")
    @Override
    protected T createDocument(String wikiPath, File f) {
        // Unchecked warning is kind of dumb here since T is restricted to being
        // derived from BasicDocument so BasicDocument must be castable to T
        return (T) new BasicDocument(wikiPath, DocumentFormatRegistry.getFormat(f.getName()));
    }

}
