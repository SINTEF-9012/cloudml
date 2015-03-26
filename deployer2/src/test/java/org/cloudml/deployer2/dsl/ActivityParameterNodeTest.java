package org.cloudml.deployer2.dsl;

import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.fail;

public class ActivityParameterNodeTest {

    @Test
    public void testAddEdge(){
        ActivityEdge objectFlow = new ActivityEdge(true);
        ActivityParameterNode parameterNode = null;
        try {
            parameterNode = new ActivityParameterNode("in", "object");
            parameterNode.addEdge(objectFlow, ActivityNode.Direction.IN);
            parameterNode.addEdge(objectFlow, ActivityNode.Direction.IN);
            fail("Exception is expected because parameter node can not have more than one incoming edge");
        } catch (Exception e) {}

        parameterNode.getIncoming().clear();

        try {
            parameterNode.addEdge(objectFlow, ActivityNode.Direction.IN);
            parameterNode.addEdge(objectFlow, ActivityNode.Direction.OUT);
            fail("Exception is expected because parameter node already has incoming edge, so we can't add outgoing");
        } catch (Exception e) {}

        parameterNode.getIncoming().clear();

        try {
            parameterNode.addEdge(objectFlow, ActivityNode.Direction.OUT);
            parameterNode.addEdge(objectFlow, ActivityNode.Direction.OUT);
            fail("Exception is expected because parameter node can not have more than one outgoing edge");
        } catch (Exception e) {}

        parameterNode.getOutgoing().clear();

        try {
            parameterNode.addEdge(objectFlow, ActivityNode.Direction.OUT);
            parameterNode.addEdge(objectFlow, ActivityNode.Direction.IN);
            fail("Exception is expected because parameter node already has outgoing edge, so we can't add incoming");
        } catch (Exception e) {}
    }
}