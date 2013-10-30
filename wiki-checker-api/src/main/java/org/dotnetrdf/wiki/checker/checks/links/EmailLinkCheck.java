/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package org.dotnetrdf.wiki.checker.checks.links;

import org.dotnetrdf.wiki.checker.checks.LinkCheck;
import org.dotnetrdf.wiki.checker.data.CheckedWiki;
import org.dotnetrdf.wiki.checker.data.documents.CheckedDocument;
import org.dotnetrdf.wiki.checker.data.links.CheckedLink;
import org.dotnetrdf.wiki.checker.issues.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A link check that issues warnings for {@code mailto:} links since these
 * expose email address publicly
 * 
 * @author rvesse
 * 
 */
public class EmailLinkCheck implements LinkCheck {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailLinkCheck.class);

    @Override
    public <TLink extends CheckedLink, TDoc extends CheckedDocument<TLink>> void check(TDoc document, TLink link,
            CheckedWiki<TLink, TDoc> wiki) {
        if (!link.isMailLink())
            return;

        // Warn on mail links
        document.addIssue(new Issue("Email Links expose email address " + link.getPath().substring(7) + " publicly", false));
        LOGGER.warn("Email link exposes email address publicly - " + link.getPath().substring(7));
    }

}
