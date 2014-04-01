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

import java.util.ArrayList;
import java.util.List;
import org.cloudml.core.collections.BindingInstanceGroup;
import org.cloudml.core.validation.Report;
import org.cloudml.core.visitors.Visitor;

public class ServerPortInstance extends ArtefactPortInstance<ServerPort> {

    public ServerPortInstance(String name, ServerPort type, ArtefactInstance owner) {
        super(name, type, owner);
    }

    public ServerPortInstance(String name, ServerPort type, List<Property> properties, ArtefactInstance owner) {
        super(name, type, properties, owner);
    }

    public ServerPortInstance(String name, ServerPort type, List<Property> properties, ArtefactInstance owner, boolean isRemote) {
        super(name, type, properties, owner, isRemote);
    }

     public List<ClientPortInstance> findClients() {
        final BindingInstanceGroup bindings = getDeployment().getBindingInstances().withPort(this);
        if (bindings.isEmpty()) {
            final String message = String.format("server port '%s' is not yet bound to any server", getName());
            throw new IllegalArgumentException(message);
        }
        final List<ClientPortInstance> clients = new ArrayList<ClientPortInstance>();
        for (BindingInstance binding : bindings) {
            clients.add(binding.getClient());
        }
        return clients;
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visitServerPortInstance(this);
    }

    @Override
    public Report validate() {
        final Report validation = new Report();
        if (type == null) {
            validation.addError("Server port instance has no type ('null' found)");
        }
        if (!hasOwner()) {
            validation.addError("Server port instance has no owner ('null' found)");
        }
        return validation;
    }

    @Override
    public String toString() {
        return "ServerPortInstance " + getName() + " owner:" + getOwner().getName();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ServerPortInstance) {
            ServerPortInstance otherNode = (ServerPortInstance) other;
            return getName().equals(otherNode.getName()) && getOwner().equals(otherNode.getOwner());
        }
        else {
            return false;
        }
    }
}
