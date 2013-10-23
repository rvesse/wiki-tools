/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package org.dotnetrdf.wiki.data.documents;

import org.dotnetrdf.wiki.data.documents.formats.Format;
import org.dotnetrdf.wiki.data.links.BasicLink;

/**
 * Basic representation of a wiki document which uses the {@link BasicLink}
 * class to represent links
 * 
 * @author rvesse
 * 
 */
public class BasicDocument extends AbstractDocument<BasicLink> {

    /**
     * Creates a document
     * 
     * @param path
     *            Path to the document
     * @param format
     *            Format of the document
     */
    public BasicDocument(String path, Format format) {
        super(path, format);
    }

    @Override
    public BasicLink createLink(String path, String text, int line, int column) {
        return new BasicLink(path, text, line, column);
    }

    @Override
    public BasicLink createLink(String path, int line, int column) {
        return new BasicLink(path, line, column);
    }

}
