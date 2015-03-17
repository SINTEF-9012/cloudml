package org.cloudml.deployer2.dsl;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Maksym on 13.03.2015.
 */
public class DatastoreNode extends ObjectNode {
    //TODO datastore node may connect only with objectnodes, not actions
    //TODO semantics is that data is used as needed, rather then when available

    public DatastoreNode(String name) {
        super(name);
    }

    @Override
    public void setObjects(ArrayList<? extends Object> objects) {

        // remove duplicates - semantics of datastore
        HashSet<Object> data = new HashSet<Object>(objects);
        objects = new ArrayList<Object>(data);
        this.objects = objects;
    }

}
