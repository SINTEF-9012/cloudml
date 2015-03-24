package org.cloudml.deployer2.dsl;

import java.util.ArrayList;

/**
 * Created by Maksym on 16.03.2015.
 */
public class ExpansionNode extends ObjectNode {

    public ExpansionNode(String name){
        super(name);
    }

    public ExpansionNode(String name, ArrayList<Object> collection) throws Exception {
        super(name);
        setObjects(collection);
    }

    private void inputIsCollection (ArrayList<? extends Object> input) throws Exception {
        if (input.size() < 2){
            throw new Exception("ExpansionNode expects collection of size two as minimum as input");
        }
    }

    @Override
    public void setObjects(ArrayList<Object> collection) throws Exception {
        inputIsCollection(collection);
        this.objects = collection;
    }

}
