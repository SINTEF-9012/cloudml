/*
 */
package org.cloudml.core.validation;

import org.cloudml.core.DeploymentModel;
import org.cloudml.core.visitors.ContainmentDispatcher;
import org.cloudml.core.visitors.Visitor;

/**
 * Validate the complete deployment model
 */
public class DeploymentValidator {

    public Report validate(DeploymentModel model) {
        Visitor visitor = new Visitor(new ContainmentDispatcher());
        ValidationCollector validator = new ValidationCollector();
        visitor.addListeners(validator);
        model.accept(visitor);
        return validator.getOverallReport();
    }
}
