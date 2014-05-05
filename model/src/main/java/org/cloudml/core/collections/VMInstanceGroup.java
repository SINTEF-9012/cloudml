/**
 * This file is part of CloudML [ http://cloudml.org ]
 *
 * Copyright (C) 2012 - SINTEF ICT
 * Contact: Franck Chauvel <franck.chauvel@sintef.no>
 *
 * Module: root
 *
 * CloudML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * CloudML is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with CloudML. If not, see
 * <http://www.gnu.org/licenses/>.
 */


package org.cloudml.core.collections;

import java.util.ArrayList;
import java.util.Collection;
import org.cloudml.core.Cloud;
import org.cloudml.core.VM;
import org.cloudml.core.VMInstance;


public class VMInstanceGroup extends NamedElementGroup<VMInstance> {

    public VMInstanceGroup() {
        super();
    }

    public VMInstanceGroup(Collection<VMInstance> content) {
        super(content);  
    }    
    
    public VMInstanceGroup ofType(VM vm) {
        final ArrayList<VMInstance> selectedInstances = new ArrayList<VMInstance>();
        for (VMInstance instance : this) {  
            if (instance.getType().equals(vm)) {
                selectedInstances.add(instance);
            }
        }
        return new VMInstanceGroup(selectedInstances);
    }
    
    
    public VMInstanceGroup ofType(String typeName) { 
        final ArrayList<VMInstance> selectedInstances = new ArrayList<VMInstance>();
        for (VMInstance nodeInstance : this) {
            if (nodeInstance.getType().getName().equals(typeName)) {
                selectedInstances.add(nodeInstance);
            }
        }
        return new VMInstanceGroup(selectedInstances);
    }
    
    
    public VMInstanceGroup whichBelongsTo(Cloud cloud) {
        final VMInstanceGroup selection = new VMInstanceGroup();
        for (final VMInstance vm: this) {
            if (vm.belongsTo(cloud)) {
                selection.add(vm);
            }
        }
        return selection;
    }
    
    
}
