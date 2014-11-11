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
