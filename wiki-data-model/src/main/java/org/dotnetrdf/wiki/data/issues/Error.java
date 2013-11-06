/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package org.dotnetrdf.wiki.data.issues;

/**
 * Represents an error
 * 
 * @author rvesse
 * 
 */
public class Error extends AbstractIssue {

    /**
     * Creates a new error
     * 
     * @param message
     *            Message
     * @param e
     *            Exception
     */
    public Error(String message, Throwable e) {
        super(message, true, e);
    }

    /**
     * Creates a new error
     * 
     * @param message
     *            Message
     */
    public Error(String message) {
        super(message, true, null);
    }
}
