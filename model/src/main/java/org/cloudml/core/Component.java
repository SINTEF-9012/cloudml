/**
 * This file is part of CloudML [ http://cloudml.org ]
 *
 * Copyright (C) 2012 - SINTEF ICT Contact: Franck Chauvel
 * <franck.chauvel@sintef.no>
 *
 * Module: root
 *
 * CloudML is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * CloudML is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CloudML. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.cloudml.core;

import java.util.Collection;
import org.cloudml.core.util.OwnedBy;
import java.util.LinkedList;
import java.util.List;
import org.cloudml.core.collections.ComponentInstanceGroup;
import org.cloudml.core.collections.ProvidedExecutionPlatformGroup;
import org.cloudml.core.collections.ProvidedPortGroup;
import org.cloudml.core.validation.Report;

public abstract class Component extends WithResources implements DeploymentElement, OwnedBy<Deployment> {

    private final OptionalOwner<Deployment> owner;
    private final LocalProvidedPortGroup providedPorts;
    private final LocalProvidedExecutionPlatformGroup providedExecutionPlatforms;

    public Component(String name) {
        this(name, new LinkedList<ProvidedPort>(), new LinkedList<ProvidedExecutionPlatform>());
    }

    public Component(String name, List<ProvidedPort> providedPorts, List<ProvidedExecutionPlatform> providedExecutionPlatforms) {
        super(name);
        this.owner = new OptionalOwner<Deployment>();
        this.providedPorts = new LocalProvidedPortGroup(providedPorts);
        this.providedExecutionPlatforms = new LocalProvidedExecutionPlatformGroup(providedExecutionPlatforms);
    }

    /**
     * @return true if this component type has the given provided port, false otherwise
     * @param port the port whose existence is to be asserted
     */
    public boolean canProvide(ProvidedPort port) {
        for (ProvidedPort eachPort: providedPorts) {
            if (eachPort.equals(port)) {
                return true;
            }
        }
        return false;
    }

    public boolean canHost(InternalComponent component) {
        return providedExecutionPlatforms.firstMatchFor(component) != null;
    }

    @Override
    public void validate(Report report) {
        if (owner.isUndefined()) {
            final String error = String.format("Component '%s' has no owner", getQualifiedName());
            report.addError(error);
        }
    }

    /**
     * @return true if this component provides at least one execution platform,
     * false otherwise.
     */
    public boolean isExecutionPlatform() {
        return !providedExecutionPlatforms.isEmpty();
    }

    public boolean isUsed() {
        return !instances().isEmpty();
    }

    public final ComponentInstanceGroup<ComponentInstance<? extends Component>> instances() {
        if (owner.isUndefined()) {
            return new ComponentInstanceGroup<ComponentInstance<? extends Component>>();
        }
        return getDeployment().getComponentInstances().ofType(getName());
    }

    public abstract boolean isInternal();

    public final InternalComponent asInternal() {
        if (isExternal()) {
            throw new IllegalStateException("Cannot cast an external component into an internal one!");
        }
        return (InternalComponent) this;
    }

    public final boolean isExternal() {
        return !isInternal();
    }

    public final ExternalComponent asExternal() {
        if (isInternal()) {
            throw new IllegalStateException("Cannot cast an internal component into an external one!");
        }
        return (ExternalComponent) this;
    }

    @Override
    public final Deployment getDeployment() {
        return getOwner().get();
    }

    @Override
    public final OptionalOwner<Deployment> getOwner() {
        return this.owner;
    }

    @Override
    public String getQualifiedName() {
        return owner.getName() + "::" + getName();
    }

    public ProvidedPortGroup getProvidedPorts() {
        return this.providedPorts;
    }

    public ProvidedExecutionPlatformGroup getProvidedExecutionPlatforms() {
        return this.providedExecutionPlatforms;
    }

    public void setProvidedExecutionPlatforms(List<ProvidedExecutionPlatform> providedExecutionPlatforms) {
        this.providedExecutionPlatforms.clear();
        this.providedExecutionPlatforms.addAll(providedExecutionPlatforms);
    }

    public void addProvidedExecutionPlatforms(ProvidedExecutionPlatform... platforms) {
        for (ProvidedExecutionPlatform platform: platforms) {
            unlessNewAndValidPlatform(platform);
            this.providedExecutionPlatforms.add(platform);
        }
    }

    private void unlessNewAndValidPlatform(ProvidedExecutionPlatform platform) {
        if (platform == null) {
            throw new IllegalArgumentException("The given provided executon platform is null");
        }
        if (platform.getName() == null) {
            throw new IllegalArgumentException("Illegal platform without name!");
        }
        if (getProvidedExecutionPlatformByName(platform.getName()) != null) {
            throw new IllegalArgumentException("Duplicated provided execution platform name: '" + platform.getName() + "'");
        }
    }

    public ProvidedExecutionPlatform getProvidedExecutionPlatformByName(String name) {
        for (ProvidedExecutionPlatform platform: providedExecutionPlatforms) {
            if (platform.getName().equals(name)) {
                return platform;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object that) {
        if (that == null) {
            return false;
        }
        if (that instanceof Component) {
            Component other = (Component) that;
            if (other.getOwner().isDefined() && getOwner().isDefined()) {
                return other.getOwner().get().equals(getOwner().get()) && other.isNamed(getName());
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + (this.owner.isDefined() ? this.owner.get().hashCode() : 0);
        hash = 31 * hash + (this.providedPorts != null ? this.providedPorts.hashCode() : 0);
        hash = 31 * hash + (this.providedExecutionPlatforms != null ? this.providedExecutionPlatforms.hashCode() : 0);
        return hash;
    }

    private class LocalProvidedPortGroup extends ProvidedPortGroup {

        public LocalProvidedPortGroup() {
        }

        public LocalProvidedPortGroup(Collection<ProvidedPort> content) {
            super(content);
        }

        @Override
        public boolean add(ProvidedPort e) {
            e.getOwner().set(Component.this);
            return super.add(e);
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof ProvidedPort) {
                ProvidedPort port = (ProvidedPort) o;
                port.getOwner().discard();
            }
            return super.remove(o);
        }

        @Override
        public void clear() {
            for (ProvidedPort port: this) {
                port.getOwner().discard();
            }
            super.clear();
        }
    }

    private class LocalProvidedExecutionPlatformGroup extends ProvidedExecutionPlatformGroup {

        public LocalProvidedExecutionPlatformGroup() {
        }

        public LocalProvidedExecutionPlatformGroup(Collection<ProvidedExecutionPlatform> content) {
            super(content);
        }

        @Override
        public boolean add(ProvidedExecutionPlatform e) {
            e.getOwner().set(Component.this);
            return super.add(e);
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof ProvidedExecutionPlatform) {
                ProvidedExecutionPlatform platform = (ProvidedExecutionPlatform) o;
                platform.getOwner().discard();
            }
            return super.remove(o);
        }

        @Override
        public void clear() {
            for (ProvidedExecutionPlatform platform: this) {
                platform.getOwner().discard();
            }
            super.clear();
        }

    }
}
