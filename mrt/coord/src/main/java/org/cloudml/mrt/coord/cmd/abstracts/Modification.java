package org.cloudml.mrt.coord.cmd.abstracts;

import java.util.List;

public class Modification {
    public Object execute(Object context, List<Change> changes){
        return _execute(context, changes);
    }
    
    protected Object _execute(final Object context, List<Change> changes){
        return null;
    }
}
