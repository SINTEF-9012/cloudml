package org.cloudml.mrt.coord.cmd.gen;

import java.util.List;
import org.cloudml.mrt.coord.cmd.abstracts.Instruction;
import org.cloudml.mrt.coord.cmd.abstracts.Modification;

@SuppressWarnings("all")
public class Commit extends Instruction {
  public boolean enforce;
  
  public List<Modification> modifications;
  
  @Override
  protected Object _execute(final Object context) {
    for (final Modification modi : this.modifications) {
      modi.execute(context);
    }
    return null;
  }
}
