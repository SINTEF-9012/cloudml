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

import org.cloudml.core.collections.BindingInstanceGroup;
import org.cloudml.core.collections.BindingTypeGroup;
import org.cloudml.core.collections.NodeTypeGroup;
import org.cloudml.core.collections.ProviderGroup;
import org.cloudml.core.collections.ArtefactTypeGroup;
import org.cloudml.core.collections.NodeInstanceGroup;
import org.cloudml.core.collections.ArtefactInstanceGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.cloudml.core.validation.CanBeValidated;
import org.cloudml.core.validation.Report;
import org.cloudml.core.visitors.Visitable;
import org.cloudml.core.visitors.Visitor;

public class DeploymentModel extends WithProperties implements Visitable, CanBeValidated {

    private final ProviderGroup providers;
    private final NodeTypeGroup nodeTypes;
    private final ArtefactTypeGroup artefactTypes;
    private final BindingTypeGroup bindingTypes;
    private final NodeInstanceGroup nodeInstances;
    private final ArtefactInstanceGroup artefactInstances;
    private final BindingInstanceGroup bindingInstances;

    public DeploymentModel() {
        super();
        this.providers = new LocalProviderGroup();
        this.nodeTypes = new LocalNodeTypeGroup();
        this.artefactTypes = new LocalArtefactTypeGroup();
        this.bindingTypes = new LocalBindingTypeGroup();
        this.nodeInstances = new LocalNodeInstanceGroup();
        this.artefactInstances = new LocalArtefactInstanceGroup();
        this.bindingInstances = new LocalBindingInstanceGroup();
    }

    public DeploymentModel(String name) {
        super(name);
        this.providers = new LocalProviderGroup();
        this.nodeTypes = new LocalNodeTypeGroup();
        this.artefactTypes = new LocalArtefactTypeGroup();
        this.bindingTypes = new LocalBindingTypeGroup();
        this.nodeInstances = new LocalNodeInstanceGroup();
        this.artefactInstances = new LocalArtefactInstanceGroup();
        this.bindingInstances = new LocalBindingInstanceGroup();
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

    public ProviderGroup getProviders() {
        return providers;
    }

    public NodeTypeGroup getNodeTypes() {
        return this.nodeTypes;
    }

    public ArtefactTypeGroup getArtefactTypes() {
        return artefactTypes;
    }

    public BindingTypeGroup getBindingTypes() {
        return bindingTypes;
    }

    public NodeInstanceGroup getNodeInstances() {
        return nodeInstances;
    }

    public ArtefactInstanceGroup getArtefactInstances() {
        return artefactInstances;
    }

    public boolean isUsed(ArtefactInstance server) {
        boolean found = false;
        final Iterator<ServerPortInstance> iterator = server.getProvided().iterator();
        while (iterator.hasNext() && !found) {
            found = isBound(iterator.next());
        }
        return found;
    }

    // Bindings instances
    public BindingInstanceGroup getBindingInstances() {
        return bindingInstances;
    }

    public boolean isBound(ArtefactPortInstance<? extends ArtefactPort> port) {
        return !getBindingInstances().withPort(port).isEmpty();
    }

    public ServerPortInstance findServerPort(ClientPortInstance clientPort) {
        final BindingInstanceGroup bindings = getBindingInstances().withPort(clientPort);
        if (bindings.isEmpty()) {
            final String message = String.format("client port '%s' is not yet bound to any server", clientPort.getName());
            throw new IllegalArgumentException(message);
        }
        return bindings.toList().get(0).getServer();
    }

    public List<ClientPortInstance> findClientPorts(ServerPortInstance serverPort) {
        final BindingInstanceGroup bindings = getBindingInstances().withPort(serverPort);
        if (bindings.isEmpty()) {
            final String message = String.format("server port '%s' is not yet bound to any server", serverPort.getName());
            throw new IllegalArgumentException(message);
        }
        final List<ClientPortInstance> clients = new ArrayList<ClientPortInstance>();
        for (BindingInstance binding : bindings) {
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
        for (Artefact t : artefactTypes) {
            builder.append("  - " + t + "\n");
        }
        builder.append("}\n");
        builder.append("- Binding types: {\n");
        for (Binding b : bindingTypes) {
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
        for (Node nt : nodeTypes) {
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

    private class LocalProviderGroup extends ProviderGroup {

        public LocalProviderGroup() {
            super();
        }

        @Override
        protected void abortIfCannotBeAdded(Provider provider) {
            if (named(provider.getName()) != null) {
                final String message = String.format("A provider named '%s' already exists", provider.getName());
                throw new IllegalArgumentException(message);
            }
        }

        @Override
        protected void setReferenceToContainer(Provider element) {
            element.attachTo(DeploymentModel.this);
        }

        @Override
        protected void abortIfCannotBeRemoved(Provider provider) {
            if (provider.isUsed()) {
                String message = String.format("Unable to remove provider '%s' as it still provides nodes", provider.getName());
                throw new IllegalStateException(message);
            }
        }

        @Override
        protected void clearReferenceToContainer(Provider element) {
            element.detach();
        }
    }

    private class LocalNodeTypeGroup extends NodeTypeGroup {

        public LocalNodeTypeGroup() {
            super();
        }

        @Override
        protected final void abortIfCannotBeAdded(Node node) {
            if (!getProviders().contains(node.getProvider())) {
                String message = String.format("The provider '%s' (used by node '%s') is not part of the model", node.getProvider().getName(), node.getName());
                throw new IllegalStateException(message);
            }
        }

        @Override
        protected void clearReferenceToContainer(Node element) {
            element.detach();
        }

        @Override
        protected void setReferenceToContainer(Node element) {
            element.attachTo(DeploymentModel.this);
        }

        @Override
        protected final void abortIfCannotBeRemoved(Node node) {
            if (node.hasAnyInstance()) { 
                final String message = String.format("Unable to remove node type '%s' as there are still some related instances", node.getName());
                throw new IllegalStateException(message);
            }
        }
    }

    private class LocalArtefactTypeGroup extends ArtefactTypeGroup {

        public LocalArtefactTypeGroup() {
            super();
        }

        @Override
        protected void abortIfCannotBeRemoved(Artefact artefact) {
            if (artefact.hasAnyInstance()) { 
                String message = String.format("Cannot remove artefact '%s' as it still has instances", artefact.getName());
                throw new IllegalStateException(message); 
            }
        }

        @Override
        protected void setReferenceToContainer(Artefact element) {
            element.attachTo(DeploymentModel.this);
        }

        @Override
        protected void clearReferenceToContainer(Artefact element) {
            element.detach();
        }
        
        
    }

    private class LocalBindingTypeGroup extends BindingTypeGroup {

        public LocalBindingTypeGroup() {
            super();
        }
    }

    private class LocalNodeInstanceGroup extends NodeInstanceGroup {

        public LocalNodeInstanceGroup() {
            super();
        }

        @Override
        protected void abortIfCannotBeAdded(NodeInstance instance) {
            if (!getNodeTypes().contains(instance.getType())) {
                final String message = String.format("The node type '%s', associated with instance '%s' is not part of this model", instance.getType().getName(), instance.getName());
                throw new IllegalArgumentException(message);
            }
        }
    }

    private class LocalArtefactInstanceGroup extends ArtefactInstanceGroup {

        public LocalArtefactInstanceGroup() {
            super();
        }

        @Override
        protected void abortIfCannotBeAdded(ArtefactInstance instance) {
            if (!getArtefactTypes().contains(instance.getType())) {
                String message = String.format("artefact type '%s' associated with instance '%s' is not part of the model", instance.getType().getName(), instance.getName());
                throw new IllegalArgumentException(message);
            }
            if (instance.hasDestination() && !getNodeInstances().contains(instance.getDestination())) {
                String message = String.format("destination '%s' of artefact instance '%s' is not part of the model", instance.getDestination().getName(), instance.getName());
                throw new IllegalArgumentException(message);
            }
        }
    }

    private class LocalBindingInstanceGroup extends BindingInstanceGroup {

        public LocalBindingInstanceGroup() {
            super();
        }
    }
}
