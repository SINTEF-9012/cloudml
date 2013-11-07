package org.cloudml.mrt.coord.cmd.gen;

import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Removed extends Change {
  public Removed() {
  }
  
  public Removed(final String nouse) {
  }
  
  public Removed(final Procedure1<Removed> initializer) {
    initializer.apply(this);
  }
  
  public Object parent;
  
  public String property;
  
  public Object index;
  
  public Object removedValue;
  
  Object parent_repr = null;
  
  String property_repr = null;
  
  Object index_repr = null;
  
  Object removedValue_repr = null;
  
  @Override
  public Change obtainRepr() {
    Removed toRepr = new Removed();
    if(this.parent_repr!=null)
    	toRepr.parent = this.parent_repr;
    else
    	toRepr.parent = this.parent;
    if(this.property_repr!=null)
    	toRepr.property = this.property_repr;
    else
    	toRepr.property = this.property;
    if(this.index_repr!=null)
    	toRepr.index = this.index_repr;
    else
    	toRepr.index = this.index;
    if(this.removedValue_repr!=null)
    	toRepr.removedValue = this.removedValue_repr;
    else
    	toRepr.removedValue = this.removedValue;
    toRepr.fromPeer = this.fromPeer;
    return toRepr;
    
  }
}
