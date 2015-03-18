package org.cloudml.deployer2.dsl;

import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class ActivityInitialNodeTest {
    ActivityInitialNode node = new ActivityInitialNode();

    @Test
    public void testAddEdge() {
        ActivityEdge edge = new ActivityEdge();

        try {
            node.addEdge(edge, ActivityNode.Direction.IN);
            fail("Exception is expected because we are adding an incoming edge to initial node");
        } catch (Exception e) {}

        edge.setObjectFlow(true);
        try {
            node.addEdge(edge, ActivityNode.Direction.OUT);
            fail("Exception is expected because we are adding an object flow edge to initial node");
        } catch (Exception e) {}
    }

    @Test
    public void noIncomingEdges(){
        assertNull(node.getIncoming());
    }
}