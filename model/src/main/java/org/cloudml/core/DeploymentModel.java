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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DeploymentModel extends WithProperties {

    private Map<String, Artefact> artefactTypes = new HashMap<String, Artefact>();
    private List<ArtefactInstance> artefactInstances = new LinkedList<ArtefactInstance>();

    private Map<String, Node> nodeTypes = new HashMap<String, Node>();
    private List<NodeInstance> nodeInstances = new LinkedList<NodeInstance>();
    
    private List<Provider> providers = new LinkedList<Provider>();

    private Map<String, Binding> bindingTypes = new HashMap<String, Binding>();
    private List<BindingInstance> bindingInstances = new LinkedList<BindingInstance>();
    
    public DeploymentModel(){}
    
    public DeploymentModel(String name) {
        super(name);
    }
    
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
    
    public DeploymentModel(String name, List<Property> properties,
    		Map<String, Artefact> artefactTypes, List<ArtefactInstance> artefactInstances,
    		Map<String, Node> nodeTypes, List<NodeInstance> nodeInstances, List<Provider> providers, Map<String, Binding> bindingTypes, List<BindingInstance> bindingInstances) {
        super(name, properties);
        this.artefactTypes = artefactTypes;
        this.artefactInstances = artefactInstances;
        this.nodeInstances = nodeInstances;
        this.nodeTypes = nodeTypes;
        this.providers = providers;
        this.bindingInstances=bindingInstances;
        this.bindingTypes=bindingTypes;
    }
    
    public List<ArtefactInstance> getArtefactInstances() {
        return artefactInstances;
    }

    public  Map<String, Artefact> getArtefactTypes() {
        return artefactTypes;
    }

    public void setArtefactInstances(List<ArtefactInstance> artefactInstances) {
        this.artefactInstances = artefactInstances;
    }

    public void setArtefactTypes( Map<String, Artefact> artefactTypes) {
        this.artefactTypes = artefactTypes;
    }

    public List<NodeInstance> getNodeInstances() {
        return nodeInstances;
    }

    public  Map<String, Node> getNodeTypes() {
        return nodeTypes;
    }

    public void setNodeInstances(List<NodeInstance> nodeInstances) {
        this.nodeInstances = nodeInstances;
    }

    public void setNodeTypes( Map<String, Node> nodeTypes) {
        this.nodeTypes = nodeTypes;
    }
    
    public List<Provider> getProviders() {
        return providers;
    }

    public void setProviders(List<Provider> providers) {
        this.providers = providers;
    }
    
    public void setBindingTypes( Map<String, Binding> bindingTypes) {
        this.bindingTypes = bindingTypes;
    }
    
    public Map<String, Binding> getBindingTypes(){
    	return bindingTypes;
    }
    
    public void setBindingInstances(List<BindingInstance> bindingInstances){
    	this.bindingInstances=bindingInstances;
    }
    
    public List<BindingInstance> getBindingInstances(){
    	return bindingInstances;
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof DeploymentModel) {
            DeploymentModel otherDepModel = (DeploymentModel) other;
            return artefactTypes.equals(otherDepModel.artefactTypes) && artefactInstances.equals(otherDepModel.artefactInstances) 
            		&& nodeTypes.equals(otherDepModel.nodeTypes) && nodeInstances.equals(otherDepModel.nodeInstances) 
            		&& bindingTypes.equals(otherDepModel.bindingTypes) && bindingInstances.equals(otherDepModel.bindingInstances);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Deployment model " + name + "{\n");
        builder.append("- Artefact types: {\n");
        for(Artefact t : artefactTypes.values()) {
            builder.append("  - " + t + "\n");
        }
        builder.append("}\n");
        builder.append("- Binding types: {\n");
        for(Binding b : bindingTypes.values()) {
            builder.append("  - " + b + "\n");
        }
        builder.append("}\n");
        builder.append("- Artefact instances: {\n");
        for(ArtefactInstance i : artefactInstances) {
            builder.append("  - " + i + "\n");
        }
        builder.append("}\n");
        builder.append("- Binding instances: {\n");
        for(BindingInstance b : bindingInstances) {
            builder.append("  - " + b + "\n");
        }
        builder.append("}\n");
        builder.append("- Node types: {\n");
        for(Node nt : nodeTypes.values()) {
            builder.append("  - " + nt + "\n");
        }
        builder.append("}\n");
        builder.append("- Node instances: {\n");
        for(NodeInstance ni : nodeInstances) {
            builder.append("  - " + ni + "\n");
        }
        builder.append("}\n");
        return builder.toString();
    }
}
