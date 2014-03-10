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

import java.util.ArrayList;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.NamedElement;

/**
 * Simplify the construction ofType CloudML deployment models
 */
public class DeploymentModelBuilder {

    private String name;
    private ArrayList<ProviderBuilder> providers;
    private ArrayList<NodeBuilder> nodes;
    private ArrayList<ArtefactBuilder> artefacts;
    private ArrayList<BindingBuilder> bindings;
    private ArrayList<NodeInstanceBuilder> nodeInstances;
    private ArrayList<ArtefactInstanceBuilder> artefactInstances;
    private ArrayList<BindingInstanceBuilder> bindingInstances;

    public DeploymentModelBuilder() {
        this.name = NamedElement.DEFAULT_NAME;
        this.nodes = new ArrayList<NodeBuilder>();
        this.providers = new ArrayList<ProviderBuilder>();
        this.artefacts = new ArrayList<ArtefactBuilder>();
        this.bindings = new ArrayList<BindingBuilder>();
        this.nodeInstances = new ArrayList<NodeInstanceBuilder>();
        this.artefactInstances = new ArrayList<ArtefactInstanceBuilder>();
        this.bindingInstances = new ArrayList<BindingInstanceBuilder>();
    }

    public DeploymentModelBuilder named(String modelName) {
        this.name = modelName;
        return this;
    }

    public DeploymentModelBuilder withProvider(ProviderBuilder provider) {
        this.providers.add(provider);
        return this;
    }

    public DeploymentModelBuilder withNodeType(NodeBuilder nodeType) {
        this.nodes.add(nodeType);
        return this;
    }

    public DeploymentModelBuilder withArtefact(ArtefactBuilder artefactType) {
        this.artefacts.add(artefactType);
        return this;
    }

    public DeploymentModelBuilder withBinding(BindingBuilder binding) {
        this.bindings.add(binding);
        return this;
    }

    public DeploymentModelBuilder withNodeInstance(NodeInstanceBuilder instance) {
        this.nodeInstances.add(instance);
        return this;
    }
    
    public DeploymentModelBuilder withArtefactInstance(ArtefactInstanceBuilder instance) {
        this.artefactInstances.add(instance);
        return this;
    }
   
    
    public DeploymentModelBuilder withBindingInstance(BindingInstanceBuilder instance) {
        this.bindingInstances.add(instance);
        return this;
    }

    public DeploymentModel build() {
        DeploymentModel model = new DeploymentModel();
        model.setName(name);
        for (ProviderBuilder providerBuilder : providers) {
            providerBuilder.integrateIn(model);
        }
        for (NodeBuilder nodeBuilder : nodes) {
            nodeBuilder.integrateIn(model);
        }
        for (ArtefactBuilder artefactBuilder : artefacts) {
            artefactBuilder.integrateIn(model);
        }
        for (BindingBuilder bindingBuilder : bindings) {
            bindingBuilder.integrateIn(model);
        }
        for (NodeInstanceBuilder nodeInstanceBuilder: nodeInstances) {
            nodeInstanceBuilder.integrateIn(model);
        }
        for (ArtefactInstanceBuilder artefactInstanceBuilder: artefactInstances) {
            artefactInstanceBuilder.integrateIn(model);
        }
        for (BindingInstanceBuilder bindingInstanceBuilder: bindingInstances) {
            bindingInstanceBuilder.integrateIn(model);
        }
        return model;
    }

  
}
