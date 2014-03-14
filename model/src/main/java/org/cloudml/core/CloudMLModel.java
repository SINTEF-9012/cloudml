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

import java.util.*;

public class CloudMLModel extends CloudMLElementWithProperties {

    private Map<String, Component> components = new HashMap<String, Component>();
    private List<ComponentInstance> componentInstances = new LinkedList<ComponentInstance>();
    
    private List<Provider> providers = new LinkedList<Provider>();

    private Map<String, Relationship> relationships = new HashMap<String, Relationship>();
    private List<RelationshipInstance> relationshipInstances = new LinkedList<RelationshipInstance>();

    private List<ExecuteInstance> executeInstances= new LinkedList<ExecuteInstance>();

    private List<Cloud> clouds = new LinkedList<Cloud>();

    public CloudMLModel(){}

    public CloudMLModel(String name) {
        super(name);
    }
    
    public CloudMLModel(String name, List<Property> properties,
                        Map<String, Component> components, List<ComponentInstance> componentInstances, List<Provider> providers) {
        super(name, properties);
        this.components = components;
        this.componentInstances = componentInstances;
        this.providers = providers;
    }
    
    public CloudMLModel(String name, List<Property> properties,
                        Map<String, Component> components, List<ComponentInstance> componentInstances,
                        List<Provider> providers, Map<String, Relationship> relationships, List<RelationshipInstance> relationshipInstances) {
        super(name, properties);
        this.components = components;
        this.componentInstances = componentInstances;
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

    /**
     * filter from the list of Components the sub-list of External component
     * @return a locked view on the sub-list
     */
    //Note: cannot be merged into a generic method due to a type erasure
    public List<ExternalComponent> getExternalComponents(){
        List<ExternalComponent> result=new ArrayList<ExternalComponent>();
        for(Component c: this.components.values()){
            if(c instanceof ExternalComponent){
                result.add((ExternalComponent)c);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     *filter from the list of Components the sub-list of External component instances
     * @return a locked view on the sub-list
     */
    public List<ExternalComponentInstance> getExternalComponentInstances(){
        List<ExternalComponentInstance> result=new ArrayList<ExternalComponentInstance>();
        for(ComponentInstance c: this.componentInstances){
            if(c instanceof ExternalComponentInstance){
                result.add((ExternalComponentInstance)c);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     *filter from the list of Components the sub-list of Internal component
     * @return  a locked view on the sub-list
     */
    public List<InternalComponent> getInternalComponents(){
        List<InternalComponent> result=new ArrayList<InternalComponent>();
        for(Component c: this.components.values()){
            if(c instanceof InternalComponent){
                result.add((InternalComponent)c);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     *filter from the list of Components the sub-list of Internal component instances
     * @return a locked view on the sub-list
     */
    public List<InternalComponentInstance> getInternalComponentInstances(){
        List<InternalComponentInstance> result=new ArrayList<InternalComponentInstance>();
        for(ComponentInstance c: this.componentInstances){
            if(c instanceof InternalComponentInstance){
                result.add((InternalComponentInstance)c);
            }
        }
        return Collections.unmodifiableList(result);
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

    public List<ExecuteInstance> getExecuteInstances(){
        return this.executeInstances;
    }

    public void setExecuteInstances(List<ExecuteInstance> executeInstances){
        this.executeInstances=executeInstances;
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof CloudMLModel) {
            CloudMLModel otherDepModel = (CloudMLModel) other;
            return components.equals(otherDepModel.components) && componentInstances.equals(otherDepModel.componentInstances)
            		&& relationships.equals(otherDepModel.relationships) && relationshipInstances.equals(otherDepModel.relationshipInstances);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Deployment model " + name + "{\n");
        builder.append("- Component types: {\n");
        for(Component t : components.values()) {
            builder.append("  - " + t + "\n");
        }
        builder.append("}\n");
        builder.append("- Relationship types: {\n");
        for(Relationship b : relationships.values()) {
            builder.append("  - " + b + "\n");
        }
        builder.append("}\n");
        builder.append("- Component instances: {\n");
        for(ComponentInstance i : componentInstances) {
            builder.append("  - " + i + "\n");
        }
        builder.append("}\n");
        builder.append("- Relationship instances: {\n");
        for(RelationshipInstance b : relationshipInstances) {
            builder.append("  - " + b + "\n");
        }
        builder.append("}\n");
        return builder.toString();
    }
}
