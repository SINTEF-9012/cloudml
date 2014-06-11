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
package org.cloudml.mrt.cmd.gen;

import org.cloudml.mrt.cmd.abstracts.Change;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Added extends Change {
  public Added() {
  }
  
  public Added(final String nouse) {
  }
  
  public Added(final Procedure1<Added> initializer) {
    initializer.apply(this);
  }
  
  public Object parent;
  
  public String property;
  
  public Object addedValue;
  
  Object parent_repr = null;
  
  String property_repr = null;
  
  Object addedValue_repr = null;
  
  @Override
  public Change obtainRepr() {
    Added toRepr = new Added();
    if(this.parent_repr!=null)
    	toRepr.parent = this.parent_repr;
    else
    	toRepr.parent = this.parent;
    if(this.property_repr!=null)
    	toRepr.property = this.property_repr;
    else
    	toRepr.property = this.property;
    if(this.addedValue_repr!=null)
    	toRepr.addedValue = this.addedValue_repr;
    else
    	toRepr.addedValue = this.addedValue;
    toRepr.fromPeer = this.fromPeer;
    return toRepr;
    
  }
}
