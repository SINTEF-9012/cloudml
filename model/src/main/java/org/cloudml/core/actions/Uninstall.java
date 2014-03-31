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
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.ClientPortInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.ServerPortInstance;

public class Uninstall extends AbstractAction<Void> {

    private final ArtefactInstance artefactInstance;

    public Uninstall(StandardLibrary library, ArtefactInstance artefactInstance) {
        super(library);
        this.artefactInstance = artefactInstance;
    }

    @Override
    public Void applyTo(DeploymentModel target) {
        disconnectRequiredPorts(target);
        List<ClientPortInstance> pendingClients = disconnectProvidedPorts(target);
        shutdownArtefactInstance(target);
        reconnectClientsWithAlternativeServer(pendingClients, target);
        return NOTHING;
    }

    private void disconnectRequiredPorts(DeploymentModel deployment) {
        for (ClientPortInstance clientPort: artefactInstance.getRequired()) {
            if (deployment.isBound(clientPort)) {
                final ArtefactInstance server = deployment.findServerPort(clientPort).getOwner();
                getLibrary().unbind(deployment, clientPort);
                if (!deployment.isUsed(server)) {
                    getLibrary().uninstall(deployment, server);
                }
            }
        }
    }

    private List<ClientPortInstance> disconnectProvidedPorts(DeploymentModel deployment) {
        final List<ClientPortInstance> customerToBeRebound = new ArrayList<ClientPortInstance>();
        for (ServerPortInstance serverPort : artefactInstance.getProvided()) {
            if (deployment.isBound(serverPort)) {
                customerToBeRebound.addAll(deployment.findClientPorts(serverPort));
                getLibrary().unbind(deployment, serverPort);
            }
        }
        return customerToBeRebound;
    }

    private void reconnectClientsWithAlternativeServer(List<ClientPortInstance> customerToBeRebound, DeploymentModel target) {
        for (ClientPortInstance clientPort : customerToBeRebound) {
            getLibrary().bind(target, clientPort);
        }
    }

    private void shutdownArtefactInstance(DeploymentModel target) {
        getLibrary().stop(target, artefactInstance);
        target.getArtefactInstances().remove(artefactInstance);
    }

 
}
