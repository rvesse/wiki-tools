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

package org.dotnetrdf.wiki.data.documents.formats;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for formats
 * @author rvesse
 *
 */
public class DocumentFormatRegistry {

    private static boolean init = false;
    private static Map<String, Format> formats = new HashMap<String, Format>();
    
    /**
     * Initialize the registry
     */
    public static synchronized void init() {
        if (init) return;
                
        //Creole
        formats.put(".wiki", Format.CREOLE);
        
        //Markdown
        Format md = Format.MARKDOWN;
        formats.put(".md", md);
        formats.put(".mkd", md);
        formats.put(".mkdn", md);
        formats.put(".mdown", md);
        formats.put(".markdown", md); 
        formats.put(".text", md);
        
        //reStructuredText
        formats.put(".rst", Format.RESTRUCTURED_TEXT);
        
        //Textile
        formats.put(".textile", Format.TEXTILE);
        
        //Plain Text
        formats.put(".txt", Format.PLAIN_TEXT);
        
        //Images
        Format img = Format.IMAGE;
        formats.put(".jpg", img);
        formats.put(".jpeg", img);
        formats.put(".png", img);
        formats.put(".gif", img);
        formats.put(".bmp", img);
        formats.put(".tiff", img);
        formats.put(".raw", img);
    }
    
    /**
     * Gets the format based upon a filename
     * @param filename Filename
     * @return Format
     */
    public static Format getFormat(String filename) {
        if (!init) DocumentFormatRegistry.init();
        if (!filename.contains(".")) return Format.UNKNOWN;
        Format fmt = formats.get(filename.substring(filename.lastIndexOf('.')));
        if (fmt != null) return fmt;
        return Format.UNKNOWN;
    }
}
