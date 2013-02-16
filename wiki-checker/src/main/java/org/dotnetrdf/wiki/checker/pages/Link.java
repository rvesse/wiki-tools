/**
 * Copyright (c) 2013 dotNetRDF Project (dotnetrdf-developer@lists.sf.net)
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

package org.dotnetrdf.wiki.checker.pages;

/**
 * Represents a link on the wiki
 * @author rvesse
 *
 */
public class Link {

    private String path, text;
    private int line = 0, column = 0;
    
    /**
     * Creates a link
     * @param path Path
     * @param text Display Text
     * @param line Line
     * @param column Column
     */
    public Link(String path, String text, int line, int column) {
        this.path = path;
        this.text = text;
        this.line = line;
        this.column = column;
    }
    
    /**
     * Creates a link
     * @param path Path
     * @param line Line
     * @param column Column
     */
    public Link(String path, int line, int column) {
        this(path, path, line, column);
    }
    
    /**
     * Gets the path i.e. URL of the link, may be absolute or relative
     * @return Path
     */
    public String getPath() {
        return this.path;
    }
    
    /**
     * Gets whether this is a wiki link
     * @return True if a wiki link
     */
    public boolean isWikiLink() {
        return !this.path.startsWith("http");
    }
    
    /**
     * Gets the display text of the link
     * @return Display Text
     */
    public String getText() {
        return this.text;
    }
    
    /**
     * Gets the line the link occurs on
     * @return Line
     */
    public int getLine() {
        return this.line;
    }
    
    /**
     * Gets the column the link occurs at
     * @return Column
     */
    public int getColumn() {
        return this.column;
    }
    
    @Override
    public String toString() {
        return this.text + (this.text.equals(this.path) ? "" : "(" + this.path + ")") + " [Line " + this.line + " Column " + this.column + "]";
    }
}
