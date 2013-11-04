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

package org.dotnetrdf.wiki.render.renderer.documents;

import java.io.File;

import org.dotnetrdf.wiki.data.links.Link;
import org.dotnetrdf.wiki.render.data.documents.RenderedDocument;
import org.dotnetrdf.wiki.render.renderer.DocumentRenderer;

/**
 * Abstract implementation of a document renderer that provides useful helper
 * functions
 * 
 * @author rvesse
 * 
 */
public abstract class AbstractRenderer implements DocumentRenderer {

    /**
     * Calculates the target path for the rendered output
     * 
     * @param document
     *            Document to be rendered
     * @param outputBase
     *            Output base directory
     * @param ext
     *            Extension for output, may be null to not add a specific
     *            extension
     * @return
     */
    protected <TLink extends Link, TDoc extends RenderedDocument<TLink>> File calculateTargetFile(TDoc document, File outputBase,
            String ext) {
        String wikiPath = document.getPath();

        // Convert wiki path separator into file system separator for runtime
        // file system
        wikiPath = wikiPath.replace('/', File.separatorChar);

        // Append relative path and file extension to base output directory
        String targetPath = outputBase.getAbsolutePath() + wikiPath;
        if (ext != null) {
            if (!ext.startsWith("."))
                ext = "." + ext;
            targetPath += ext;
        }

        return new File(targetPath);
    }

}
