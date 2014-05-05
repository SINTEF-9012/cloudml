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

import org.cloudml.core.visitors.Visitor;

public class ProvidedExecutionPlatformInstance extends ExecutionPlatformInstance<ProvidedExecutionPlatform> {

    public ProvidedExecutionPlatformInstance(String name, ProvidedExecutionPlatform type) {
        super(name, type);
    }

    public boolean match(InternalComponent component) {
        return getType().match(component.getRequiredExecutionPlatform());
    }

    public boolean match(InternalComponentInstance component) {
        return match(component.getType());
    }

    public boolean match(RequiredExecutionPlatformInstance platform) {
        return getType().match(platform.getType());
    }

    @Override
    public String toString() {
        return "ProvidedExecutionPlatformInstance " + getQualifiedName();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other instanceof ProvidedExecutionPlatformInstance) {
            ProvidedExecutionPlatformInstance otherNode = (ProvidedExecutionPlatformInstance) other;
            return getName().equals(otherNode.getName()) && this.getOwner().equals(otherNode.getOwner());
        }
        return false;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitProvidedExecutionPlatformInstance(this);
    }
}
