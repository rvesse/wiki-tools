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

package org.dotnetrdf.wiki.parser.links;

import java.util.Iterator;

import org.dotnetrdf.wiki.data.documents.BasicDocument;
import org.dotnetrdf.wiki.data.documents.Document;
import org.dotnetrdf.wiki.data.documents.formats.Format;
import org.dotnetrdf.wiki.data.links.BasicLink;
import org.dotnetrdf.wiki.data.links.Link;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for the {@link MarkdownLinkDetector}
 * 
 * @author rvesse
 * 
 */
public class TestMarkdownLinkDetection {

    private LinkDetector linkDetector = new MarkdownLinkDetector();

    /**
     * Tests reference link detection
     */
    @Test
    public void referenceLinks01() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Simple reference link
        String text = "[Test][1]\n\n[1]: http://example.org";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 1);
        Link link = doc.getOutboundLinks().next();
        Assert.assertEquals(link.getText(), "Test");
        Assert.assertEquals(link.getPath(), "http://example.org");
    }

    /**
     * Tests reference link detection
     */
    @Test
    public void referenceLinks02() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Simple reference link with optional angle brackets
        String text = "[Test][1]\n\n[1]: <http://example.org>";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 1);
        Link link = doc.getOutboundLinks().next();
        Assert.assertEquals(link.getText(), "Test");
        Assert.assertEquals(link.getPath(), "http://example.org");
    }

    /**
     * Tests reference link detection
     */
    @Test
    public void referenceLinks03() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Simple reference link with leading spaces before ID
        String text = "[Test][1]\n\n   [1]: http://example.org";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 1);
        Link link = doc.getOutboundLinks().next();
        Assert.assertEquals(link.getText(), "Test");
        Assert.assertEquals(link.getPath(), "http://example.org");
    }

    /**
     * Tests reference link detection
     */
    @Test
    public void referenceLinks04() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Simple reference link with leading tabs before ID
        String text = "[Test][1]\n\n\t\t\t[1]: http://example.org";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 1);
        Link link = doc.getOutboundLinks().next();
        Assert.assertEquals(link.getText(), "Test");
        Assert.assertEquals(link.getPath(), "http://example.org");
    }

    /**
     * Tests reference link detection
     */
    @Test
    public void referenceLinks05() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Simple reference link with leading spaces before ID
        // 4 or more spaces mean its in an unformatted block so not considered a
        // link
        String text = "[Test][1]\n\n    [1]: http://example.org";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 0);
    }

    /**
     * Tests reference link detection
     */
    @Test
    public void referenceLinks06() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Simple reference link with title
        String text = "[Test][1]\n\n[1]: http://example.org \"title\"";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 1);
        Link link = doc.getOutboundLinks().next();
        Assert.assertEquals(link.getText(), "Test");
        Assert.assertEquals(link.getPath(), "http://example.org");
    }

    /**
     * Tests reference link detection
     */
    @Test
    public void referenceLinks07() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Simple reference link with title
        String text = "[Test][1]\n\n[1]: http://example.org 'title'";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 1);
        Link link = doc.getOutboundLinks().next();
        Assert.assertEquals(link.getText(), "Test");
        Assert.assertEquals(link.getPath(), "http://example.org");
    }

    /**
     * Tests reference link detection
     */
    @Test
    public void referenceLinks08() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Simple reference link with title
        String text = "[Test][1]\n\n[1]: http://example.org (title)";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 1);
        Link link = doc.getOutboundLinks().next();
        Assert.assertEquals(link.getText(), "Test");
        Assert.assertEquals(link.getPath(), "http://example.org");
    }

    /**
     * Tests reference link detection
     */
    @Test
    public void referenceLinks09() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Simple reference link
        // No corresponding reference so should not get treated as a link
        String text = "[Test][2]\n\n[1]: http://example.org";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 0);
    }

    /**
     * Tests reference link detection
     */
    @Test
    public void referenceLinks10() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Simple reference link using more complex reference
        String text = "[Test][test-ref]\n\n[test-ref]: http://example.org";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 1);
        Link link = doc.getOutboundLinks().next();
        Assert.assertEquals(link.getText(), "Test");
        Assert.assertEquals(link.getPath(), "http://example.org");
    }

    /**
     * Tests reference link detection
     */
    @Test
    public void referenceLinks11() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Simple reference link using implicit reference
        String text = "[Test][]\n\n[Test]: http://example.org";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 1);
        Link link = doc.getOutboundLinks().next();
        Assert.assertEquals(link.getText(), "Test");
        Assert.assertEquals(link.getPath(), "http://example.org");
    }

    /**
     * Tests reference link detection
     */
    @Test
    public void referenceLinks12() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Not a reference link because the link is escaped
        String text = "\\[Test][1]\n\n[1]: http://example.org";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 0);
    }

    /**
     * Tests reference link detection
     */
    @Test
    public void referenceLinks13() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Simple reference link
        String text = "Some text and then I [Test][1] whether my link is detected\n\n[1]: http://example.org";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 1);
        Link link = doc.getOutboundLinks().next();
        Assert.assertEquals(link.getText(), "Test");
        Assert.assertEquals(link.getPath(), "http://example.org");
    }

    /**
     * Tests reference link detection
     */
    @Test
    public void referenceLinks14() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Multiple reference links
        String text = "I link to [A][1] and then to [B][1]\n\n[1]: http://example.org";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 2);
        Iterator<BasicLink> iter = doc.getOutboundLinks();
        Link link = iter.next();
        if (link.getText().equals("A")) {
            Assert.assertEquals(link.getText(), "A");
            Assert.assertEquals(link.getPath(), "http://example.org");

            link = iter.next();
            Assert.assertEquals(link.getText(), "B");
            Assert.assertEquals(link.getPath(), "http://example.org");
        } else {
            Assert.assertEquals(link.getText(), "B");
            Assert.assertEquals(link.getPath(), "http://example.org");

            link = iter.next();
            Assert.assertEquals(link.getText(), "A");
            Assert.assertEquals(link.getPath(), "http://example.org");
        }
    }

    /**
     * Tests reference link detection
     */
    @Test
    public void referenceLinks15() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Not a reference link because the link is escaped
        String text = "[Test\\][1]\n\n[1]: http://example.org";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 0);
    }

    /**
     * Tests reference link detection
     */
    @Test
    public void referenceLinks16() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Simple reference link, the escape is for a backslash not for the [
        String text = "\\\\[Test][1]\n\n[1]: http://example.org";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 1);
        Link link = doc.getOutboundLinks().next();
        Assert.assertEquals(link.getText(), "Test");
        Assert.assertEquals(link.getPath(), "http://example.org");
    }
    
    /**
     * Tests reference link detection
     */
    @Test
    public void referenceLinks17() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Single reference links since second is escaped
        String text = "I link to [A][1] and then to \\[B][1]\n\n[1]: http://example.org";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 1);
        Link link = doc.getOutboundLinks().next();
        Assert.assertEquals(link.getText(), "A");
        Assert.assertEquals(link.getPath(), "http://example.org");
    }
    
    /**
     * Tests reference link detection
     */
    @Test
    public void referenceLinks18() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Single reference links since first is escaped
        String text = "I link to \\[A][1] and then to [B][1]\n\n[1]: http://example.org";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 1);
        Link link = doc.getOutboundLinks().next();
        Assert.assertEquals(link.getText(), "B");
        Assert.assertEquals(link.getPath(), "http://example.org");
    }

    /**
     * Tests inline link detection
     */
    @Test
    public void inlineLinks01() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Simple inline link
        String text = "[Test](http://example.org)";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 1);
        Link link = doc.getOutboundLinks().next();
        Assert.assertEquals(link.getText(), "Test");
        Assert.assertEquals(link.getPath(), "http://example.org");
    }

    /**
     * Tests inline link detection
     */
    @Test
    public void inlineLinks02() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Simple inline link
        String text = "[Test](http://example.org \"title\")";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 1);
        Link link = doc.getOutboundLinks().next();
        Assert.assertEquals(link.getText(), "Test");
        Assert.assertEquals(link.getPath(), "http://example.org");
    }

    /**
     * Tests inline link detection
     */
    @Test
    public void inlineLinks03() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Simple inline link
        String text = "[Test](http://example.org 'title')";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 1);
        Link link = doc.getOutboundLinks().next();
        Assert.assertEquals(link.getText(), "Test");
        Assert.assertEquals(link.getPath(), "http://example.org");
    }

    /**
     * Tests inline link detection
     */
    @Test
    public void inlineLinks04() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Simple inline link
        String text = "[Test](http://example.org (title))";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 1);
        Link link = doc.getOutboundLinks().next();
        Assert.assertEquals(link.getText(), "Test");
        Assert.assertEquals(link.getPath(), "http://example.org");
    }

    /**
     * Tests auto link detection
     */
    @Test
    public void autoLinks01() {
        Document<BasicLink> doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Simple inline link
        String text = "<http://example.org>";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 1);
        Link link = doc.getOutboundLinks().next();
        Assert.assertFalse(link.hasFriendlyText());
        Assert.assertEquals(link.getPath(), "http://example.org");
    }
}
