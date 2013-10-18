package org.cloudml.mrt.coord.cmd.abstracts;

import java.util.List;

public class Instruction {
    public Object execute(Object context, List<Change> changes){
        return _execute(context, changes);
    }
    
    /**
     * This method is left to the generated instructions
     * @param context
     * @return 
     */
    protected Object _execute(final Object context, List<Change> changes){
        return null;
    }
}
