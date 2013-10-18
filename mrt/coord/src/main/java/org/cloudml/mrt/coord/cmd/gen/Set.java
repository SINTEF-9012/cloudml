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
  
  public Set(final Procedure1<Set> initializer) {
    initializer.apply(this);
  }
  
  public XPath parent;
  
  public Map<Property,String> keyValues;
  
  @Override
  protected Object _execute(final Object context, final List<Change> changes) {
    Object _xblockexpression = null;
    {
      final Object par = this.parent.query(context);
      final Function2<Property,String,Boolean> _function = new Function2<Property,String,Boolean>() {
          public Boolean apply(final Property property, final String value) {
            boolean _setProperty = CloudMLCmds.setProperty(par, property, value);
            return Boolean.valueOf(_setProperty);
          }
        };
      Map<Property,String> _filter = MapExtensions.<Property, String>filter(this.keyValues, _function);
      final Procedure2<Property,String> _function_1 = new Procedure2<Property,String>() {
          public void apply(final Property p, final String v) {
            final Procedure1<Updated> _function = new Procedure1<Updated>() {
                public void apply(final Updated it) {
                  it.parent = par;
                  it.property = p.name;
                  it.newValue = v;
                }
              };
            Updated _updated = new Updated(_function);
            changes.add(_updated);
          }
        };
      MapExtensions.<Property, String>forEach(_filter, _function_1);
      _xblockexpression = (null);
    }
    return _xblockexpression;
  }
}
