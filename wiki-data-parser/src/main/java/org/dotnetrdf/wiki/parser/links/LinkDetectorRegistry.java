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

import java.util.HashMap;
import java.util.Map;

import org.dotnetrdf.wiki.data.documents.formats.DocumentFormatRegistry;
import org.dotnetrdf.wiki.data.documents.formats.Format;

/**
 * Registry of link detectors
 * 
 * @author rvesse
 * 
 */
@SuppressWarnings("rawtypes")
public class LinkDetectorRegistry {

    private static boolean init = false;
    private static Map<Format, LinkDetector> detectors;

    /**
     * Private constructor prevents instantiation
     */
    private LinkDetectorRegistry() {
    }

    /**
     * Initializes the registry
     */
    private static synchronized void init() {
        if (init)
            return;

        detectors = new HashMap<Format, LinkDetector>();

        // Creole
        detectors.put(Format.CREOLE, new CreoleLinkDetector());

        // Markdown
        detectors.put(Format.MARKDOWN, new MarkdownLinkDetector());

        // Restructured Text
        // detectors.put(Format.RESTRUCTURED_TEXT, null);

        // Textile
        // detectors.put(Format.TEXTILE, null);

        // Plain text
        detectors.put(Format.PLAIN_TEXT, new SimpleLinkDetector());

        // Images
        detectors.put(Format.IMAGE, new NoLinkDetector());

        init = true;
    }

    /**
     * Gets a link detector based on the file name
     * 
     * @param filename
     *            File name
     * @return Link detector, null if none is available
     */
    public static LinkDetector getLinkDetector(String filename) {
        return getLinkDetector(DocumentFormatRegistry.getFormat(filename));
    }

    /**
     * Gets a link detector based on the format
     * 
     * @param format
     *            Format
     * @return Link detector, null if none is available
     */
    public static LinkDetector getLinkDetector(Format format) {
        if (!init)
            init();
        if (format == null)
            return null;
        return detectors.get(format);
    }
}
