

package org.dotnetrdf.wiki.checker.checks;

import org.dotnetrdf.wiki.checker.data.CheckedWiki;
import org.dotnetrdf.wiki.checker.data.documents.CheckedDocument;
import org.dotnetrdf.wiki.checker.data.links.CheckedLink;
import org.dotnetrdf.wiki.checker.issues.Issue;

/**
 * Checks for short documents
 * @author rvesse
 *
 */
public class ShortDocumentCheck implements DocumentCheck {

    @Override
    public <TLink extends CheckedLink, TDoc extends CheckedDocument<TLink>> void check(TDoc document, String text,
            CheckedWiki<TLink, TDoc> wiki) {
        if (document.getFormat().isText() && text != null) {
            if (text.length() < 256) {
                document.addIssue(new Issue("Page has only " + text.length()
                        + " characters, this document may be an incomplete/stub document"));
            }
        }
    }

}
