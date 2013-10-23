package org.dotnetrdf.wiki.checker.data.links;

import org.dotnetrdf.wiki.data.links.BasicLink;

/**
 * Basic implementation of a checked link
 * 
 * @author rvesse
 * 
 */
public class BasicCheckedLink extends BasicLink implements CheckedLink {

    /**
     * Creates a link
     * 
     * @param path
     *            Path
     * @param line
     *            Line
     * @param column
     *            Column
     */
    public BasicCheckedLink(String path, int line, int column) {
        super(path, line, column);
    }

    /**
     * Creates a link
     * 
     * @param path
     *            Path
     * @param text
     *            Display Text
     * @param line
     *            Line
     * @param column
     *            Column
     */
    public BasicCheckedLink(String path, String text, int line, int column) {
        super(path, text, line, column);
    }

}
