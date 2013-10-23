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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dotnetrdf.wiki.data.documents.Document;
import org.dotnetrdf.wiki.data.links.BasicLink;

/**
 * Link Detector for Creole pages
 * 
 * @author rvesse
 * 
 */
public class MarkdownLinkDetector extends BaseLinkDetector {

    private Pattern referencesRegex = Pattern.compile("\\[([^\\]]+)\\]:\\s+([^\\s]+)(\\s+(\"[^\"]+\"|'[^']+'|\\([^\\(]+\\)))?",
            Pattern.MULTILINE);
    private Pattern referenceLinkRegex = Pattern.compile("\\[([^\\]]+)\\] ?\\[([^\\]]*)\\]", Pattern.MULTILINE);
    private Pattern inlineLinkRegex = Pattern.compile(
            "\\[([^\\]]+)\\]\\(([^\\)\\s]+)(\\s(\"[^\"]+\"|'[^']+'|\\([^\\)]+\\)))?\\)", Pattern.MULTILINE);
    private Pattern autoLinkRegex = Pattern.compile("[^<\\n\\r]*<([^>]+)>", Pattern.MULTILINE);

    /**
     * Is the current line pre-formatted?
     * 
     * @param lineData
     *            Line data
     * @param line
     *            Line number (1 based index)
     * @return True if the line is pre-formatted and thus links on it should be
     *         ignored, false otherwise
     */
    private boolean isPreformattedLine(String[] lineData, int line) {
        String lineText = lineData[line - 1];
        if (lineText.length() < 4)
            return false;

        char[] cs = lineText.toCharArray();
        for (int i = 0; i < 4; i++) {
            char c = cs[i];
            switch (c) {
            case ' ':
            case '\t':
                continue;
            default:
                return false;
            }
        }
        return true;
    }

    private String applyMarkdownEscapes(String[] lineData, String text) {
        // Escape backslash first
        text = applyEscape(lineData, text, "\\\\", "&#5C;");
        // Escape \[
        text = applyEscape(lineData, text, "\\[", "&#5B;");
        // Escape \]
        text = applyEscape(lineData, text, "\\]", "&#5D;");
        // Escape \(
        text = applyEscape(lineData, text, "\\(", "&#28;");
        // Escape \)
        text = applyEscape(lineData, text, "\\)", "&#29;");
        return text;
    }

    private String applyEscape(String[] lineData, String text, String find, String replace) {
        int start = 0;
        int index = text.indexOf(find, start);
        while (index >= 0 && start < text.length()) {
            int line = this.calculateLine(lineData, index);
            int column = this.calculateColumn(lineData, index);
            if (this.isPreformattedLine(lineData, line)) {
                start += find.length();
            } else {
                lineData[line - 1] = lineData[line - 1].substring(0, column - 1) + replace + lineData[line - 1].substring(column - 1 + find.length());
                text = text.substring(0, index) + replace + text.substring(index + find.length());
                start += replace.length();
            }
            index = text.indexOf(find, start);
        }

        return text;
    }

    @Override
    public void findLinks(Document page, String text) {
        // Apply relevant escapes
        String[] lineData = text.split("\n");
        text = applyMarkdownEscapes(lineData, text);
        lineData = text.split("\n");

        // First off we want to find reference link mappings
        Map<String, String> references = new HashMap<String, String>();
        Set<Integer> refLines = new HashSet<Integer>();
        Matcher referencesMatcher = referencesRegex.matcher(text);
        while (referencesMatcher.find()) {
            MatchResult refMatch = referencesMatcher.toMatchResult();

            // Find position
            int line = this.calculateLine(lineData, refMatch.start());
            if (this.isPreformattedLine(lineData, line))
                continue;
            refLines.add(line);

            String refId = refMatch.group(1);
            String uri = refMatch.group(2);
            // Strip off optional angle brackets
            if (uri.startsWith("<") && uri.endsWith(">"))
                uri = uri.substring(1, uri.length() - 1);

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

            if (this.isPreformattedLine(lineData, line))
                continue;

            // Track as a link
            String linkText = linkMatch.group(1);
            String refId = linkMatch.group(2);
            // Empty reference uses the link text as the reference
            if (refId.length() == 0)
                refId = linkText;

            if (references.containsKey(refId)) {
                page.addOutboundLink(new BasicLink(references.get(refId), linkText, line, col));
            }
            // TODO Really should handle missing targets somehow
        }

        // Next find inline links
        linkMatcher = inlineLinkRegex.matcher(text);
        while (linkMatcher.find()) {
            MatchResult linkMatch = linkMatcher.toMatchResult();

            // Find position
            int line = this.calculateLine(lineData, linkMatch.start());
            int col = this.calculateColumn(lineData, linkMatch.start());

            if (this.isPreformattedLine(lineData, line))
                continue;

            // Track as a link
            String linkText = linkMatch.group(1);
            String linkPath = linkMatch.group(2);
            page.addOutboundLink(new BasicLink(linkPath, linkText, line, col));
        }

        // Finally find auto links
        linkMatcher = autoLinkRegex.matcher(text);
        while (linkMatcher.find()) {
            MatchResult linkMatch = linkMatcher.toMatchResult();

            // Find position
            int line = this.calculateLine(lineData, linkMatch.start());
            int col = this.calculateColumn(lineData, linkMatch.start());

            if (this.isPreformattedLine(lineData, line))
                continue;

            // Ignore if this is actually just a link reference
            if (refLines.contains(line))
                continue;

            // Track as link
            page.addOutboundLink(new BasicLink(linkMatch.group(1), line, col));
        }
    }

}
