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

import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.ArtefactPort;
import org.cloudml.core.ArtefactPortInstance;
import org.cloudml.core.Binding;
import org.cloudml.core.ClientPortInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.ServerPortInstance;

public class StandardLibrary {

    private final NamingStrategy naming; 

    public StandardLibrary() {
        this.naming = new NamingStrategy();
    }
    
    public StandardLibrary(NamingStrategy naming) {
        this.naming = naming;
    }   
    
    public NodeInstance provision(DeploymentModel deployment, Node nodeType) {
        return new Provision(this, nodeType).applyTo(deployment);
    }

    public void terminate(DeploymentModel deployment, NodeInstance nodeInstance) {
        new Terminate(this, nodeInstance).applyTo(deployment);  
    }

    public ArtefactInstance install(DeploymentModel deployment, Artefact artefactType) {
        return new Install(this, artefactType).applyTo(deployment);
    }

    public void uninstall(DeploymentModel deployment, ArtefactInstance artefactInstance) {
        new Uninstall(this, artefactInstance).applyTo(deployment);
    }

    public NodeInstance migrate(DeploymentModel deployment, ArtefactInstance artefactInstance) {
        return new Migrate(this, artefactInstance).applyTo(deployment);
    }

    public void bind(DeploymentModel deployment, ClientPortInstance clientPort) {
        new Bind(this, clientPort).applyTo(deployment);
    }

    public Binding findBindingFor(DeploymentModel deployment, ClientPortInstance clientPort) {
        return new FindBinding(this, clientPort).applyTo(deployment);
    }

    public ServerPortInstance findServerPortFor(DeploymentModel deployment, Binding bindingType) {
        return new FindServerPortInstance(this, bindingType).applyTo(deployment);
    }

    public NodeInstance findDestinationFor(DeploymentModel deployment, Artefact artefact) {
        return new FindDestination(this, artefact).applyTo(deployment);
    }

    public NodeInstance findAlternativeDestinationFor(DeploymentModel deployment, ArtefactInstance artefact) {
        return new FindDestination(this, artefact.getType(), artefact.getDestination()).applyTo(deployment);
    }

    public String createUniqueNodeInstanceName(DeploymentModel deployment, Node type) {
        return naming.createUniqueNodeInstanceName(deployment, type);
    }

    public String createUniqueArtefactInstanceName(DeploymentModel deployment, Artefact type) {
        return naming.createUniqueArtefactInstanceName(deployment, type);
    }

    public String createUniqueBindingInstanceName(DeploymentModel deployment, Binding type) {
        return naming.createUniqueBindingInstanceName(deployment, type);
    }

    public void unbind(DeploymentModel deployment, ArtefactPortInstance<? extends ArtefactPort> port) {
        new Unbind(this, port).applyTo(deployment);
    }

    public void stop(DeploymentModel deployment, ArtefactInstance artefactInstance) {
        new StopArtefactInstance(this, artefactInstance).applyTo(deployment);
    }

    public void stop(DeploymentModel deployment, NodeInstance instance) {
        new StopNodeInstance(this, instance).applyTo(deployment);
    }

    public Node findNodeType(DeploymentModel deployment, Artefact type) {
        return new FindNodeType(this, type).applyTo(deployment);
    }

    public Artefact findArtefactTypeProviding(DeploymentModel deployment, Binding bindingType) {
        return new FindArtefactTypeProviding(this, bindingType).applyTo(deployment);
    }

}
