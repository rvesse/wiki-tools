/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package org.dotnetrdf.wiki.checker.parser;

import java.io.File;

import org.dotnetrdf.wiki.checker.data.CheckedWiki;
import org.dotnetrdf.wiki.checker.data.documents.CheckedDocument;
import org.dotnetrdf.wiki.data.documents.formats.DocumentFormatRegistry;
import org.dotnetrdf.wiki.parser.AbstractWikiScanner;

/**
 * Scans a directory to populate a {@link CheckedWiki}
 * 
 * @author rvesse
 * 
 * @param <T> Document type
 */
public class CheckedWikiScanner<T extends CheckedDocument> extends AbstractWikiScanner<T> {

    @SuppressWarnings("unchecked")
    @Override
    protected T createDocument(String wikiPath, File f) {
        // Unchecked warning is kind of pointless because the type restriction
        // guarantees that T will derived from CheckedDocument so casting
        // CheckedDocument to T should always work
        return (T) new CheckedDocument(wikiPath, DocumentFormatRegistry.getFormat(f.getName()));
    }

}
