package org.cloudml.deployer2.dsl;


import org.junit.Test;


import java.util.ArrayList;

import static org.junit.Assert.*;

public class DatastoreNodeTest {

    @Test
    public void noDuplicates() throws Exception {
        DatastoreNode store = new DatastoreNode("store");

        String object1 = "o1";
        String object2 = "o2";
        String object3 = "o1";
        ArrayList<Object> objects = new ArrayList<Object>();
        objects.add(object1);
        objects.add(object2);
        objects.add(object3);

        store.setObjects(objects);
        ArrayList<String> datastoreObjects = (ArrayList<String>) store.getObjects();

        //datastore node deletes all duplicates
        assertEquals(datastoreObjects.indexOf("o1"), datastoreObjects.lastIndexOf("o1"));
    }
}