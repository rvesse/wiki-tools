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

package org.dotnetrdf.wiki.parser.links;

/**
 * Abstract base implementation of a link detector
 * @author rvesse
 *
 */
public abstract class BaseLinkDetector implements LinkDetector {


    /**
     * Calculates the line number from an offset
     * @param lines Line data
     * @param offset Offset
     * @return Line Number
     */
    protected final int calculateLine(String[] lines, int offset) {
        int line = 0;
        int count = 0;
        while (count < offset) {
            int len = lines[line].length();
            if (count + len >= offset)
                return line + 1;
            count += len + 1; // The +1 is for the \n
            line++;
        }
        return line + 1;
    }

    /**
     * Calculates a column number from an offset
     * @param lines Line data
     * @param offset Offset
     * @return Column Number
     */
    protected final int calculateColumn(String[] lines, int offset) {
        int line = 0;
        int count = 0;
        while (count < offset) {
            int len = lines[line].length();
            if (count + len >= offset)
                return offset - count;
            count += len + 1; // The +1 is for the \n
        }
        return 1;
    }
}
