/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package org.dotnetrdf.wiki.checker.checks;

import org.dotnetrdf.wiki.checker.data.documents.CheckedDocument;
import org.dotnetrdf.wiki.data.links.Link;

/**
 * Interface for link checks
 * 
 * @author rvesse
 * 
 * @param <TLink>
 *            Checked link type
 * @param <TDoc>
 *            Checked document type
 */
public interface LinkCheck<TLink extends Link, TDoc extends CheckedDocument<TLink>> {

}
