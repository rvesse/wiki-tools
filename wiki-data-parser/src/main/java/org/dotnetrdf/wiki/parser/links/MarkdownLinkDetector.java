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

package org.dotnetrdf.wiki.parser.links;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dotnetrdf.wiki.data.documents.Document;
import org.dotnetrdf.wiki.data.links.Link;

/**
 * Link Detector for Creole pages
 * 
 * @author rvesse
 * 
 */
public class MarkdownLinkDetector extends BaseLinkDetector {

    private Pattern referencesRegex = Pattern.compile("^\\s{0,3}\\[([^\\]]+)\\]:\\s+([^\\s]+)(\\s+(\"[^\"]+\"|'[^']+'|\\([^\\(]+\\)))?", Pattern.MULTILINE);
    private Pattern referenceLinkRegex = Pattern.compile("^\\s{0,3}\\[([^\\]]+)\\] ?\\[([^\\]]*)\\]", Pattern.MULTILINE);
    private Pattern inlineLinkRegex = Pattern.compile("^\\s{0,3}\\[([^\\]]+)\\]\\(([^\\)\\s]+)(\\s(\"[^\"]+\"|'[^']+'|\\([^\\)]+\\)))?\\)", Pattern.MULTILINE);

    @Override
    public void findLinks(Document page, String text) {
        String[] lineData = text.split("\n");

        // First off we want to find numeric link mappings
        Map<String, String> references = new HashMap<String, String>();
        Matcher referencesMatcher = referencesRegex.matcher(text);
        while (referencesMatcher.find()) {
            MatchResult refMatch = referencesMatcher.toMatchResult();

            String refId = refMatch.group(1);
            String uri = refMatch.group(2);
            // Strip off optional angle brackets
            if (uri.startsWith("<") && uri.endsWith(">")) uri = uri.substring(1, uri.length() - 1);

            references.put(refId, uri);
            System.out.println("Reference " + refId + " -> " + uri);
        }

        // Then find reference links
        Matcher linkMatcher = referenceLinkRegex.matcher(text);
        while (linkMatcher.find()) {
            MatchResult linkMatch = linkMatcher.toMatchResult();

            // Find position
            int line = this.calculateLine(lineData, linkMatch.start());
            int col = this.calculateColumn(lineData, linkMatch.start());

            // Track as a link
            String linkText = linkMatch.group(1);
            String refId = linkMatch.group(2);
            // Empty reference uses the link text as the reference
            if (refId.length() == 0) refId = linkText;

            if (references.containsKey(refId)) {
                page.addOutboundLink(new Link(references.get(refId), linkText, line, col));
            }
            // TODO Really should handle missing targets somehow
        }

        // Finally find inline links
        linkMatcher = inlineLinkRegex.matcher(text);
        while (linkMatcher.find()) {
            MatchResult linkMatch = linkMatcher.toMatchResult();

            // Find position
            int line = this.calculateLine(lineData, linkMatch.start());
            int col = this.calculateColumn(lineData, linkMatch.start());

            // Track as a link
            String linkText = linkMatch.group(1);
            String linkPath = linkMatch.group(2);
            page.addOutboundLink(new Link(linkPath, linkText, line, col));
        }
    }

}
