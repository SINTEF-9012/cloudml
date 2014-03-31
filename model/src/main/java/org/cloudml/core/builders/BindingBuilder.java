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
package org.cloudml.core.builders;

import org.cloudml.core.Artefact;
import static org.cloudml.core.builders.Commons.*;
import static org.cloudml.core.Binding.*;
import org.cloudml.core.Binding;
import org.cloudml.core.DeploymentModel;

public class BindingBuilder {

    private String name;
    private String clientOwnerName;
    private String clientPortName;
    private String serverOwnerName;
    private String serverPortName;

    public BindingBuilder() {
        this.name = DEFAULT_NAME;
        this.clientPortName = DEFAULT_NAME;
        this.clientOwnerName = DEFAULT_NAME;
        this.serverOwnerName = DEFAULT_NAME;
        this.serverPortName = DEFAULT_NAME;
    }

    public BindingBuilder named(String name) {
        this.name = name;
        return this;
    }

    public BindingBuilder from(String ownerName, String portName) {
        this.clientOwnerName = ownerName;
        this.clientPortName = portName;
        return this;
    }

    public BindingBuilder to(String ownerName, String portName) {
        this.serverOwnerName = ownerName;
        this.serverPortName = portName;
        return this;
    }

    public Binding build() {
        Binding result = new Binding();
        result.setName(name);
        Artefact client = anArtefact()
                .named(clientOwnerName)
                .withClientPort(aClientPort().named(clientPortName))
                .build();
        result.setClient(client.findRequiredPortByName(clientPortName));
        Artefact server = anArtefact()
                .named(serverOwnerName)
                .withServerPort(aServerPort().named(serverPortName))
                .build();
        result.setServer(server.findProvidedPortByName(serverPortName));
        return result;
    }
 
    public void integrateIn(DeploymentModel container) {
        Binding result = new Binding();
        result.setName(name);
        Artefact client = container.getArtefactTypes().named(clientOwnerName);
        result.setClient(client.findRequiredPortByName(clientPortName));
        Artefact server = container.getArtefactTypes().named(serverOwnerName);
        result.setServer(server.findProvidedPortByName(serverPortName));
        container.addBinding(result);
    }

}
