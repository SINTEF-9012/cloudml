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
import org.cloudml.facade.mrt.cmd.abstracts.Change;
import org.cloudml.facade.mrt.cmd.abstracts.Listener;
import org.cloudml.facade.mrt.cmd.gen.Flush;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ListenToFlush extends Listener {
  public ListenToFlush() {
  }
  
  public ListenToFlush(final String nouse) {
  }
  
  public ListenToFlush(final Procedure1<ListenToFlush> initializer) {
    initializer.apply(this);
  }
  
  public String code;
  
  @Override
  protected boolean _careFor(final Change change) {
    boolean _switchResult = false;
    boolean _matched = false;
    if (!_matched) {
      if (change instanceof Flush) {
        final Flush _flush = (Flush)change;
        _matched=true;
        boolean _equals = Objects.equal(_flush.code, this.code);
        _switchResult = _equals;
      }
    }
    if (!_matched) {
      _switchResult = false;
    }
    return _switchResult;
  }
}
