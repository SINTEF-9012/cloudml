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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.cloudml.facade.mrt.cmd.abstracts.Change;
import org.cloudml.facade.mrt.cmd.abstracts.Instruction;
import org.cloudml.facade.mrt.cmd.abstracts.XPath;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.MapExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

/**
 * ---
 * !getSnapshot
 *   path : /nodeInstances
 *   codec : plain-text   #This is optional
 *   map: name            #optional
 */
@SuppressWarnings("all")
public class GetSnapshot extends Instruction {
  public XPath path;
  
  public String codec;
  
  public XPath map;
  
  public Map<String,XPath> multimaps;
  
  public GetSnapshot() {
  }
  
  public GetSnapshot(final String nouse) {
  }
  
  public GetSnapshot(final Procedure1<GetSnapshot> initializer) {
    initializer.apply(this);
  }
  
  @Override
  protected Object _execute(final Object context, final List<Change> changes) {
    Object _xifexpression = null;
    boolean _and = false;
    boolean _equals = Objects.equal(this.map, null);
    if (!_equals) {
      _and = false;
    } else {
      boolean _or = false;
      boolean _equals_1 = Objects.equal(this.multimaps, null);
      if (_equals_1) {
        _or = true;
      } else {
        boolean _isEmpty = this.multimaps.isEmpty();
        _or = (_equals_1 || _isEmpty);
      }
      _and = (_equals && _or);
    }
    if (_and) {
      Object _xifexpression_1 = null;
      boolean _equals_2 = Objects.equal(this.codec, "plain-text");
      if (_equals_2) {
        Object _query = this.path.query(context);
        String _string = _query.toString();
        _xifexpression_1 = _string;
      } else {
        Object _query_1 = this.path.query(context);
        _xifexpression_1 = _query_1;
      }
      _xifexpression = _xifexpression_1;
    } else {
      List<? extends Object> _xifexpression_2 = null;
      boolean _and_1 = false;
      boolean _notEquals = (!Objects.equal(this.map, null));
      if (!_notEquals) {
        _and_1 = false;
      } else {
        boolean _equals_3 = Objects.equal(this.map.literal, "key");
        _and_1 = (_notEquals && _equals_3);
      }
      if (_and_1) {
        ArrayList<Map> _xblockexpression = null;
        {
          final ArrayList<Map> lst = CollectionLiterals.<Map>newArrayList();
          Object _query_2 = this.path.query(context);
          Set _keySet = ((Map) _query_2).keySet();
          lst.addAll(_keySet);
          _xblockexpression = (lst);
        }
        _xifexpression_2 = _xblockexpression;
      } else {
        List<? extends Object> _xblockexpression_1 = null;
        {
          Collection _xifexpression_3 = null;
          Object _query_2 = this.path.query(context);
          if ((_query_2 instanceof Map)) {
            Object _query_3 = this.path.query(context);
            Collection _values = ((Map) _query_3).values();
            _xifexpression_3 = _values;
          } else {
            ArrayList<Object> _xblockexpression_2 = null;
            {
              final ArrayList<Object> x = CollectionLiterals.<Object>newArrayList();
              final Iterator iterator = this.path.iterate(context);
              boolean _hasNext = iterator.hasNext();
              boolean _while = _hasNext;
              while (_while) {
                Object _next = iterator.next();
                x.add(_next);
                boolean _hasNext_1 = iterator.hasNext();
                _while = _hasNext_1;
              }
              _xblockexpression_2 = (x);
            }
            _xifexpression_3 = _xblockexpression_2;
          }
          final Collection oriColl = _xifexpression_3;
          List<? extends Object> _xifexpression_4 = null;
          boolean _and_2 = false;
          boolean _notEquals_1 = (!Objects.equal(this.multimaps, null));
          if (!_notEquals_1) {
            _and_2 = false;
          } else {
            boolean _isEmpty_1 = this.multimaps.isEmpty();
            boolean _not = (!_isEmpty_1);
            _and_2 = (_notEquals_1 && _not);
          }
          if (_and_2) {
            ArrayList<Map> _xblockexpression_3 = null;
            {
              final ArrayList<Map> lstmap = CollectionLiterals.<Map>newArrayList();
              for (final Object ori : oriColl) {
                {
                  final HashMap<String,Object> m = CollectionLiterals.<String, Object>newHashMap();
                  lstmap.add(m);
                  final Procedure2<String,XPath> _function = new Procedure2<String,XPath>() {
                      public void apply(final String k, final XPath v) {
                        Object _query = v.query(ori);
                        m.put(k, _query);
                      }
                    };
                  MapExtensions.<String, XPath>forEach(this.multimaps, _function);
                }
              }
              _xblockexpression_3 = (lstmap);
            }
            _xifexpression_4 = _xblockexpression_3;
          } else {
            List<Object> _xifexpression_5 = null;
            boolean _notEquals_2 = (!Objects.equal(this.map, null));
            if (_notEquals_2) {
              final Function1<Object,Object> _function = new Function1<Object,Object>() {
                  public Object apply(final Object ori) {
                    Object _query = GetSnapshot.this.map.query(ori);
                    return _query;
                  }
                };
              Iterable<Object> _map = IterableExtensions.<Object, Object>map(oriColl, _function);
              List<Object> _list = IterableExtensions.<Object>toList(_map);
              _xifexpression_5 = _list;
            }
            _xifexpression_4 = _xifexpression_5;
          }
          _xblockexpression_1 = (_xifexpression_4);
        }
        _xifexpression_2 = _xblockexpression_1;
      }
      _xifexpression = _xifexpression_2;
    }
    return _xifexpression;
  }
}
