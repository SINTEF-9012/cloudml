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
package org.cloudml.deployer2.dsl;

import java.util.ArrayList;

/**
 * Created by Maksym on 13.03.2015.
 */
public abstract class ActivityNode extends Element {

    public enum Direction {IN, OUT};

    protected ArrayList<ActivityEdge> incoming = new ArrayList<ActivityEdge>();
    protected ArrayList<ActivityEdge> outgoing = new ArrayList<ActivityEdge>();

    // constructors
    public ActivityNode() {
    }

    public ActivityNode(String name) {
        super(name);
    }

    public ActivityNode(String name, ArrayList<ActivityEdge> incoming, ArrayList<ActivityEdge> outgoing) {
        super(name);
        this.setIncoming(incoming);
        this.setOutgoing(outgoing);
    }

    // methods
    public void addEdge(ActivityEdge edge, Direction direction) throws Exception {
        if (direction.equals(Direction.IN)) {
            edge.setTarget(this);
            getIncoming().add(edge);
        } else {
            edge.setSource(this);
            getOutgoing().add(edge);
        }
    }

    public void removeEdge(ActivityEdge edge, Direction direction) {
        if (direction.equals(Direction.IN)) {
            getIncoming().remove(edge);
            edge.setTarget(null);
        } else {
            getOutgoing().remove(edge);
            edge.setSource(null);
        }
    }


    // setters and getters

    public ArrayList<ActivityEdge> getIncoming() {
        return incoming;
    }

    public void setIncoming(ArrayList<ActivityEdge> incoming) {
        for (ActivityEdge edge : incoming) {
            edge.setTarget(this);
        }
        this.incoming = incoming;
    }

    public ArrayList<ActivityEdge> getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(ArrayList<ActivityEdge> outgoing) {
        for (ActivityEdge edge : outgoing) {
            edge.setSource(this);
        }
        this.outgoing = outgoing;
    }

    @Override
    public String toString(){
        return this.getClass().getSimpleName() + ":" + this.getName();
    }
}
