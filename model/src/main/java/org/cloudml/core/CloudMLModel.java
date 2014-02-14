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

public class CloudMLModel extends CloudMLElementWithProperties {

    private Map<String, Component> components = new HashMap<String, Component>();
    private List<ComponentInstance> componentInstances = new LinkedList<ComponentInstance>();

    private Map<String, VM> vms = new HashMap<String, VM>();
    private List<VMInstance> VMInstances = new LinkedList<VMInstance>();
    
    private List<Provider> providers = new LinkedList<Provider>();

    private Map<String, Relationship> relationships = new HashMap<String, Relationship>();
    private List<RelationshipInstance> relationshipInstances = new LinkedList<RelationshipInstance>();

    private List<Cloud> clouds = new LinkedList<Cloud>();

    public CloudMLModel(){}

    public CloudMLModel(String name) {
        super(name);
    }
    
    public CloudMLModel(String name, List<Property> properties,
                        Map<String, Component> components, List<ComponentInstance> componentInstances,
                        Map<String, VM> vms, List<VMInstance> VMInstances, List<Provider> providers) {
        super(name, properties);
        this.components = components;
        this.componentInstances = componentInstances;
        this.VMInstances = VMInstances;
        this.vms = vms;
        this.providers = providers;
    }
    
    public CloudMLModel(String name, List<Property> properties,
                        Map<String, Component> components, List<ComponentInstance> componentInstances,
                        Map<String, VM> vms, List<VMInstance> VMInstances, List<Provider> providers, Map<String, Relationship> relationships, List<RelationshipInstance> relationshipInstances) {
        super(name, properties);
        this.components = components;
        this.componentInstances = componentInstances;
        this.VMInstances = VMInstances;
        this.vms = vms;
        this.providers = providers;
        this.relationshipInstances = relationshipInstances;
        this.relationships = relationships;
    }
    
    public List<ComponentInstance> getComponentInstances() {
        return componentInstances;
    }

    public  Map<String, Component> getComponents() {
        return components;
    }

    public void setComponentInstances(List<ComponentInstance> componentInstances) {
        this.componentInstances = componentInstances;
    }

    public void setComponents(Map<String, Component> components) {
        this.components = components;
    }

    public List<VMInstance> getVMInstances() {
        return VMInstances;
    }

    public  Map<String, VM> getVms() {
        return vms;
    }

    public void setVMInstances(List<VMInstance> VMInstances) {
        this.VMInstances = VMInstances;
    }

    public void setVms(Map<String, VM> vms) {
        this.vms = vms;
    }
    
    public List<Provider> getProviders() {
        return providers;
    }

    public void setProviders(List<Provider> providers) {
        this.providers = providers;
    }
    
    public void setRelationships(Map<String, Relationship> relationships) {
        this.relationships = relationships;
    }
    
    public Map<String, Relationship> getRelationships(){
    	return relationships;
    }
    
    public void setRelationshipInstances(List<RelationshipInstance> relationshipInstances){
    	this.relationshipInstances = relationshipInstances;
    }
    
    public List<RelationshipInstance> getRelationshipInstances(){
    	return relationshipInstances;
    }

    public List<Cloud> getClouds(){
        return clouds;
    }

    public void setClouds(List<Cloud> clouds){
        this.clouds=clouds;
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof CloudMLModel) {
            CloudMLModel otherDepModel = (CloudMLModel) other;
            return components.equals(otherDepModel.components) && componentInstances.equals(otherDepModel.componentInstances)
            		&& vms.equals(otherDepModel.vms) && VMInstances.equals(otherDepModel.VMInstances)
            		&& relationships.equals(otherDepModel.relationships) && relationshipInstances.equals(otherDepModel.relationshipInstances);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Deployment model " + name + "{\n");
        builder.append("- InternalComponent types: {\n");
        for(Component t : components.values()) {
            builder.append("  - " + t + "\n");
        }
        builder.append("}\n");
        builder.append("- Relationship types: {\n");
        for(Relationship b : relationships.values()) {
            builder.append("  - " + b + "\n");
        }
        builder.append("}\n");
        builder.append("- InternalComponent instances: {\n");
        for(ComponentInstance i : componentInstances) {
            builder.append("  - " + i + "\n");
        }
        builder.append("}\n");
        builder.append("- Relationship instances: {\n");
        for(RelationshipInstance b : relationshipInstances) {
            builder.append("  - " + b + "\n");
        }
        builder.append("}\n");
        builder.append("- VM types: {\n");
        for(VM nt : vms.values()) {
            builder.append("  - " + nt + "\n");
        }
        builder.append("}\n");
        builder.append("- VM instances: {\n");
        for(VMInstance ni : VMInstances) {
            builder.append("  - " + ni + "\n");
        }
        builder.append("}\n");
        return builder.toString();
    }
}
