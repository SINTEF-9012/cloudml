package org.cloudml.mrt.coord.cmd.gen;

import com.google.common.base.Objects;
import java.util.List;
import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.cloudml.mrt.coord.cmd.abstracts.Instruction;
import org.cloudml.mrt.coord.cmd.abstracts.XPath;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * ---
 * !getSnapshot
 *   path : /
 *   codec : plain-text   #This is optional
 */
@SuppressWarnings("all")
public class GetSnapshot extends Instruction {
  public XPath path;
  
  public String codec;
  
  public GetSnapshot() {
  }
  
  public GetSnapshot(final String nouse) {
  }
  
  public GetSnapshot(final Procedure1<GetSnapshot> initializer) {
    initializer.apply(this);
  }
  
  @Override
  protected Object _execute(final Object context, final List<Change> changes) {
    Object _xifexpression = null;
    boolean _equals = Objects.equal(this.codec, "plain-text");
    if (_equals) {
      Object _query = this.path.query(context);
      String _string = _query.toString();
      _xifexpression = _string;
    } else {
      Object _query_1 = this.path.query(context);
      _xifexpression = _query_1;
    }
    return _xifexpression;
  }
}
