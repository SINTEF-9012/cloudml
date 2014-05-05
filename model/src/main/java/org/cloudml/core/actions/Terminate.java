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
package org.cloudml.core.actions;

import org.cloudml.core.Component;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.Deployment;
import org.cloudml.core.InternalComponentInstance;
import org.cloudml.core.collections.InternalComponentInstanceGroup;


public class Terminate extends AbstractAction<Void> {

    private final ComponentInstance<? extends Component> instance;

    public Terminate(StandardLibrary library, ComponentInstance<? extends Component> instance) {
        super(library); 
        this.instance = instance;
    } 
    
    @Override
    public Void applyTo(Deployment deployment) {
        final InternalComponentInstanceGroup hosted = deployment.getComponentInstances().onlyInternals().hostedOn(instance);  
        for(InternalComponentInstance hostedComponent: hosted) {
            getLibrary().migrate(deployment, hostedComponent);
        } 
        getLibrary().stop(deployment, instance);
        deployment.getComponentInstances().remove(instance);
        return NOTHING;
    }
    
}
