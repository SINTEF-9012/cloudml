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
  
  String code_repr = null;
  
  Object object_repr = null;
  
  @Override
  public Change obtainRepr() {
    Flush toRepr = new Flush();
    if(this.code_repr!=null)
    	toRepr.code = this.code_repr;
    else
    	toRepr.code = this.code;
    if(this.object_repr!=null)
    	toRepr.object = this.object_repr;
    else
    	toRepr.object = this.object;
    return toRepr;
    
  }
}
