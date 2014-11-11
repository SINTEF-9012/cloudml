package org.cloudml.indicators;

import static eu.diversify.trio.core.requirements.Factory.*;

import eu.diversify.trio.core.requirements.Requirement;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.InternalComponentInstance;
import org.cloudml.core.RequiredPortInstance;

/**
 * Extract the explicit requirements/dependencies of a given component.
 *
 * The resulting requirement capture the actual dependencies of the given
 * component. Alternative dependencies (possible relationship or executeOn) are
 * not taken into account.
 */
public class OnlyExplicitDependencies implements DependencyExtractor {

    @Override
    public Requirement from(ComponentInstance<?> instance) {
        requireValidComponent(instance);

        Requirement requirements = nothing();
        
        if (instance.isInternal()) {
            final InternalComponentInstance internalInstance = instance.asInternal();
            for (RequiredPortInstance eachDependency: internalInstance.getRequiredPorts()) {
                if (eachDependency.isBound()) {
                    requirements = requirements.and(require(eachDependency.findProvider().getName()));
                }
            }
            
           requirements = requirements.and(require(internalInstance.getHost().getName()));
        }
        
        return requirements;
    }

    
    
    private void requireValidComponent(ComponentInstance<?> componentInstance) throws IllegalArgumentException {
        if (componentInstance == null) {
            throw new IllegalArgumentException("Cannot extract the dependencies of 'null'");
        }
    }

}
