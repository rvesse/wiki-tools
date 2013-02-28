/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */ 

package org.dotnetrdf.wiki.checker.links;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dotnetrdf.wiki.checker.pages.Page;

/**
 * Simple link detector for files, this simply finds http://, https:// and mailto: links in the text
 * @author rvesse
 *
 */
public class SimpleLinkDetector extends BaseLinkDetector {

    private Pattern linkRegex = Pattern.compile("(https?://|mailto:)[^\\s]+");

    @Override
    public void findLinks(Page page, String text) {
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
            page.addOutboundLink(new Link(linkText, line, col));
        }
    }


}
