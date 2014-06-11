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
public class Snapshot extends Change {
  public Snapshot() {
  }
  
  public Snapshot(final String nouse) {
  }
  
  public Snapshot(final Procedure1<Snapshot> initializer) {
    initializer.apply(this);
  }
  
  public Object content;
  
  Object content_repr = null;
  
  @Override
  public Change obtainRepr() {
    Snapshot toRepr = new Snapshot();
    if(this.content_repr!=null)
    	toRepr.content = this.content_repr;
    else
    	toRepr.content = this.content;
    toRepr.fromPeer = this.fromPeer;
    return toRepr;
    
  }
}
