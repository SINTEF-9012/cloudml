package org.cloudml.mrt.coord.cmd.abstracts;

import org.apache.commons.jxpath.JXPathContext;

public class XPath {
	public String literal;
	public XPath(String literal){
		this.literal = literal;
	}
    
    public Object query(Object context){
        JXPathContext jpathcontext = JXPathContext.newContext(context);
        return jpathcontext.getValue(literal);
    }
    
}
