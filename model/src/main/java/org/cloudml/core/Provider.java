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
import org.cloudml.core.collections.ExternalComponentGroup;
import org.cloudml.core.credentials.Credentials;
import org.cloudml.core.credentials.NoCredentials;
import org.cloudml.core.validation.Report;
import org.cloudml.core.visitors.Visitor;

public class Provider extends WithResources implements DeploymentElement, OwnedBy<Deployment> {

    private final OptionalOwner<Deployment> owner;
    private Credentials credentials;

    public Provider() {
        this(DEFAULT_NAME);
    }

    public Provider(String name) {
        this(name, NoCredentials.getInstance());
    }

    public Provider(String name, Credentials credentials) {
        super(name);
        this.owner = new OptionalOwner<Deployment>();
        this.credentials = credentials;
    }

    @Override
    public Deployment getDeployment() {
        return getOwner().get();
    }

    @Override
    public OptionalOwner<Deployment> getOwner() {
        return owner;
    }

    @Override
    public String getQualifiedName() {
        return getOwner().getName() + "::" + getName();
    }

    public boolean isUsed() {
        if (getOwner().isUndefined()) {
            return false;
        }
        return !providedComponents().isEmpty();
    }

    public ExternalComponentGroup providedComponents() {
        if (getOwner().isUndefined()) {
            return new ExternalComponentGroup();
        }
        return getDeployment().getComponents().onlyExternals().providedBy(this);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitProvider(this);
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public String toString() {
        return "Name: " + getName();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other instanceof Provider) {
            Provider otherProvider = (Provider) other;
            return otherProvider.isNamed(getName());
        }
        return false;
    }
}
