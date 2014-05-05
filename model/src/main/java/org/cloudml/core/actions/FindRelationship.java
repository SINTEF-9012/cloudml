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
import org.cloudml.core.Relationship;
import org.cloudml.core.RequiredPortInstance;
import org.cloudml.core.Deployment;

public class FindRelationship extends AbstractFind<Relationship> {

    private final RequiredPortInstance requiredPort;

    public FindRelationship(StandardLibrary library, RequiredPortInstance clientPort) {
        super(library);
        this.requiredPort = clientPort;
    }

    @Override
    protected List<Relationship> collectCandidates(Deployment deployment) {
        final ArrayList<Relationship> candidates = new ArrayList<Relationship>();
        for (Relationship binding : deployment.getRelationships()) {
            if (isRelevant(requiredPort, binding)) {
                candidates.add(binding);
            }
        }
        return candidates;
    }

    @Override
    protected void handleLackOfCandidate(Deployment deployment, List<Relationship> candidates) {
        final String message = String.format(
                "Unable to find any relevant relationship type for client port '%s' of type '%s'",
                requiredPort.getQualifiedName(),
                requiredPort.getType().getQualifiedName());
        throw new IllegalStateException(message);
    }


    private boolean isRelevant(RequiredPortInstance requiredPort, Relationship relationship) {
        return relationship.getRequiredEnd().equals(requiredPort.getType())
                && !isExcluded(relationship);
    }

    protected boolean isExcluded(Relationship binding) {
        return false;
    }
}
