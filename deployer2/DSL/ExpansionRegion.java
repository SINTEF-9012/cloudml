package dsl;

import java.util.ArrayList;

/**
 * Created by Maksym on 16.03.2015.
 */
public class ExpansionRegion extends Action{
    public enum ExpansionMode {PARALLEL, ITERATIVE};
    private ExpansionMode mode;
    private ArrayList<ExpansionNode> inputCollections = new ArrayList<ExpansionNode>();
    private ArrayList<ExpansionNode> outputCollections = new ArrayList<ExpansionNode>();

    public ExpansionRegion(String name, ExpansionMode mode){
        super(name);
        setMode(mode);
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
        int sameSize = inputCollections.get(0).getCollection().size();
        for (ExpansionNode node:inputCollections){
            if (node.getCollection().size() != sameSize){
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
}
