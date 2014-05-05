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
import org.cloudml.core.validation.CanBeValidated;
import org.cloudml.core.validation.Report;
import org.cloudml.core.visitors.Visitable;

public abstract class Port extends WithResources implements DeploymentElement, OwnedBy<Component>, Visitable, CanBeValidated {

    public static final int DEFAULT_PORT_NUMBER = 0;
    public static final boolean LOCAL = false;
    public static final boolean REMOTE = true;
    private final OptionalOwner<Component> owner;
    private boolean remote;
    private int portNumber;

    public Port(String name) {
        this(name, REMOTE);
    }

    public Port(String name, boolean isRemote) {
        super(name);
        this.owner = new OptionalOwner<Component>();
        this.remote = isRemote;
        this.portNumber = DEFAULT_PORT_NUMBER;
    }

    @Override
    public void validate(Report report) {
        if (owner.isUndefined()) {
            final String message = String.format("The port '%s' has no owner", getQualifiedName());
            report.addError(message);
        }
    }
    
    public abstract PortInstance<? extends Port> instantiate();

    @Override
    public final Deployment getDeployment() {
        return getOwner().get().getDeployment();
    }

    @Override
    public OptionalOwner<Component> getOwner() {
        return owner;
    }

    @Override
    public String getQualifiedName() {
        return String.format("%s%s%s", getOwner().getName(), CONTAINED, getName());
    }

    public final int getPortNumber() {
        return portNumber;
    }

    public final void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public final void setRemote(boolean isRemote) {
        this.remote = isRemote;
    }

    public final boolean isRemote() {
        return this.remote;
    }

    public final boolean isLocal() {
        return !isRemote();
    }

    @Override
    public String toString() {
        return "PortType: " + getQualifiedName();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other instanceof Port) {
            Port otherArt = (Port) other;
            return getName().equals(otherArt.getName()) && owner.equals(otherArt.getOwner());
        }
        return false;
    }
}
