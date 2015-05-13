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

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.cloudml.core.builders.Commons.*;

public class RelationshipInstanceBuilder extends WithResourcesBuilder<RelationshipInstance, RelationshipInstanceBuilder> implements SubPartBuilder<Deployment> {
    public static final String DEFAULT_RELATIONSHIP_INSTANCE_NAME = "Default relationship instance name";
    private static final Logger journal = Logger.getLogger(RelationshipInstanceBuilder.class.getName());

    private String typeName;
    private String client;
    private String clientPort;
    private String server;
    private String serverPort;

    public RelationshipInstanceBuilder() {
        typeName = DEFAULT_RELATIONSHIP_INSTANCE_NAME;
        client = "default client instance";
        clientPort = "port";
        server = "default server instance";
        serverPort = "port";
    }

    public RelationshipInstanceBuilder ofType(String typeName) {
        this.typeName = typeName;
        return next();
    }

    public RelationshipInstanceBuilder from(String componentName, String portName) {
        client = componentName;
        clientPort = portName;
        return next();
    }

    public RelationshipInstanceBuilder to(String componentName, String portName) {
        server = componentName;
        serverPort = portName;
        return next();
    }

    @Override
    public RelationshipInstance build() {
        final RelationshipInstance relationship = new RelationshipInstance(getName(), createClientPort(), createServerPort(), createType());
        super.prepare(relationship);
        return relationship;
    }

    private RequiredPortInstance createClientPort() {
        return new RequiredPortInstance(clientPort, new RequiredPort("required port type"));
    }

    private ProvidedPortInstance createServerPort() {
        return new ProvidedPortInstance(serverPort, new ProvidedPort("provided port type"));
    }

    private Relationship createType() {
        return new Relationship(typeName, new RequiredPort("required port type"), new ProvidedPort("provided port type"));
    }


    @Override
    protected RelationshipInstanceBuilder next() {
        return this;
    }

    @Override
    public void integrateIn(Deployment container) {
        final RelationshipInstance relationship = new RelationshipInstance(getName(), findClientPort(container), findServerPort(container), findType(container));
        super.prepare(relationship);
        container.getRelationshipInstances().add(relationship);
    }

    private RequiredPortInstance findClientPort(Deployment container) {
        final InternalComponentInstance client = findClient(container);
        final RequiredPort portType = client.getType().getRequiredPorts().firstNamed(clientPort);
        if(portType == null){
            final String error = String.format("Unable to find the required port type '%s'", clientPort);
            throw new IllegalStateException(error);
        }
        final RequiredPortInstance port = client.getRequiredPorts().ofType(portType);
        if (port == null) {
            final String error = String.format("Unable to find the required port instance '%s' in '%s'", clientPort, client);
            throw new IllegalStateException(error);
        }
        return port;
    }

    private ProvidedPortInstance findServerPort(Deployment container) {
        final ComponentInstance server = findServer(container);
        final ProvidedPort portType = server.getType().getProvidedPorts().firstNamed(serverPort);
        final ProvidedPortInstance port = server.getProvidedPorts().ofType(portType);
        if (port == null) {
            final String error = String.format("Unable to find the provided port '%s' in '%s'", serverPort, server);
            throw new IllegalStateException(error);
        }
        return port;
    }

    private Relationship findType(Deployment container) {
        final Relationship type = container.getRelationships().firstNamed(typeName);
        if (type == null) {
            final String error = String.format("Unable to find the relationship '%s'", typeName);
            throw new IllegalStateException(error);
        }
        return type;
    }

    private ComponentInstance<? extends Component> findServer(Deployment container) throws IllegalStateException {
        final ComponentInstance<? extends Component> serverComponent = container.getComponentInstances().firstNamed(server);
        if (serverComponent == null) {
            final String error = String.format("Unable to find the component instance '%s'", server);
            throw new IllegalStateException(error);
        }
        return serverComponent;
    }

    private InternalComponentInstance findClient(Deployment container) throws IllegalStateException {
        final InternalComponentInstance clientComponent = container.getComponentInstances().onlyInternals().firstNamed(client);
        if (clientComponent == null) {
            final String error = String.format("Unable to find the internal component instance '%s'", client);
            throw new IllegalStateException(error);
        }
        return clientComponent;
    }
}
