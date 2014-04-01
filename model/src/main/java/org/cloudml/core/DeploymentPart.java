/*
 */

package org.cloudml.core;

import java.util.List;


public abstract class DeploymentPart extends WithProperties {

    private DeploymentModel deployment;

    public DeploymentPart() {
    }

    public DeploymentPart(String name) {
        super(name);
    }

    public DeploymentPart(String name, List<Property> properties) {
        super(name, properties);
    }
    
    public DeploymentModel getDeployment() {
        if (!isAttachedToADeployment()) {
            final String message = String.format("The element '%s' (of type '%s') is not attached to any deployment model", getName(), this.getClass().getName());
            throw new IllegalStateException(message);
        }
        return deployment;
    }
        
    public boolean isAttachedToADeployment() {
        return deployment != null;
    }
     
    public void attachTo(DeploymentModel deployment) {
       this.deployment = deployment; 
    }
    
    public void detach() {
        this.deployment = null;
    }
    
}
