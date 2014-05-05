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

import org.cloudml.core.collections.RelationshipInstanceGroup;
import org.cloudml.core.validation.Report;
import org.cloudml.core.visitors.Visitor;

public class RequiredPortInstance extends PortInstance<RequiredPort> {

    public RequiredPortInstance(String name, RequiredPort type) {
        super(name, type);
    }

    public ProvidedPortInstance findServerPort() {
        final RelationshipInstanceGroup relationships = getDeployment().getRelationshipInstances().whereEitherEndIs(this);
        if (relationships.isEmpty()) {
            final String message = String.format("client port '%s' is not yet bound to any server", getName());
            throw new IllegalArgumentException(message);
        }
        return relationships.toList().get(0).getProvidedEnd();
    }

    public ComponentInstance<? extends Component> findProvider() {
        return findServerPort().getOwner().get();
    }

    @Override
    public String toString() {
        return "RequiredPortInstance " + getQualifiedName();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitRequiredPortInstance(this);
    }

    @Override
    public void validate(Report report) {
        if (getOwner().isUndefined()) {
            report.addError("Client port instance has no owner");
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof RequiredPortInstance) {
            RequiredPortInstance otherNode = (RequiredPortInstance) other;
            return getName().equals(otherNode.getName()) && getOwner().equals(otherNode.getOwner());
        }
        return false;
    }
}
