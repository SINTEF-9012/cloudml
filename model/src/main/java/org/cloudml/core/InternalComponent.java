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
import org.cloudml.core.collections.RequiredPortGroup;
import org.cloudml.core.validation.Report;
import org.cloudml.core.visitors.Visitor;

/*
 * InternalComponent describes the type of an InternalComponent instance. It
 * also contains communication channels and dependencies between Port Types
 */
public class InternalComponent extends Component {

    private RequiredExecutionPlatform requiredExecutionPlatform;
    private final LocalRequiredPortGroup requiredPorts;

    public InternalComponent(String name, RequiredExecutionPlatform requiredExecutionPlatform) {
        super(name);
        this.requiredPorts = new LocalRequiredPortGroup();
        setRequiredExecutionPlatform(requiredExecutionPlatform);
    }

    private void rejectIfInvalid(RequiredExecutionPlatform platform) {
        if (platform == null) {
            throw new IllegalArgumentException("'null' is not a valid required execution platform");
        }
    }

    @Override
    public void validate(Report report) {
        super.validate(report);
        if (hasNoPorts() && !isExecutionPlatform()) {
            final String warning = String.format("Internal component '%s' has no port (neither required or provided) and does not provide any execution platform!", getQualifiedName());
            report.addWarning(warning);
        }
    }

    @Override
    public final boolean isInternal() {
        return true;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitInternalComponent(this);
    }


    @Override
    public boolean equals(Object other) {
        if (other instanceof InternalComponent) {
            InternalComponent otherComp = (InternalComponent) other;
            return getName().equals(otherComp.getName());
        }
        else {
            return false;
        }
    }

    public InternalComponentInstance instantiate(String name) {
        return new InternalComponentInstance(name, this);
    }

    public InternalComponentInstance instantiate() {
        return new InternalComponentInstance(this);
    }

    /*
     * Getters & Setters
     */
    public RequiredPortGroup getRequiredPorts() {
        return this.requiredPorts;
    }

    public final RequiredExecutionPlatform getRequiredExecutionPlatform() {
        return requiredExecutionPlatform;
    }

    public final void setRequiredExecutionPlatform(RequiredExecutionPlatform requiredExecutionPlatform) {
        rejectIfInvalid(requiredExecutionPlatform);
        requiredExecutionPlatform.getOwner().set(this);
        this.requiredExecutionPlatform = requiredExecutionPlatform;
    }

    private boolean hasNoPorts() {
        return getRequiredPorts().isEmpty() && getProvidedPorts().isEmpty();
    }

    private class LocalRequiredPortGroup extends RequiredPortGroup {

        public LocalRequiredPortGroup() {
        }

        public LocalRequiredPortGroup(Collection<RequiredPort> content) {
            super(content);
        }

        @Override
        public boolean add(RequiredPort e) {
            e.getOwner().set(InternalComponent.this);
            return super.add(e);
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof RequiredPort) {
                RequiredPort port = (RequiredPort) o;
                port.getOwner().discard();
            }
            return super.remove(o); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void clear() {
            for (RequiredPort port : this) {
                port.getOwner().discard();
            }
            super.clear(); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
