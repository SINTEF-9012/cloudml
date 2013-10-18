package org.cloudml.mrt.coord.cmd.gen;

import com.google.common.base.Objects;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.cloudml.mrt.coord.cmd.abstracts.Modification;
import org.cloudml.mrt.coord.cmd.abstracts.Property;
import org.cloudml.mrt.coord.cmd.abstracts.Type;
import org.cloudml.mrt.coord.cmd.abstracts.XPath;
import org.cloudml.mrt.coord.cmd.gen.Added;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.MapExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class Create extends Modification {
  public Create() {
  }
  
  public Create(final Procedure1<Create> initializer) {
    initializer.apply(this);
  }
  
  public XPath parent;
  
  public Property containing;
  
  public Type type;
  
  public Map<Property,String> initializer;
  
  public Map<Property,String> keyValues;
  
  @Override
  protected Object _execute(final Object context, final List<Change> changes) {
    try {
      Object _xblockexpression = null;
      {
        final Class<? extends Object> clz = this.type.obtainClass();
        final Object obj = clz.newInstance();
        final Procedure2<Property,String> _function = new Procedure2<Property,String>() {
            public void apply(final Property property, final String value) {
              property.updateValue(obj, value);
            }
          };
        MapExtensions.<Property, String>forEach(this.keyValues, _function);
        String _plus = (this.parent.literal + "/");
        String _plus_1 = (_plus + this.containing.name);
        XPath _xPath = new XPath(_plus_1);
        final XPath newPath = _xPath;
        final Object col = newPath.query(context);
        Boolean _switchResult = null;
        boolean _matched = false;
        if (!_matched) {
          if (col instanceof Map) {
            final Map _map = (Map)col;
            _matched=true;
            boolean _xblockexpression_1 = false;
            {
              final Function2<Property,String,Boolean> _function_1 = new Function2<Property,String,Boolean>() {
                  public Boolean apply(final Property p, final String v) {
                    boolean _equals = Objects.equal(p.name, "name");
                    return Boolean.valueOf(_equals);
                  }
                };
              Map<Property,String> _filter = MapExtensions.<Property, String>filter(this.keyValues, _function_1);
              Collection<String> _values = _filter.values();
              final String name = ((String[])Conversions.unwrapArray(_values, String.class))[0];
              Object _put = _map.put(name, obj);
              boolean _equals = Objects.equal(_put, null);
              _xblockexpression_1 = (_equals);
            }
            _switchResult = Boolean.valueOf(_xblockexpression_1);
          }
        }
        if (!_matched) {
          if (col instanceof Collection) {
            final Collection _collection = (Collection)col;
            _matched=true;
            boolean _add = _collection.add(obj);
            _switchResult = Boolean.valueOf(_add);
          }
        }
        final Boolean succeeded = _switchResult;
        if ((succeeded).booleanValue()) {
          final Procedure1<Added> _function_1 = new Procedure1<Added>() {
              public void apply(final Added it) {
                Object _query = Create.this.parent.query(context);
                it.parent = _query;
                it.property = Create.this.containing.name;
                it.addedValue = obj;
              }
            };
          Added _added = new Added(_function_1);
          changes.add(_added);
        }
        _xblockexpression = (null);
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
