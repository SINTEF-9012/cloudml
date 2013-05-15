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
     * Communication channels <PortName,Port Reference>
     */
    private List<ArtefactPort> inputs = new LinkedList<ArtefactPort>();
    private List<ArtefactPort> outputs = new LinkedList<ArtefactPort>();
    /*
     * Dependencies <PortName,Port Reference>
     */
    private List<ArtefactPort> required = new LinkedList<ArtefactPort>();
    private List<ArtefactPort> provided = new LinkedList<ArtefactPort>();

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

    public Artefact(String name, List<Property> properties, List<ArtefactPort> inputs, List<ArtefactPort> outputs, List<ArtefactPort> required, List<ArtefactPort> provided) {
        super(name, properties);
        this.inputs = inputs;
        this.outputs = outputs;
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

    public ArtefactInstance instanciates(String name, NodePortInstance destination) {
        return new ArtefactInstance(name, this, destination);
    }

    /*
     * Getters & Setters
     */
    public List<ArtefactPort> getInputs() {
        return this.inputs;
    }

    public List<ArtefactPort> getOutputs() {
        return this.outputs;
    }

    public List<ArtefactPort> getRequired() {
        return this.required;
    }

    public List<ArtefactPort> getProvided() {
        return this.provided;
    }

    public void setInputs(List<ArtefactPort> inputs) {
        this.inputs = inputs;
    }

    public void setOutputs(List<ArtefactPort> outputs) {
        this.outputs = outputs;
    }

    public void setProvided(List<ArtefactPort> provided) {
        this.provided = provided;
    }

    public void setRequired(List<ArtefactPort> required) {
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
