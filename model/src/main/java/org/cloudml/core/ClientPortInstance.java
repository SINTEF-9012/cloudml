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
package org.cloudml.core;

import java.util.List;
import org.cloudml.core.collections.BindingInstanceGroup;
import org.cloudml.core.validation.Report;
import org.cloudml.core.visitors.Visitor;

public class ClientPortInstance extends ArtefactPortInstance<ClientPort> {

    public ClientPortInstance(String name, ClientPort type, ArtefactInstance owner) {
        super(name, type, owner);
    }

    public ClientPortInstance(String name, ClientPort type, List<Property> properties, ArtefactInstance owner) {
        super(name, type, properties, owner);
    }

     public ServerPortInstance findServerPort() {
        final BindingInstanceGroup bindings = getDeployment().getBindingInstances().withPort(this);
        if (bindings.isEmpty()) {
            final String message = String.format("client port '%s' is not yet bound to any server", getName());
            throw new IllegalArgumentException(message);
        }
        return bindings.toList().get(0).getServer();
    }
    
    @Override
    public String toString() {
        return "ClientPortInstance " + getName() + " owner:" + getOwner().getName();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitClientPortInstance(this);
    }

    @Override
    public Report validate() {
        final Report validation = new Report();
        if (type == null) {
            validation.addError("Client port instance has no type ('null' found)");
        }
        if (!hasOwner()) {
            validation.addError("Client port instance has no owner ('null' found)");
        }
        return validation;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ClientPortInstance) {
            ClientPortInstance otherNode = (ClientPortInstance) other;
            return getName().equals(otherNode.getName()) && getOwner().equals(otherNode.getOwner());
        }
        else {
            return false;
        }
    }
}
