/**
 * This file is part of CloudML [ http://cloudml.org ]
 *
 * Copyright (C) 2012 - SINTEF ICT Contact: Franck Chauvel
 * <franck.chauvel@sintef.no>
 *
 * Module: root
 *
 * CloudML is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * CloudML is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CloudML. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.cloudml.core;

import java.util.List;
import org.cloudml.core.visitors.Visitor;

public class ClientPortInstance extends ArtefactPortInstance<ClientPort> {

    public ClientPortInstance(String name, ClientPort type, ArtefactInstance owner) {
        super(name, type, owner);
    }

    public ClientPortInstance(String name, ClientPort type, List<Property> properties, ArtefactInstance owner) {
        super(name, type, properties, owner);
    }

    @Override
    public String toString() {
        return "ClientPortInstance " + name + " owner:" + owner.getName();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitClientPortInstance(this); 
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ClientPortInstance) {
            ClientPortInstance otherNode = (ClientPortInstance) other;
            return name.equals(otherNode.getName()) && owner.equals(otherNode.getOwner());
        }
        else {
            return false;
        }
    }
}
