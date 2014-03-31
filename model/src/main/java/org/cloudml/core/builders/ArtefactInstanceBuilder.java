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
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.ClientPort;
import org.cloudml.core.DeploymentModel;
import static org.cloudml.core.NamedElement.DEFAULT_NAME;
import org.cloudml.core.ServerPort;
import static org.cloudml.core.builders.Commons.*;

public class ArtefactInstanceBuilder {

    private String instanceName;
    private String typeName;
    private String hostName;

    public ArtefactInstanceBuilder() {
        this.instanceName = DEFAULT_NAME;
        this.typeName = DEFAULT_NAME;
        this.hostName = DEFAULT_NAME;
    }

    public ArtefactInstanceBuilder named(String name) {
        this.instanceName = name;
        return this;
    }

    public ArtefactInstanceBuilder ofType(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public ArtefactInstanceBuilder hostedBy(String hostName) {
        this.hostName = hostName;
        return this;
    }

    public ArtefactInstance build() {
        final ArtefactInstance artefactInstance = new ArtefactInstance();
        artefactInstance.setName(instanceName);
        artefactInstance.setType(anArtefact().named(typeName).build());
        artefactInstance.setDestination(aNodeInstance().named(hostName).build());
        return artefactInstance;
    }

    public void integrateIn(DeploymentModel model) {
        final ArtefactInstance result = new ArtefactInstance();
        result.setName(instanceName);
        final Artefact type = model.getArtefactTypes().named(typeName);
        result.setType(type);
        for (ServerPort serverPort : type.getProvided()) {
            aServerPortInstance()
                    .withName(serverPort.getName())
                    .ofType(serverPort.getName())
                    .integrateIn(result);
        }
        for (ClientPort clientPort : type.getRequired()) {
            aClientPortInstance()
                    .withName(clientPort.getName())
                    .ofType(clientPort.getName())
                    .integrateIn(result);
        }
        result.setDestination(model.getNodeInstances().named(hostName));
        model.getArtefactInstances().add(result);
    }
}
