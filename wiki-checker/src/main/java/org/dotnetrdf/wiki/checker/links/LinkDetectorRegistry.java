/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package org.dotnetrdf.wiki.checker.links;

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
        // detectors.put(Format.MARKDOWN, null);

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

    public static LinkDetector getLinkDetector(String filename) {
        return getLinkDetector(DocumentFormatRegistry.getFormat(filename));
    }

    /**
     * Gets a link detector based on the format
     * 
     * @param format
     *            Format
     * @return Link detector
     */
    public static LinkDetector getLinkDetector(Format format) {
        if (!init)
            init();
        if (format == null)
            return null;
        return detectors.get(format);
    }
}
