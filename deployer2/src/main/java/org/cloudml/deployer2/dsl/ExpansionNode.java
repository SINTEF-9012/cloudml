package org.cloudml.deployer2.dsl;

import java.util.ArrayList;

/**
 * Created by Maksym on 16.03.2015.
 */
public class ExpansionNode extends ObjectNode {

    public ExpansionNode(String name, ArrayList<? extends Object> collection) throws Exception {
        super(name);
        inputIsCollection(collection);
        setObjects(collection);
    }

    private void inputIsCollection (ArrayList<? extends Object> input) throws Exception {
        if (input.size() < 2){
            throw new Exception("ExpansionNode expects collection of size two as minimum as input");
        }
    }

}
