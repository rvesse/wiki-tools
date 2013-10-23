/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package org.dotnetrdf.wiki.checker.data.documents;

import java.util.Iterator;

import org.dotnetrdf.wiki.checker.issues.Issue;
import org.dotnetrdf.wiki.data.documents.Document;
import org.dotnetrdf.wiki.data.links.Link;

/**
 * Interface for documents that may be checked for issues
 * 
 * @author rvesse
 * 
 * @param <TLink>
 *            Link type
 */
public interface CheckedDocument<TLink extends Link> extends Document<TLink> {

    /**
     * Gets whether the document has been checked
     * 
     * @return True if it has been checked
     */
    public abstract boolean hasBeenChecked();

    /**
     * Adds an issue to the document
     * 
     * @param issue
     *            Issue
     */
    public abstract void addIssue(Issue issue);

    /**
     * Gets whether the document has any issues (errors/warnings)
     * 
     * @return True if there are any issues, false otherwise
     */
    public abstract boolean hasIssues();

    /**
     * Gets whether the document has any error issues
     * 
     * @return True if there are errors, false otherwise
     */
    public abstract boolean hasErrors();

    /**
     * Gets the issues for the document
     * 
     * @return Issues
     */
    public abstract Iterator<Issue> getIssues();

    /**
     * Gets the number of issues identified
     * 
     * @return Number of issues
     */
    public abstract int getIssueCount();

    /**
     * Gets the number of errors identified
     * 
     * @return Number of errors
     */
    public abstract int getErrorCount();

    /**
     * Gets the number of warnings identified
     * 
     * @return Number of warnings
     */
    public abstract int getWarningCount();

    /**
     * Sets whether the document has been checked
     * 
     * @param checked
     *            Check status of the document
     */
    public abstract void setChecked(boolean checked);

}