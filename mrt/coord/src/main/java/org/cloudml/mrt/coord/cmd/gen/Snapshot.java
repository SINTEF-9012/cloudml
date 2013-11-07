package org.cloudml.mrt.coord.cmd.gen;

import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Snapshot extends Change {
  public Snapshot() {
  }
  
  public Snapshot(final String nouse) {
  }
  
  public Snapshot(final Procedure1<Snapshot> initializer) {
    initializer.apply(this);
  }
  
  public Object content;
  
  Object content_repr = null;
  
  @Override
  public Change obtainRepr() {
    Snapshot toRepr = new Snapshot();
    if(this.content_repr!=null)
    	toRepr.content = this.content_repr;
    else
    	toRepr.content = this.content;
    toRepr.fromPeer = this.fromPeer;
    return toRepr;
    
  }
}
