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

import java.util.LinkedList;
import java.util.List;
import org.cloudml.core.collections.ArtefactInstanceGroup;
import org.cloudml.core.validation.CanBeValidated;
import org.cloudml.core.validation.Report;
import org.cloudml.core.visitors.Visitable;
import org.cloudml.core.visitors.Visitor;

/*
 * Artefact describes the type of an Artefact instance. It also contains
 * communication channels and dependencies between Port Types
 */
public class Artefact extends DeploymentPart implements Visitable, CanBeValidated {

    private ArtefactPort destination;
    private Resource resource;

    /*
     * Dependencies <PortName,Port Reference>
     */
    private List<ClientPort> required = new LinkedList<ClientPort>();
    private List<ServerPort> provided = new LinkedList<ServerPort>();

    public Artefact() {
    }

    public Artefact(String name) {
        super(name);
    }

    public Artefact(String name, ArtefactPort destination) {
        super(name);
        this.destination = destination;
    }

    public Artefact(String name, List<Property> properties) {
        super(name, properties);
    }

    public Artefact(String name, List<Property> properties, ArtefactPort destination) {
        super(name, properties);
        this.destination = destination;
    }

    public Artefact(String name, List<Property> properties, List<ClientPort> required, List<ServerPort> provided) {
        super(name, properties);
        this.required = required;
        this.provided = provided;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitArtefact(this);
    }

    @Override
    public Report validate() {
        Report report = new Report();
        if (required.isEmpty() && provided.isEmpty()) {
            final String message = String.format("Artefact '%s' has no port (neither provided or required)!", getName());
            report.addWarning(message);
        }
        return report;
    }

    @Override
    public String toString() {
        return "Type " + getName();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Artefact) {
            Artefact otherArt = (Artefact) other;
            return getName().equals(otherArt.getName());
        }
        else {
            return false;
        }
    }

    public ArtefactInstance instanciates(String name) {
        return new ArtefactInstance(name, this);
    }

    public ArtefactInstance instanciates(String name, NodeInstance destination) {
        return new ArtefactInstance(name, this, destination);
    }
    
    public boolean hasAnyInstance() {
        return !getInstances().isEmpty();
    }

    public ArtefactInstanceGroup getInstances() {
        if (isAttachedToADeployment()) {
            return getDeployment().getArtefactInstances().ofType(this);
        } else {
            return new ArtefactInstanceGroup();
        }
    }
    
    /*
     * Getters & Setters
     */
    public List<ClientPort> getRequired() {
        return this.required;
    }

    public List<ServerPort> getProvided() {
        return this.provided;
    }

    public void setProvided(List<ServerPort> provided) {
        this.provided = provided;
    }

    public void setRequired(List<ClientPort> required) {
        this.required = required;
    }

    public void setDestination(ArtefactPort destination) {
        this.destination = destination;
    }

    public ArtefactPort getDestination() {
        return destination;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public boolean contains(ArtefactPort port) {
        if (port instanceof ClientPort) {
            return this.getRequired().contains((ClientPort) port);
        }
        else {
            return this.getProvided().contains((ServerPort) port);
        }
    }

    public ClientPort findRequiredPortByName(String clientPortName) {
        return findByName(clientPortName, required);
    }
    
    public void addProvidedPort(ServerPort server) {
        this.provided.add(server);
    }
    
    public void removeProvidedPort(ServerPort server) {
        this.provided.remove(server);
    }

    public ServerPort findProvidedPortByName(String serverPortName) {
        return findByName(serverPortName, provided);
    }
    
    public void addRequiredPort(ClientPort client) {
        this.required.add(client);
    }
    
    public void removeRequiredPort(ClientPort client) {
        this.required.remove(client);
    }
    
}
