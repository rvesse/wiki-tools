/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package org.dotnetrdf.wiki.parser.links;

import org.dotnetrdf.wiki.data.documents.BasicDocument;
import org.dotnetrdf.wiki.data.documents.Document;
import org.dotnetrdf.wiki.data.documents.formats.Format;
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
        Document doc = new BasicDocument("Home.wiki", Format.CREOLE);
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
        Document doc = new BasicDocument("Home.wiki", Format.CREOLE);
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
        Document doc = new BasicDocument("Home.wiki", Format.CREOLE);
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
        Document doc = new BasicDocument("Home.wiki", Format.CREOLE);
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
        Document doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Simple reference link with leading spaces before ID
        // 4 or more spaces mean its in an unformatted block so not considered a link
        String text = "[Test][1]\n\n    [1]: http://example.org";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 0);
    }
    
    /**
     * Tests reference link detection
     */
    @Test
    public void referenceLinks06() {
        Document doc = new BasicDocument("Home.wiki", Format.CREOLE);
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
        Document doc = new BasicDocument("Home.wiki", Format.CREOLE);
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
        Document doc = new BasicDocument("Home.wiki", Format.CREOLE);
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
        Document doc = new BasicDocument("Home.wiki", Format.CREOLE);
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
        Document doc = new BasicDocument("Home.wiki", Format.CREOLE);
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
        Document doc = new BasicDocument("Home.wiki", Format.CREOLE);
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
     * Tests inline link detection
     */
    @Test
    public void inlineLinks01() {
        Document doc = new BasicDocument("Home.wiki", Format.CREOLE);
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
        Document doc = new BasicDocument("Home.wiki", Format.CREOLE);
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
        Document doc = new BasicDocument("Home.wiki", Format.CREOLE);
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
        Document doc = new BasicDocument("Home.wiki", Format.CREOLE);
        Assert.assertEquals(doc.getOutboundLinkCount(), 0);

        // Simple inline link
        String text = "[Test](http://example.org (title))";
        linkDetector.findLinks(doc, text);

        Assert.assertEquals(doc.getOutboundLinkCount(), 1);
        Link link = doc.getOutboundLinks().next();
        Assert.assertEquals(link.getText(), "Test");
        Assert.assertEquals(link.getPath(), "http://example.org");
    }
}
