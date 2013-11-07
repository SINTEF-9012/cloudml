package org.cloudml.mrt.coord.cmd.gen;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.cloudml.mrt.coord.cmd.abstracts.Instruction;
import org.cloudml.mrt.coord.cmd.abstracts.Property;
import org.cloudml.mrt.coord.cmd.abstracts.XPath;
import org.cloudml.mrt.coord.cmd.gen.Remove;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * ===============Here comes the instructions for DIVERSIFY=========
 */
@SuppressWarnings("all")
public class Terminate extends Instruction {
  public String nodeName;
  
  public Terminate() {
  }
  
  public Terminate(final String nouse) {
  }
  
  public Terminate(final Procedure1<Terminate> initializer) {
    initializer.apply(this);
  }
  
  @Override
  protected Object _execute(final Object context, final List<Change> changes) {
    Object _xblockexpression = null;
    {
      Remove _remove = new Remove();
      final Procedure1<Remove> _function = new Procedure1<Remove>() {
          public void apply(final Remove it) {
            XPath _xPath = new XPath("/");
            it.parent = _xPath;
            Property _property = new Property("nodeInstances");
            it.property = _property;
            final ArrayList<Object> lst = CollectionLiterals.<Object>newArrayList();
            int i = 0;
            int foundid = (-1);
            XPath _xPath_1 = new XPath("nodeInstances");
            Object _query = _xPath_1.query(context);
            for (final Object x : ((Collection) _query)) {
              {
                XPath _xPath_2 = new XPath("name");
                Object _query_1 = _xPath_2.query(x);
                boolean _equals = Objects.equal(_query_1, Terminate.this.nodeName);
                if (_equals) {
                  foundid = i;
                }
                int _plus = (i + 1);
                i = _plus;
              }
            }
            it.index = Integer.valueOf(foundid);
          }
        };
      final Remove remove = ObjectExtensions.<Remove>operator_doubleArrow(_remove, _function);
      Object _execute = remove.execute(context, changes);
      _xblockexpression = (_execute);
    }
    return _xblockexpression;
  }
}
