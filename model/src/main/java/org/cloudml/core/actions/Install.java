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
package org.cloudml.core.actions;

import org.cloudml.core.*;
import org.cloudml.core.builders.InternalComponentInstanceBuilder;


import static org.cloudml.core.builders.Commons.*;

public class Install extends AbstractAction<InternalComponentInstance> {

    private final InternalComponent type;
    private final ComponentInstance<? extends Component> host;

    public Install(StandardLibrary library, InternalComponent type, ComponentInstance<? extends Component> host) {
        super(library);
        this.type = rejectIfInvalid(type);
        this.host = rejectIfInvalid(host);
    }

    private InternalComponent rejectIfInvalid(InternalComponent type) {
        if (type == null) {
            throw new IllegalArgumentException("'null' is not a valid component type for installation");
        }
        return type;
    }

    private ComponentInstance<? extends Component> rejectIfInvalid(ComponentInstance<? extends Component> host) {
        if (host == null) {
            throw new IllegalArgumentException("'null' is not a valid destination for installation");
        }
        if (!host.canHost(type)) {
            final String error = String.format("Illegal installation: '%s' cannot host component of type '%s'", host.getName(), type.getName()); 
            throw new IllegalArgumentException(error);
        }
        return host;
    }

    @Override
    public InternalComponentInstance applyTo(Deployment target) {
        final String instanceName = getLibrary().createUniqueComponentInstanceName(target, type);
        InternalComponentInstanceBuilder builder = anInternalComponentInstance()
                .named(instanceName)
                .ofType(type.getName())
                .hostedBy(host.getName());
        builder.integrateIn(target);
        
        InternalComponentInstance instance = target.getComponentInstances().onlyInternals().firstNamed(instanceName);

        resolveDependencies(target, instance);

        return instance;
    }

    private void resolveDependencies(Deployment target, InternalComponentInstance instance) {
        for (RequiredPortInstance clientPort : instance.getRequiredPorts()) {
            getLibrary().bind(target, clientPort);
        }
    }
}
