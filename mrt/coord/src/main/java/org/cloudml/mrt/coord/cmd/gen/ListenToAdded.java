package org.cloudml.mrt.coord.cmd.gen;

import com.google.common.base.Objects;
import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.cloudml.mrt.coord.cmd.abstracts.Listener;
import org.cloudml.mrt.coord.cmd.abstracts.Property;
import org.cloudml.mrt.coord.cmd.abstracts.XPath;
import org.cloudml.mrt.coord.cmd.gen.Added;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ListenToAdded extends Listener {
  public ListenToAdded() {
  }
  
  public ListenToAdded(final String nouse) {
  }
  
  public ListenToAdded(final Procedure1<ListenToAdded> initializer) {
    initializer.apply(this);
  }
  
  public XPath parent;
  
  public Property property;
  
  @Override
  protected boolean _careFor(final Change change) {
    boolean _switchResult = false;
    boolean _matched = false;
    if (!_matched) {
      if (change instanceof Added) {
        final Added _added = (Added)change;
        _matched=true;
        boolean _and = false;
        Object _queryXPathFromRoot = this.queryXPathFromRoot(this.parent);
        boolean _equals = Objects.equal(_added.parent, _queryXPathFromRoot);
        if (!_equals) {
          _and = false;
        } else {
          boolean _equals_1 = Objects.equal(_added.property, this.property.name);
          _and = (_equals && _equals_1);
        }
        _switchResult = _and;
      }
    }
    if (!_matched) {
      _switchResult = false;
    }
    return _switchResult;
  }
}
