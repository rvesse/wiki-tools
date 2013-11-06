/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */ 

package org.dotnetrdf.wiki.data.issues;

/**
 * Represents a warning
 * @author rvesse
 *
 */
public class Warning extends AbstractIssue {

    /**
     * Creates a new warning
     * @param message
     */
    public Warning(String message) {
        super(message, false, null);
    }
}
