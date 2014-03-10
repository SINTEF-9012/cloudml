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
/*
 */
package org.cloudml.core.builders;

import java.util.ArrayList;
import org.cloudml.core.Artefact;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.NamedElement;

/**
 * Simplify the construction of complex artefact types
 */
public class ArtefactBuilder {

    private String name;
    private ArrayList<ClientPortBuilder> clientPorts;
    private ArrayList<ServerPortBuilder> serverPorts;

    public ArtefactBuilder() {
        this.name = NamedElement.DEFAULT_NAME;
        this.clientPorts = new ArrayList<ClientPortBuilder>();
        this.serverPorts = new ArrayList<ServerPortBuilder>();
    }

    public ArtefactBuilder named(String name) {
        this.name = name;
        return this;
    }

    public ArtefactBuilder withClientPort(ClientPortBuilder clientPort) {
        this.clientPorts.add(clientPort);
        return this;
    }

    public ArtefactBuilder withServerPort(ServerPortBuilder serverPort) {
        this.serverPorts.add(serverPort);
        return this;
    }

    public Artefact build() {
        Artefact result = new Artefact();
        prepareArtefact(result);
        return result;
    }

    public void integrateIn(DeploymentModel model) {
        Artefact result = new Artefact();
        prepareArtefact(result);
        model.addArtefact(result);
    }

    private void prepareArtefact(Artefact result) {
        result.setName(name);
        for (ClientPortBuilder clientPortBuilder : clientPorts) {
            clientPortBuilder.integrateIn(result);
        }
        for (ServerPortBuilder serverPortBuilder : serverPorts) {
            serverPortBuilder.integrateIn(result);
        }
    }
}
