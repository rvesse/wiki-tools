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

package org.dotnetrdf.wiki.data.documents.formats;

/**
 * Represents a document format
 * 
 * @author rvesse
 * 
 */
public class Format {
    
    public static Format UNKNOWN = new Format(DataFormat.UNKNOWN, "Unknown Format", false, false);
    public static Format CREOLE = new Format(DataFormat.CREOLE, "Creole");
    public static Format MARKDOWN = new Format(DataFormat.MARKDOWN, "Markdown");
    public static Format RESTRUCTURED_TEXT = new Format(DataFormat.RESTRUCTURED_TEXT, "reStructuredText");
    public static Format TEXTILE = new Format(DataFormat.TEXTILE, "Textile");
    public static Format PLAIN_TEXT = new Format(DataFormat.PLAIN_TEXT, "Plain Text", true, false);
    public static Format IMAGE = new Format(DataFormat.IMAGE, "Image", false, false);
    

    private DataFormat fmt;
    private String name;
    private boolean isText = true, isWiki = true;

    /**
     * Creates new format information
     * 
     * @param format
     *            Data Format
     * @param name
     *            Friendly name for the format
     */
    public Format(DataFormat format, String name) {
        this(format, name, true, true);
    }

    /**
     * Creates new format information
     * 
     * @param format
     *            Data Format
     * @param name
     *            Friendly name for the format
     * @param isText
     *            Whether this is a text format
     * @param isWiki
     *            Whether this is a wiki format
     */
    public Format(DataFormat format, String name, boolean isText, boolean isWiki) {
        this.fmt = format;
        this.name = name;
        this.isText = isText;
        this.isWiki = isWiki;
    }

    /**
     * Gets the data format
     * 
     * @return Data Format
     */
    public DataFormat getDataFormat() {
        return this.fmt;
    }

    /**
     * Gets the friendly name
     * 
     * @return Friendly name
     */
    public String getFriendlyName() {
        return this.name;
    }

    /**
     * Is this a text format?
     * 
     * @return True if a text format, false otherwise
     */
    public boolean isText() {
        return this.isText;
    }

    /**
     * Is this a wiki format?
     * 
     * @return True if a wiki format, false otherwise
     */
    public boolean isWiki() {
        return this.isWiki;
    }

    @Override
    public String toString() {
        return this.getFriendlyName();
    }
}
