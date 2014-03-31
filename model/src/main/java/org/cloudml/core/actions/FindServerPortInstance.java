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
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.Binding;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.ServerPortInstance;

public class FindServerPortInstance extends AbstractFind<ServerPortInstance> {

    private final Binding bindingType;

    public FindServerPortInstance(StandardLibrary library, Binding bindingType) {
        super(library);
        this.bindingType = bindingType;
    }

    @Override
    protected List<ServerPortInstance> collectCandidates(DeploymentModel deployment) {
        ArrayList<ServerPortInstance> candidates = new ArrayList<ServerPortInstance>();
        for (ArtefactInstance artefactInstance : deployment.getArtefactInstances()) {
            for (ServerPortInstance serverPort : artefactInstance.getProvided()) {
                if (isCandidate(bindingType, serverPort)) {
                    candidates.add(serverPort);
                }
            }
        }
        return candidates;
    }

    private boolean isCandidate(Binding bindingType, ServerPortInstance serverPort) {
        return bindingType.getServer().equals(serverPort.getType());
    }

    @Override
    protected void handleLackOfCandidate(DeploymentModel deployment, List<ServerPortInstance> candidates) {
        final Artefact artefact = getLibrary().findArtefactTypeProviding(deployment, bindingType);
        final ArtefactInstance instance = getLibrary().install(deployment, artefact);
        final ServerPortInstance serverPort = instance.findProvidedPortByName(bindingType.getServer().getName());
        candidates.add(serverPort);
    }

}
