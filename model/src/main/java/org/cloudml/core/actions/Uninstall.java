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
import org.cloudml.core.Component;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.InternalComponentInstance;
import org.cloudml.core.Deployment;
import org.cloudml.core.RequiredPortInstance;
import org.cloudml.core.ProvidedPortInstance;

public class Uninstall extends AbstractAction<Void> {

    private final InternalComponentInstance artefactInstance;

    public Uninstall(StandardLibrary library, InternalComponentInstance artefactInstance) {
        super(library);
        this.artefactInstance = artefactInstance;
    }

    @Override
    public Void applyTo(Deployment target) {
        disconnectRequiredPorts(target);
        List<RequiredPortInstance> pendingClients = disconnectProvidedPorts(target);
        if(artefactInstance.externalHost() == null){
            System.out.println("111111111111111111111111111111111111111"+artefactInstance.getName());
        }
        shutdownInternalComponentInstance(target);
        reconnectClientsWithAlternativeServer(pendingClients, target);
        return NOTHING;
    }

    private void disconnectRequiredPorts(Deployment deployment) {
        for (RequiredPortInstance clientPort: artefactInstance.getRequiredPorts()) {
            if (clientPort.isBound()) {
                final ComponentInstance<? extends Component> server = clientPort.findProvider();  
                getLibrary().unbind(deployment, clientPort);
                if (server.canBeUninstalled()) {
                    getLibrary().uninstall(deployment, (InternalComponentInstance) server);
                }
            }
        }
    }

    private List<RequiredPortInstance> disconnectProvidedPorts(Deployment deployment) {
        final List<RequiredPortInstance> customerToBeRebound = new ArrayList<RequiredPortInstance>();
        for (ProvidedPortInstance serverPort : artefactInstance.getProvidedPorts()) {
            if (serverPort.isBound()) {
                customerToBeRebound.addAll(serverPort.findClients());
                getLibrary().unbind(deployment, serverPort);
            }
        }
        return customerToBeRebound;
    }

    private void reconnectClientsWithAlternativeServer(List<RequiredPortInstance> customerToBeRebound, Deployment target) {
        for (RequiredPortInstance clientPort : customerToBeRebound) {
            getLibrary().bind(target, clientPort);
        }
    }

    private void shutdownInternalComponentInstance(Deployment target) {
        getLibrary().stop(target, artefactInstance);
        target.getComponentInstances().remove(artefactInstance);
    }

 
}
