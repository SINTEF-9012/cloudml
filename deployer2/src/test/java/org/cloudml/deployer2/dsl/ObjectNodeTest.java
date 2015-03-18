package org.cloudml.deployer2.dsl;

import junit.framework.TestCase;
import org.junit.Test;

public class ObjectNodeTest {

    @Test(expected = Exception.class)
    public void testAddEdge() throws Exception {
        ObjectNode object = new ObjectNode("data");
        object.addEdge(new ActivityEdge(true), ActivityNode.Direction.IN);
        object.addEdge(new ActivityEdge(), ActivityNode.Direction.IN); //exception is thrown here
    }
}