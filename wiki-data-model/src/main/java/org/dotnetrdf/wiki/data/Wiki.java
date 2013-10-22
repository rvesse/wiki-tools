/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package org.dotnetrdf.wiki.data;

import java.util.Iterator;

import org.dotnetrdf.wiki.data.documents.Document;

/**
 * Represents a wiki which is a collection of documents
 * 
 * @author rvesse
 * @param <T>
 *            Document type
 * 
 */
public interface Wiki<T extends Document> {

    /**
     * Adds a document
     * 
     * @param document
     *            Document
     */
    public void addDocument(T document);

    /**
     * Gets whether a document with the given path exists
     * 
     * @param path
     *            Path
     * @return True if the document exists, false otherwise
     */
    public boolean hasDocument(String path);

    /**
     * Gets the document associated with the given wiki path
     * 
     * @param linkPath
     *            Wiki Link Path
     * @return Page or null if no such document
     */
    public T getDocument(String linkPath);

    /**
     * Gets the documents that are known to exist
     * 
     * @return Documents
     */
    public Iterator<T> getDocuments();

    /**
     * Gets the document count
     * 
     * @return Total documents
     */
    public int getTotalDocuments();

    /**
     * Gets the total number of links
     * 
     * @return Total Links
     */
    public int getTotalLinks();

    /**
     * Gets the total number of wiki links
     * 
     * @return Total wiki links
     */
    public int getTotalWikiLinks();

    /**
     * Gets the total number of external links
     * 
     * @return Total external links
     */
    public int getTotalExternalLinks();
}
