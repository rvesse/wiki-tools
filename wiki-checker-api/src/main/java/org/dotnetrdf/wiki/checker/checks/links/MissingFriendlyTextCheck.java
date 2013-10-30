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
 * A link check that issues warnings for links without friendly text which are
 * not trivial relative links
 * 
 * @author rvesse
 * 
 */
public class MissingFriendlyTextCheck implements LinkCheck {
    private static final Logger LOGGER = LoggerFactory.getLogger(MissingFriendlyTextCheck.class);

    @Override
    public <TLink extends CheckedLink, TDoc extends CheckedDocument<TLink>> void check(TDoc document, TLink link,
            CheckedWiki<TLink, TDoc> wiki) {
        // Issue warnings for links without friendly text
        // Don't issue a warning if this is a trivial relative link i.e. a link
        // to another page in the same directory
        if (!link.hasFriendlyText() && !link.isMailLink() && (!link.isWikiLink() || link.getPath().contains("/"))) {
            document.addIssue(new Issue("Link does not have friendly text - " + link.toString(), false));
            LOGGER.warn("Link does not have friendly text - " + link.toString());
        }
    }

}
