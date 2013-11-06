/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package org.dotnetrdf.wiki.data.issues;

/**
 * Interface for issues
 * @author rvesse
 *
 */
public interface Issue {

    /**
     * Gets the issue message
     * 
     * @return Message
     */
    public abstract String getMessage();

    /**
     * Gets whether this is an error
     * 
     * @return True if error, false if a warning
     */
    public abstract boolean isError();

    /**
     * Gets whether the issue has an exception associated with it
     * 
     * @return True if an exception is associated with the issue and can be
     *         retrieved using the {@link #getException()} method
     */
    public abstract boolean hasException();

    /**
     * Gets the exception associated with the issue (if any)
     * 
     * @return Exception or null
     */
    public Throwable getException();
}