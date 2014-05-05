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
import org.cloudml.core.VM;
import org.cloudml.core.VMInstance;

import static org.cloudml.core.builders.Commons.*;

public class VMInstanceBuilder extends ExternalComponentInstanceBuilder<VMInstance, VMInstanceBuilder> {

    @Override
    public VMInstance build() {
        final VMInstance result = createTypeStub().instantiates(getName());
        setupProperties(result);
        setupResources(result);
        return result;
    }

    private VM createTypeStub() {
        return aVM()
                .named(getTypeName())
                .with(aProvidedExecutionPlatform()
                .named("a platform"))
                .build();
    }

    @Override
    protected VMInstanceBuilder next() {
        return this;
    }

    @Override
    public void integrateIn(Deployment container) {
        final VMInstance result = findType(container).instantiates(getName());
        setupProperties(result);
        setupResources(result);
        container.getComponentInstances().add(result);
    }

    private VM findType(Deployment container) throws IllegalStateException {
        final VM type = container.getComponents().onlyVMs().firstNamed(getTypeName());
        if (type == null) {
            final String error = String.format("Unable to find a VM type named '%s'", getTypeName());
            throw new IllegalStateException(error);
        }
        return type;
    }
}
