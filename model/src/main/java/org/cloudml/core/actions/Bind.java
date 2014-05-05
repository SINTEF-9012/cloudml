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



import org.cloudml.core.Deployment;
import org.cloudml.core.ProvidedPortInstance;
import org.cloudml.core.Relationship;
import org.cloudml.core.RequiredPortInstance;
import static org.cloudml.core.builders.Commons.*;


public class Bind extends AbstractAction<Void> { 

    private final RequiredPortInstance clientPort;
    
    public Bind(StandardLibrary library, RequiredPortInstance clientPort) {
      super(library);
      this.clientPort = clientPort;
    }

    @Override
    public Void applyTo(Deployment deployment) {
        if (!clientPort.isBound()) {
            final Relationship bindingType = getLibrary().findRelationshipFor(deployment, clientPort);
            final String name = getLibrary().createUniqueRelationshipInstanceName(deployment, bindingType); 
            final ProvidedPortInstance serverPort = getLibrary().findProvidedPortFor(deployment, bindingType);   
            aRelationshipInstance()
                    .named(name)
                    .from(clientPort.getOwner().getName(), clientPort.getName())
                    .to(serverPort.getOwner().getName(), serverPort.getName())
                    .ofType(bindingType.getName())
                    .integrateIn(deployment);        
        }
        return NOTHING;
    }
    
}
