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
import java.io.FileOutputStream;
import java.io.IOException;

import org.dotnetrdf.wiki.data.documents.formats.DataFormat;
import org.dotnetrdf.wiki.data.links.Link;
import org.dotnetrdf.wiki.render.data.RenderedWiki;
import org.dotnetrdf.wiki.render.data.documents.RenderedDocument;
import org.dotnetrdf.wiki.render.renderer.DocumentRenderer;

import eu.mrico.creole.Creole;
import eu.mrico.creole.CreoleException;
import eu.mrico.creole.ast.Document;
import eu.mrico.creole.xhtml.XHtmlWriter;

/**
 * A renderer for Creole format documents
 * 
 * @author rvesse
 * 
 */
public class CreoleRenderer extends AbstractRenderer {

    private XHtmlWriter writer = new XHtmlWriter();

    // TODO Allow renderer to be customized

    @Override
    public <TLink extends Link, TDoc extends RenderedDocument<TLink>> void render(TDoc document, File outputBase,
            RenderedWiki<TLink, RenderedDocument<TLink>> wiki) {
        if (document.getFormat().getDataFormat() == DataFormat.CREOLE) {
            try {
                String text = document.getText();
                if (text == null)
                    throw new NullPointerException("CreoleRenderer cannot render null document text");
                Document creoleDoc = Creole.parse(document.getText());
                File target = this.calculateTargetFile(document, outputBase, ".html");
                FileOutputStream output = new FileOutputStream(target);
                this.writer.write(creoleDoc, output);
                output.close();

            } catch (IOException e) {
                // TODO Add issue to rendered document
            } catch (CreoleException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("CreoleRenderer cannot render non-Creole documents");
        }
    }

}
