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


package test.cloudml.core;

import java.util.HashSet;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.Provider;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class NodeTest extends TestCase {

    @Test
    public void testValidateEmptyNode() {
        Node node = new Node();
        Report report = node.validate();
        assertTrue(report.hasErrorAbout("provider"));
    }

    @Test
    public void testValidateValidNode() {
        Node node = new Node("node type", new Provider("provider"));
        Report report = node.validate();
        assertFalse(report.hasError());
        assertFalse(report.hasWarning());
    }

    @Test
    public void testFindingNodeInAModel() {
        DeploymentModel model = new DeploymentModel();
        final String nodeName = "My Node Type";
        final Provider provider = new Provider("EC2");
        model.addProvider(provider);
        Node node = new Node(nodeName, provider);
        model.getNodeTypes().add(node);
        assertTrue(model.getNodeTypes().contains(node));
    }

    @Test
    public void testNodeEqualityWithNull() {
        Node node1 = new Node();
        assertFalse(node1.equals(null));
    }

    @Test
    public void testNodeEqualityWithWrongObjectType() {
        Node node1 = new Node();
        assertFalse(node1.equals(new Double(23.)));
    }

    
    @Test
    public void testNodeEqualityWithEmptyNode() {
        Node node1 = new Node();
        Node node2 = new Node();
        assertTrue(node1.equals(node2));
        assertTrue(node2.equals(node1));
    }

    @Test
    public void testNodeEqualityWhenOneSideHasANullName() {
        Node node1 = new Node("Node 1");
        Node node2 = new Node();
        assertFalse(node1.equals(node2));
        assertFalse(node2.equals(node1));
    }

    @Test
    public void testNodeEqualityWhenOneSideHasANullPovider() {
        Node node1 = new Node("Node 1", new Provider("Provider 1"));
        Node node2 = new Node("Node 2");
        assertFalse(node1.equals(node2));
        assertFalse(node2.equals(node1));
    }

    @Test
    public void testNodeEqualitWhenTwoSidesAreSimilar() {
        Node node1 = new Node("Node 1", new Provider("Provider 1"));
        Node node2 = new Node("Node 1", new Provider("Provider 1"));
        assertTrue(node1.equals(node2));
        assertTrue(node2.equals(node1));
    }
        
    @Test
    public void testUsageInHashSet() {
        HashSet<Node> set = new HashSet<Node>();
        set.add(new Node("Node 1"));
        set.add(new Node("Node 2"));
        assertEquals(2, set.size());
    }
}
