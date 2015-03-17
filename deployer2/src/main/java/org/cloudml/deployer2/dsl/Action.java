package org.cloudml.deployer2.dsl;

import java.util.ArrayList;

/**
 * Created by Maksym on 13.03.2015.
 */
public class Action extends ActivityNode {
    private ArrayList<ObjectNode> inputs;
    private ArrayList<ObjectNode> outputs;

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
                  ArrayList<ObjectNode> inputs,
                  ArrayList<ObjectNode> outputs) {
        super(name, incoming, outgoing);
        setInputs(inputs);
        setOutputs(outputs);

    }

    public void addInput(ObjectNode object) {
        getInputs().add(object);
    }

    public void addOutput(ObjectNode object) {
        getOutputs().add(object);
    }

    public void removeInput(ObjectNode object) {
        getInputs().remove(object);
    }

    public void removeOutput(ObjectNode object) {
        getOutputs().remove(object);
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

    public ArrayList<ObjectNode> getInputs() {
        return inputs;
    }

    public void setInputs(ArrayList<ObjectNode> inputs) {
        this.inputs = inputs;
    }

    public ArrayList<ObjectNode> getOutputs() {
        return outputs;
    }

    public void setOutputs(ArrayList<ObjectNode> outputs) {
        this.outputs = outputs;
    }
}
