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


import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.BindingInstance;
import static org.cloudml.core.builders.Commons.*;
import static org.cloudml.core.BindingInstance.*;
import org.cloudml.core.DeploymentModel;

public class BindingInstanceBuilder {

    private String name;
    private String typeName;
    private String clientArtefactInstanceName;
    private String clientPortName;
    private String serverArtefactInstanceName;
    private String serverPortName;
    
    public BindingInstanceBuilder() {
        this.name = DEFAULT_NAME;
        this.typeName = DEFAULT_NAME;
        this.clientArtefactInstanceName = DEFAULT_NAME;
        this.clientPortName = DEFAULT_NAME;
        this.serverArtefactInstanceName = DEFAULT_NAME;
        this.serverPortName = DEFAULT_NAME;
    }
    
    public BindingInstanceBuilder named(String name) {
        this.name = name;
        return this;
    }
    
    public BindingInstanceBuilder ofType(String typeName) {
        this.typeName = typeName;
        return this;
    }
    
    public BindingInstanceBuilder from(String clientArtefactInstanceName, String portName) {
        this.clientArtefactInstanceName = clientArtefactInstanceName;
        this.clientPortName = portName;
        return this;
    }
    
    public BindingInstanceBuilder to(String serverArtefactInstanceName, String portName) {
        this.serverArtefactInstanceName = serverArtefactInstanceName;
        this.serverPortName = portName;
        return this;
    }
    
    
    public BindingInstance build() {
        BindingInstance result = new BindingInstance();
        result.setName(name);
        result.setType(aBinding().named(typeName).build());
        ArtefactInstance client = anArtefactInstance()
                .named(clientArtefactInstanceName)
                .build();
        client.addRequiredPort(aClientPortInstance().withName(clientPortName).build());
        result.setClient(client.findRequiredPortByName(clientPortName));
        ArtefactInstance server = anArtefactInstance()
                .named(serverArtefactInstanceName)
                .build();
        server.addProvidedPort(aServerPortInstance().withName(serverPortName).build());
        result.setServer(server.findProvidedPortByName(serverPortName));
        return result;
    }
    
    public void integrateIn(DeploymentModel container) {
        BindingInstance result = new BindingInstance();
        result.setName(name);
        result.setType(container.getBindingTypes().named(this.typeName));
        result.setClient(container
                .getArtefactInstances().named(this.clientArtefactInstanceName)
                .findRequiredPortByName(this.clientPortName));
        result.setServer(container
                .getArtefactInstances().named(this.serverArtefactInstanceName)
                .findProvidedPortByName(this.serverPortName));
        container.getBindingInstances().add(result); 
    }
    
}
