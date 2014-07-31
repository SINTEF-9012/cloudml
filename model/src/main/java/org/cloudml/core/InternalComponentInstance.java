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
import org.cloudml.core.collections.RequiredPortInstanceGroup;
import org.cloudml.core.visitors.Visitor;

/*
 * An instance of artefact is an elementary unit to be deployed on a single
 * node. It contains properties, communications channels and dependencies.
 */
public class InternalComponentInstance extends ComponentInstance<InternalComponent> {

    public static enum State {

        UNINSTALLED,
        INSTALLED,
        CONFIGURED,
        RUNNING,
        ERROR,
    }
    private RequiredPortInstanceGroup requiredPortInstances;
    private RequiredExecutionPlatformInstance requiredExecutionPlatformInstance;
    protected State status;

    public InternalComponentInstance(InternalComponent type) {
        this(NamedElement.DEFAULT_NAME, type);
    }

    public InternalComponentInstance(String name, InternalComponent type) {
        super(name, type);
        requiredPortInstances = instantiateAllRequiredPorts(type);
        requiredExecutionPlatformInstance = type.getRequiredExecutionPlatform().instantiate();
        requiredExecutionPlatformInstance.getOwner().set(this);
    }

    private LocalRequiredPortInstanceGroup instantiateAllRequiredPorts(InternalComponent type) {
        final RequiredPortInstanceGroup instances = new RequiredPortInstanceGroup();
        for (RequiredPort port : type.getRequiredPorts()) {
            final RequiredPortInstance instance = port.instantiate();
            instance.getOwner().set(this);
            instances.add(instance);
        }
        return new LocalRequiredPortInstanceGroup(instances);
    }

    public RequiredPortInstanceGroup getRequiredPorts() {
        return this.requiredPortInstances;
    }

    public void setRequiredPorts(RequiredPortInstanceGroup rpig){
        this.requiredPortInstances=rpig;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitInternalComponentInstance(this);
    }

    public ComponentInstance<? extends Component> getHost() {
        if (getOwner().isUndefined()) {
            return null;
        }
        return getDeployment().getExecuteInstances().hostOf(this);
    }

    public boolean isHostedBy(ComponentInstance<? extends Component> host) {
        if (getOwner().isUndefined()) {
            return false;
        }
        return getHost().equals(host);
    }

    /**
     * @return the external component at the bottom of the underlying software
     * stack. For instance, if an application, is running on the top of a war
     * container, itself running on linux virtual machine, externalHost will
     * return the linux VM.
     */
    public ExternalComponentInstance<? extends ExternalComponent> externalHost() {
        final ComponentInstance<? extends Component> directHost = getHost();
        if (directHost.isInternal()) {
            return directHost.asInternal().externalHost(); 
        }
        return directHost.asExternal();
    }

    public State getStatus() {
        return this.status;
    }

    public void setStatus(State s) {
        this.status = s;
    }

    public void setStatus(String s) {
        this.status = State.valueOf(s);
    }

    public RequiredExecutionPlatformInstance getRequiredExecutionPlatform() {
        return requiredExecutionPlatformInstance;
    }

    public void setRequiredExecutionPlatform(RequiredExecutionPlatformInstance platform) {
        this.requiredExecutionPlatformInstance = rejectIfInvalid(platform);
    }

    private RequiredExecutionPlatformInstance rejectIfInvalid(RequiredExecutionPlatformInstance platform) {
        if (platform == null) {
            final String error = String.format("Error in internal component instance '%s'! ('null' cannot be the required execution platform)", getQualifiedName());
            throw new IllegalArgumentException(error);
        }
        if (!platform.getType().equals(this.getType().getRequiredExecutionPlatform())) {
            final String error = String.format("Error in internal component instance '%s'! Required execution platform has a wrong type (expected: '%s' but found '%s')", getQualifiedName(), getType().getRequiredExecutionPlatform().getQualifiedName(), platform.getType().getQualifiedName());
            throw new IllegalArgumentException(error);
        }
        return platform;
    }

    @Override
    public String toString() {
        return "Instance " + getName() + " : " + getType().getName();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof InternalComponentInstance) {
            InternalComponentInstance otherCompInst = (InternalComponentInstance) other;
            Boolean match = getName().equals(otherCompInst.getName()) && getType().equals(otherCompInst.getType());
            if (requiredExecutionPlatformInstance != null) {
                return getName().equals(otherCompInst.getName()) && getType().equals(otherCompInst.getType()) && requiredExecutionPlatformInstance.equals(otherCompInst.getRequiredExecutionPlatform());
            }
            else {
                return match && (otherCompInst.getRequiredExecutionPlatform() == null);
            }
        }
        else {
            return false;
        }
    }

    private class LocalRequiredPortInstanceGroup extends RequiredPortInstanceGroup {

        public LocalRequiredPortInstanceGroup(Collection<RequiredPortInstance> content) {
            super();
            for (RequiredPortInstance instance : content) {
                super.add(instance);
                instance.getOwner().set(InternalComponentInstance.this);
            }
        }

        @Override
        public boolean add(RequiredPortInstance e) {
            throw new UnsupportedOperationException("Required ports of an internal component instance cannot be changed");
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException("Required ports of an internal component instance cannot be changed");
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("Required ports of an internal component instance cannot be changed");
        }
    }
}
