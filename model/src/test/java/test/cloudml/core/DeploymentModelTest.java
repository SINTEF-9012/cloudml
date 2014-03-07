/*
 */

package test.cloudml.core;

import junit.framework.TestCase;
import static junit.framework.TestCase.assertTrue;
import org.cloudml.core.DeploymentModel;
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
public class DeploymentModelTest extends TestCase {

    @Test
    public void validateWithoutWarningsWhenValid() {
        DeploymentModel model = new DeploymentModel();
        
        Report validation = model.validate();
        
        assertTrue(validation.pass(Report.WITHOUT_WARNING));
    }
    
    @Test
    public void validationReportsEmptyModel() {
        DeploymentModel model = new DeploymentModel();
        
        Report validation = model.validate();
        
        assertTrue(validation.hasWarningAbout("empty", "deployment", "model"));
    }
    
}
