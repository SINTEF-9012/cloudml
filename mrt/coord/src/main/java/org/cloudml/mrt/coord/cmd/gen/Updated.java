package org.cloudml.mrt.coord.cmd.gen;

import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Updated extends Change {
  public Updated() {
  }
  
  public Updated(final Procedure1<Updated> initializer) {
    initializer.apply(this);
  }
  
  public Object parent;
  
  public String property;
  
  public Object newValue;
}
