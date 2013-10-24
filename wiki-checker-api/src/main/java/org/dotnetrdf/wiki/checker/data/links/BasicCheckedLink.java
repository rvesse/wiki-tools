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

package org.dotnetrdf.wiki.checker.data.links;

import org.dotnetrdf.wiki.data.links.BasicLink;

/**
 * Basic implementation of a checked link
 * 
 * @author rvesse
 * 
 */
public class BasicCheckedLink extends BasicLink implements CheckedLink {

    /**
     * Creates a link
     * 
     * @param path
     *            Path
     * @param line
     *            Line
     * @param column
     *            Column
     */
    public BasicCheckedLink(String path, int line, int column) {
        super(path, line, column);
    }

    /**
     * Creates a link
     * 
     * @param path
     *            Path
     * @param text
     *            Display Text
     * @param line
     *            Line
     * @param column
     *            Column
     */
    public BasicCheckedLink(String path, String text, int line, int column) {
        super(path, text, line, column);
    }

}