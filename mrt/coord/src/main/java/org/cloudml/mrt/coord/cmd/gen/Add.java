package org.cloudml.mrt.coord.cmd.gen;

import com.google.common.base.Objects;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.cloudml.mrt.coord.cmd.abstracts.Modification;
import org.cloudml.mrt.coord.cmd.abstracts.Property;
import org.cloudml.mrt.coord.cmd.abstracts.XPath;
import org.cloudml.mrt.coord.cmd.gen.Added;
import org.cloudml.mrt.coord.cmd.gen.CloudMLCmds;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * add a new element/value to a multi-valued property
 */
@SuppressWarnings("all")
public class Add extends Modification {
  public Add() {
  }
  
  public Add(final String nouse) {
  }
  
  public Add(final Procedure1<Add> initializer) {
    initializer.apply(this);
  }
  
  public XPath parent;
  
  public Property property;
  
  public XPath crossRef;
  
  public Object newValue;
  
  public Object index;
  
  @Override
  protected Object _execute(final Object context, final List<Change> changes) {
    Object _xblockexpression = null;
    {
      Object _query = null;
      if (this.crossRef!=null) {
        _query=this.crossRef.query(context);
      }
      final Object resolvedCrossRef = _query;
      String _plus = (this.parent.literal + "/");
      String _plus_1 = (_plus + this.property.name);
      XPath _xPath = new XPath(_plus_1);
      final XPath toAddPath = _xPath;
      final Object toAddColl = toAddPath.query(context);
      Object _elvis = null;
      if (resolvedCrossRef != null) {
        _elvis = resolvedCrossRef;
      } else {
        _elvis = ObjectExtensions.<Object>operator_elvis(resolvedCrossRef, this.newValue);
      }
      final Object toAddValue = _elvis;
      boolean _switchResult = false;
      boolean _matched = false;
      if (!_matched) {
        if (toAddColl instanceof Map) {
          final Map _map = (Map)toAddColl;
          _matched=true;
          boolean _xblockexpression_1 = false;
          {
            Object _put = _map.put(this.index, toAddValue);
            /* Objects.equal(_put, null); */
            _xblockexpression_1 = (true);
          }
          _switchResult = _xblockexpression_1;
        }
      }
      if (!_matched) {
        if (toAddColl instanceof List) {
          final List _list = (List)toAddColl;
          boolean _notEquals = (!Objects.equal(this.index, null));
          if (_notEquals) {
            _matched=true;
            boolean _xtrycatchfinallyexpression = false;
            try {
              boolean _xblockexpression_1 = false;
              {
                Object _convert = CloudMLCmds.convert("int", this.index, context);
                _list.add((((Integer) _convert)).intValue(), toAddValue);
                _xblockexpression_1 = (true);
              }
              _xtrycatchfinallyexpression = _xblockexpression_1;
            } catch (final Throwable _t) {
              if (_t instanceof Exception) {
                final Exception e = (Exception)_t;
                _xtrycatchfinallyexpression = false;
              } else {
                throw Exceptions.sneakyThrow(_t);
              }
            }
            _switchResult = _xtrycatchfinallyexpression;
          }
        }
      }
      if (!_matched) {
        if (toAddColl instanceof Collection) {
          final Collection _collection = (Collection)toAddColl;
          _matched=true;
          boolean _add = _collection.add(toAddValue);
          _switchResult = _add;
        }
      }
      if (!_matched) {
        _switchResult = false;
      }
      final boolean success = _switchResult;
      if (success) {
        final Procedure1<Added> _function = new Procedure1<Added>() {
            public void apply(final Added it) {
              Object _query = Add.this.parent.query(context);
              it.parent = _query;
              it.parent_repr = Add.this.parent.literal;
              it.property = Add.this.property.name;
              it.addedValue = toAddValue;
            }
          };
        Added _added = new Added(_function);
        changes.add(_added);
      }
      _xblockexpression = (null);
    }
    return _xblockexpression;
  }
}
