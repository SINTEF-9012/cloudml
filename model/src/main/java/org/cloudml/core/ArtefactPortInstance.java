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
import org.cloudml.core.util.OwnedBy;
import org.cloudml.core.validation.CanBeValidated;
import org.cloudml.core.visitors.Visitable;

public abstract class ArtefactPortInstance<T extends ArtefactPort> extends WithProperties implements OwnedBy<ArtefactInstance>, DeploymentElement, Visitable, CanBeValidated {

    protected T type;
    private ArtefactInstance owner;

    public ArtefactPortInstance() {
    }

    public ArtefactPortInstance(String name, T type, ArtefactInstance owner) {
        super(name);
        this.type = type;
        this.owner = owner;
    }

    public ArtefactPortInstance(String name, T type, List<Property> properties, ArtefactInstance owner) {
        super(name, properties);
        this.type = type;
        this.owner = owner;
    }

    public ArtefactPortInstance(String name, T type, List<Property> properties, ArtefactInstance owner, boolean isRemote) {
        super(name, properties);
        this.type = type;
        this.owner = owner;
    }

    @Override
    public ArtefactInstance getOwner() {
        return this.owner;
    }

    @Override
    public void setOwner(ArtefactInstance newOwner) {
        this.owner = newOwner;
    }

    @Override
    public void discardOwner() {
        this.owner = null;
    }

    @Override
    public boolean hasOwner() {
        return this.owner != null;
    }

    @Override
    public DeploymentModel getDeployment() {
        return getOwner().getOwner();
    }

    public boolean isBound() {
        return !getDeployment().getBindingInstances().withPort(this).isEmpty();
    }

    public void setType(T type) {
        this.type = type;
    }

    public T getType() {
        return this.type;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ArtefactPortInstance) {
            ArtefactPortInstance otherNode = (ArtefactPortInstance) other;
            return getName().equals(otherNode.getName()) && owner.equals(otherNode.getOwner());
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "ArtefactPortInstance " + getName() + " owner:" + owner.getName();
    }
}
