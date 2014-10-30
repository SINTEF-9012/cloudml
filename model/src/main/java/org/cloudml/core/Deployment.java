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

import org.cloudml.core.collections.CloudGroup;

import org.cloudml.core.collections.ComponentGroup;
import org.cloudml.core.collections.ComponentInstanceGroup;
import org.cloudml.core.collections.ExecuteInstanceGroup;
import org.cloudml.core.collections.InternalComponentGroup;
import org.cloudml.core.collections.ProviderGroup;
import org.cloudml.core.collections.RelationshipGroup;
import org.cloudml.core.collections.RelationshipInstanceGroup;
import org.cloudml.core.util.ModelUtils;
import org.cloudml.core.validation.CanBeValidated;
import org.cloudml.core.validation.Report;
import org.cloudml.core.visitors.Visitable;
import org.cloudml.core.visitors.Visitor;

import java.util.Random;

public class Deployment extends WithProperties implements Visitable, CanBeValidated {

    private final ProviderGroup providers;
    private final ComponentGroup components;
    private final ComponentInstanceGroup<ComponentInstance<? extends Component>> componentInstances;
    private final RelationshipGroup relationships;
    private final RelationshipInstanceGroup relationshipInstances;
    private final ExecuteInstanceGroup executeInstances;
    private final CloudGroup clouds;

    public Deployment() {
        this(DEFAULT_NAME);
    }

    public Deployment(String name) {
        super(name);
        this.providers = new LocalProviderGroup();
        this.components = new LocalComponentGroup();
        this.componentInstances = new LocalComponentInstanceGroup<ComponentInstance<? extends Component>>();
        this.relationships = new LocalRelationshipGroup();
        this.relationshipInstances = new LocalRelationshipInstanceGroup();
        this.executeInstances = new LocalExecuteInstanceGroup();
        this.clouds = new LocalCloudGroup();
    }

    public Deployment clone(){
        Deployment tmp=new Deployment();
        tmp.getComponents().addAll(this.getComponents());
        tmp.getComponentInstances().addAll(this.getComponentInstances());
        tmp.getExecuteInstances().addAll(this.getExecuteInstances());
        tmp.getProviders().addAll(this.getProviders());
        tmp.getRelationshipInstances().addAll(this.getRelationshipInstances());
        tmp.getClouds().addAll(this.getClouds());
        tmp.getRelationships().addAll(this.getRelationships());
        tmp.setName(this.getName());
        return tmp;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitDeployment(this);
    }

    @Override
    public void validate(Report report) {
        if (providers.isEmpty()
                && components.isEmpty()
                && componentInstances.isEmpty()
                && relationships.isEmpty()
                && relationshipInstances.isEmpty()) {
            report.addWarning("empty deployment model");
        }
    }

    public ProviderGroup getProviders() {
        return this.providers;
    }

    public ComponentGroup getComponents() {
        return components;
    }

    public InternalComponentGroup internalComponents() {
        return components.onlyInternals();
    }

    public ComponentInstanceGroup<ComponentInstance<? extends Component>> getComponentInstances() {
        return componentInstances;
    }
   
    public ExecuteInstanceGroup getExecuteInstances() {
        return this.executeInstances;
    }

    public RelationshipGroup getRelationships() {
        return relationships;
    }

    public RelationshipInstanceGroup getRelationshipInstances() {
        return relationshipInstances;
    }

    public CloudGroup getClouds() {
        return clouds;
    }

    public void deploy(InternalComponentInstance component, ComponentInstance<? extends Component> host) {
        final ProvidedExecutionPlatformInstance platform = host.getProvidedExecutionPlatforms().firstMatchFor(component);
        if (platform == null) {
            final String error = String.format(
                    "Unable to deploy! '%s' does not provide any execution platform that suits '%s' (candidates are: %s)",
                    host.getQualifiedName(),
                    component.getRequiredExecutionPlatform().getQualifiedName(),
                    host.getProvidedExecutionPlatforms().onlyNames().toString());
            throw new IllegalArgumentException(error);
        }

        final ExecuteInstance execute = new ExecuteInstance(ModelUtils.generateUniqueName("runOn"),component, platform);
        executeInstances.add(execute);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Deployment) {
            Deployment otherDepModel = (Deployment) other;
            return components.equals(otherDepModel.components) && componentInstances.equals(otherDepModel.componentInstances)
                    && relationships.equals(otherDepModel.relationships) && relationshipInstances.equals(otherDepModel.relationshipInstances);
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Deployment model " + getName() + "{\n");
        builder.append("- Component types: {\n");
        for (Component t : components) {
            builder.append("  - " + t + "\n");
        }
        builder.append("}\n");
        builder.append("- Relationship types: {\n");
        for (Relationship b : relationships) {
            builder.append("  - " + b + "\n");
        }
        builder.append("}\n");
        builder.append("- Component instances: {\n");
        for (ComponentInstance i : componentInstances) {
            builder.append("  - " + i + "\n");
        }
        builder.append("}\n");
        builder.append("- Relationship instances: {\n");
        for (RelationshipInstance b : relationshipInstances) {
            builder.append("  - " + b + "\n");
        }
        builder.append("}\n");
        return builder.toString();
    }

    private class LocalProviderGroup extends ProviderGroup {

        @Override
        public boolean add(Provider e) {
            if (firstNamed(e.getName()) != null) {
                final String error = String.format("Provider name must be unique! There is already a provider named '%s'", e.getName());
                throw new IllegalStateException(error);
            }
            e.getOwner().set(Deployment.this);
            return super.add(e);
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof Provider) {
                final Provider provider = (Provider) o;
                if (provider.isUsed()) {
                    final String error = String.format("Unable to remove provider '5s' as it is still in use", provider.getName());
                    throw new IllegalStateException(error);
                }
                provider.getOwner().discard();
            }
            return super.remove(o);
        }
    }

    private class LocalComponentGroup extends ComponentGroup {

        @Override
        public boolean add(Component component) {
            if (firstNamed(component.getName()) != null) {
                final String error = String.format("Component name must be unique! There is already a component  named '%s'.", component.getName());
                throw new IllegalStateException(error);
            }
            component.getOwner().set(Deployment.this);
            return super.add(component);
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof Component) {
                final Component component = (Component) o;
                if (component.isUsed()) {
                    final String error = String.format("Unable to remove the component '%s' as it is still in use!", component.getQualifiedName());
                    throw new IllegalStateException(error);
                }
                component.getOwner().discard();
            }
            return super.remove(o);
        }
    }

    private class LocalComponentInstanceGroup<T extends ComponentInstance<? extends Component>> extends ComponentInstanceGroup<T> {

        @Override
        public boolean add(T element) {
            element.getOwner().set(Deployment.this);
            if (firstNamed(element.getName()) != null) {
                final String error = String.format("Component instance name must be unique! There is already a component instance named '%s'.", element.getName());
                throw new IllegalStateException(error);
            }
            return super.add(element);
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof ComponentInstance) {
                final ComponentInstance<? extends Component> instance = (ComponentInstance<? extends Component>) o;
                instance.getOwner().discard();
            }
            return super.remove(o);
        }
    }

    private class LocalRelationshipGroup extends RelationshipGroup {

        @Override
        public boolean add(Relationship relationship) {
            relationship.getOwner().set(Deployment.this);
            if (firstNamed(relationship.getName()) != null) {
                final String error = String.format("Relationship name must be unique! There is already a relationhip named '%s'.", relationship.getName());
                throw new IllegalStateException(error);
            }
            return super.add(relationship);
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof Relationship) {
                ((Relationship) o).getOwner().discard();
            }
            return super.remove(o);
        }
    }

    private class LocalRelationshipInstanceGroup extends RelationshipInstanceGroup {

        @Override
        public boolean add(RelationshipInstance instance) {
            if (firstNamed(instance.getName()) != null) {
                final String error = String.format("Relationship instance name must be unique! There is already a relationhip instance named '%s'.", instance.getName());
                throw new IllegalStateException(error);
            }
            instance.getOwner().set(Deployment.this);
            return super.add(instance);
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof RelationshipInstance) {
                ((RelationshipInstance) o).getOwner().discard();
            }
            return super.remove(o);
        }
    }

    private class LocalExecuteInstanceGroup extends ExecuteInstanceGroup {

        @Override
        public boolean add(ExecuteInstance e) {
            e.getOwner().set(Deployment.this);
            return super.add(e);
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof ExecuteInstance) {
                ((ExecuteInstance) o).getOwner().discard();
            }
            return super.remove(o);
        }
    }

    private class LocalCloudGroup extends CloudGroup {

        @Override
        public boolean add(Cloud e) {
            e.getOwner().set(Deployment.this);
            return super.add(e);
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof Cloud) {
                ((Cloud) o).getOwner().discard();
            }
            return super.remove(o);
        }
    }
}
