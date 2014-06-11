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

import java.util.ArrayList;
import java.util.List;
import org.cloudml.mrt.cmd.abstracts.Change;
import org.cloudml.mrt.cmd.abstracts.Instruction;
import org.cloudml.mrt.cmd.abstracts.Parameter;
import org.cloudml.mrt.cmd.abstracts.Property;
import org.cloudml.mrt.cmd.abstracts.Type;
import org.cloudml.mrt.cmd.abstracts.XPath;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Provision extends Instruction {
  public String type;
  
  public String name;
  
  public Provision() {
  }
  
  public Provision(final String nouse) {
  }
  
  public Provision(final Procedure1<Provision> initializer) {
    initializer.apply(this);
  }
  
  @Override
  protected Object _execute(final Object context, final List<Change> changes) {
    Object _xblockexpression = null;
    {
      CreateAndAdd _createAndAdd = new CreateAndAdd();
      final Procedure1<CreateAndAdd> _function = new Procedure1<CreateAndAdd>() {
          public void apply(final CreateAndAdd it) {
            XPath _xPath = new XPath("/");
            it.parent = _xPath;
            Property _property = new Property("nodeInstances");
            it.property = _property;
            Type _type = new Type("VMInstance");
            it.type = _type;
            Parameter _parameter = new Parameter("String", Provision.this.name);
            String _plus = ("/nodeTypes/" + Provision.this.type);
            XPath _xPath_1 = new XPath(_plus);
            Parameter _parameter_1 = new Parameter("VM", _xPath_1);
            ArrayList<Parameter> _newArrayList = CollectionLiterals.<Parameter>newArrayList(_parameter, _parameter_1);
            it.initializer = _newArrayList;
          }
        };
      final CreateAndAdd create = ObjectExtensions.<CreateAndAdd>operator_doubleArrow(_createAndAdd, _function);
      create.execute(context, changes);
      CreateAndAdd _createAndAdd_1 = new CreateAndAdd();
      final Procedure1<CreateAndAdd> _function_1 = new Procedure1<CreateAndAdd>() {
          public void apply(final CreateAndAdd it) {
            String _plus = ("/nodeInstances[name=\'" + Provision.this.name);
            String _plus_1 = (_plus + "\']");
            XPath _xPath = new XPath(_plus_1);
            it.parent = _xPath;
            Property _property = new Property("properties");
            it.property = _property;
            Type _type = new Type("Property");
            it.type = _type;
            Parameter _parameter = new Parameter("String", "state");
            Parameter _parameter_1 = new Parameter("String", "on");
            ArrayList<Parameter> _newArrayList = CollectionLiterals.<Parameter>newArrayList(_parameter, _parameter_1);
            it.initializer = _newArrayList;
          }
        };
      final CreateAndAdd create2 = ObjectExtensions.<CreateAndAdd>operator_doubleArrow(_createAndAdd_1, _function_1);
      Object _execute = create2.execute(context, changes);
      _xblockexpression = (_execute);
    }
    return _xblockexpression;
  }
}
