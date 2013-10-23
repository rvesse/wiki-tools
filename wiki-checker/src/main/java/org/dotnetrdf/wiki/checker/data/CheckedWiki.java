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

package org.dotnetrdf.wiki.checker.data;

import org.dotnetrdf.wiki.checker.data.documents.CheckedDocument;
import org.dotnetrdf.wiki.data.BasicWiki;
import org.dotnetrdf.wiki.data.links.BasicLink;

/**
 * Represents a wiki composed of documents which may be checked and have issues
 * logged against them
 * 
 * @author rvesse
 * 
 * @param <T> Checked document type
 */
public class CheckedWiki<T extends CheckedDocument> extends BasicWiki<BasicLink, T> {

    /**
     * Gets the total number of errors
     * 
     * @return Total errors
     */
    public int getTotalErrors() {
        int count = 0;
        for (T document : this.documents.values()) {
            count += document.getErrorCount();
        }
        return count;
    }

    /**
     * Gets the total number of warnings
     * 
     * @return Total warnings
     */
    public int getTotalWarnings() {
        int count = 0;
        for (T document : this.documents.values()) {
            count += document.getWarningCount();
        }
        return count;
    }
}
