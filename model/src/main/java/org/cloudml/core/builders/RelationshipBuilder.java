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
/*
 */
package org.cloudml.core.builders;

import org.cloudml.core.*;

import static org.cloudml.core.builders.Commons.*;

public class RelationshipBuilder extends WithResourcesBuilder<Relationship, RelationshipBuilder> implements SubPartBuilder<Deployment> {

    private String clientName;
    private String clientPortName;
    private String serverName;
    private String serverPortName;
    private ResourceBuilder clientResource;
    private ResourceBuilder serverResource;

    public RelationshipBuilder() {
        clientName = "default client component";
        clientPortName = "default client port";
        serverName = "default server component";
        serverPortName = "default server port";
    }

    public RelationshipBuilder from(String componentName, String requiredPortName) {
        clientName = componentName;
        clientPortName = requiredPortName;
        return next();
    }

    public RelationshipBuilder to(String componentName, String providedPortName) {
        serverName = componentName;
        serverPortName = providedPortName;
        return next();
    }

    @Override
    public Relationship build() {
        final Relationship relationship = new Relationship(
                getName(),
                aRequiredPort().named(clientPortName).build(),
                aProvidedPort().named(serverPortName).build());
        if(clientResource != null)
            relationship.setClientResource(clientResource.build());
        if(serverResource != null)
            relationship.setServerResource(serverResource.build());
        super.prepare(relationship);
        return relationship;
    }

    @Override
    protected RelationshipBuilder next() {
        return this;
    }

    @Override
    public void integrateIn(Deployment container) {
        final Relationship relationship = new Relationship(getName(), findClientPort(container), findServerPort(container));
        if(clientResource != null)
            relationship.setClientResource(clientResource.build());
        if(serverResource != null)
            relationship.setServerResource(serverResource.build());
        super.prepare(relationship);
        container.getRelationships().add(relationship);
        relationship.getOwner().set(container);
    }

    private InternalComponent findClient(Deployment container) throws IllegalStateException {
        final InternalComponent client = container.getComponents().onlyInternals().firstNamed(clientName);
        if (client == null) {
            final String error = String.format("type '%s' not found",clientName);
            throw new IllegalStateException(formatError(error));
        }
        return client;
    }

    private String formatError(String issue) {
        return String.format("Unable to create relationship instance '%s'! (%s)", getName(), issue);
    }

    private RequiredPort findClientPort(Deployment container) throws IllegalStateException {
        final RequiredPort clientPort = findClient(container).getRequiredPorts().firstNamed(clientPortName);
        if (clientPort == null) {
            final String error = String.format("required port '%s::%s' not found!", clientName, clientPortName);
            throw new IllegalStateException(formatError(error));
        }
        return clientPort;
    }

    private Component findServer(Deployment container) throws IllegalStateException {
        final Component server = container.getComponents().firstNamed(serverName);
        if (server == null) {
            final String error = String.format("component type '%s' not found", serverName);
            throw new IllegalStateException(formatError(error));
        }
        return server;
    }

    private ProvidedPort findServerPort(Deployment container) throws IllegalStateException {
        final ProvidedPort serverPort = findServer(container).getProvidedPorts().firstNamed(serverPortName);
        if (serverPort == null) {
            final String error = String.format("provided port '%s::%s' not found", serverName, serverPortName);
            throw new IllegalStateException(formatError(error));
        }
        return serverPort;
    }

    public RelationshipBuilder withClientResource(ResourceBuilder builder) {
        this.clientResource=builder;
        return next();
    }

    public RelationshipBuilder withServerResource(ResourceBuilder builder) {
        this.serverResource=builder;
        return next();
    }
}
