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

package org.cloudml.core.builders;


import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.ClientPort;
import static org.cloudml.core.builders.Commons.*;
import static org.cloudml.core.ClientPortInstance.*;
import org.cloudml.core.ClientPortInstance;

public class ClientPortInstanceBuilder {

    private String name;
    private String typeName;
    private String ownerName;
    
    public ClientPortInstanceBuilder() {
        this.name = DEFAULT_NAME;
        this.typeName = DEFAULT_NAME;
        this.ownerName = DEFAULT_NAME;
    }
    
    
    public ClientPortInstanceBuilder withName(String name) {
        this.name = name;
        return this;
    }
    
    
    public ClientPortInstanceBuilder ofType(String typeName) {
        this.typeName = typeName;
        return this;
    }
    
    public ClientPortInstanceBuilder ownedBy(String ownerName) {
        this.ownerName = ownerName;
        return this;
    }
    
    
    public ClientPortInstance build() {
        ArtefactInstance owner = anArtefactInstance().named(ownerName).build();
        ClientPort portType = aClientPort().named(typeName).build();
        owner.getType().addRequiredPort(portType);
        final ClientPortInstance result = new ClientPortInstance(name, portType, owner);
        owner.addRequiredPort(result);
        return result;
    }
    
    
    public void integrateIn(ArtefactInstance container) {
        ClientPortInstance result = new ClientPortInstance(
                name, 
                container.getType().findRequiredPortByName(typeName), 
                container);
        container.addRequiredPort(result);
    }
}
