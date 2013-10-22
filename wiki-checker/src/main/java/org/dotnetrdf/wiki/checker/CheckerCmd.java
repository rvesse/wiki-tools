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

package org.dotnetrdf.wiki.checker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.dotnetrdf.wiki.checker.data.CheckedWiki;
import org.dotnetrdf.wiki.checker.data.documents.CheckedDocument;
import org.dotnetrdf.wiki.checker.documents.DocumentChecker;
import org.dotnetrdf.wiki.checker.issues.Issue;
import org.dotnetrdf.wiki.checker.parser.CheckedWikiScanner;

/**
 * Entry point for the Wiki Checker program
 * 
 * @author rvesse
 * 
 */
public class CheckerCmd {

    /**
     * Entry point for the CLI
     * 
     * @param args
     *            Arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            showUsage();
            System.exit(1);
        } else {
            try {
                // Configure log4j based on the warn and quiet settings
                boolean warn = args.length > 1 ? args[1].trim().toLowerCase().equals("true") : true;
                boolean quiet = args.length > 2 ? args[2].trim().toLowerCase().equals("true") : true;
                BasicConfigurator.configure();
                if (!quiet) {
                    // When not in quiet mode up log level to DEBUG
                    Logger.getRootLogger().setLevel(Level.DEBUG);
                }
                // Ensure Apache HTTP Client logging is set to WARN
                Logger.getLogger("org.apache.http").setLevel(Level.WARN);
                
                // TODO Should really create a per-run log file and add an appender for it
                
                // Scan specified directory
                CheckedWiki<CheckedDocument> wiki = new CheckedWiki<CheckedDocument>();
                CheckedWikiScanner<CheckedDocument> scanner = new CheckedWikiScanner<CheckedDocument>();
                scanner.scan(wiki, args[0]);
                
                // Carry out checks
                DocumentChecker<CheckedDocument> checker = new DocumentChecker<CheckedDocument>(wiki, args[0]);
                checker.run();

                // Dump Report
                Iterator<CheckedDocument> iter = wiki.getDocuments();
                System.out.println("Checked " + wiki.getTotalDocuments() + " Document(s) for issues");
                System.out.println(wiki.getTotalLinks() + " Link(s) discovered - " + wiki.getTotalWikiLinks() + " Wiki Link(s) and " + wiki.getTotalExternalLinks() + " External Link(s)");
                System.out.println(wiki.getTotalErrors() + " Error(s) and " + wiki.getTotalWarnings() + " Warning(s)");
                System.out.println();
                while (iter.hasNext()) {
                    CheckedDocument document = iter.next();
                    if ((document.hasIssues() && (warn || document.hasErrors())) || !quiet) {
                        System.out.println(document.toString());
                    }

                    // Report Issues
                    if (document.hasIssues() && (warn || document.hasErrors())) {
                        Iterator<Issue> issues = document.getIssues();
                        while (issues.hasNext()) {
                            Issue issue = issues.next();
                            System.out.println(issue.toString());
                        }
                        System.out.println();
                    }
                }
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
                e.printStackTrace(System.err);
            } catch (IOException e) {
                System.err.println(e.getMessage());
                e.printStackTrace(System.err);
            }
        }
    }

    /**
     * Show usage summary
     */
    private static void showUsage() {
        System.err.println("Wiki Checker");
        System.err.println("============");
        System.err.println();
        System.err.println("Usage is:");
        System.err.println("./check directory [warn quiet]");
        System.err.println();
        System.err.println("directory is the base directory for the wiki");
        System.err.println("warn controls whether warnings are shown (default true)");
        System.err.println("quiet controls quite mode (default true)");
    }

}
