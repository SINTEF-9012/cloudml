package org.cloudml.deployer2.dsl;

import org.cloudml.deployer2.dsl.util.Validator;

import java.util.ArrayList;

/**
 * Created by Maksym on 16.03.2015.
 */
public class ExpansionRegion extends Action {

    public enum ExpansionMode {PARALLEL, ITERATIVE}

    ;
    private ExpansionMode mode;
    // ExpansionRegion must have at least one input ExpansionNodes
    private ArrayList<ExpansionNode> inputCollections = new ArrayList<ExpansionNode>(1);
    private ArrayList<ExpansionNode> outputCollections = new ArrayList<ExpansionNode>();
    private ArrayList<ActivityEdge> edges = new ArrayList<ActivityEdge>();
    private ArrayList<ActivityNode> nodes = new ArrayList<ActivityNode>();

    public ExpansionRegion(String name, ExpansionMode mode) {
        super(name);
        setMode(mode);
    }

    public void addInput(ExpansionNode node) throws Exception {
        Validator.validateInputSize(node, this);
        getInputCollections().add(node);
    }

    public void addOutput(ExpansionNode node) throws Exception {
        Validator.validateOutputSize(node, this);
        getOutputCollections().add(node);
    }

    public void removeInput(ExpansionNode node) throws Exception {
        if (getInputCollections().size() == 1) {
            throw new Exception("ExpansionRegion must have at least one input");
        } else if (getInputCollections().contains(node)){
            getInputCollections().remove(node);
        }
    }

    public void removeOutput(ExpansionNode node) {
        if (getOutputCollections().contains(node)){
            getOutputCollections().remove(node);
        }
    }

    public ExpansionMode getMode() {
        return mode;
    }

    public void setMode(ExpansionMode mode) {
        this.mode = mode;
    }

    public ArrayList<ExpansionNode> getInputCollections() {
        return inputCollections;
    }

    public void setInputCollections(ArrayList<ExpansionNode> inputCollections) throws Exception {
        for (ExpansionNode node : inputCollections) {
            Validator.validateInputSize(node, this);
        }
        this.inputCollections = inputCollections;
    }

    public ArrayList<ExpansionNode> getOutputCollections() {
        return outputCollections;
    }

    public void setOutputCollections(ArrayList<ExpansionNode> outputCollections) throws Exception {
        for (ExpansionNode node : outputCollections) {
            Validator.validateOutputSize(node, this);
        }
        this.outputCollections = outputCollections;
    }

    public ArrayList<ActivityEdge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<ActivityEdge> edges) {
        this.edges = edges;
    }

    public ArrayList<ActivityNode> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<ActivityNode> nodes) {
        this.nodes = nodes;
    }

}
