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
import java.util.List;
import org.cloudml.core.InternalComponent;
import org.cloudml.core.Deployment;
import org.cloudml.core.Component;

public class FindHostType extends AbstractFind<Component> {

    private final InternalComponent component;

    public FindHostType(StandardLibrary library, InternalComponent artefact) {
        super(library);
        this.component = artefact;
    }

    @Override
    protected List<Component> collectCandidates(Deployment deployment) {
        List<Component> candidates = new ArrayList<Component>();
        for (Component node : deployment.getComponents()) {
            if (isCandidate(component, node)) {
                candidates.add(node);
            }
        }
        return candidates;
    }

    protected boolean isCandidate(InternalComponent component, Component candidateHost) {
        return candidateHost.canHost(component);  
    }

    @Override
    protected void handleLackOfCandidate(Deployment deployment, List<Component> candidates) {
        final String message = String.format(
                "Unable to find a node type relevant to install artefacts of type '%s'",
                component.getRequiredExecutionPlatform().getName());
        throw new IllegalStateException(message);
    }
}
