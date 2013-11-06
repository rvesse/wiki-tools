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

package org.dotnetrdf.wiki.data.issues;

/**
 * Represents an issue with a page
 * 
 * @author rvesse
 * 
 */
public abstract class AbstractIssue implements Issue {

    private Throwable exception;
    private boolean error = true;
    private String message;

    /**
     * Creates an Issue
     * 
     * @param message
     *            Message
     * @param isError
     *            Whether this should be treated as an error
     * @param e
     *            Exception to associate with the issue
     */
    public AbstractIssue(String message, boolean isError, Throwable e) {
        this.message = message;
        this.error = isError;
        this.exception = e;
    }

    /**
     * Creates an Issue
     * 
     * @param message
     *            Message
     * @param isError
     *            Whether this should be treated as an error
     */
    public AbstractIssue(String message, boolean isError) {
        this(message, isError, null);
    }

    /**
     * Creates a warning
     * 
     * @param message
     *            Warning Message
     */
    public AbstractIssue(String message) {
        this(message, false);
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public boolean isError() {
        return this.error;
    }
    
    @Override
    public boolean hasException() {
        return this.exception != null;
    }
    
    @Override
    public Throwable getException() {
        return this.exception;
    }

    @Override
    public String toString() {
        return (this.error ? "Error" : "Warning") + ": " + this.message;
    }

}
