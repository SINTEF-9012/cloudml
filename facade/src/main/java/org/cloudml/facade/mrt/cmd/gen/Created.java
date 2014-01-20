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
package org.cloudml.facade.mrt.cmd.gen;

import org.cloudml.facade.mrt.cmd.abstracts.Change;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Created extends Change {
  public Created() {
  }
  
  public Created(final String nouse) {
  }
  
  public Created(final Procedure1<Created> initializer) {
    initializer.apply(this);
  }
  
  public Object object;
  
  Object object_repr = null;
  
  @Override
  public Change obtainRepr() {
    Created toRepr = new Created();
    if(this.object_repr!=null)
    	toRepr.object = this.object_repr;
    else
    	toRepr.object = this.object;
    toRepr.fromPeer = this.fromPeer;
    return toRepr;
    
  }
}
