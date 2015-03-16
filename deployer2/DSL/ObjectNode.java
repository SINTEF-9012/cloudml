package dsl;

import java.util.HashMap;

/**
 * Created by Maksym on 16.03.2015.
 */
public class ObjectNode extends ActivityNode {
    protected HashMap<String,? extends Object> inputs;
    protected HashMap<String,? extends Object> outputs;

    public ObjectNode(String name){
        super(name);
    }

    @Override
    public void addEdge(ActivityEdge dataFlow, String direction) throws Exception {
        if (!dataFlow.isObjectFlow()) {
            throw new Exception("Only object flow to/from object node is allowed: you are trying to add control flow");
        } else if (direction.equals(IN)) {
            dataFlow.setTarget(this);
            getIncoming().add(dataFlow);
        } else if (direction.equals(OUT)){
            dataFlow.setSource(this);
            getOutgoing().add(dataFlow);
        }
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
