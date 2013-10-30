/**
 * Copyright (c) 2013 dotNetRDF Project (dotnetrdf-develop@lists.sf.net)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */

package org.dotnetrdf.wiki.checker;

import org.dotnetrdf.wiki.checker.checks.document.IsolatedDocumentCheck;
import org.dotnetrdf.wiki.checker.checks.document.PoorlyLinkedDocumentCheck;
import org.dotnetrdf.wiki.checker.checks.document.ShortDocumentCheck;
import org.dotnetrdf.wiki.checker.checks.links.EmailLinkCheck;
import org.dotnetrdf.wiki.checker.checks.links.ExternalLinkCheck;
import org.dotnetrdf.wiki.checker.checks.links.MissingFriendlyTextCheck;
import org.dotnetrdf.wiki.checker.checks.links.WikiLinkCheck;
import org.dotnetrdf.wiki.checker.data.CheckedWiki;
import org.dotnetrdf.wiki.checker.data.documents.CheckedDocument;
import org.dotnetrdf.wiki.checker.data.links.CheckedLink;
import org.dotnetrdf.wiki.checker.parser.CheckedWikiScanner;

/**
 * A basic wiki checker implementation which has the standard set of link and
 * document checks already registered
 * 
 * @author rvesse
 * @param <TLink>
 *            Checked link type
 * @param <TDoc>
 *            Checked document type
 * 
 */
public class BasicWikiChecker<TLink extends CheckedLink, TDoc extends CheckedDocument<TLink>> extends
        AbstractWikiChecker<TLink, TDoc> {

    /**
     * Creates a new document checker
     * 
     * @param wiki
     *            Wiki which is presumed to have already been populated by an
     *            appropriate {@link CheckedWikiScanner}
     * @param dir
     *            Base Directory
     */
    public BasicWikiChecker(CheckedWiki<TLink, TDoc> wiki, String dir) {
        super(wiki, dir);
        
        // Standard link checks
        this.addLinkCheck(new MissingFriendlyTextCheck());
        this.addLinkCheck(new WikiLinkCheck());
        this.addLinkCheck(new EmailLinkCheck());
        this.addLinkCheck(new ExternalLinkCheck());
        
        // Standard document checks
        this.addDocumentCheck(new ShortDocumentCheck());
        this.addDocumentCheck(new IsolatedDocumentCheck());
        this.addDocumentCheck(new PoorlyLinkedDocumentCheck());
    }
}
