package org.cloudml.mrt.coord.cmd.gen;

import java.util.ArrayList;
import java.util.List;
import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.cloudml.mrt.coord.cmd.abstracts.Instruction;
import org.cloudml.mrt.coord.cmd.abstracts.Parameter;
import org.cloudml.mrt.coord.cmd.abstracts.Property;
import org.cloudml.mrt.coord.cmd.abstracts.Type;
import org.cloudml.mrt.coord.cmd.abstracts.XPath;
import org.cloudml.mrt.coord.cmd.gen.CreateAndAdd;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Provision extends Instruction {
  public String type;
  
  public String name;
  
  public Provision() {
  }
  
  public Provision(final String nouse) {
  }
  
  public Provision(final Procedure1<Provision> initializer) {
    initializer.apply(this);
  }
  
  @Override
  protected Object _execute(final Object context, final List<Change> changes) {
    Object _xblockexpression = null;
    {
      CreateAndAdd _createAndAdd = new CreateAndAdd();
      final Procedure1<CreateAndAdd> _function = new Procedure1<CreateAndAdd>() {
          public void apply(final CreateAndAdd it) {
            XPath _xPath = new XPath("/");
            it.parent = _xPath;
            Property _property = new Property("nodeInstances");
            it.property = _property;
            Type _type = new Type("NodeInstance");
            it.type = _type;
            Parameter _parameter = new Parameter("String", Provision.this.name);
            String _plus = ("/nodeTypes/" + Provision.this.type);
            XPath _xPath_1 = new XPath(_plus);
            Parameter _parameter_1 = new Parameter("Node", _xPath_1);
            ArrayList<Parameter> _newArrayList = CollectionLiterals.<Parameter>newArrayList(_parameter, _parameter_1);
            it.initializer = _newArrayList;
          }
        };
      final CreateAndAdd create = ObjectExtensions.<CreateAndAdd>operator_doubleArrow(_createAndAdd, _function);
      create.execute(context, changes);
      CreateAndAdd _createAndAdd_1 = new CreateAndAdd();
      final Procedure1<CreateAndAdd> _function_1 = new Procedure1<CreateAndAdd>() {
          public void apply(final CreateAndAdd it) {
            String _plus = ("/nodeInstances[name=\'" + Provision.this.name);
            String _plus_1 = (_plus + "\']");
            XPath _xPath = new XPath(_plus_1);
            it.parent = _xPath;
            Property _property = new Property("properties");
            it.property = _property;
            Type _type = new Type("Property");
            it.type = _type;
            Parameter _parameter = new Parameter("String", "state");
            Parameter _parameter_1 = new Parameter("String", "on");
            ArrayList<Parameter> _newArrayList = CollectionLiterals.<Parameter>newArrayList(_parameter, _parameter_1);
            it.initializer = _newArrayList;
          }
        };
      final CreateAndAdd create2 = ObjectExtensions.<CreateAndAdd>operator_doubleArrow(_createAndAdd_1, _function_1);
      Object _execute = create2.execute(context, changes);
      _xblockexpression = (_execute);
    }
    return _xblockexpression;
  }
}
