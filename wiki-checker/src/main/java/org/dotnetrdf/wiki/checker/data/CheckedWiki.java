/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package org.dotnetrdf.wiki.checker.data;

import org.dotnetrdf.wiki.checker.data.documents.CheckedDocument;
import org.dotnetrdf.wiki.data.BasicWiki;

/**
 * Represents a wiki composed of documents which may be checked and have issues
 * logged against them
 * 
 * @author rvesse
 * 
 * @param <T>
 */
public class CheckedWiki<T extends CheckedDocument> extends BasicWiki<T> {

    /**
     * Gets the total number of errors
     * 
     * @return Total errors
     */
    public int getTotalErrors() {
        int count = 0;
        for (T document : this.documents.values()) {
            count += document.getErrorCount();
        }
        return count;
    }

    /**
     * Gets the total number of warnings
     * 
     * @return Total warnings
     */
    public int getTotalWarnings() {
        int count = 0;
        for (T document : this.documents.values()) {
            count += document.getWarningCount();
        }
        return count;
    }
}
