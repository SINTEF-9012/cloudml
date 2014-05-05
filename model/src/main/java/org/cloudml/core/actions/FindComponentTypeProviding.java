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
import org.cloudml.core.Relationship;
import org.cloudml.core.Deployment;

class FindComponentTypeProviding extends AbstractFind<InternalComponent> {

    private final Relationship relationshipType;

    public FindComponentTypeProviding(StandardLibrary library, Relationship relationshipType) {
        super(library);
        this.relationshipType = rejectIfInvalid(relationshipType);
    }
    
    private Relationship rejectIfInvalid(Relationship relationship) {
        if (relationship == null) {
            throw new IllegalArgumentException("'null' is not a type of relationship that can be satisfied!");
        }
        return relationship;
    }

    @Override
    protected void handleLackOfCandidate(Deployment deployment, List<InternalComponent> candidates) {
        final String string = String.format(
                "Unable to find a component type matching the provided end of relationship type '%s' (port '%s')",
                relationshipType.getName(),
                relationshipType.getProvidedEnd().getQualifiedName());
        throw new IllegalStateException(string);
    }

    @Override
    protected List<InternalComponent> collectCandidates(Deployment deployment) {
        List<InternalComponent> candidates = new ArrayList<InternalComponent>();
        for (InternalComponent artefact : deployment.getComponents().onlyInternals()) {
            if (isCandidate(artefact)) {
                candidates.add(artefact);
            }
        }
        return candidates;
    }

    private boolean isCandidate(InternalComponent component) {
        return component.getProvidedPorts().contains(relationshipType.getProvidedEnd()); 
    }

}
