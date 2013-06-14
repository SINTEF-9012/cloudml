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

/*
 * Artefact describes the type of an Artefact instance. It also contains
 * communication channels and dependencies between Port Types
 */
public class Artefact extends WithProperties {

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
    public String toString() {
        return "Type " + name;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Artefact) {
            Artefact otherArt = (Artefact) other;
            return name.equals(otherArt.getName());
        } else {
            return false;
        }
    }

    public ArtefactInstance instanciates(String name) {
        return new ArtefactInstance(name, this);
    }

    public ArtefactInstance instanciates(String name, NodeInstance destination) {
        return new ArtefactInstance(name, this, destination);
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
}
