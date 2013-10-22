/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package org.dotnetrdf.wiki.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dotnetrdf.wiki.data.documents.Document;

/**
 * A basic representation of a wiki
 * 
 * @author rvesse
 * 
 * @param <T> Document type
 */
public class BasicWiki<T extends Document> implements Wiki<T> {

    /**
     * Map of wiki paths to documents
     */
    protected Map<String, T> documents = new HashMap<String, T>();

    /**
     * Adds a document
     * 
     * @param document
     *            Document
     */
    public void addDocument(T document) {
        this.documents.put(document.getPath(), document);
    }

    /**
     * Gets whether a document with the given path exists
     * 
     * @param path
     *            Path
     * @return True if the document exists, false otherwise
     */
    public boolean hasDocument(String path) {
        return this.documents.containsKey(path);
    }
    
    /**
     * Gets the document associated with the given wiki path
     * 
     * @param linkPath
     *            Wiki Link Path
     * @return Page or null if no such document
     */
    public T getDocument(String linkPath) {
        return this.documents.get(linkPath);
    }

    /**
     * Gets the documents that are known to exist
     * 
     * @return Documents
     */
    public Iterator<T> getDocuments() {
        return this.documents.values().iterator();
    }

    /**
     * Gets the document count
     * 
     * @return Total documents
     */
    public int getTotalDocuments() {
        return this.documents.size();
    }

    /**
     * Gets the total number of links
     * 
     * @return Total Links
     */
    public int getTotalLinks() {
        int count = 0;
        for (T document : this.documents.values()) {
            count += document.getOutboundLinkCount();
        }
        return count;
    }

    /**
     * Gets the total number of wiki links
     * 
     * @return Total wiki links
     */
    public int getTotalWikiLinks() {
        int count = 0;
        for (T document : this.documents.values()) {
            count += document.getOutboundWikiLinkCount();
        }
        return count;
    }

    /**
     * Gets the total number of external links
     * 
     * @return Total external links
     */
    public int getTotalExternalLinks() {
        int count = 0;
        for (T document : this.documents.values()) {
            count += document.getOutboundExternalLinkCount();
        }
        return count;
    }
}
