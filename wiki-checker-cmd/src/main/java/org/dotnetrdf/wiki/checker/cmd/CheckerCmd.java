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

package org.dotnetrdf.wiki.checker.cmd;

import io.airlift.command.Command;
import io.airlift.command.Help;
import io.airlift.command.HelpOption;
import io.airlift.command.Option;
import io.airlift.command.OptionType;
import io.airlift.command.ParseArgumentsMissingException;
import io.airlift.command.ParseArgumentsUnexpectedException;
import io.airlift.command.ParseOptionMissingException;
import io.airlift.command.ParseOptionMissingValueException;
import io.airlift.command.SingleCommand;
import io.airlift.command.model.CommandMetadata;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import javax.inject.Inject;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.dotnetrdf.wiki.checker.BasicWikiChecker;
import org.dotnetrdf.wiki.checker.WikiChecker;
import org.dotnetrdf.wiki.checker.data.AbstractCheckedWiki;
import org.dotnetrdf.wiki.checker.data.BasicCheckedWiki;
import org.dotnetrdf.wiki.checker.data.documents.BasicCheckedDocument;
import org.dotnetrdf.wiki.checker.data.documents.CheckedDocument;
import org.dotnetrdf.wiki.checker.data.links.BasicCheckedLink;
import org.dotnetrdf.wiki.checker.issues.Issue;
import org.dotnetrdf.wiki.checker.parser.CheckedWikiScanner;
import org.slf4j.LoggerFactory;

/**
 * Entry point for the Wiki Checker program
 * 
 * @author rvesse
 * 
 */
@Command(name = "check", description = "Checks a wiki for common issues and errors")
public class CheckerCmd {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CheckerCmd.class);

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    /**
     * Help option
     */
    @Inject
    public HelpOption helpOption;

    /**
     * Verbose option
     */
    @Option(name = { "-v", "--verbose" }, required = false, type = OptionType.COMMAND, title = "Verbose Mode", description = "Enables verbose output to the console")
    public boolean verbose = false;

    /**
     * Warnings option
     */
    @Option(name = { "-w", "--warn" }, required = false, type = OptionType.COMMAND, title = "Show Warnings", description = "Enables report output for documents that only have warnings")
    public boolean showWarnings = false;

    /**
     * Input option
     */
    @Option(name = { "-i", "--input" }, required = true, arity = 1, type = OptionType.COMMAND, title = "Input Directory", description = "Specifies a directory containing the wiki to be checked")
    public String input;

    /**
     * Log file option
     */
    @Option(name = { "-l", "--log" }, required = false, arity = 1, type = OptionType.COMMAND, title = "Log File", description = "Specifies a file to write log output to (defaults to wiki-checker.log)")
    public String logFile = "wiki-checker.log";

    /**
     * Entry point for the CLI
     * 
     * @param args
     *            Arguments
     */
    public static void main(String[] args) {
        try {
            CheckerCmd cmd = SingleCommand.singleCommand(CheckerCmd.class).parse(args);

            if (cmd.helpOption.showHelpIfRequested()) {
                return;
            }

            cmd.run();
        } catch (ParseOptionMissingException e) {
            System.err.println(ANSI_RED + e.getMessage());
            System.err.println();
            showUsage();
        } catch (ParseOptionMissingValueException e) {
            System.err.println(ANSI_RED + e.getMessage());
            System.err.println();
            showUsage();
        } catch (ParseArgumentsMissingException e) {
            System.err.println(ANSI_RED + e.getMessage());
            System.err.println();
            showUsage();
        } catch (ParseArgumentsUnexpectedException e) {
            System.err.println(ANSI_RED + e.getMessage());
            System.err.println();
            showUsage();
        } finally {
            System.err.println(ANSI_RESET);
        }
    }

    private static void showUsage() {
        CommandMetadata metadata = SingleCommand.singleCommand(CheckerCmd.class).getCommandMetadata();
        StringBuilder builder = new StringBuilder();
        Help.help(metadata, builder);
        System.err.print(ANSI_RESET);
        System.err.println(builder.toString());
        System.exit(1);
    }

    /**
     * Runs the command
     */
    public void run() {
        try {
            // Configure log4j based on the warn and quiet settings
            BasicConfigurator.resetConfiguration();
            Logger.getRootLogger().removeAllAppenders();
            if (this.verbose) {
                // When not in quiet mode up log level to DEBUG and add
                // Console Appender
                System.out.println("Verbose Mode is enabled, log output will be written to stdout");
                Logger.getRootLogger().setLevel(Level.DEBUG);
                Logger.getRootLogger().addAppender(
                        new ConsoleAppender(new PatternLayout(PatternLayout.DEFAULT_CONVERSION_PATTERN)));
            }

            // Regardless of setting add a file appender
            System.out.println(String.format("Logging to %s", this.logFile));
            Logger.getRootLogger().addAppender(
                    new FileAppender(new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN), this.logFile, true));

            // Ensure Apache HTTP Client logging is set to WARN
            Logger.getLogger("org.apache.http").setLevel(Level.WARN);

            // Scan specified directory
            BasicCheckedWiki wiki = new BasicCheckedWiki();
            CheckedWikiScanner<BasicCheckedLink, BasicCheckedDocument> scanner = new CheckedWikiScanner<BasicCheckedLink, BasicCheckedDocument>();
            System.out.println(String.format("Checking wiki located at %s", this.input));
            scanner.scan(wiki, this.input);

            // Carry out checks
            WikiChecker<BasicCheckedLink, BasicCheckedDocument> checker = new BasicWikiChecker<BasicCheckedLink, BasicCheckedDocument>(
                    wiki, this.input);
            checker.run();

            // Dump Report
            String report = getReport(wiki);
            System.out.println(report);
            LOGGER.info("\n" + report);

        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    /**
     * Generates the report string
     * 
     * @param wiki
     *            Wiki
     * @param warn
     *            Whether to include warnings in the report
     * @param quiet
     *            Whether to enable quiet mode, if enabled only documents with
     *            issues will be reported on
     * @return
     */
    private String getReport(AbstractCheckedWiki<BasicCheckedLink, BasicCheckedDocument> wiki) {
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);

        // Report Summary
        pw.println("Checked " + wiki.getTotalDocuments() + " Document(s) for issues");
        pw.println(wiki.getTotalLinks() + " Link(s) discovered - " + wiki.getTotalWikiLinks() + " Wiki Link(s) and "
                + wiki.getTotalExternalLinks() + " External Link(s)");
        pw.println(wiki.getTotalErrorCount() + " Error(s) and " + wiki.getTotalWarningCount() + " Warning(s)");
        pw.println();

        // Report on global issues
        pw.println(wiki.getGlobalErrorCount() + " Global Error(s) and " + wiki.getGlobalWarningCount() + " Global Warning(s)");
        Iterator<Issue> globalIssues = wiki.getGlobalIssues();
        while (globalIssues.hasNext()) {
            Issue i = globalIssues.next();
            if (i.isError() || this.verbose || this.showWarnings) {
                pw.println(i.toString());
            }
        }

        // Report on documents
        pw.println();
        Iterator<BasicCheckedDocument> iter = wiki.getDocuments();
        while (iter.hasNext()) {
            CheckedDocument<BasicCheckedLink> document = iter.next();
            if ((document.hasIssues() && (this.showWarnings || document.hasErrors())) || this.verbose) {
                pw.println(document.toString());
            }

            // Report Issues
            if (document.hasIssues() && (this.showWarnings || document.hasErrors())) {
                Iterator<Issue> issues = document.getIssues();
                while (issues.hasNext()) {
                    Issue issue = issues.next();
                    pw.println(issue.toString());
                }
                pw.println();
            }
        }

        return writer.toString();
    }
}
