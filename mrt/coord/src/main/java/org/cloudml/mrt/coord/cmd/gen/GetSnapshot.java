package org.cloudml.mrt.coord.cmd.gen;

import java.util.List;
import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.cloudml.mrt.coord.cmd.abstracts.Instruction;
import org.cloudml.mrt.coord.cmd.abstracts.XPath;

@SuppressWarnings("all")
public class GetSnapshot extends Instruction {
  public XPath path;
  
  @Override
  protected Object _execute(final Object context, final List<Change> changes) {
    Object _query = this.path.query(context);
    return _query;
  }
}
