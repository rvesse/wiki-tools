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
 * A link check which validates wiki links and registers inbound links on the
 * targeted documents
 * 
 * @author rvesse
 * 
 */
public class WikiLinkCheck implements LinkCheck {
    private static final Logger LOGGER = LoggerFactory.getLogger(WikiLinkCheck.class);

    @Override
    public <TLink extends CheckedLink, TDoc extends CheckedDocument<TLink>> void check(TDoc document, TLink link,
            CheckedWiki<TLink, TDoc> wiki) {
        if (!link.isWikiLink())
            return;
        
        // Wiki Link Validation
        String linkPath = link.getPath();
        TDoc target = wiki.getDocument(linkPath);
        if (target == null) {
            // Mark as Broken
            document.addIssue(new Issue("Broken Wiki Link - " + link.toString(), true));
            LOGGER.error("Broken wiki link " + link.toString());
        } else {
            // Mark as Inbound Link on target Page
            // Don't count self referential links in inbound links
            if (!document.getPath().equals(link.getPath())) {
                target.addInboundLink(link);
            }
        }

    }

}
