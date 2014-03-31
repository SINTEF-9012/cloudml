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
import org.cloudml.core.Binding;
import org.cloudml.core.ClientPortInstance;
import org.cloudml.core.DeploymentModel;

public class FindBinding extends AbstractFind<Binding> {

    private final ClientPortInstance clientPort;

    public FindBinding(StandardLibrary library, ClientPortInstance clientPort) {
        super(library);
        this.clientPort = clientPort;
    }

    @Override
    protected List<Binding> collectCandidates(DeploymentModel deployment) {
        final ArrayList<Binding> candidates = new ArrayList<Binding>();
        for (Binding binding : deployment.getBindingTypes()) {
            if (isRelevant(deployment, clientPort, binding)) {
                candidates.add(binding);
            }
        }
        return candidates;
    }

    @Override
    protected void handleLackOfCandidate(DeploymentModel deployment, List<Binding> candidates) {
        final String message = String.format(
                "Unable to find any relevant binding type for client port '%s:%s' of type '%s'",
                clientPort.getOwner().getName(),
                clientPort.getName(),
                clientPort.getType().getName());
        throw new IllegalStateException(message);
    }


    private boolean isRelevant(DeploymentModel deployment, ClientPortInstance clientPort, Binding binding) {
        return binding.getClient().equals(clientPort.getType())
                && !isExcluded(binding);
    }

    protected boolean isExcluded(Binding binding) {
        return false;
    }
}
