package org.cloudml.deployer2.dsl;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class ActivityFinalNodeTest {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    ActivityFinalNode node = new ActivityFinalNode();

    @Test
    public void testAddEdge() {
        ActivityEdge edge = new ActivityEdge();

        try {
            node.addEdge(edge, ActivityNode.Direction.OUT);
            fail("Exception is expected because we are adding an outgoing edge to the final node");
        } catch (Exception e) {}

        edge.setObjectFlow(true);
        try {
            node.addEdge(edge, ActivityNode.Direction.IN);
            fail("Exception is expected because we are adding an object flow edge to the final node");
        } catch (Exception e) {}
    }

    @Test
    public void noOutgoingEdges(){
        assertNull(node.getOutgoing());
    }

    @Test
    public void testFinish() throws Exception {
        exit.expectSystemExitWithStatus(0);
        node.finish();
    }
}