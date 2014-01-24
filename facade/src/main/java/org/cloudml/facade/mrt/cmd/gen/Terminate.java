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

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.cloudml.facade.mrt.cmd.abstracts.Change;
import org.cloudml.facade.mrt.cmd.abstracts.Instruction;
import org.cloudml.facade.mrt.cmd.abstracts.Property;
import org.cloudml.facade.mrt.cmd.abstracts.XPath;
import org.cloudml.facade.mrt.cmd.gen.Remove;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * ===============Here comes the instructions for DIVERSIFY=========
 */
@SuppressWarnings("all")
public class Terminate extends Instruction {
  public String nodeName;
  
  public Terminate() {
  }
  
  public Terminate(final String nouse) {
  }
  
  public Terminate(final Procedure1<Terminate> initializer) {
    initializer.apply(this);
  }
  
  @Override
  protected Object _execute(final Object context, final List<Change> changes) {
    Object _xblockexpression = null;
    {
      Remove _remove = new Remove();
      final Procedure1<Remove> _function = new Procedure1<Remove>() {
          public void apply(final Remove it) {
            XPath _xPath = new XPath("/");
            it.parent = _xPath;
            Property _property = new Property("nodeInstances");
            it.property = _property;
            final ArrayList<Object> lst = CollectionLiterals.<Object>newArrayList();
            int i = 0;
            int foundid = (-1);
            XPath _xPath_1 = new XPath("nodeInstances");
            Object _query = _xPath_1.query(context);
            for (final Object x : ((Collection) _query)) {
              {
                XPath _xPath_2 = new XPath("name");
                Object _query_1 = _xPath_2.query(x);
                boolean _equals = Objects.equal(_query_1, Terminate.this.nodeName);
                if (_equals) {
                  foundid = i;
                }
                int _plus = (i + 1);
                i = _plus;
              }
            }
            it.index = Integer.valueOf(foundid);
          }
        };
      final Remove remove = ObjectExtensions.<Remove>operator_doubleArrow(_remove, _function);
      Object _execute = remove.execute(context, changes);
      _xblockexpression = (_execute);
    }
    return _xblockexpression;
  }
}
