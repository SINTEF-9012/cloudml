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
 * An instance of artefact is an elementary unit to be deployed on a single
 * node. It contains properties, communications channels and dependencies.
 *
 */
public class ArtefactInstance extends WithProperties {

    private Artefact type;
    private NodeInstance destination = null;

    /*
     * Dependencies <PortName,PortInstance Reference>
     */
    private List<ClientPortInstance> required = new LinkedList<ClientPortInstance>();
    private List<ServerPortInstance> provided = new LinkedList<ServerPortInstance>();

    public ArtefactInstance() {
    }

    public ArtefactInstance(String name, Artefact type) {
        super(name);
        this.type = type;
    }

    public ArtefactInstance(String name, Artefact type, NodeInstance destination) {
        super(name);
        this.type = type;
        this.destination = destination;
    }

    public ArtefactInstance(String name, List<Property> properties, Artefact type) {
        super(name, properties);
        this.type = type;
    }

    public ArtefactInstance(String name, List<Property> properties, Artefact type, NodeInstance destination) {
        super(name, properties);
        this.destination = destination;
    }

    public ArtefactInstance(String name, List<Property> properties, List<ClientPortInstance> required, List<ServerPortInstance> provided) {
        super(name, properties);
        this.required = required;
        this.provided = provided;
    }

    @Override
    public String toString() {
        return "Instance " + name + " : " + getType().getName();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ArtefactInstance) {
            ArtefactInstance otherArt = (ArtefactInstance) other;
            return name.equals(otherArt.getName()) && type.equals(otherArt.getType());
        } else {
            return false;
        }
    }

    /*
     * Getters
     */

    public List<ClientPortInstance> getRequired() {
        return this.required;
    }

    public List<ServerPortInstance> getProvided() {
        return this.provided;
    }

    public Artefact getType() {
        return this.type;
    }


    public void setProvided(List<ServerPortInstance> provided) {
        this.provided = provided;
    }

    public void setRequired(List<ClientPortInstance> required) {
        this.required = required;
    }

    public void setType(Artefact type) {
        this.type = type;
    }

    public void setDestination(NodeInstance destination) {
        this.destination = destination;
    }

    public NodeInstance getDestination() {
        return destination;
    }
}
