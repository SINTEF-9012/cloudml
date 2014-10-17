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
import org.cloudml.core.builders.ComponentInstanceBuilder;
import org.cloudml.core.builders.InternalComponentInstanceBuilder;

import static org.cloudml.core.builders.Commons.*;

/**
 * Created by nicolasf on 16.07.14.
 */
public class ReplicateComponentInstance extends AbstractAction<ComponentInstance> {

    private final ComponentInstance<? extends Component> instance;
    private final ComponentInstance<? extends Component>  host;

    public ReplicateComponentInstance(StandardLibrary library, ComponentInstance<? extends Component> instance, ComponentInstance<? extends Component>  host) {
        super(library);
        this.instance = rejectIfInvalid(instance);
        this.host = host;
    }

    private ComponentInstance<? extends Component> rejectIfInvalid(ComponentInstance<? extends Component> c) {
        if (c == null) {
            throw new IllegalArgumentException("'null' is not a valid component instance for duplication");
        }
        return c;
    }

    @Override
    public ComponentInstance applyTo(Deployment target) {
        ComponentInstance result=null;
        if(instance.isInternal()){
            final String instanceName = getLibrary().createUniqueComponentInstanceName(target, instance.asInternal().getType());
            InternalComponentInstanceBuilder builder = anInternalComponentInstance()
                    .named(instanceName)
                    .ofType(instance.asInternal().getType().getName())
                    .hostedBy(host.getName());
            builder.integrateIn(target);

            result = target.getComponentInstances().onlyInternals().firstNamed(instanceName);
        }
        if(instance.isExternal()){
            result=getLibrary().provision(target, instance.getType());
        }
        return result;
    }

}
