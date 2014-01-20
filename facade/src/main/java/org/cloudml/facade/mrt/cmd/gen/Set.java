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

import java.util.List;
import java.util.Map;
import org.cloudml.facade.mrt.cmd.abstracts.Change;
import org.cloudml.facade.mrt.cmd.abstracts.Modification;
import org.cloudml.facade.mrt.cmd.abstracts.Property;
import org.cloudml.facade.mrt.cmd.abstracts.XPath;
import org.cloudml.facade.mrt.cmd.gen.CloudMLCmds;
import org.cloudml.facade.mrt.cmd.gen.Updated;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.MapExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

/**
 * Give new values to the fields
 *     - !set
 *     parent : nodeTypes/node1
 *     keyValues :
 *        minCore : 4
 *        minDisk : 1
 * 
 * Not that if you need to set a cross reference, you need to add a tag !xpath
 * (TODO: need to get rid of this later)
 * e.g.,
 *     keyValues:
 *       minRam : 1024
 *       provider : xpath /providers[name='provider1']
 */
@SuppressWarnings("all")
public class Set extends Modification {
  public Set() {
  }
  
  public Set(final String nouse) {
  }
  
  public Set(final Procedure1<Set> initializer) {
    initializer.apply(this);
  }
  
  public XPath parent;
  
  public Map<Property,Object> keyValues;
  
  @Override
  protected Object _execute(final Object context, final List<Change> changes) {
    Object _xblockexpression = null;
    {
      final Object par = this.parent.query(context);
      final Function2<Property,Object,Boolean> _function = new Function2<Property,Object,Boolean>() {
          public Boolean apply(final Property property, final Object value) {
            boolean _setProperty = CloudMLCmds.setProperty(par, property, value, context);
            return Boolean.valueOf(_setProperty);
          }
        };
      Map<Property,Object> _filter = MapExtensions.<Property, Object>filter(this.keyValues, _function);
      final Procedure2<Property,Object> _function_1 = new Procedure2<Property,Object>() {
          public void apply(final Property p, final Object v) {
            final Procedure1<Updated> _function = new Procedure1<Updated>() {
                public void apply(final Updated it) {
                  it.parent = par;
                  it.parent_repr = Set.this.parent.literal;
                  it.property = p.name;
                  it.newValue = v;
                }
              };
            Updated _updated = new Updated(_function);
            changes.add(_updated);
          }
        };
      MapExtensions.<Property, Object>forEach(_filter, _function_1);
      _xblockexpression = (null);
    }
    return _xblockexpression;
  }
}
