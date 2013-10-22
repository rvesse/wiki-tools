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

package org.dotnetrdf.wiki.parser;

import java.io.File;
import java.io.FileNotFoundException;
import org.dotnetrdf.wiki.data.Wiki;
import org.dotnetrdf.wiki.data.documents.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract implementation of a wiki scanner that defers creation of documents
 * to derived implementations
 * 
 * @author rvesse
 * @param <T>
 *            Document type
 * 
 */
public abstract class AbstractWikiScanner<T extends Document> implements WikiScanner<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWikiScanner.class);

    public final void scan(Wiki<T> wiki, String directory) throws FileNotFoundException {
        File dir = new File(directory);
        if (dir.exists() && dir.isDirectory()) {
            this.scan(wiki, "", dir, true);
        } else {
            throw new FileNotFoundException("Scan Directory " + directory + " does not exist or is not a directory");
        }
    }

    /**
     * Scans recursively
     * 
     * @param basePath
     *            Base path
     * @param dir
     *            Directory to scan
     * @param top
     *            Whether this is the top level directory
     */
    private void scan(Wiki<T> wiki, String basePath, File dir, boolean top) {
        // Ignore hidden directories
        if (dir.getPath().contains("."))
            return;

        LOGGER.info("Scanning Directory " + dir.getAbsolutePath() + " (Base Path " + basePath + ")");

        // Scan for files and folders in this directory
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                // Scan sub-directory
                this.scan(wiki, basePath + (top ? "" : dir.getName() + "/"), f, false);
            } else {
                // Create document and add to wiki
                T document = createDocument(basePath + (top ? "" : dir.getName() + "/") + f.getName(), f);
                if (!wiki.hasDocument(document.getPath())) {
                    LOGGER.info("Found document " + document.getPath());
                    wiki.addDocument(document);
                }
            }
        }

        LOGGER.info("Finished Directory " + dir.getAbsolutePath() + " (Base Path " + basePath + ")");
    }

    protected abstract T createDocument(String wikiPath, File f);

}
