package org.cloudml.mrt.coord.cmd.gen;

import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.cloudml.mrt.coord.cmd.abstracts.Listener;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ListenToAny extends Listener {
  public ListenToAny() {
  }
  
  public ListenToAny(final String nouse) {
  }
  
  public ListenToAny(final Procedure1<ListenToAny> initializer) {
    initializer.apply(this);
  }
  
  @Override
  protected boolean _careFor(final Change change) {
    return true;
  }
}
