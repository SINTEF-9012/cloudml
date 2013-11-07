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
  
  Object parent_repr = null;
  
  String property_repr = null;
  
  Object addedValue_repr = null;
  
  @Override
  public Change obtainRepr() {
    Added toRepr = new Added();
    if(this.parent_repr!=null)
    	toRepr.parent = this.parent_repr;
    else
    	toRepr.parent = this.parent;
    if(this.property_repr!=null)
    	toRepr.property = this.property_repr;
    else
    	toRepr.property = this.property;
    if(this.addedValue_repr!=null)
    	toRepr.addedValue = this.addedValue_repr;
    else
    	toRepr.addedValue = this.addedValue;
    toRepr.fromPeer = this.fromPeer;
    return toRepr;
    
  }
}
