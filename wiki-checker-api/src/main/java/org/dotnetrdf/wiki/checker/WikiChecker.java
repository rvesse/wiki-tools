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

package org.dotnetrdf.wiki.checker;

import java.io.IOException;
import java.util.Iterator;

import org.dotnetrdf.wiki.checker.checks.DocumentCheck;
import org.dotnetrdf.wiki.checker.checks.LinkCheck;
import org.dotnetrdf.wiki.checker.data.CheckedWiki;
import org.dotnetrdf.wiki.checker.data.documents.CheckedDocument;
import org.dotnetrdf.wiki.checker.data.links.CheckedLink;

/**
 * Interface for wiki checkers
 * 
 * @author rvesse
 * 
 * @param <TLink>
 *            Checked link type
 * @param <TDoc>
 *            Checked document type
 */
public interface WikiChecker<TLink extends CheckedLink, TDoc extends CheckedDocument<TLink>> {

    /**
     * Gets the wiki
     * 
     * @return Wiki
     */
    public abstract CheckedWiki<TLink, TDoc> getWiki();

    /**
     * Adds a document check
     * 
     * @param check
     *            Document check
     */
    public abstract void addDocumentCheck(DocumentCheck check);

    /**
     * Gets an iterator over the registered document checks, must return the
     * checks in the order registered
     * 
     * @return Iterator of document checks
     */
    public abstract Iterator<DocumentCheck> getDocumentChecks();

    /**
     * Adds a link check
     * 
     * @param check
     *            Link check
     */
    public abstract void addLinkCheck(LinkCheck check);

    /**
     * Gets an iterator over the registered link checks, must return the checks
     * in the order registered
     * 
     * @return Iterator of link checks
     */
    public abstract Iterator<LinkCheck> getLinkChecks();

    // TODO Base Directory can likely be safely removed if we add a
    // getInputStream() method to Document

    /**
     * Gets the base directory on disk where the wiki is located
     * 
     * @return Base Directory
     */
    public abstract String getBaseDirectory();

    /**
     * Runs the wiki checker
     * 
     * @throws IOException
     */
    public abstract void run() throws IOException;

    /**
     * Run the wiki checker
     * 
     * @param recheck
     *            Whether previously checked documents should be rechecked, true
     *            forces rechecks, false (default) causes them to be skipped
     * 
     * @throws IOException
     */
    public abstract void run(boolean recheck) throws IOException;
}