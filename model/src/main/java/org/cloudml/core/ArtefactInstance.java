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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.cloudml.core.validation.CanBeValidated;
import org.cloudml.core.validation.Report;
import org.cloudml.core.visitors.Visitable;
import org.cloudml.core.visitors.Visitor;


/*
 * An instance of artefact is an elementary unit to be deployed on a single
 * node. It contains properties, communications channels and dependencies.
 *
 */
public class ArtefactInstance extends WithProperties implements Visitable, CanBeValidated {

    private Artefact type;
    private NodeInstance destination = null;
    private State status;

    public enum State {
        uninstalled,
        installed,
        configured,
        running,
        error,
    }
    /*
     * Dependencies <PortName,PortInstance Reference>
     */
    private List<ClientPortInstance> required = new LinkedList<ClientPortInstance>();
    private List<ServerPortInstance> provided = new LinkedList<ServerPortInstance>();

    public ArtefactInstance() {
        super();
    }

    public ArtefactInstance(String name, Artefact type) {
        super(name);
        this.type = type;
        this.status = State.uninstalled;
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
    public void accept(Visitor visitor) {
        visitor.visitArtefactInstance(this);
    }

    @Override
    public Report validate() {
        final Report report = new Report();
        if (type == null) {
            final String message = String.format("Artefact instance '%s' has no type ('null' found)", getName());
            report.addError(message);
        }
        else {
            checkForMissingPorts(report, "server", this.type.getProvided());
            checkForExtraPorts(report, "server", this.getProvided());
            checkForMissingPorts(report, "client", this.type.getRequired());
            checkForExtraPorts(report, "client", this.getRequired());
        }
        return report;
    }

    private void checkForMissingPorts(Report toComplete, String kind, Collection<? extends ArtefactPort> signature) {
        for (ArtefactPort portInType : signature) {
            if (!hasPortWithType(portInType)) {
                String message = String.format("Missing %s port instance for port '%s' in artefact instance '%s'", kind, portInType.getName(), getName());
                toComplete.addError(message);
            }
        }
    }

    private boolean hasPortWithType(ArtefactPort portType) {
        Collection<ArtefactPortInstance> allPortsInstances = new ArrayList<ArtefactPortInstance>();
        allPortsInstances.addAll(this.getProvided());
        allPortsInstances.addAll(this.getRequired());

        boolean found = false;
        Iterator<ArtefactPortInstance> iterator = allPortsInstances.iterator();
        while (iterator.hasNext() && !found) {
            ArtefactPortInstance instance = iterator.next();
            if (instance.getType().equals(portType)) {
                found = true;
            }
        }
        return found;
    }

    private void checkForExtraPorts(Report report, String kind, Collection<? extends ArtefactPortInstance> instancePorts) {
        for (ArtefactPortInstance port : instancePorts) {
            if (!this.type.contains(port.getType())) {
                String message = String.format("extra %s port '%s' that does not match any port in type '%s'", kind, port.getName(), type.getName());
                report.addError(message);
            }
        }
    }

    @Override
    public String toString() {
        return "Instance " + getName() + " : " + getType().getName();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other instanceof ArtefactInstance) {
            ArtefactInstance otherArt = (ArtefactInstance) other;
            Boolean match = getName().equals(otherArt.getName()) && type.equals(otherArt.getType());
            if (destination != null) {
                return getName().equals(otherArt.getName()) && type.equals(otherArt.getType()) && destination.equals(otherArt.getDestination());
            }
            else {
                return match && (otherArt.getDestination() == null);
            }
        }
        else {
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

    public State getStatus() {
        return this.status;
    }

    public void setStatus(State s) {
        this.status = s;
    }

    public void setStatus(String s) {
        this.status = State.valueOf(s);
    }
}
