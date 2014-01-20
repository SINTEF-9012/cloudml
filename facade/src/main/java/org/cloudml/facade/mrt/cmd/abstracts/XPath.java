/**
 * This file is part of CloudML [ http://cloudml.org ]
 *
 * Copyright (C) 2012 - SINTEF ICT
 * Contact: Franck Chauvel <franck.chauvel@sintef.no>
 *
 * Module: root
 *
 * CloudML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * CloudML is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with CloudML. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.cloudml.facade.mrt.cmd.abstracts;

import java.util.Iterator;

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
    
    public Iterator iterate(Object context){
    	JXPathContext jpathcontext = JXPathContext.newContext(context);
    	return jpathcontext.iterate(literal);
    }
    
}
