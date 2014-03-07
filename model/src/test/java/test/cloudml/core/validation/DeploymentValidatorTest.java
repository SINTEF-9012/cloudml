/*
 */

package test.cloudml.core.validation;

import junit.framework.TestCase;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.validation.DeploymentValidator;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 *
 */
@RunWith(JUnit4.class)
public class DeploymentValidatorTest extends TestCase {

    final DeploymentValidator validator;
    
    public DeploymentValidatorTest() {
        this.validator = new DeploymentValidator();
    }
        
    @Test
    public void validationsPassWithWarningOnValidModels() {
        DeploymentModel model = getValidModel();
        
        Report validation = this.validator.validate(model);
        
        assertTrue(validation.pass(Report.WITHOUT_WARNING));
    }
    
    
    private DeploymentModel getValidModel() {
        return new DeploymentModel();
    }
    
}
