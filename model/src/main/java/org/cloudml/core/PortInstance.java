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

import org.cloudml.core.util.OwnedBy;

public abstract class PortInstance<T extends Port> extends WithResources implements DeploymentElement, OwnedBy<ComponentInstance<? extends Component>> {

    private T type;
    private final OptionalOwner<ComponentInstance<? extends Component>> owner;

    public PortInstance(String name, T type) {
        super(name);
        this.owner = new OptionalOwner<ComponentInstance<? extends Component>>();
        setType(type);
    }

    @Override
    public OptionalOwner<ComponentInstance<? extends Component>> getOwner() {
        return this.owner;
    }

    @Override
    public String getQualifiedName() {
        return String.format("%s%s%s", getOwner().getName(), CONTAINED, getName());
    }

    @Override
    public Deployment getDeployment() {
        return getOwner().get().getDeployment();
    }

    public boolean isBound() {
        return !getDeployment().getRelationshipInstances().whereEitherEndIs(this).isEmpty();
    }

    public boolean isInstanceOf(T type) {
        return this.type.equals(type);
    }

    public final void setType(T type) {
        if (type == null) {
            final String error = String.format("'null' is not a valid port type for port instance '%s'", getQualifiedName());
            throw new IllegalArgumentException(error);
        }
        this.type = type;
    }

    public T getType() {
        return this.type;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof PortInstance) {
            PortInstance otherNode = (PortInstance) other;
            return getName().equals(otherNode.getName()) && getOwner().equals(otherNode.getOwner());
        }
        return false;
    }

    @Override
    public String toString() {
        return "PortInstance " + getQualifiedName();
    }
}
