package org.cloudml.mrt.coord.cmd.gen;

import java.util.List;
import java.util.Map;
import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.cloudml.mrt.coord.cmd.abstracts.Modification;
import org.cloudml.mrt.coord.cmd.abstracts.Property;
import org.cloudml.mrt.coord.cmd.abstracts.XPath;
import org.cloudml.mrt.coord.cmd.gen.CloudMLCmds;
import org.cloudml.mrt.coord.cmd.gen.Updated;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.MapExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class Set extends Modification {
  public Set() {
  }
  
  public Set(final String nouse) {
  }
  
  public Set(final Procedure1<Set> initializer) {
    initializer.apply(this);
  }
  
  public XPath parent;
  
  public Map<Property,Object> keyValues;
  
  @Override
  protected Object _execute(final Object context, final List<Change> changes) {
    Object _xblockexpression = null;
    {
      final Object par = this.parent.query(context);
      final Function2<Property,Object,Boolean> _function = new Function2<Property,Object,Boolean>() {
          public Boolean apply(final Property property, final Object value) {
            boolean _setProperty = CloudMLCmds.setProperty(par, property, value);
            return Boolean.valueOf(_setProperty);
          }
        };
      Map<Property,Object> _filter = MapExtensions.<Property, Object>filter(this.keyValues, _function);
      final Procedure2<Property,Object> _function_1 = new Procedure2<Property,Object>() {
          public void apply(final Property p, final Object v) {
            final Procedure1<Updated> _function = new Procedure1<Updated>() {
                public void apply(final Updated it) {
                  it.parent = par;
                  it.parent_repr = Set.this.parent.literal;
                  it.property = p.name;
                  it.newValue = v;
                }
              };
            Updated _updated = new Updated(_function);
            changes.add(_updated);
          }
        };
      MapExtensions.<Property, Object>forEach(_filter, _function_1);
      _xblockexpression = (null);
    }
    return _xblockexpression;
  }
}
