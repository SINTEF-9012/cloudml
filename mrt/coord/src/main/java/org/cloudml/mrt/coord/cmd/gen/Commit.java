package org.cloudml.mrt.coord.cmd.gen;

import java.util.List;
import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.cloudml.mrt.coord.cmd.abstracts.Instruction;
import org.cloudml.mrt.coord.cmd.abstracts.Modification;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Commit extends Instruction {
  public boolean enforce;
  
  public List<Modification> modifications;
  
  public Commit() {
  }
  
  public Commit(final String nouse) {
  }
  
  public Commit(final Procedure1<Commit> initializer) {
    initializer.apply(this);
  }
  
  @Override
  protected Object _execute(final Object context, final List<Change> changes) {
    for (final Modification modi : this.modifications) {
      modi.execute(context, changes);
    }
    return null;
  }
}
