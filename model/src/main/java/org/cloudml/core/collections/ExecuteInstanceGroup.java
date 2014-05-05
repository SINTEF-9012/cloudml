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

import org.cloudml.core.Component;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.ExecuteInstance;
import org.cloudml.core.InternalComponentInstance;


public class ExecuteInstanceGroup extends WithResourceGroup<ExecuteInstance> {

    public ExecuteInstance between(String demanderName, String demandName, String providerName, String providedName) {
        for (ExecuteInstance instance: this) {
            if (instance.isBetween(demanderName, demandName, providerName, providedName)) {
                return instance;
            }
        }
        return null;
    }
    
    public ComponentInstance<? extends Component> hostOf(InternalComponentInstance component) {
        for (ExecuteInstance execution: this) {
            if (execution.hasSubject(component)) { 
                return execution.getHost();
            }
        }
        return null;
    }
    
    public InternalComponentInstanceGroup componentsHostedBy(ComponentInstance<? extends Component> host) {
        final InternalComponentInstanceGroup selection = new InternalComponentInstanceGroup();
        for (ExecuteInstance execution: this) {
            if (execution.isHostedBy(host)) {
                selection.add(execution.getSubject());
            } 
        }
        return selection;
    }

}
