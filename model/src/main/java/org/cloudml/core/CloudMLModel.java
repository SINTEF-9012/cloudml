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

    private Map<String, ExternalComponent> externalComponents = new HashMap<String, ExternalComponent>();
    private List<ExternalComponentInstance> externalComponentInstances = new LinkedList<ExternalComponentInstance>();
    
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
                        Map<String, ExternalComponent> externalComponents, List<ExternalComponentInstance> externalComponentInstances, List<Provider> providers) {
        super(name, properties);
        this.components = components;
        this.componentInstances = componentInstances;
        this.externalComponentInstances = externalComponentInstances;
        this.externalComponents = externalComponents;
        this.providers = providers;
    }
    
    public CloudMLModel(String name, List<Property> properties,
                        Map<String, Component> components, List<ComponentInstance> componentInstances,
                        Map<String, ExternalComponent> externalComponents, List<ExternalComponentInstance> externalComponentInstances, List<Provider> providers, Map<String, Relationship> relationships, List<RelationshipInstance> relationshipInstances) {
        super(name, properties);
        this.components = components;
        this.componentInstances = componentInstances;
        this.externalComponentInstances = externalComponentInstances;
        this.externalComponents = externalComponents;
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

    public List<ExternalComponentInstance> getExternalComponentInstances() {
        return externalComponentInstances;
    }

    public  Map<String, ExternalComponent> getExternalComponents() {
        return externalComponents;
    }

    public void setExternalComponentInstances(List<ExternalComponentInstance> externalComponentInstances) {
        this.externalComponentInstances = externalComponentInstances;
    }

    public void setExternalComponents(Map<String, ExternalComponent> externalComponents) {
        this.externalComponents = externalComponents;
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
            		&& externalComponents.equals(otherDepModel.externalComponents) && externalComponentInstances.equals(otherDepModel.externalComponentInstances)
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
        for(ExternalComponent nt : externalComponents.values()) {
            builder.append("  - " + nt + "\n");
        }
        builder.append("}\n");
        builder.append("- VM instances: {\n");
        for(ExternalComponentInstance ni : externalComponentInstances) {
            builder.append("  - " + ni + "\n");
        }
        builder.append("}\n");
        return builder.toString();
    }
}
