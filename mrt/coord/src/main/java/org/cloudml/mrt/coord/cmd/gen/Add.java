package org.cloudml.mrt.coord.cmd.gen;

import org.cloudml.mrt.coord.cmd.abstracts.Modification;
import org.cloudml.mrt.coord.cmd.abstracts.Property;
import org.cloudml.mrt.coord.cmd.abstracts.XPath;

@SuppressWarnings("all")
public class Add extends Modification {
  public XPath parent;
  
  public Property property;
  
  public String value;
  
  public XPath object;
  
  @Override
  protected Object _execute(final Object context) {
    return null;
  }
}
