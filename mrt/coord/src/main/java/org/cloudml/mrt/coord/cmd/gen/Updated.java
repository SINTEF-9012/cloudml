package org.cloudml.mrt.coord.cmd.gen;

import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Updated extends Change {
  public Updated() {
  }
  
  public Updated(final String nouse) {
  }
  
  public Updated(final Procedure1<Updated> initializer) {
    initializer.apply(this);
  }
  
  public Object parent;
  
  public String property;
  
  public Object newValue;
  
  Object parent_repr = null;
  
  String property_repr = null;
  
  Object newValue_repr = null;
  
  @Override
  public Change obtainRepr() {
    Updated toRepr = new Updated();
    if(this.parent_repr!=null)
    	toRepr.parent = this.parent_repr;
    else
    	toRepr.parent = this.parent;
    if(this.property_repr!=null)
    	toRepr.property = this.property_repr;
    else
    	toRepr.property = this.property;
    if(this.newValue_repr!=null)
    	toRepr.newValue = this.newValue_repr;
    else
    	toRepr.newValue = this.newValue;
    toRepr.fromPeer = this.fromPeer;
    return toRepr;
    
  }
}
