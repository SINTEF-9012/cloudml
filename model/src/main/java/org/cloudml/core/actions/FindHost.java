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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.cloudml.core.InternalComponent;
import org.cloudml.core.Deployment;
import org.cloudml.core.Component;
import org.cloudml.core.ComponentInstance;

public class FindHost extends AbstractFind<ComponentInstance<? extends Component>> {

    private final InternalComponent componentType;
    private final ArrayList<ComponentInstance<? extends Component>> excluded;

    public FindHost(StandardLibrary library, InternalComponent artefact, ComponentInstance<? extends Component>... toExclude) {
        super(library);
        this.componentType = rejectIfInvalid(artefact);
        this.excluded = new ArrayList<ComponentInstance<? extends Component>>(Arrays.asList(toExclude));
    }

    private InternalComponent rejectIfInvalid(InternalComponent type) {
        if (type == null) {
            throw new IllegalStateException("Unable to find a host for the 'null' component type!");
        }
        return type;
    }

    @Override
    protected ArrayList<ComponentInstance<? extends Component>> collectCandidates(Deployment deployment) {
        final ArrayList<ComponentInstance<? extends Component>> candidates = new ArrayList<ComponentInstance<? extends Component>>();
        for (ComponentInstance<? extends Component> instance : deployment.getComponentInstances()) {
            if (isCandidate(instance)) {
                candidates.add(instance);
            }
        }
        return candidates;
    }

    @Override
    protected void handleLackOfCandidate(Deployment deployment, List<ComponentInstance<? extends Component>> candidates) {
        Component type = getLibrary().findHostType(deployment, componentType);
        candidates.add(getLibrary().provision(deployment, type));
    }

    private boolean isCandidate(ComponentInstance<? extends Component> instance) {
        return instance.canHost(componentType)
                && !isExcluded(instance);
    }

    private boolean isExcluded(ComponentInstance<? extends Component> instance) {
        return this.excluded.contains(instance);
    }
}
