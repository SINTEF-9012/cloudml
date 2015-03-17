package dsl;

import java.util.ArrayList;

/**
 * Created by Maksym on 16.03.2015.
 */
public class ExpansionRegion extends Action{

    public enum ExpansionMode {PARALLEL, ITERATIVE};
    private ExpansionMode mode;
    // ExpansionRegion must have at least one input ExpansionNodes
    private ArrayList<ExpansionNode> inputCollections = new ArrayList<ExpansionNode>(1);
    private ArrayList<ExpansionNode> outputCollections = new ArrayList<ExpansionNode>();
    private ArrayList<ActivityEdge> edges = new ArrayList<ActivityEdge>();
    private ArrayList<ActivityNode> nodes = new ArrayList<ActivityNode>();

    public ExpansionRegion(String name, ExpansionMode mode){
        super(name);
        setMode(mode);
    }

    public void addInput(ExpansionNode node){
        getInputCollections().add(node);
    }

    public void addOutput(ExpansionNode node){
        int maxSize = getInputCollections().get(0).getObjects().size();
        if (node.getObjects().size() > maxSize){
            try {
                throw new Exception("Output collection can not be bigger than input collections");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getOutputCollections().add(node);
        }
    }

    public void removeInput(ExpansionNode node){
        if (getInputCollections().size() == 1){
            try {
                throw new Exception("ExpansionRegion must have at least one input");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getInputCollections().remove(node);
        }
    }

    public void removeOutput(ExpansionNode node){
        getOutputCollections().remove(node);
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

    public void setInputCollections(ArrayList<ExpansionNode> inputCollections) {
        int sameSize = inputCollections.get(0).getObjects().size();
        for (ExpansionNode node:inputCollections){
            if (node.getObjects().size() != sameSize){
                try {
                    throw new Exception("Number of elements in all input expansion nodes must be the same");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        this.inputCollections = inputCollections;
    }

    public ArrayList<ExpansionNode> getOutputCollections() {
        return outputCollections;
    }

    public void setOutputCollections(ArrayList<ExpansionNode> outputCollections) {
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
