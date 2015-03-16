package dsl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Maksym on 13.03.2015.
 */
public class DatastoreNode extends ObjectNode {

    public DatastoreNode (String name){
        super(name);
    }

    @Override
    public void setOutputs(HashMap<String, ? extends Object> outputs){
        Collection<?extends Object> inputValues = getInputs().values();
        Set<String> inputKeys = getInputs().keySet();
        if(!outputs.keySet().equals(inputKeys)){
            try {
                throw new Exception("Inputs and outputs of datastore node must be the same: input and output keys are different");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!outputs.values().toString().equals(inputValues.toString())){
            try {
                throw new Exception("Inputs and outputs of datastore node must be the same: input and output values are different");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.outputs = outputs;
    }
}
