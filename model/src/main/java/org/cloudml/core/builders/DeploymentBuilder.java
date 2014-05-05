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
import org.cloudml.core.Component;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.Deployment;

/**
 * Simplify the construction ofType CloudML deployment models
 */
public class DeploymentBuilder extends WithPropertyBuilder<Deployment, DeploymentBuilder> {

    private final ArrayList<ProviderBuilder> providers;
    private final ArrayList<ComponentBuilder<? extends Component, ? extends ComponentBuilder<?, ?>>> components;
    private final ArrayList<RelationshipBuilder> relationships;
    private final ArrayList<ComponentInstanceBuilder<? extends ComponentInstance<? extends Component>, ? extends ComponentInstanceBuilder<?,?>>> componentInstances;
    private final ArrayList<RelationshipInstanceBuilder> relationshipInstances;
    
    
    public DeploymentBuilder() {
        this.providers = new ArrayList<ProviderBuilder>();
        this.components = new ArrayList<ComponentBuilder<? extends Component, ? extends ComponentBuilder<?, ?>>>();
        this.relationships = new ArrayList<RelationshipBuilder>();
        this.componentInstances = new ArrayList<ComponentInstanceBuilder<? extends ComponentInstance<? extends Component>, ? extends ComponentInstanceBuilder<?,?>>>();
        this.relationshipInstances = new ArrayList<RelationshipInstanceBuilder>();
    }

    public DeploymentBuilder with(ProviderBuilder aProvider) {
        this.providers.add(aProvider);
        return next();
    }

    public DeploymentBuilder with(ComponentBuilder<? extends Component, ? extends ComponentBuilder<?, ?>> builder) {
        this.components.add(builder);
        return next();
    }
    
    public DeploymentBuilder with(RelationshipBuilder relationship) {
        relationships.add(relationship);
        return next();
    }
    
    public DeploymentBuilder with(ComponentInstanceBuilder<? extends ComponentInstance<? extends Component>, ? extends ComponentInstanceBuilder<?,?>> component) {
        componentInstances.add(component);
        return next();
    }
    
    public DeploymentBuilder with(RelationshipInstanceBuilder relationshipInstance) {
        this.relationshipInstances.add(relationshipInstance); 
        return next();
    }
    
    @Override
    public Deployment build() {
        Deployment model = new Deployment(getName());
        super.prepare(model);
        for(ProviderBuilder provider: providers) {
            model.getProviders().add(provider.build());
        }
        for (ComponentBuilder<? extends Component, ? extends ComponentBuilder<?, ?>> builder : components) {
           builder.integrateIn(model); 
        }
        for(ComponentInstanceBuilder<? extends ComponentInstance<? extends Component>, ? extends ComponentInstanceBuilder<?,?>> builder: componentInstances) {
            builder.integrateIn(model);
        }
        for (RelationshipBuilder relationship: relationships) {
           relationship.integrateIn(model);
        }
        for (RelationshipInstanceBuilder builder: relationshipInstances) {
            builder.integrateIn(model);
        }
        return model;
    }

    @Override
    protected DeploymentBuilder next() {
        return this;
    }
    
    
}
