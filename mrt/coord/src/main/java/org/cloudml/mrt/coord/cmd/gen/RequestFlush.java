package org.cloudml.mrt.coord.cmd.gen;

import java.util.List;
import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.cloudml.mrt.coord.cmd.abstracts.Instruction;
import org.cloudml.mrt.coord.cmd.gen.Flush;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class RequestFlush extends Instruction {
  public String code;
  
  public RequestFlush() {
  }
  
  public RequestFlush(final String nouse) {
  }
  
  public RequestFlush(final Procedure1<RequestFlush> initializer) {
    initializer.apply(this);
  }
  
  @Override
  protected Object _execute(final Object context, final List<Change> changes) {
    final Procedure1<Flush> _function = new Procedure1<Flush>() {
        public void apply(final Flush it) {
          it.code = RequestFlush.this.code;
          it.object = context;
        }
      };
    Flush _flush = new Flush(_function);
    boolean _add = changes.add(_flush);
    return Boolean.valueOf(_add);
  }
}
