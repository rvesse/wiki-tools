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
import org.dotnetrdf.wiki.checker.data.AbstractCheckedWiki;
import org.dotnetrdf.wiki.checker.data.documents.CheckedDocument;
import org.dotnetrdf.wiki.checker.data.links.CheckedLink;
import org.dotnetrdf.wiki.data.issues.Issue;
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
            AbstractCheckedWiki<TLink, TDoc> wiki) {
        // Issue warnings for links without friendly text
        // Don't issue a warning if this is a trivial relative link i.e. a link
        // to another page in the same directory
        if (!link.hasFriendlyText() && !link.isMailLink() && (!link.isWikiLink() || link.getPath().contains("/"))) {
            document.addIssue(new Issue("Link does not have friendly text - " + link.toString(), false));
            LOGGER.warn("Link does not have friendly text - " + link.toString());
        }
    }

}
