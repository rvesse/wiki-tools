/**
 * Copyright (c) 2013 dotNetRDF Project (dotnetrdf-developer@lists.sf.net)
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
import java.util.Iterator;

import org.dotnetrdf.wiki.checker.issues.Issue;
import org.dotnetrdf.wiki.checker.pages.Page;
import org.dotnetrdf.wiki.checker.pages.PageTracker;

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
                // Scan specified directory for pages
                PageTracker tracker = new PageTracker();
                tracker.scan(args[0]);
                
                // TODO: Add Checker Steps
                
                // Dump Report
                Iterator<Page> iter = tracker.getPages();
                while (iter.hasNext()) {
                    Page page = iter.next();
                    System.out.println(page.toString());
                    
                    // Report Issues
                    if (page.hasIssues()) {
                        Iterator<Issue> issues = page.getIssues();
                        while (issues.hasNext()) {
                            Issue issue = issues.next();
                            System.out.println(issue.toString());
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
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
        System.err.println("./check directory");
    }

}
