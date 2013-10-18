package org.cloudml.mrt.coord.cmd.gen;

import com.google.common.base.Objects;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.cloudml.mrt.coord.cmd.abstracts.Modification;
import org.cloudml.mrt.coord.cmd.abstracts.Property;
import org.cloudml.mrt.coord.cmd.abstracts.Type;
import org.cloudml.mrt.coord.cmd.abstracts.XPath;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.MapExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class Create extends Modification {
  public XPath parent;
  
  public Property containing;
  
  public Type type;
  
  public Map<Property,String> initializer;
  
  public Map<Property,String> keyValues;
  
  @Override
  protected Object _execute(final Object context) {
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
        boolean _matched = false;
        if (!_matched) {
          if (col instanceof Map) {
            final Map _map = (Map)col;
            _matched=true;
            final Function2<Property,String,Boolean> _function_1 = new Function2<Property,String,Boolean>() {
                public Boolean apply(final Property p, final String v) {
                  boolean _equals = Objects.equal(p.name, "name");
                  return Boolean.valueOf(_equals);
                }
              };
            Map<Property,String> _filter = MapExtensions.<Property, String>filter(this.keyValues, _function_1);
            Collection<String> _values = _filter.values();
            final String name = ((String[])Conversions.unwrapArray(_values, String.class))[0];
            _map.put(name, obj);
          }
        }
        if (!_matched) {
          if (col instanceof List) {
            final List _list = (List)col;
            _matched=true;
            _list.add(obj);
          }
        }
        _xblockexpression = (null);
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
