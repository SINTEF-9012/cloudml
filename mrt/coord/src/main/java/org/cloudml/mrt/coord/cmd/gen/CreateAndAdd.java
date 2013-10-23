package org.cloudml.mrt.coord.cmd.gen;

import java.util.List;
import java.util.Map;
import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.cloudml.mrt.coord.cmd.abstracts.Modification;
import org.cloudml.mrt.coord.cmd.abstracts.Parameter;
import org.cloudml.mrt.coord.cmd.abstracts.Property;
import org.cloudml.mrt.coord.cmd.abstracts.Type;
import org.cloudml.mrt.coord.cmd.abstracts.XPath;
import org.cloudml.mrt.coord.cmd.gen.Add;
import org.cloudml.mrt.coord.cmd.gen.Create;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * Composite modification from Create and Add. This is probably the most
 * frequently used modification.
 * 
 *     - !createAndAdd
 *     parent : /
 *     property : nodeInstances
 *     type : NodeInstance
 *     initializer :
 *       - {type: String, value: ni1}
 *       - {type: Node, value: !xpath /nodeTypes/node1}
 */
@SuppressWarnings("all")
public class CreateAndAdd extends Modification {
  public CreateAndAdd() {
  }
  
  public CreateAndAdd(final String nouse) {
  }
  
  public CreateAndAdd(final Procedure1<CreateAndAdd> initializer) {
    initializer.apply(this);
  }
  
  public XPath parent;
  
  public Property property;
  
  public Type type;
  
  public List<Parameter> initializer;
  
  public Map<Property,Object> keyValues;
  
  public Object index;
  
  @Override
  protected Object _execute(final Object context, final List<Change> changes) {
    Object _xblockexpression = null;
    {
      final Procedure1<Create> _function = new Procedure1<Create>() {
          public void apply(final Create it) {
            it.type = CreateAndAdd.this.type;
            it.keyValues = CreateAndAdd.this.keyValues;
            it.initializer = CreateAndAdd.this.initializer;
          }
        };
      Create _create = new Create(_function);
      final Object newObject = _create.execute(context, changes);
      final Procedure1<Add> _function_1 = new Procedure1<Add>() {
          public void apply(final Add it) {
            it.parent = CreateAndAdd.this.parent;
            it.property = CreateAndAdd.this.property;
            it.newValue = newObject;
            it.index = CreateAndAdd.this.index;
          }
        };
      Add _add = new Add(_function_1);
      final Object add = _add.execute(context, changes);
      _xblockexpression = (null);
    }
    return _xblockexpression;
  }
}
