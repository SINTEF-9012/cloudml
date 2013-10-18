package org.cloudml.mrt.coord.cmd.gen;

import java.util.Map;
import org.cloudml.mrt.coord.cmd.abstracts.Modification;
import org.cloudml.mrt.coord.cmd.abstracts.Property;
import org.cloudml.mrt.coord.cmd.abstracts.XPath;
import org.eclipse.xtext.xbase.lib.MapExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class Set extends Modification {
  public XPath parent;
  
  public Map<Property,String> keyValues;
  
  @Override
  protected Object _execute(final Object context) {
    Object _xblockexpression = null;
    {
      final Object par = this.parent.query(context);
      final Procedure2<Property,String> _function = new Procedure2<Property,String>() {
          public void apply(final Property property, final String value) {
            property.updateValue(par, value);
          }
        };
      MapExtensions.<Property, String>forEach(this.keyValues, _function);
      _xblockexpression = (null);
    }
    return _xblockexpression;
  }
}
