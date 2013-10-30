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
 * Document check that issues warnings for documents which have no incoming
 * links
 * 
 * @author rvesse
 * 
 */
public class IsolatedDocumentCheck implements DocumentCheck {

    @Override
    public <TLink extends CheckedLink, TDoc extends CheckedDocument<TLink>> void check(TDoc document, String text,
            CheckedWiki<TLink, TDoc> wiki) {
        if (document.getInboundLinkCount() == 0)
            document.addIssue(new Issue("Document is isolated,  no inbound links to this document were found", true));
    }

}
