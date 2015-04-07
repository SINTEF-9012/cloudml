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
public class Action extends ActivityNode {
    private ArrayList<Object> inputs = new ArrayList<Object>();
    private ArrayList<Object> outputs = new ArrayList<Object>();

    // constructors
    public Action(String name) {
        super(name);
    }

    public Action(String name, ArrayList<ActivityEdge> incoming, ArrayList<ActivityEdge> outgoing) {
        super(name, incoming, outgoing);
    }

    public Action(String name,
                  ArrayList<ActivityEdge> incoming,
                  ArrayList<ActivityEdge> outgoing,
                  ArrayList<Object> inputs,
                  ArrayList<Object> outputs) {
        super(name, incoming, outgoing);
        setInputs(inputs);
        setOutputs(outputs);

    }

    public void addInput(Object object) {
        getInputs().add(object);
    }

    public void addOutput(Object object) {
        getOutputs().add(object);
    }

    public void removeInput(Object object) {
        if (getInputs().contains(object)){
            getInputs().remove(object);
        }
    }

    public void removeOutput(Object object) {
        if(getOutputs().contains(object)){
            getOutputs().remove(object);
        }
    }

    public ArrayList<ActivityEdge> getControlOrObjectFlowEdges(boolean control, Direction direction) {
        ArrayList<ActivityEdge> result = new ArrayList<ActivityEdge>();
        if (direction.equals(Direction.IN)) {
            if (control) {
                // retrieve all incoming control edges
                for (ActivityEdge edge : getIncoming()) {
                    if (!edge.isObjectFlow()) {
                        result.add(edge);
                    }
                }
            } else {
                // retrieve all incoming object edges
                for (ActivityEdge edge : getIncoming()) {
                    if (edge.isObjectFlow()) {
                        result.add(edge);
                    }
                }
            }
        } else {
            if (control) {
                // retrieve all outgoing control edges
                for (ActivityEdge edge : getOutgoing()) {
                    if (!edge.isObjectFlow()) {
                        result.add(edge);
                    }
                }
            } else {
                // retrieve all outgoing object edges
                for (ActivityEdge edge : getOutgoing()) {
                    if (edge.isObjectFlow()) {
                        result.add(edge);
                    }
                }
            }
        }
        return result;
    }

    public ArrayList<Object> getInputs() {
        return inputs;
    }

    public void setInputs(ArrayList<Object> inputs) {
        this.inputs = inputs;
    }

    public ArrayList<Object> getOutputs() {
        return outputs;
    }

    public void setOutputs(ArrayList<Object> outputs) {
        this.outputs = outputs;
    }
}
