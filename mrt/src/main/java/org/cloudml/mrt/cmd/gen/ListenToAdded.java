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

import com.google.common.base.Objects;
import org.cloudml.mrt.cmd.abstracts.Change;
import org.cloudml.mrt.cmd.abstracts.Listener;
import org.cloudml.mrt.cmd.abstracts.Property;
import org.cloudml.mrt.cmd.abstracts.XPath;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ListenToAdded extends Listener {
  public ListenToAdded() {
  }
  
  public ListenToAdded(final String nouse) {
  }
  
  public ListenToAdded(final Procedure1<ListenToAdded> initializer) {
    initializer.apply(this);
  }
  
  public XPath parent;
  
  public Property property;
  
  @Override
  protected boolean _careFor(final Change change) {
    boolean _switchResult = false;
    boolean _matched = false;
    if (!_matched) {
      if (change instanceof Added) {
        final Added _added = (Added)change;
        _matched=true;
        boolean _and = false;
        Object _queryXPathFromRoot = this.queryXPathFromRoot(this.parent);
        boolean _equals = Objects.equal(_added.parent, _queryXPathFromRoot);
        if (!_equals) {
          _and = false;
        } else {
          boolean _equals_1 = Objects.equal(_added.property, this.property.name);
          _and = (_equals && _equals_1);
        }
        _switchResult = _and;
      }
    }
    if (!_matched) {
      _switchResult = false;
    }
    return _switchResult;
  }
}
