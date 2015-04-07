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