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

public class NodePort extends WithProperties {

    private Node owner;

    public NodePort() {
    }

    public NodePort(String name, Node owner) {
        super(name);
        this.owner = owner;
    }

    public NodePort(String name, List<Property> properties, Node owner) {
        super(name, properties);
        this.owner = owner;
    }

    public Node getOwner() {
        return owner;
    }

    public void setOwner(Node owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "NodePort: " + name + " ownerType: " + owner.getName();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof NodePort) {
            NodePort otherNode = (NodePort) other;
            return name.equals(otherNode.getName());
        } else {
            return false;
        }
    }
}
