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

package org.dotnetrdf.wiki.data.issues;

/**
 * Interface for issues
 * @author rvesse
 *
 */
public interface Issue {

    /**
     * Gets the issue message
     * 
     * @return Message
     */
    public abstract String getMessage();

    /**
     * Gets whether this is an error
     * 
     * @return True if error, false if a warning
     */
    public abstract boolean isError();

    /**
     * Gets whether the issue has an exception associated with it
     * 
     * @return True if an exception is associated with the issue and can be
     *         retrieved using the {@link #getException()} method
     */
    public abstract boolean hasException();

    /**
     * Gets the exception associated with the issue (if any)
     * 
     * @return Exception or null
     */
    public Throwable getException();
}