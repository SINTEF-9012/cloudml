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

    @Override
    public void accept(Visitor visitor) {
        visitor.visitServerPortInstance(this);
    }

    @Override
    public Report validate() {
        final Report validation = new Report();
        if (name == null) {
            validation.addError("Server port instance has no name ('null' found)");
        }
        if (type == null) {
            validation.addError("Server port instance has no type ('null' found)");
        }
        if (owner == null) {
            validation.addError("Server port instance has no owner ('null' found)");
        }
        return validation;
    }

    @Override
    public String toString() {
        return "ServerPortInstance " + name + " owner:" + owner.getName();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ServerPortInstance) {
            ServerPortInstance otherNode = (ServerPortInstance) other;
            return name.equals(otherNode.getName()) && owner.equals(otherNode.getOwner());
        }
        else {
            return false;
        }
    }
}
