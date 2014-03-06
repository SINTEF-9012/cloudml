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

package test.cloudml.core.builder;

import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.ArtefactPort;
import org.cloudml.core.ArtefactPortInstance;
import org.cloudml.core.Binding;
import org.cloudml.core.BindingInstance;
import org.cloudml.core.ClientPort;
import org.cloudml.core.ClientPortInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.Provider;
import org.cloudml.core.ServerPort;
import org.cloudml.core.ServerPortInstance;

/**
 * Simplify the construction of CloudML deployment models
 */
public class Builder {

    private final DeploymentModel inProgress;
    
    public Builder() {
        this.inProgress = new DeploymentModel();
    }
    
    public Builder(DeploymentModel toModify) {
        this.inProgress = toModify;
    }
    
    public Provider createProvider(String providerName) {
        Provider provider = new Provider(providerName);
        this.inProgress.getProviders().add(provider);
        return provider;
    }
    
    public Node createNodeType(String typeName, Provider provider) {
        Node type = new Node(typeName, provider);
        this.inProgress.getNodeTypes().put(typeName, type);
        return type;
    }
    
    public ArtefactBuilder createArtefactType(String typeName) {
        ArtefactBuilder builder = new ArtefactBuilder(typeName);
        this.inProgress.getArtefactTypes().put(typeName, builder.getResult());
        return builder;
    }
    
    public NodeInstance provision(Node type, String instanceName) {
        abortIfExternal(type);
        NodeInstance instance = new NodeInstance(instanceName, type);
        this.inProgress.getNodeInstances().add(instance);
        return instance;
    }
    
    public ArtefactInstance install(Artefact type, String instanceName, NodeInstance host) {
        ArtefactInstance instance = new ArtefactInstance(instanceName, type, host);
        this.inProgress.getArtefactInstances().add(instance);
        for (ClientPort clientPort: type.getRequired()) {
            instance.getRequired().add(new ClientPortInstance(clientPort.getName(), clientPort, instance));
        }
        for (ServerPort serverPort: type.getProvided()) {
            instance.getProvided().add(new ServerPortInstance(serverPort.getName(), serverPort, instance));
        }
        return instance;
    }
    
    public Binding createBindingType(String name, ClientPort client, ServerPort server) {
        abortIfExternal(client);
        abortIfExternal(server);
        Binding binding = new Binding(client, server);
        this.inProgress.getBindingTypes().put(name, binding);
        return binding;
    }
    
    public BindingInstance connect(String name, Binding type, ClientPortInstance client, ServerPortInstance server) {
        abortIfExternal(type);
        abortIfExternal(client);
        abortIfExternal(server);
        BindingInstance binding = new BindingInstance(client, server, type);
        binding.setName(name);
        this.inProgress.getBindingInstances().add(binding);
        return binding;
    }
    
    public DeploymentModel getResult() {
        // TODO validate the model
        return this.inProgress;
    }

    private void abortIfExternal(Node nodeType) {
        if (!this.inProgress.contains(nodeType)) { 
            final String message = String.format("The node type '%s' is not part of the model under construction!", nodeType.getName());
            throw new IllegalArgumentException(message);
        }
    }
    
    private void abortIfExternal(ArtefactPort port) {
        if (!this.inProgress.contains(port.getOwner())) { 
            final String message = String.format(
                    "The port type '%s::%s' is not part of the model under construction!", 
                    port.getOwner().getName(), 
                    port.getName());
            throw new IllegalArgumentException(message);
        }
    }
    
    private void abortIfExternal(Binding bindingType) {
        if (!this.inProgress.contains(bindingType)) {  
            final String message = String.format(
                    "The binding type '%s -- %s' is not part of the model under construction!", 
                    bindingType.getClient().getName(), 
                    bindingType.getServer().getName());
            throw new IllegalArgumentException(message);
        }
    }
    
    private void abortIfExternal(ArtefactPortInstance portInstance) { 
        if (!this.inProgress.contains(portInstance.getOwner())) {  
            final String message = String.format(
                    "The port instance '%s/%s' is not part of the model under construction!", 
                    portInstance.getOwner().getName(), 
                    portInstance.getName());
            throw new IllegalArgumentException(message);
        }
    }
    
}
