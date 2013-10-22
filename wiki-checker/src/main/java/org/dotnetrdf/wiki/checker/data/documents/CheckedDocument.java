/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package org.dotnetrdf.wiki.checker.data.documents;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dotnetrdf.wiki.checker.issues.Issue;
import org.dotnetrdf.wiki.data.documents.BasicDocument;
import org.dotnetrdf.wiki.data.documents.formats.Format;

/**
 * A document which can be checked and have issues logged against it
 * 
 * @author rvesse
 * 
 */
public class CheckedDocument extends BasicDocument {

    private boolean checked = false;
    private List<Issue> issues = new ArrayList<Issue>();

    /**
     * Creates a document
     * 
     * @param path
     *            Path to the document
     * @param format
     *            Format of the document
     */
    public CheckedDocument(String path, Format format) {
        super(path, format);
    }

    /**
     * Gets whether the document has been checked
     * 
     * @return True if it has been checked
     */
    public boolean hasBeenChecked() {
        return this.checked;
    }

    /**
     * Sets whether the document has been checked
     * 
     * @param checked
     *            Check status of the document
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    /**
     * Adds an issue to the document
     * 
     * @param issue
     *            Issue
     */
    public void addIssue(Issue issue) {
        this.issues.add(issue);
    }

    /**
     * Gets whether the document has any issues (errors/warnings)
     * 
     * @return True if there are any issues, false otherwise
     */
    public boolean hasIssues() {
        return this.issues.size() > 0;
    }

    /**
     * Gets whether the document has any error issues
     * 
     * @return True if there are errors, false otherwise
     */
    public boolean hasErrors() {
        for (Issue issue : this.issues) {
            if (issue.isError())
                return true;
        }
        return false;
    }

    /**
     * Gets the issues for the document
     * 
     * @return Issues
     */
    public Iterator<Issue> getIssues() {
        return this.issues.iterator();
    }

    /**
     * Gets the number of issues identified
     * 
     * @return Number of issues
     */
    public int getIssueCount() {
        return this.issues.size();
    }

    /**
     * Gets the number of errors identified
     * 
     * @return Number of errors
     */
    public int getErrorCount() {
        int count = 0;
        for (Issue i : this.issues) {
            if (i.isError())
                count++;
        }
        return count;
    }

    /**
     * Gets the number of warnings identified
     * 
     * @return Number of warnings
     */
    public int getWarningCount() {
        int count = 0;
        for (Issue i : this.issues) {
            if (!i.isError())
                count++;
        }
        return count;
    }

    @Override
    public String toString() {
        if (this.checked) {
            return this.getPath() + " (Format: " + this.format.toString() + " with " + this.links.size()
                    + " Outbound Link(s) with " + this.issues.size() + " Issue(s) and " + this.inboundLinks.size()
                    + " Inbound Link(s))";
        } else {
            return this.getPath() + " (Format: " + this.format.toString() + " Unchecked)";
        }
    }
}
