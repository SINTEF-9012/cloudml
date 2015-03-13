package dsl;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Maksym on 13.03.2015.
 */
public class Action extends ActivityNode{
    // true means a few instances may start concurrently
    // private boolean isLocallyReentrant = true;
    private HashMap<String,? extends Object> inputs;
    private HashMap<String,? extends Object> outputs;

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
                   HashMap<String,? extends Object> inputs,
                   HashMap<String,? extends Object> outputs){
        super(name, incoming, outgoing);
        setInputs(inputs);
        setOutputs(outputs);

    }

    // setters and getters
    public HashMap<String, ? extends Object> getInputs() {
        return inputs;
    }

    public void setInputs(HashMap<String, ? extends Object> inputs) {
        this.inputs = inputs;
    }

    public HashMap<String, ? extends Object> getOutputs() {
        return outputs;
    }

    public void setOutputs(HashMap<String, ? extends Object> outputs) {
        this.outputs = outputs;
    }
}
