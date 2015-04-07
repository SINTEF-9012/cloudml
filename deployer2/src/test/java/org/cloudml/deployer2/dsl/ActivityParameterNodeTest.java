/**
 * This file is part of CloudML [ http://cloudml.org ]
 *
 * Copyright (C) 2012 - SINTEF ICT
 * Contact: Franck Chauvel <franck.chauvel@sintef.no>
 *
 * Module: root
 *
 * CloudML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * CloudML is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with CloudML. If not, see
 * <http://www.gnu.org/licenses/>.
 */
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