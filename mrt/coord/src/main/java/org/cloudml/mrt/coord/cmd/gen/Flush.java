package org.cloudml.mrt.coord.cmd.gen;

import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Flush extends Change {
  public Flush() {
  }
  
  public Flush(final String nouse) {
  }
  
  public Flush(final Procedure1<Flush> initializer) {
    initializer.apply(this);
  }
  
  public String code;
  
  public Object object;
}
