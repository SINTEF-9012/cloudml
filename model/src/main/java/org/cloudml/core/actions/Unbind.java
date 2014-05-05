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

import java.util.List;
import org.cloudml.core.Deployment;
import org.cloudml.core.Port;
import org.cloudml.core.PortInstance;
import org.cloudml.core.RelationshipInstance;
import org.cloudml.core.collections.RelationshipInstanceGroup;


public class Unbind extends AbstractAction<Void> {

    private final PortInstance<? extends Port> port;

    public Unbind(StandardLibrary library, PortInstance<? extends Port> port) {
        super(library);
        this.port = port;
    }

    @Override
    public Void applyTo(Deployment deployment) {
        RelationshipInstanceGroup bindings = deployment.getRelationshipInstances().whereEitherEndIs(port); 
        for(RelationshipInstance binding: bindings) {
            deployment.getRelationshipInstances().remove(binding);
        }
        return NOTHING;
    }  
    
}
