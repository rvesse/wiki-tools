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

package org.dotnetrdf.wiki.checker.checks.links;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.jena.iri.IRI;
import org.apache.jena.iri.IRIFactory;
import org.apache.jena.iri.Violation;
import org.dotnetrdf.wiki.checker.checks.LinkCheck;
import org.dotnetrdf.wiki.checker.data.AbstractCheckedWiki;
import org.dotnetrdf.wiki.checker.data.documents.CheckedDocument;
import org.dotnetrdf.wiki.checker.data.links.CheckedLink;
import org.dotnetrdf.wiki.checker.issues.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A link check which validates external links by making HTTP requests to see if
 * the URLs can be reached. It also validates that external links are valid
 * URIs.
 * 
 * @author rvesse
 */
public class ExternalLinkCheck implements LinkCheck {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalLinkCheck.class);

    private Map<String, Boolean> externalUris = new HashMap<String, Boolean>();
    private Map<String, Integer> httpStatuses = new HashMap<String, Integer>();
    private HttpClient httpClient = new DefaultHttpClient();

    @Override
    public <TLink extends CheckedLink, TDoc extends CheckedDocument<TLink>> void check(TDoc document, TLink link,
            AbstractCheckedWiki<TLink, TDoc> wiki) {
        if (link.isMailLink() || link.isWikiLink())
            return;

        // External Link Validation

        // TODO Strip off #fragment and ?querystrings to speed this
        // stage up

        if (this.externalUris.get(link.getPath()) != null) {
            // Already validated, report broken link if necessary
            if (this.externalUris.get(link.getPath()) != true) {
                document.addIssue(new Issue("Broken External Link (HTTP Status " + this.httpStatuses.get(link.getPath()) + ") - "
                        + link.toString(), true));
            }
        } else {
            // Validate the external link

            // Firstly look for obvious issues with the IRI
            IRI iri = IRIFactory.uriImplementation().create(link.getPath());
            if (iri.hasViolation(true)) {
                Iterator<Violation> violations = iri.violations(true);
                boolean iriErrors = false;
                while (violations.hasNext()) {
                    Violation violation = violations.next();
                    if (violation.isError()) {
                        iriErrors = true;
                        document.addIssue(new Issue("External Link " + link.toString() + " violates the IRI specification - "
                                + violation.getLongMessage(), true));
                        LOGGER.error("Link " + link.toString() + " is not a valid IRI - " + violation.getLongMessage());
                    } else {
                        document.addIssue(new Issue("External Link " + link.toString()
                                + " has a warning against the IRI specification - " + violation.getShortMessage(), false));
                        LOGGER.warn("Link " + link.toString() + " has an IRI warning - " + violation.getLongMessage());
                    }
                }

                // Skip further validation if has errors
                if (iriErrors)
                    return;
            }

            // Try a HTTP HEAD request first then fall back to a
            // HTTP GET when necessary
            HttpHead head = null;
            HttpGet get = null;
            try {
                head = new HttpHead(link.getPath());
                head.setHeader("Accept", "*/*");
                LOGGER.debug("Validating External Link " + link.getPath());
                HttpResponse resp = this.httpClient.execute(head);

                if (resp.getStatusLine().getStatusCode() >= 200 && resp.getStatusLine().getStatusCode() < 400) {
                    // Valid
                    this.externalUris.put(link.getPath(), true);
                    LOGGER.debug("External Link " + link.getPath() + " is valid");
                } else {
                    // Try a GET instead, in case the server doesn't
                    // support HEAD nicely
                    head.releaseConnection();
                    head.reset();
                    head = null;
                    get = new HttpGet(link.getPath());
                    get.setHeader("Accept", "*/*");
                    resp = this.httpClient.execute(get);

                    if (resp.getStatusLine().getStatusCode() >= 200 && resp.getStatusLine().getStatusCode() < 400) {
                        // Valid
                        this.externalUris.put(link.getPath(), true);
                        LOGGER.debug("External Link " + link.getPath() + " is valid");
                    } else {
                        // Invalid
                        this.externalUris.put(link.getPath(), false);
                        this.httpStatuses.put(link.getPath(), resp.getStatusLine().getStatusCode());
                        document.addIssue(new Issue("Broken External Link (HTTP Status " + resp.getStatusLine().getStatusCode()
                                + ") - " + link.toString(), true));
                        LOGGER.error("External Link " + link.getPath() + " is invalid");
                    }
                }

            } catch (IllegalArgumentException e) {
                this.externalUris.put(link.getPath(), false);
                document.addIssue(new Issue("Invalid External Link URI - " + link.toString(), true));
                LOGGER.debug("External Link " + link.getPath() + " is invalid", e);
            } catch (UnknownHostException e) {
                this.externalUris.put(link.getPath(), false);
                document.addIssue(new Issue("Invalid External Link URI - " + link.toString(), true));
                LOGGER.debug("External Link " + link.getPath() + " is invalid", e);
            } catch (Throwable e) {
                this.externalUris.put(link.getPath(), false);
                document.addIssue(new Issue("Unexpected Error with External Link URI - " + link.toString(), true));
                LOGGER.debug("External Link " + link.getPath() + " is invalid", e);
            } finally {
                if (head != null) {
                    head.releaseConnection();
                    head.reset();
                }
                if (get != null) {
                    get.releaseConnection();
                    get.reset();
                }
            }
        }
    }
}
