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

package org.dotnetrdf.wiki.data.links;

/**
 * Represents a link on the wiki
 * @author rvesse
 *
 */
public class BasicLink implements Link {

    private String path, text;
    private int line = 0, column = 0;
    
    /**
     * Creates a link
     * @param path Path
     * @param text Display Text
     * @param line Line
     * @param column Column
     */
    public BasicLink(String path, String text, int line, int column) {
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
    public BasicLink(String path, int line, int column) {
        this(path, path, line, column);
    }
    
    /* (non-Javadoc)
     * @see org.dotnetrdf.wiki.data.links.Link#getPath()
     */
    @Override
    public String getPath() {
        return this.path;
    }
    
    /* (non-Javadoc)
     * @see org.dotnetrdf.wiki.data.links.Link#isWikiLink()
     */
    @Override
    public boolean isWikiLink() {
        //TODO: Make this a regex based check to see if the path has a scheme component
        return !this.path.startsWith("http") && !this.path.startsWith("mailto:");
    }
    
    /* (non-Javadoc)
     * @see org.dotnetrdf.wiki.data.links.Link#isMailLink()
     */
    @Override
    public boolean isMailLink() {
        return this.path.startsWith("mailto:");
    }
    
    /* (non-Javadoc)
     * @see org.dotnetrdf.wiki.data.links.Link#getText()
     */
    @Override
    public String getText() {
        return this.text;
    }
    
    /* (non-Javadoc)
     * @see org.dotnetrdf.wiki.data.links.Link#hasFriendlyText()
     */
    @Override
    public boolean hasFriendlyText() {
        return !this.text.equals(this.path);
    }
    
    /* (non-Javadoc)
     * @see org.dotnetrdf.wiki.data.links.Link#getLine()
     */
    @Override
    public int getLine() {
        return this.line;
    }
    
    /* (non-Javadoc)
     * @see org.dotnetrdf.wiki.data.links.Link#getColumn()
     */
    @Override
    public int getColumn() {
        return this.column;
    }
    
    @Override
    public String toString() {
        return this.text + (this.text.equals(this.path) ? "" : "(" + this.path + ")") + " [Line " + this.line + " Column " + this.column + "]";
    }
    
    @Override
    public int hashCode() {
        return this.path.hashCode();
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof Link) {
            Link l = (Link)other;
            
            // Equal if and only if path, position and friendly text are equal (or neither have friendly text)
            if (this.getPath().equals(l.getPath())) {
                if (this.getLine() == l.getLine() && this.getColumn() == l.getColumn()) {
                    if (this.hasFriendlyText() && l.hasFriendlyText()) {
                        return this.getText().equals(l.getText());
                    } else if (!this.hasFriendlyText() && !l.hasFriendlyText()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
