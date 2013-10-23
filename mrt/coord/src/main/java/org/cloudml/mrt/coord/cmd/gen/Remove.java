package org.cloudml.mrt.coord.cmd.gen;

import com.google.common.base.Objects;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.cloudml.mrt.coord.cmd.abstracts.Modification;
import org.cloudml.mrt.coord.cmd.abstracts.Property;
import org.cloudml.mrt.coord.cmd.abstracts.XPath;
import org.cloudml.mrt.coord.cmd.gen.CloudMLCmds;
import org.cloudml.mrt.coord.cmd.gen.Removed;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * Remove an element from a multi-valued property
 * 
 * For lists, you can remove by giving directly the object, or an index (int)
 * For maps, only remove by index (key) is supported so far
 */
@SuppressWarnings("all")
public class Remove extends Modification {
  public Remove() {
  }
  
  public Remove(final String nouse) {
  }
  
  public Remove(final Procedure1<Remove> initializer) {
    initializer.apply(this);
  }
  
  public XPath parent;
  
  public Property property;
  
  public Object index;
  
  public Object value;
  
  public XPath crossRef;
  
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
      final XPath toRemovePath = _xPath;
      final Object toRemoveColl = toRemovePath.query(context);
      Object _elvis = null;
      if (resolvedCrossRef != null) {
        _elvis = resolvedCrossRef;
      } else {
        _elvis = ObjectExtensions.<Object>operator_elvis(resolvedCrossRef, this.value);
      }
      final Object toRemoveValue = _elvis;
      boolean _switchResult = false;
      boolean _matched = false;
      if (!_matched) {
        if (toRemoveColl instanceof Map) {
          final Map _map = (Map)toRemoveColl;
          _matched=true;
          Object _remove = _map.remove(this.index);
          boolean _notEquals = (!Objects.equal(_remove, null));
          _switchResult = _notEquals;
        }
      }
      if (!_matched) {
        if (toRemoveColl instanceof List) {
          final List _list = (List)toRemoveColl;
          boolean _notEquals = (!Objects.equal(this.index, null));
          if (_notEquals) {
            _matched=true;
            Object _convert = CloudMLCmds.convert("int", this.index, context);
            int _intValue = ((Integer) _convert).intValue();
            Object _remove = _list.remove(_intValue);
            boolean _notEquals_1 = (!Objects.equal(_remove, null));
            _switchResult = _notEquals_1;
          }
        }
      }
      if (!_matched) {
        if (toRemoveColl instanceof Collection) {
          final Collection _collection = (Collection)toRemoveColl;
          _matched=true;
          boolean _remove = _collection.remove(toRemoveValue);
          _switchResult = _remove;
        }
      }
      if (!_matched) {
        _switchResult = false;
      }
      final boolean success = _switchResult;
      if (success) {
        final Procedure1<Removed> _function = new Procedure1<Removed>() {
            public void apply(final Removed it) {
              Object _query = Remove.this.parent.query(context);
              it.parent = _query;
              it.parent_repr = Remove.this.parent.literal;
              it.index = Remove.this.index;
              it.property = Remove.this.property.name;
              it.removedValue = toRemoveValue;
              String _xifexpression = null;
              boolean _notEquals = (!Objects.equal(Remove.this.crossRef, null));
              if (_notEquals) {
                _xifexpression = Remove.this.crossRef.literal;
              } else {
                _xifexpression = null;
              }
              it.removedValue_repr = _xifexpression;
            }
          };
        Removed _removed = new Removed(_function);
        changes.add(_removed);
      }
      _xblockexpression = (null);
    }
    return _xblockexpression;
  }
}
