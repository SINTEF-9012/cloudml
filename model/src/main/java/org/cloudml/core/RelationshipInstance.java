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

public class RelationshipInstance extends WithResources implements DeploymentElement, OwnedBy<Deployment> {

    private final OptionalOwner<Deployment> owner;
    private RequiredPortInstance requiredEnd;
    private ProvidedPortInstance providedEnd;
    private Relationship type;

    public RelationshipInstance(String name, RequiredPortInstance requiredEnd, ProvidedPortInstance providedEnd, Relationship type) {
        super(name);
        this.owner = new OptionalOwner<Deployment>();
        setRequiredEnd(requiredEnd);
        setProvidedEnd(providedEnd);
        setType(type);
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

    @Override
    public void accept(Visitor visitor) {
        visitor.visitRelationshipInstance(this);
    }

    @Override
    public void validate(Report report) {
        validateRequiredEnd(report); 
        validateProvidedEnd(report);
    }

    private void validateRequiredEnd(Report report) {
        if (!requiredEnd.getType().equals(type.getRequiredEnd())) {
            final String message = String.format(
                    "illegal relationship '%s' instance that does not match its type (required port found '%s' but expected '%s')",
                    getName(),
                    requiredEnd.getType().getName(),
                    type.getRequiredEnd().getName());
            report.addError(message);
        }
    }

    private void validateProvidedEnd(Report report) {
        if (!providedEnd.getType().equals(type.getProvidedEnd())) {
            final String message = String.format(
                    "illegal relationship instance '%s' that does not match its type (provided port found '%s' but expected '%s')", 
                    getName(),
                    providedEnd.getType().getQualifiedName(), 
                    type.getProvidedEnd().getQualifiedName());
            report.addError(message);
        }
    }

    public final void setRequiredEnd(RequiredPortInstance port) {
        if (port == null) {
            final String error = String.format("'null' is not a valid required port end for relationship '%s'", getQualifiedName());
            throw new IllegalArgumentException(error);
        }
        this.requiredEnd = port;
    }

    public final void setProvidedEnd(ProvidedPortInstance port) {
        if (port == null) {
            final String error = String.format("'null' is not a valid provided port end for relationship '%s'", getQualifiedName());
            throw new IllegalArgumentException(error);
        }
        this.providedEnd = port;
    }

    public RequiredPortInstance getRequiredEnd() {
        return this.requiredEnd;
    }
    
    public InternalComponentInstance getClientComponent() {
        return (InternalComponentInstance) getRequiredEnd().getOwner().get();
    }

    public ProvidedPortInstance getProvidedEnd() {
        return this.providedEnd;
    }
    
    public ComponentInstance<? extends Component> getServerComponent() {
        return getProvidedEnd().getOwner().get();
    }
    
    public boolean isProvidedBy(ComponentInstance<? extends Component> candidateServer) {
        return getServerComponent().equals(candidateServer);
    }
    
    public boolean isRequiredBy(ComponentInstance<? extends Component> candidateClient) {
    	return getClientComponent().equals(candidateClient);
    }

    public boolean eitherEndIs(PortInstance<? extends Port> port) {
        return getProvidedEnd().equals(port) || getRequiredEnd().equals(port);
    }

    public Relationship getType() {
        return type;
    }

    private void setType(Relationship type) {
        if (type == null) {
            final String error = String.format("'null' is not a valid relationship type for '%s'", getQualifiedName());
            throw new IllegalArgumentException(error);
        }
        this.type = type;
    }

    @Override
    public String toString() {
        return requiredEnd + "->" + providedEnd;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof RelationshipInstance) {
            RelationshipInstance otherBinding = (RelationshipInstance) other;
            return (
                    ((requiredEnd == null && otherBinding.getRequiredEnd() == null) || requiredEnd.equals(otherBinding.getRequiredEnd())) && 
                    ((providedEnd == null && otherBinding.getProvidedEnd() == null) || providedEnd.equals(otherBinding.getProvidedEnd()))
                   );
        }
        else {
            return false;
        }
    }
}
