/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package org.dotnetrdf.wiki.checker.checks.document;

import org.dotnetrdf.wiki.checker.checks.DocumentCheck;
import org.dotnetrdf.wiki.checker.data.CheckedWiki;
import org.dotnetrdf.wiki.checker.data.documents.CheckedDocument;
import org.dotnetrdf.wiki.checker.data.links.CheckedLink;
import org.dotnetrdf.wiki.checker.issues.Issue;

/**
 * Document check which issues warnings for documents that don't have more
 * incoming links than a given threshold
 * 
 * @author rvesse
 * 
 */
public class PoorlyLinkedDocumentCheck implements DocumentCheck {

    /**
     * Default threshold at/below which a document is considered poorly linked
     */
    public static final int DEFAULT_POOR_LINK_THRESHOLD = 1;

    private int threshold;

    /**
     * Creates a new check using the default threshold provided by
     * {@link #DEFAULT_POOR_LINK_THRESHOLD}
     */
    public PoorlyLinkedDocumentCheck() {
        this(DEFAULT_POOR_LINK_THRESHOLD);
    }

    /**
     * Creates a new check using the provided threshold
     * 
     * @param threshold
     *            Threshold, must be >= 1
     */
    public PoorlyLinkedDocumentCheck(int threshold) {
        if (threshold < 1)
            throw new IllegalArgumentException("Threshold must be >= 1");
        this.threshold = threshold;
    }

    @Override
    public <TLink extends CheckedLink, TDoc extends CheckedDocument<TLink>> void check(TDoc document, String text,
            CheckedWiki<TLink, TDoc> wiki) {
        if (document.getInboundLinkCount() > 0 && document.getInboundLinkCount() <= this.threshold)
            document.addIssue(new Issue("Document is poorly linked with only " + document.getInboundLinkCount()
                    + " inbound link(s)", false));
    }

}
