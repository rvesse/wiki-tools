/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package org.dotnetrdf.wiki.checker.checks.document;

import org.dotnetrdf.wiki.checker.checks.DocumentCheck;
import org.dotnetrdf.wiki.checker.data.CheckedWiki;
import org.dotnetrdf.wiki.checker.data.documents.CheckedDocument;
import org.dotnetrdf.wiki.checker.data.links.CheckedLink;
import org.dotnetrdf.wiki.checker.issues.Issue;
import org.dotnetrdf.wiki.data.documents.formats.DataFormat;

/**
 * Document check that looks for use of the BitBucket Creole {@code <<toc>>}
 * macro in pages that aren't at the top level since Bitbucket has known issues
 * with generates bad TOCs for these pages
 * 
 * @author rvesse
 * 
 */
public class BitbucketCreoleTocMacroUsageCheck implements DocumentCheck {

    @Override
    public <TLink extends CheckedLink, TDoc extends CheckedDocument<TLink>> void check(TDoc document, String text,
            CheckedWiki<TLink, TDoc> wiki) {
        if (!document.isTopLevel() && document.getFormat().getDataFormat().equals(DataFormat.CREOLE) && text.contains("<<toc")) {
            document.addIssue(new Issue(
                    "Non-top level document uses the TOC macro, if this is used to list directory contents the generated links will be incorrect, see BitBucket Issue #2224",
                    false));
        }
    }

}
