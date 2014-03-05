/*
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

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class NodeTest extends TestCase {

    @Test
    public void testValidateEmptyNode() {
        Node node = new Node();
        Report report = node.validate();
        assertTrue(report.hasErrorAbout("name"));
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
        Node node = new Node(nodeName);
        model.getNodeTypes().put(nodeName, node);
        assertTrue(model.getNodeTypes().values().contains(node));
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
