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

package org.dotnetrdf.wiki.checker.checks.document;

import org.dotnetrdf.wiki.checker.checks.DocumentCheck;
import org.dotnetrdf.wiki.checker.data.CheckedWiki;
import org.dotnetrdf.wiki.checker.data.documents.CheckedDocument;
import org.dotnetrdf.wiki.checker.data.links.CheckedLink;
import org.dotnetrdf.wiki.data.issues.Warning;

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
            document.addIssue(new Warning("Document is poorly linked with only " + document.getInboundLinkCount()
                    + " inbound link(s)"));
    }

}
