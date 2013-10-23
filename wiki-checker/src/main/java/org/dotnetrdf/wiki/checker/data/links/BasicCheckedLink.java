
package org.dotnetrdf.wiki.checker.data.links;

import org.dotnetrdf.wiki.data.links.BasicLink;

public class BasicCheckedLink extends BasicLink implements CheckedLink {

    public BasicCheckedLink(String path, int line, int column) {
        super(path, line, column);
    }
    
    public BasicCheckedLink(String path, String text, int line, int column) {
        super(path, text, line, column);
    }

}
