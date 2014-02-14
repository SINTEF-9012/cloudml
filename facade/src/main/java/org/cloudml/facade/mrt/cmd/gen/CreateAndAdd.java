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
import org.cloudml.facade.mrt.cmd.abstracts.Parameter;
import org.cloudml.facade.mrt.cmd.abstracts.Property;
import org.cloudml.facade.mrt.cmd.abstracts.Type;
import org.cloudml.facade.mrt.cmd.abstracts.XPath;
import org.cloudml.facade.mrt.cmd.gen.Add;
import org.cloudml.facade.mrt.cmd.gen.Create;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * Composite modification from Create and Add. This is probably the most
 * frequently used modification.
 * 
 *     - !createAndAdd
 *     parent : /
 *     property : nodeInstances
 *     type : VMInstance
 *     initializer :
 *       - {type: String, value: ni1}
 *       - {type: VM, value: !xpath /nodeTypes/node1}
 */
@SuppressWarnings("all")
public class CreateAndAdd extends Modification {
  public CreateAndAdd() {
  }
  
  public CreateAndAdd(final String nouse) {
  }
  
  public CreateAndAdd(final Procedure1<CreateAndAdd> initializer) {
    initializer.apply(this);
  }
  
  public XPath parent;
  
  public Property property;
  
  public Type type;
  
  public List<Parameter> initializer;
  
  public Map<Property,Object> keyValues;
  
  public Object index;
  
  @Override
  protected Object _execute(final Object context, final List<Change> changes) {
    Object _xblockexpression = null;
    {
      final Procedure1<Create> _function = new Procedure1<Create>() {
          public void apply(final Create it) {
            it.type = CreateAndAdd.this.type;
            it.keyValues = CreateAndAdd.this.keyValues;
            it.initializer = CreateAndAdd.this.initializer;
          }
        };
      Create _create = new Create(_function);
      final Object newObject = _create.execute(context, changes);
      final Procedure1<Add> _function_1 = new Procedure1<Add>() {
          public void apply(final Add it) {
            it.parent = CreateAndAdd.this.parent;
            it.property = CreateAndAdd.this.property;
            it.newValue = newObject;
            it.index = CreateAndAdd.this.index;
          }
        };
      Add _add = new Add(_function_1);
      final Object add = _add.execute(context, changes);
      _xblockexpression = (null);
    }
    return _xblockexpression;
  }
}
