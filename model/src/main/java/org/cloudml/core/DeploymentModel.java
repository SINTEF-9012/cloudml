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
package org.cloudml.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.cloudml.core.validation.CanBeValidated;
import org.cloudml.core.validation.Report;
import org.cloudml.core.visitors.Visitable;
import org.cloudml.core.visitors.Visitor;

public class DeploymentModel extends WithProperties implements Visitable, CanBeValidated {

    private Map<String, Artefact> artefactTypes = new HashMap<String, Artefact>();
    private List<ArtefactInstance> artefactInstances = new LinkedList<ArtefactInstance>();
    private Map<String, Node> nodeTypes = new HashMap<String, Node>();
    private List<NodeInstance> nodeInstances = new LinkedList<NodeInstance>();
    private List<Provider> providers = new LinkedList<Provider>();
    private Map<String, Binding> bindingTypes = new HashMap<String, Binding>();
    private List<BindingInstance> bindingInstances = new LinkedList<BindingInstance>();

    public DeploymentModel() {
    }

    public DeploymentModel(String name) {
        super(name);
    }

    @Deprecated
    public DeploymentModel(String name, List<Property> properties,
                           Map<String, Artefact> artefactTypes, List<ArtefactInstance> artefactInstances,
                           Map<String, Node> nodeTypes, List<NodeInstance> nodeInstances, List<Provider> providers) {
        super(name, properties);
        this.artefactTypes = artefactTypes;
        this.artefactInstances = artefactInstances;
        this.nodeInstances = nodeInstances;
        this.nodeTypes = nodeTypes;
        this.providers = providers;
    }

    @Deprecated
    public DeploymentModel(String name, List<Property> properties,
                           Map<String, Artefact> artefactTypes, List<ArtefactInstance> artefactInstances,
                           Map<String, Node> nodeTypes, List<NodeInstance> nodeInstances, List<Provider> providers, Map<String, Binding> bindingTypes, List<BindingInstance> bindingInstances) {
        super(name, properties);
        this.artefactTypes = artefactTypes;
        this.artefactInstances = artefactInstances;
        this.nodeInstances = nodeInstances;
        this.nodeTypes = nodeTypes;
        this.providers = providers;
        this.bindingInstances = bindingInstances;
        this.bindingTypes = bindingTypes;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitDeploymentModel(this);
    }

    @Override
    public Report validate() {
        Report validation = new Report();
        if (isEmpty()) {
            validation.addWarning("empty deployment model");
        }
        return validation;
    }

    public boolean isEmpty() {
        return this.providers.isEmpty()
                && this.nodeTypes.isEmpty()
                && this.artefactTypes.isEmpty()
                && this.bindingTypes.isEmpty()
                && this.nodeInstances.isEmpty()
                && this.artefactInstances.isEmpty()
                && this.bindingInstances.isEmpty();
    }

    /*
     * Providers
     */
    public List<Provider> getProviders() {
        return providers;
    }

    @Deprecated
    public void setProviders(List<Provider> providers) {
        this.providers = providers;
    }

    public void addProvider(Provider provider) {
        if (findProviderByName(provider.getName()) != null) {
            final String message = String.format("A provider named '%s' already exists", provider.getName());
            throw new IllegalArgumentException(message);
        }
        this.providers.add(provider);
    }

    public void removeProvider(Provider provider) {
        if (isUsed(provider)) {
            String message = String.format("Unable to remove provider '%s' as it still provides nodes", provider.getName());
            throw new IllegalStateException(message);
        }
        this.providers.remove(provider);
    }

    public boolean contains(Provider provider) {
        return this.providers.contains(provider);
    }

    public boolean isUsed(Provider provider) {
        return !findNodesByProvider(provider).isEmpty();
    }

    public Provider findProviderByName(String providerName) {
        return findByName(providerName, this.providers);
    }

    // Node Types
    @Deprecated
    public void setNodeTypes(Map<String, Node> nodeTypes) {
        this.nodeTypes = nodeTypes;
    }

    @Deprecated
    public Map<String, Node> getNodeTypes() {
        return nodeTypes;
    }

    public List<Node> getNodes() {
        return Collections.unmodifiableList(new ArrayList<Node>(this.nodeTypes.values()));
    }

    public void addNode(Node node) {
        if (!contains(node.getProvider())) {
            String message = String.format("The provider '%s' (used by node '%s') is not part of the model", node.getProvider().getName(), node.getName());
            throw new IllegalStateException(message);
        }
        this.nodeTypes.put(node.getName(), node);
    }

    public void removeNode(Node node) {
        if (isUsed(node)) {
            final String message = String.format("Unable to remove node type '%s' as there are still some related instances", node.getName());
            throw new IllegalStateException(message);
        }
        this.nodeTypes.remove(node.getName());
    }

    public boolean isUsed(Node node) {
        return !findInstancesOf(node).isEmpty();
    }

    public List<NodeInstance> findInstancesOf(Node node) {
        ArrayList<NodeInstance> selectedInstances = new ArrayList<NodeInstance>();
        for (NodeInstance nodeInstance : this.nodeInstances) {
            if (nodeInstance.getType().equals(node)) {
                selectedInstances.add(nodeInstance);
            }
        }
        return selectedInstances;
    }

    public Node findNodeByName(String nodeName) {
        return this.nodeTypes.get(nodeName);
    }

    public List<Node> findNodesByProvider(Provider provider) {
        ArrayList<Node> selectedNodes = new ArrayList<Node>();
        for (Node node : this.nodeTypes.values()) {
            if (node.isProvidedBy(provider)) {
                selectedNodes.add(node);
            }
        }
        return selectedNodes;
    }

    public boolean contains(Node nodeType) {
        return this.nodeTypes.values().contains(nodeType);
    }

    // Artefacts types
    @Deprecated
    public void setArtefactTypes(Map<String, Artefact> artefactTypes) {
        this.artefactTypes = artefactTypes;
    }

    @Deprecated
    public Map<String, Artefact> getArtefactTypes() {
        return artefactTypes;
    }

    public List<Artefact> getArtefacts() {
        return Collections.unmodifiableList(new ArrayList<Artefact>(this.artefactTypes.values()));
    }

    public void addArtefact(Artefact artefact) {
        this.artefactTypes.put(artefact.getName(), artefact);
    }

    public void removeArtefact(Artefact artefact) {
        if (isUsed(artefact)) {
            String message = String.format("Cannot remove artefact '%s' as it still has instances", artefact.getName());
            throw new IllegalStateException(message);
        }
        this.artefactTypes.remove(artefact.getName());
    }

    public Artefact findArtefactByName(String artefactName) {
        return this.artefactTypes.get(artefactName);
    }

    public boolean contains(Artefact artefactType) {
        return this.artefactTypes.values().contains(artefactType);
    }

    public boolean isUsed(Artefact artefact) {
        return !findArtefactInstancesByType(artefact).isEmpty();
    }

    public boolean isUsed(ArtefactInstance server) {
        boolean found = false;
        final Iterator<ServerPortInstance> iterator = server.getProvided().iterator();
        while (iterator.hasNext() && !found) {
            found = isBound(iterator.next());
        }
        return found;
    }

    // Bindings Types
    @Deprecated
    public void setBindingTypes(Map<String, Binding> bindingTypes) {
        this.bindingTypes = bindingTypes;
    }

    @Deprecated
    public Map<String, Binding> getBindingTypes() {
        return bindingTypes;
    }

    public List<Binding> getBindings() {
        return Collections.unmodifiableList(new ArrayList<Binding>(this.bindingTypes.values()));
    }

    public Binding addBinding(Binding binding) {
        return this.bindingTypes.put(binding.getName(), binding);
    }

    public void removeBinding(Binding binding) {
        this.bindingTypes.remove(binding.getName());
    }

    public Binding findBindingByName(String bindingName) {
        return this.bindingTypes.get(bindingName);
    }

    public boolean contains(Binding bindingType) {
        return this.bindingTypes.values().contains(bindingType);
    }

    // Node Instances
    public List<NodeInstance> getNodeInstances() {
        return nodeInstances;
    }

    public void addNodeInstance(NodeInstance instance) {
        if (!contains(instance.getType())) {
            final String message = String.format("The node type '%s', associated with instance '%s' is not part of this model", instance.getType().getName(), instance.getName());
            throw new IllegalArgumentException(message);
        }
        this.nodeInstances.add(instance);
    }

    @Deprecated
    public void setNodeInstances(List<NodeInstance> nodeInstances) {
        this.nodeInstances = nodeInstances;
    }

    public NodeInstance findNodeInstanceByName(String nodeInstanceName) {
        return findByName(nodeInstanceName, this.nodeInstances);
    }

    public List<NodeInstance> findNodeInstancesByType(Node nodeType) {
        final ArrayList<NodeInstance> selection = new ArrayList<NodeInstance>();
        for (NodeInstance ni : nodeInstances) {
            if (ni.getType().equals(nodeType)) {
                selection.add(ni);
            }
        }
        return selection;
    }

    public boolean contains(NodeInstance nodeInstance) {
        return this.nodeInstances.contains(nodeInstance);
    }

    // Artefact instances
    public void setArtefactInstances(List<ArtefactInstance> artefactInstances) {
        this.artefactInstances = artefactInstances;
    }

    public List<ArtefactInstance> getArtefactInstances() {
        return artefactInstances;
    }

    public void addArtefactInstance(ArtefactInstance instance) {
        if (!contains(instance.getType())) {
            String message = String.format("artefact type '%s' associated with instance '%s' is not part of the model", instance.getType().getName(), instance.getName());
            throw new IllegalArgumentException(message);
        }
        if (instance.hasDestination() && !contains(instance.getDestination())) {
            String message = String.format("destination '%s' of artefact instance '%s' is not part of the model", instance.getDestination().getName(), instance.getName());
            throw new IllegalArgumentException(message);
        }
        this.artefactInstances.add(instance);
    }

    public boolean contains(ArtefactInstance artefactInstance) {
        return this.artefactInstances.contains(artefactInstance);
    }

    public ArtefactInstance findArtefactInstanceByName(String instanceName) {
        return findByName(instanceName, this.artefactInstances);
    }

    public List<ArtefactInstance> findArtefactInstancesByType(Artefact type) {
        ArrayList<ArtefactInstance> selection = new ArrayList<ArtefactInstance>();
        for (ArtefactInstance instance : this.artefactInstances) {
            if (instance.getType().equals(type)) {
                selection.add(instance);
            }
        }
        return selection;
    }

    public List<ArtefactInstance> findArtefactInstancesByDestination(NodeInstance destination) {
        final ArrayList<ArtefactInstance> selection = new ArrayList<ArtefactInstance>();
        for (ArtefactInstance artefact : artefactInstances) {
            if (artefact.getDestination().equals(destination)) {
                selection.add(artefact);
            }
        }
        return selection;
    }

    // Bindings instances
    public void setBindingInstances(List<BindingInstance> bindingInstances) {
        this.bindingInstances = bindingInstances;
    }

    public List<BindingInstance> getBindingInstances() {
        return bindingInstances;
    }

    public void addBindingInstance(BindingInstance bindingToAdd) {
        this.bindingInstances.add(bindingToAdd);
    }

    public void removeBindingInstance(BindingInstance bindingToRemove) {
        this.bindingInstances.remove(bindingToRemove);
    }

    public BindingInstance findBindingInstanceByName(String bindingInstanceName) {
        return findByName(bindingInstanceName, this.bindingInstances);
    }

    public List<BindingInstance> findBindingInstancesByPort(ArtefactPortInstance<? extends ArtefactPort> port) {
        final ArrayList<BindingInstance> selection = new ArrayList<BindingInstance>();
        for (BindingInstance binding : bindingInstances) {
            if (binding.eitherEndIs(port)) {
                selection.add(binding);
            }
        }
        return selection;
    }

    public List<BindingInstance> findBindingInstancesByClientEnd(ClientPortInstance cpi) {
        return findBindingInstancesByPort(cpi);
    }

    public List<BindingInstance> findBindingInstancesByServerEnd(ServerPortInstance cpi) {
        return findBindingInstancesByPort(cpi);
    }

    public boolean isBound(ArtefactPortInstance<? extends ArtefactPort> port) {
        return !findBindingInstancesByPort(port).isEmpty();
    }
    
    public ServerPortInstance findServerPort(ClientPortInstance clientPort) {
        final List<BindingInstance> bindings = findBindingInstancesByPort(clientPort);
        if (bindings.isEmpty()) {
            final String message = String.format("client port '%s' is not yet bound to any server", clientPort.getName());
            throw new IllegalArgumentException(message);
        }
        return bindings.get(0).getServer();
    }
    
   public List<ClientPortInstance> findClientPorts(ServerPortInstance serverPort) {
       final List<BindingInstance> bindings = findBindingInstancesByPort(serverPort);
         if (bindings.isEmpty()) {
            final String message = String.format("server port '%s' is not yet bound to any server", serverPort.getName());
            throw new IllegalArgumentException(message);
        }
        final List<ClientPortInstance> clients = new ArrayList<ClientPortInstance>();
        for(BindingInstance binding: bindings) {
            clients.add(binding.getClient());
        }
        return clients;
    }
    

    @Override
    public boolean equals(Object other) {
        if (other instanceof DeploymentModel) {
            DeploymentModel otherDepModel = (DeploymentModel) other;
            return artefactTypes.equals(otherDepModel.artefactTypes) && artefactInstances.equals(otherDepModel.artefactInstances)
                    && nodeTypes.equals(otherDepModel.nodeTypes) && nodeInstances.equals(otherDepModel.nodeInstances)
                    && bindingTypes.equals(otherDepModel.bindingTypes) && bindingInstances.equals(otherDepModel.bindingInstances);
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Deployment model " + getName() + "{\n");
        builder.append("- Artefact types: {\n");
        for (Artefact t : artefactTypes.values()) {
            builder.append("  - " + t + "\n");
        }
        builder.append("}\n");
        builder.append("- Binding types: {\n");
        for (Binding b : bindingTypes.values()) {
            builder.append("  - " + b + "\n");
        }
        builder.append("}\n");
        builder.append("- Artefact instances: {\n");
        for (ArtefactInstance i : artefactInstances) {
            builder.append("  - " + i + "\n");
        }
        builder.append("}\n");
        builder.append("- Binding instances: {\n");
        for (BindingInstance b : bindingInstances) {
            builder.append("  - " + b + "\n");
        }
        builder.append("}\n");
        builder.append("- Node types: {\n");
        for (Node nt : nodeTypes.values()) {
            builder.append("  - " + nt + "\n");
        }
        builder.append("}\n");
        builder.append("- Node instances: {\n");
        for (NodeInstance ni : nodeInstances) {
            builder.append("  - " + ni + "\n");
        }
        builder.append("}\n");
        return builder.toString();
    }
}
