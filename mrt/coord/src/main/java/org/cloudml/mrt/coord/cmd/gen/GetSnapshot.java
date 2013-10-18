package org.cloudml.mrt.coord.cmd.gen;

import org.cloudml.mrt.coord.cmd.abstracts.Instruction;
import org.cloudml.mrt.coord.cmd.abstracts.XPath;

@SuppressWarnings("all")
public class GetSnapshot extends Instruction {
  public XPath path;
  
  @Override
  protected Object _execute(final Object context) {
    Object _query = this.path.query(context);
    return _query;
  }
}
