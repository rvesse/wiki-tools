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

package org.dotnetrdf.wiki.parser.links;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dotnetrdf.wiki.data.documents.Document;
import org.dotnetrdf.wiki.data.links.BasicLink;

/**
 * Simple link detector for files, this simply finds http://, https:// and mailto: links in the text
 * @author rvesse
 *
 */
public class SimpleLinkDetector extends BaseLinkDetector {

    private Pattern linkRegex = Pattern.compile("(https?://|mailto:)[^\\s]+");

    @Override
    public void findLinks(Document page, String text) {
        String[] lineData = text.split("\n");
        Matcher linkMatcher = linkRegex.matcher(text);
        while (linkMatcher.find()) {
            MatchResult linkMatch = linkMatcher.toMatchResult();

            // Find position
            int line = this.calculateLine(lineData, linkMatch.start());
            int col = this.calculateColumn(lineData, linkMatch.start());

            // Find link information and track as a link
            String linkText = linkMatch.group().toString();
            linkText = linkText.substring(2, linkText.length() - 2);
            page.addOutboundLink(new BasicLink(linkText, line, col));
        }
    }


}
