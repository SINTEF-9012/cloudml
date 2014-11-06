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
import org.cloudml.core.validation.Report;
import org.cloudml.core.visitors.Visitor;

/**
 * Relationship between two InternalComponents
 */
public class Relationship extends WithResources implements DeploymentElement, OwnedBy<Deployment> {

    private final OptionalOwner<Deployment> owner;
    private RequiredPort requiredEnd;
    private ProvidedPort providedEnd;
    private Resource clientResource;
    private Resource serverResource;

    public Relationship(String name, RequiredPort requiredPort, ProvidedPort providedPort) {
        super(name);
        this.owner = new OptionalOwner<Deployment>();
        setRequiredEnd(requiredPort);
        setProvidedEnd(providedPort); 
    }

    @Override
    public Deployment getDeployment() {
        return getOwner().get();
    }

    @Override
    public String getQualifiedName() {
        return String.format("%s%s%s", getOwner().getName(), CONTAINED, getName());
    }

    @Override
    public OptionalOwner<Deployment> getOwner() {
        return owner;
    }

    public RelationshipInstance instantiates(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitRelationship(this);
    }

    @Override
    public void validate(Report report) {
        if (requiredEnd != null && providedEnd != null) {
            if (requiredEnd.isLocal() && providedEnd.isRemote()) {
                final String message = String.format(
                        "Illegal relationship (%s) between a local client (%s) and a remote server (%s)",
                        getName(),
                        requiredEnd.getQualifiedName(),
                        providedEnd.getQualifiedName()
                        );
                report.addError(message);
            }
            if (requiredEnd.isRemote() && providedEnd.isLocal()) {
                final String message = String.format(
                        "Illegal relationship (%s) between a remote client (%s) and a local server (%s)",
                        getName(),
                        requiredEnd.getQualifiedName(),
                        providedEnd.getQualifiedName());
                report.addError(message);
            }
        }
    }

    public RelationshipInstance instantiates(RequiredPortInstance client, ProvidedPortInstance server) {
        throw new UnsupportedOperationException();
    }

    public final void setRequiredEnd(RequiredPort port) {
        rejectIfInvalid(port);
        this.requiredEnd = port;
    }

    private void rejectIfInvalid(RequiredPort port) {
        if (port == null) {
            final String error = String.format("'null' is not a valid required end for relationship '%s'", getQualifiedName());
            throw new IllegalArgumentException(error);
        }
    }

    public final void setProvidedEnd(ProvidedPort port) {
        rejectIfInvalid(port);
        this.providedEnd = port;
    }

    private void rejectIfInvalid(ProvidedPort port) {
        if (port == null) {
            final String error = String.format("'null' is not a valid provided end for relationship '%s'", getQualifiedName());
            throw new IllegalArgumentException(error);
        }
    }

    public RequiredPort getRequiredEnd() {
        return this.requiredEnd;
    }
    
    public InternalComponent getClientComponent() {
        return this.requiredEnd.getOwner().get().asInternal();
    }

    public ProvidedPort getProvidedEnd() {
        return this.providedEnd;
    }
    
    public Component getServerComponent() {
        return this.providedEnd.getOwner().get();
    }

    public void setClientResource(Resource clientResource) {
        this.clientResource = clientResource;
    }

    public Resource getClientResource() {
        return clientResource;
    }

    public void setServerResource(Resource serverResource) {
        this.serverResource = serverResource;
    }

    public Resource getServerResource() {
        return serverResource;
    }

    @Override
    public String toString() {
        return requiredEnd + "->" + providedEnd;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other instanceof Relationship) {
            Relationship relationship = (Relationship) other;
            return (requiredEnd.getName().equals(relationship.getRequiredEnd().getName()) && providedEnd.getName().equals(relationship.getProvidedEnd().getName()));
        }
        return false;
    }
}
