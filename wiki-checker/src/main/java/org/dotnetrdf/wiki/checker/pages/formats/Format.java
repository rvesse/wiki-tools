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

package org.dotnetrdf.wiki.checker.pages.formats;

import org.dotnetrdf.wiki.checker.links.LinkDetector;


/**
 * Represents a format
 * @author rvesse
 *
 */
public class Format {

    private DataFormat fmt;
    private String name;
    private LinkDetector linkDetector;
    
    /**
     * Creates new format information
     * @param format Data Format
     * @param name Friendly name for the format
     * @param detector Link Detector
     */
    public Format(DataFormat format, String name, LinkDetector detector) {
        this.fmt = format;
        this.name = name;
        this.linkDetector = detector;
    }
    
    /**
     * Gets the data format
     * @return Data Format
     */
    public DataFormat getDataFormat() {
        return this.fmt;
    }
    
    /**
     * Gets the friendly name
     * @return Friendly name
     */
    public String getFriendlyName() {
        return this.name;
    }
    
    /**
     * Gets the link detector for the format (if any)
     * @return Link Detector or null
     */
    public LinkDetector getLinkDetector() {
        return this.linkDetector;
    }
    
    @Override
    public String toString() {
        return this.getFriendlyName();
    }
}
