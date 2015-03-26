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
