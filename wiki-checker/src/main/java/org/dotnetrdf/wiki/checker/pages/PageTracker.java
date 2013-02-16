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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Tracks all the detected wiki pages, whether they have been validated and any
 * issues associated with them
 * 
 * @author rvesse
 * 
 */
public class PageTracker {

    private Map<String, Page> pages = new HashMap<String, Page>();
    
    /**
     * Creates a new Page Tracker
     */
    public PageTracker() {
        
    }
    
    /**
     * Discovers pages by scanning a directory
     * @param directory Directory
     * @param quiet Quiet Mode
     * @throws FileNotFoundException Thrown if the specified directory does not exist
     */
    void scan(String directory, boolean quiet) throws FileNotFoundException {
        File dir = new File(directory);
        if (dir.exists() && dir.isDirectory()) {
            this.scan("", dir, true, quiet);
        } else {
            throw new FileNotFoundException("Scan Directory " + directory + " does not exist or is not a directory");
        }
    }
    
    /**
     * Scans recursively
     * @param basePath Base path
     * @param dir Directory to scan
     * @param top Whether this is the top level directory
     */
    private void scan(String basePath, File dir, boolean top, boolean quiet) {
        // Ignore hidden directories
        if (dir.getPath().contains(".")) return;
        
        if (!quiet) System.out.println("Scanning Directory " + dir.getAbsolutePath() + " (Base Path " + basePath + ")");
        
        // Scan for files and folders in this directory
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                // Scan sub-directory
                this.scan(basePath + (top ? "" : dir.getName() + File.separator), f, false, quiet);
            } else if (f.getName().endsWith(".wiki")) {
                // Track page
                Page page = new Page(basePath + (top ? "" : dir.getName() + File.separator) + f.getName());
                if (!pages.containsKey(page.getPath())) {
                    if (!quiet) System.out.println("Found page " + page.getPath());
                    pages.put(page.getPath(), page);
                }
            }
        }
        
        if (!quiet) System.out.println("Finished Directory " + dir.getAbsolutePath() + " (Base Path " + basePath + ")");
    }
    
    /**
     * Gets whether a page with the given path exists
     * @param path Path
     * @return True if the page exists, false otherwise
     */
    public boolean hasPage(String path) {
        return this.pages.containsKey(path);
    }
    
    /**
     * Gets the pages that are known to exist
     * @return Pages
     */
    public Iterator<Page> getPages() {
        return this.pages.values().iterator();
    }
    
    /**
     * Gets the page count
     * @return Page Count
     */
    public int getPageCount() {
        return this.pages.size();
    }
}
