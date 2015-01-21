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

import java.util.Collection;
import org.cloudml.core.collections.ComponentInstanceGroup;
import org.cloudml.core.collections.InternalComponentInstanceGroup;
import org.cloudml.core.util.OwnedBy;
import org.cloudml.core.collections.ProvidedExecutionPlatformInstanceGroup;
import org.cloudml.core.collections.ProvidedPortInstanceGroup;
import org.cloudml.core.validation.Report;

public abstract class ComponentInstance<T extends Component> extends WithResources implements DeploymentElement, OwnedBy<Deployment> {

    public static enum State {

        /**
         * The node is in transition.
         */
        PENDING("PENDING"),
        /**
         * The node is visible, and in the process of being deleted. Available
         * on jCLouds only.
         */
        TERMINATED("TERMINATED"),
        /**
         * The node is deployed, but suspended or stopped.
         */
        STOPPED("STOPPED"),
        /**
         * The node is available for requests
         */
        RUNNING("RUNNING"),
        /**
         * There is an error on the node
         */
        ERROR("ERROR"),
        /**
         * The state of the node is unrecognized.
         */
        UNRECOGNIZED("UNRECOGNIZED"),
        /**
         * This status is available only on Flexiant
         */
        RECOVERY("RECOVERY");

        private String name = "PENDING";

        private State(String s) {
            this.name = s;
        }

        public boolean equalsName(String otherName) {
            return (otherName == null) ? false : name.equals(otherName);
        }

        public String toString() {
            return name;
        }

    }
    private final OptionalOwner<Deployment> owner;
    private T type;
    private ProvidedPortInstanceGroup providedPorts;
    private ProvidedExecutionPlatformInstanceGroup providedExecutionPlatforms;

    public ComponentInstance(String name, T type) {
        super(name);
        this.owner = new OptionalOwner<Deployment>();
        setType(type);
        this.providedPorts = instantiateAllProvidedPorts(type);
        this.providedExecutionPlatforms = instantiateAllExecutionPlatforms(type);
    }

    private ProvidedPortInstanceGroup instantiateAllProvidedPorts(T type) {
        final ProvidedPortInstanceGroup instances = new ProvidedPortInstanceGroup();
        for (ProvidedPort port: type.getProvidedPorts()) {
            instances.add(port.instantiate());
        }
        return new LocalProvidedPortInstanceGroup(instances);
    }

    private LocalProvidedExecutionPlatformInstanceGroup instantiateAllExecutionPlatforms(T type) {
        final ProvidedExecutionPlatformInstanceGroup group = new ProvidedExecutionPlatformInstanceGroup();
        for (ProvidedExecutionPlatform platform: type.getProvidedExecutionPlatforms()) {
            group.add(platform.instantiate());
        }
        return new LocalProvidedExecutionPlatformInstanceGroup(group);
    }

    /**
     * @return true if this component instance can host other instance, (i.e.,
     * if it provide at least one execution platform), false otherwise.
     */
    public boolean canHost() {
        return !getProvidedExecutionPlatforms().isEmpty();
    }

    /**
     * @return true if this component instance can host the given component
     * (i.e., if it provides a relevant execution platform), false otherwise
     * @param componentType the component type that need an host
     */
    public boolean canHost(InternalComponent componentType) {
        return getProvidedExecutionPlatforms().firstMatchFor(componentType) != null;
    }

    public boolean isHosting(InternalComponentInstance component) {
        return hostedComponents().contains(component);
    }

    public InternalComponentInstanceGroup clientComponents() {
        if (getOwner().isUndefined()) {
            return new InternalComponentInstanceGroup();
        }
        return getDeployment().getRelationshipInstances().clientsOf(this);
    }

    public InternalComponentInstanceGroup hostedComponents() {
        if (getOwner().isUndefined()) {
            return new InternalComponentInstanceGroup();
        }
        return getDeployment().getExecuteInstances().componentsHostedBy(this);
    }

    @Override
    public void validate(Report report) {
        if (owner.isUndefined()) {
            final String error = String.format("Component instance '%s' has no owner", getQualifiedName());
            report.addError(error);
        }
    }

    public boolean canBeUninstalled() {
        return !isUsed() && isInternal();
    }

    public boolean isUsed() {
        return !hostedComponents().isEmpty() || !clientComponents().isEmpty();
    }

    public final boolean isInternal() {
        return type.isInternal();
    }

    @SuppressWarnings("unchecked")
    public final InternalComponentInstance asInternal() {
        if (isExternal()) {
            throw new IllegalStateException("Unable to convert an external component instance into an internal one");
        }
        return (InternalComponentInstance) this;
    }

    public final boolean isExternal() {
        return !isInternal();
    }

    @SuppressWarnings("unchecked")
    public final ExternalComponentInstance<ExternalComponent> asExternal() {
        if (isInternal()) {
            throw new IllegalStateException("Unable to convert an internal component instance into an external one");
        }
        return (ExternalComponentInstance<ExternalComponent>) this;
    }

    @Override
    public Deployment getDeployment() {
        return owner.get();
    }

    @Override
    public String getQualifiedName() {
        return getOwner().getName() + "::" + getName();
    }

    @Override
    public OptionalOwner<Deployment> getOwner() {
        return this.owner;
    }

    public ProvidedPortInstanceGroup getProvidedPorts() {
        return this.providedPorts;
    }

    public void setProvidedPorts(ProvidedPortInstanceGroup ppig) {
        this.providedPorts = ppig;
    }

    public T getType() {
        return this.type;
    }

    public final void setType(T type) {
        if (type == null) {
            throw new IllegalArgumentException("'null' is not a valid type for a component instance");
        }
        this.type = type;
    }

    public ProvidedExecutionPlatformInstanceGroup getProvidedExecutionPlatforms() {
        return providedExecutionPlatforms;
    }

    public void setProvidedExecutionPlatforms(ProvidedExecutionPlatformInstanceGroup pepig) {
        this.providedExecutionPlatforms = pepig;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Instance " + getName() + " : " + getType().getName());
        for(PortInstance p : this.getProvidedPorts())
            builder.append("-Provided port:"+p.getName());
        return builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (other instanceof ComponentInstance) {
            ComponentInstance otherCompInst = (ComponentInstance) other;
            Boolean match = getName().equals(otherCompInst.getName()) && type.equals(otherCompInst.getType());
            return match;
        } else {
            return false;
        }
    }

    private class LocalProvidedPortInstanceGroup extends ProvidedPortInstanceGroup {

        public LocalProvidedPortInstanceGroup(Collection<ProvidedPortInstance> content) {
            super();
            for (ProvidedPortInstance port: content) {
                super.add(port);
                port.getOwner().set(ComponentInstance.this);
            }
        }

        @Override
        public boolean add(ProvidedPortInstance e) {
            throw new UnsupportedOperationException("The provided ports of a component instance cannot be changed");
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException("The provided ports of a component instance cannot be changed");
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("The provided ports of a component instance cannot be changed");
        }
    }

    private class LocalProvidedExecutionPlatformInstanceGroup extends ProvidedExecutionPlatformInstanceGroup {

        public LocalProvidedExecutionPlatformInstanceGroup(Collection<ProvidedExecutionPlatformInstance> content) {
            super();
            for (ProvidedExecutionPlatformInstance platform: content) {
                super.add(platform);
                platform.getOwner().set(ComponentInstance.this);
            }
        }

        @Override
        public boolean add(ProvidedExecutionPlatformInstance e) {
            throw new UnsupportedOperationException("The provided execution platforms of a component instance cannot be changed");
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException("The provided execution platforms of a component instance cannot be changed");
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("The provided execution platforms of a component instance cannot be changed");
        }
    }
}
