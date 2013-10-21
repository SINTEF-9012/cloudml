package org.cloudml.mrt.coord.cmd.gen;

import com.google.common.base.Objects;
import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.cloudml.mrt.coord.cmd.abstracts.Listener;
import org.cloudml.mrt.coord.cmd.gen.Flush;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ListenToFlush extends Listener {
  public ListenToFlush() {
  }
  
  public ListenToFlush(final Procedure1<ListenToFlush> initializer) {
    initializer.apply(this);
  }
  
  public String code;
  
  @Override
  protected boolean _careFor(final Change change) {
    boolean _switchResult = false;
    boolean _matched = false;
    if (!_matched) {
      if (change instanceof Flush) {
        final Flush _flush = (Flush)change;
        _matched=true;
        boolean _equals = Objects.equal(_flush.code, this.code);
        _switchResult = _equals;
      }
    }
    if (!_matched) {
      _switchResult = false;
    }
    return _switchResult;
  }
}
