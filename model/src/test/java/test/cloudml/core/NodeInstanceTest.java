package test.cloudml.core;

import junit.framework.TestCase;
import static junit.framework.TestCase.assertTrue;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.Provider;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import test.cloudml.core.builder.Builder;

@RunWith(JUnit4.class)
public class NodeInstanceTest extends TestCase {

    @Test
    public void validationPassWithWarningWhenValid() {
        NodeInstance instance = prepareInstance();
        assertTrue(instance.validate().pass(Report.WITHOUT_WARNING));
    }

    @Test
    public void validationReportsNullName() {
        NodeInstance instance = prepareInstance();
        instance.setName(null);
        assertTrue(instance.validate().hasErrorAbout("name", "null"));
    }

    @Test
    public void validationReportsNullType() {
        NodeInstance instance = prepareInstance();
        instance.setType(null);
        assertTrue(instance.validate().hasErrorAbout("type", "null"));
    }

    private NodeInstance prepareInstance() {
        Builder builder = new Builder();
        Provider provider = builder.createProvider("EC2");
        Node nodeType = builder.createNodeType("Test Type", provider);
        return builder.provision(nodeType, "My instance");
    }
    
    
}
