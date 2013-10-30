/*
Copyright (c) 2013 dotNetRDF Project (dotnetrdf-develop@lists.sf.net)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is furnished
to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

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
