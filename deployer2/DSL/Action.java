package dsl;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Maksym on 13.03.2015.
 */
public class Action extends ActivityNode{
    private ObjectNode input;
    private ObjectNode output;

    // constructors
    public Action(String name){
        super(name);
    }

    public Action (String name, ArrayList<ActivityEdge> incoming, ArrayList<ActivityEdge> outgoing){
        super(name, incoming, outgoing);
    }

    public Action (String name,
                   ArrayList<ActivityEdge> incoming,
                   ArrayList<ActivityEdge> outgoing,
                   ObjectNode input,
                   ObjectNode output){
        super(name, incoming, outgoing);
        setInput(input);
        setOutput(output);

    }


    public ObjectNode getInput() {
        return input;
    }

    public void setInput(ObjectNode input) {
        this.input = input;
    }

    public ObjectNode getOutput() {
        return output;
    }

    public void setOutput(ObjectNode output) {
        this.output = output;
    }
}
