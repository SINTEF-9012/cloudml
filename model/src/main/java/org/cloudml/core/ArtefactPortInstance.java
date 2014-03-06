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
import org.cloudml.core.visitors.Visitable;

public abstract class ArtefactPortInstance<T extends ArtefactPort> extends WithProperties implements Visitable {

    protected T type;
    protected ArtefactInstance owner;

    public ArtefactPortInstance() {
    }

    public ArtefactPortInstance(String name, T type, ArtefactInstance owner) {
        super(name);
        this.type = type;
        this.owner = owner;
        // If we define the owner of the port, then we can add it in the provided port list
        /*
         * owner.getProvided().add(this);
        owner.getType().getProvided().add(type);
         */
    }

    public ArtefactPortInstance(String name, T type, List<Property> properties, ArtefactInstance owner) {
        super(name, properties);
        this.type = type;
        this.owner = owner;
        //If we define the owner of the port, then we can add it in the provided port list
        /*
         * owner.getProvided().add(this);
        owner.getType().getProvided().add(type);
         */
    }

    public ArtefactPortInstance(String name, T type, List<Property> properties, ArtefactInstance owner, boolean isRemote) {
        super(name, properties);
        this.type = type;
        this.owner = owner;
        //If we define the owner of the port, then we can add it in the provided port list
        /*
         * owner.getProvided().add(this);
        owner.getType().getProvided().add(type);
         */
    }

    public ArtefactInstance getOwner() {
        return this.owner;
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
            return name.equals(otherNode.getName()) && owner.equals(otherNode.getOwner());
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "ArtefactPortInstance " + name + " owner:" + owner.getName();
    }
}
