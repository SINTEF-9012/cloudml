package org.cloudml.mrt.coord.cmd.gen;

import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Added extends Change {
  public Added() {
  }
  
  public Added(final String nouse) {
  }
  
  public Added(final Procedure1<Added> initializer) {
    initializer.apply(this);
  }
  
  public Object parent;
  
  public String property;
  
  public Object addedValue;
}
