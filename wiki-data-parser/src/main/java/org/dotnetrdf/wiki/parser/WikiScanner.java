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

import java.io.FileNotFoundException;
import org.dotnetrdf.wiki.data.Wiki;
import org.dotnetrdf.wiki.data.documents.Document;
import org.dotnetrdf.wiki.data.links.Link;

/**
 * Interface for wiki scanners which take in a directory and populate a
 * {@link Wiki} instance
 * 
 * @author rvesse
 * @param <TLink> Link type
 * @param <TDoc>
 *            Document type
 * 
 */
public interface WikiScanner<TLink extends Link, TDoc extends Document<TLink>> {

    /**
     * Discovers documents in the wiki by scanning a directory
     * 
     * @param wiki
     *            Wiki to populate
     * @param directory
     *            Directory
     * @throws FileNotFoundException
     *             Thrown if the specified directory does not exist
     */
    public void scan(Wiki<TLink, TDoc> wiki, String directory) throws FileNotFoundException;

}
