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

import org.cloudml.core.Component;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.Deployment;
import org.cloudml.core.InternalComponent;
import org.cloudml.core.InternalComponentInstance;

import static org.cloudml.core.builders.Commons.*;

public class InternalComponentInstanceBuilder extends ComponentInstanceBuilder<InternalComponentInstance, InternalComponentInstanceBuilder> {

    private String hostName;

    public InternalComponentInstanceBuilder hostedBy(String hostName) {
        this.hostName = hostName;
        return next();
    }

    @Override
    public InternalComponentInstance build() {
        final InternalComponent type = createStubType();
        final InternalComponentInstance result = type.instantiate(getName());
        setupProperties(result);
        setupResources(result);
        // We do not do anytgng with the host name, as without a deployment 
        // owner, it is not possible for the component instance to retrieved 
        // its host
        return result;
    }

    private InternalComponent createStubType() {
        return anInternalComponent()
                .named(getTypeName())
                .with(aRequiredExecutionPlatform().named("Ubuntu 12.04"))
                .build();
    }

    @Override
    protected InternalComponentInstanceBuilder next() {
        return this;
    }

    @Override
    public void integrateIn(Deployment container) {
        final InternalComponentInstance result = findType(container).instantiate(getName());
        setupProperties(result);
        setupResources(result);
        container.getComponentInstances().add(result);
        container.deploy(result, findHost(container));
    }

    private ComponentInstance<? extends Component> findHost(Deployment container) {
        final ComponentInstance<? extends Component> host = container.getComponentInstances().firstNamed(hostName);
        if (host == null) {
            final String error = String.format("Unable to find the host component named '%s'!", hostName);
            throw new IllegalStateException(error);
        }
        return host;
    }

    private InternalComponent findType(Deployment container) throws IllegalStateException {
        final InternalComponent type = container.getComponents().onlyInternals().firstNamed(getTypeName());
        if (type == null) {
            final String error = String.format("Unable to find an internal component type named '%s'", getTypeName());
            throw new IllegalStateException(error);
        }
        return type;
    }
}
