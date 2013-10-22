package org.cloudml.mrt.coord.cmd.gen;

import com.google.common.base.Objects;
import java.util.List;
import java.util.Map;
import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.cloudml.mrt.coord.cmd.abstracts.Modification;
import org.cloudml.mrt.coord.cmd.abstracts.Property;
import org.cloudml.mrt.coord.cmd.abstracts.Type;
import org.cloudml.mrt.coord.cmd.gen.CloudMLCmds;
import org.cloudml.mrt.coord.cmd.gen.Created;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.MapExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class Create extends Modification {
  public Create() {
  }
  
  public Create(final String nouse) {
  }
  
  public Create(final Procedure1<Create> initializer) {
    initializer.apply(this);
  }
  
  public Type type;
  
  public Map<Property,Object> initializer;
  
  public Map<Property,Object> keyValues;
  
  @Override
  protected Object _execute(final Object context, final List<Change> changes) {
    try {
      Object _xblockexpression = null;
      {
        final Class<? extends Object> clz = this.type.obtainClass();
        Object _xifexpression = null;
        boolean _or = false;
        boolean _equals = Objects.equal(this.initializer, null);
        if (_equals) {
          _or = true;
        } else {
          boolean _isEmpty = this.initializer.isEmpty();
          _or = (_equals || _isEmpty);
        }
        if (_or) {
          Object _newInstance = clz.newInstance();
          _xifexpression = _newInstance;
        } else {
          _xifexpression = null;
        }
        final Object newObject = _xifexpression;
        final Procedure2<Property,Object> _function = new Procedure2<Property,Object>() {
            public void apply(final Property p, final Object v) {
              CloudMLCmds.setProperty(newObject, p, v);
            }
          };
        MapExtensions.<Property, Object>forEach(this.keyValues, _function);
        boolean _notEquals = (!Objects.equal(newObject, null));
        if (_notEquals) {
          final Procedure1<Created> _function_1 = new Procedure1<Created>() {
              public void apply(final Created it) {
                it.object = newObject;
              }
            };
          Created _created = new Created(_function_1);
          changes.add(_created);
        }
        _xblockexpression = (newObject);
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
