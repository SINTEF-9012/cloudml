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
/*
 */
package org.cloudml.core.builders;

import org.cloudml.core.Deployment;
import org.cloudml.core.ExternalComponent;
import org.cloudml.core.ExternalComponentInstance;

import static org.cloudml.core.builders.Commons.*;

public class ExternalComponentInstanceBuilder<T extends ExternalComponentInstance<? extends ExternalComponent>, N extends ExternalComponentInstanceBuilder<?, ?>> extends ComponentInstanceBuilder<T, N> {

    @Override
    public T build() {
        final ExternalComponentInstance<? extends ExternalComponent> result = createTypeStub().instantiates(getName());
        setupProperties(result);  
        setupResources(result);  
        return (T) result; 
    }

    @Override 
    protected N next() {
        return (N) this;
    }

    @Override
    public void integrateIn(Deployment container) {
        final ExternalComponentInstance<? extends ExternalComponent> result = findType(container).instantiates(getName());
        setupProperties(result);  
        setupResources(result);  
        container.getComponentInstances().add(result);
    }

    private ExternalComponent createTypeStub() {
        return anExternalComponent()  
                .named(getTypeName())
                .build();
    }
    
    
    private ExternalComponent findType(Deployment container) {
        final ExternalComponent type = container.getComponents().onlyExternals().firstNamed(getTypeName());
        if (type == null) {
            final String error = String.format("Unable to find an external component type named '%s'!", getTypeName());
            throw new IllegalStateException(error);
        }
        return type;
    }
}
