package org.cloudml.mrt.coord.cmd.gen;

import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Created extends Change {
  public Created() {
  }
  
  public Created(final String nouse) {
  }
  
  public Created(final Procedure1<Created> initializer) {
    initializer.apply(this);
  }
  
  public Object object;
}
