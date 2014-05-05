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

import static org.cloudml.core.builders.Commons.*;

public class Provision extends AbstractAction<ComponentInstance<? extends Component>> {

    private final Component type;

    public Provision(StandardLibrary library, Component type) {
        super(library);
        this.type = rejectIfInvalid(type);
    }

    private Component rejectIfInvalid(Component type) {
        if (type == null) {
            throw new IllegalArgumentException("'null' is not a valid component type for provisioning!");
        }
        return type;
    }

    @Override
    public ComponentInstance<? extends Component> applyTo(Deployment deployment) {
        final String name = getLibrary().createUniqueComponentInstanceName(deployment, type);

        if (type.isExternal()) {
            ExternalComponent external = type.asExternal();
            if (external.isVM()) {
                aVMInstance()
                        .named(name)
                        .ofType(type.getName())
                        .integrateIn(deployment);
            }
            else {
                anExternalComponentInstance()
                        .named(name)
                        .ofType(external.getName())
                        .integrateIn(deployment);
            }
        }
        else {
            InternalComponent component = type.asInternal();
            ComponentInstance<? extends Component> host = getLibrary().findDestinationFor(deployment, component);
            return getLibrary().install(deployment, component, host);
        }
        return deployment.getComponentInstances().firstNamed(name);
    }
}
