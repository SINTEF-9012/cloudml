package dsl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Maksym on 16.03.2015.
 */
public class ExpansionNode extends DatastoreNode {



    private ArrayList<Object> collection = new ArrayList<Object>();


    public ExpansionNode(String name, HashMap<String, ? extends Object> collection){
        super(name);
        setInputs(collection);
    }

    @Override
    public void setInputs(HashMap<String, ? extends Object> inputs) {
        Iterator<? extends Object> iterator = inputs.values().iterator();
        Class elementClass = iterator.next().getClass();
        while (iterator.hasNext()){
            if (!elementClass.equals(iterator.next().getClass())){
                try {
                    throw new Exception("All input values of ExpansionNode must be of the same class");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        this.inputs = inputs;
        // transform hashmap into arraylist collection: expansion node is a collection of elements by definition
        getCollection().addAll(inputs.values());
    }

    public ArrayList<Object> getCollection() {
        return collection;
    }
}
