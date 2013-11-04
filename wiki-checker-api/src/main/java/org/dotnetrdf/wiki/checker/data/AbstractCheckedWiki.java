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

package org.dotnetrdf.wiki.checker.data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.dotnetrdf.wiki.checker.data.documents.CheckedDocument;
import org.dotnetrdf.wiki.checker.data.links.CheckedLink;
import org.dotnetrdf.wiki.data.AbstractWiki;
import org.dotnetrdf.wiki.data.issues.Issue;

/**
 * Abstract implementation of a checked wiki which is a wiki composed of
 * documents which may be checked
 * 
 * @author rvesse
 * @param <TLink>
 *            Checked link type
 * @param <TDoc>
 *            Checked document type
 */
public abstract class AbstractCheckedWiki<TLink extends CheckedLink, TDoc extends CheckedDocument<TLink>> extends
        AbstractWiki<TLink, TDoc> implements CheckedWiki<TLink, TDoc> {

    private boolean checked = false;
    protected Set<Issue> globalIssues = new HashSet<Issue>();

    @Override
    public int getTotalErrorCount() {
        int count = 0;
        for (TDoc document : this.documents.values()) {
            count += document.getErrorCount();
        }
        for (Issue i : this.globalIssues) {
            if (i.isError())
                count++;
        }
        return count;
    }

    @Override
    public int getTotalWarningCount() {
        int count = 0;
        for (TDoc document : this.documents.values()) {
            count += document.getWarningCount();
        }
        for (Issue i : this.globalIssues) {
            if (!i.isError())
                count++;
        }
        return count;
    }

    @Override
    public boolean hasBeenChecked() {
        return this.checked;
    }

    @Override
    public void addGlobalIssue(Issue issue) {
        if (issue == null)
            return;
        this.globalIssues.add(issue);
    }

    @Override
    public boolean hasGlobalIssues() {
        return this.globalIssues.size() > 0;
    }

    @Override
    public boolean hasGlobalErrors() {
        for (Issue i : this.globalIssues) {
            if (i.isError())
                return true;
        }
        return false;
    }

    @Override
    public Iterator<Issue> getGlobalIssues() {
        return this.globalIssues.iterator();
    }

    @Override
    public int getGlobalIssueCount() {
        return this.globalIssues.size();
    }

    @Override
    public int getGlobalErrorCount() {
        int count = 0;
        for (Issue i : this.globalIssues) {
            if (i.isError())
                count++;
        }
        return count;
    }

    @Override
    public int getGlobalWarningCount() {
        int count = 0;
        for (Issue i : this.globalIssues) {
            if (!i.isError())
                count++;
        }
        return count;
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
