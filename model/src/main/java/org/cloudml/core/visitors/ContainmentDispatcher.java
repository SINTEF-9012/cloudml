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
/*
 */
package org.cloudml.core.visitors;

import org.cloudml.core.*;

public class ContainmentDispatcher implements Dispatcher {

    @Override
    public void dispatchTo(Visitor visitor, Deployment deployment) {
        for (Provider provider : deployment.getProviders()) {
            provider.accept(visitor);
        }
        for (Component component : deployment.getComponents()) {
            component.accept(visitor);
        }
        for (ComponentInstance<? extends Component> instance : deployment.getComponentInstances()) { 
            instance.accept(visitor); 
        }
        for (Relationship relationship : deployment.getRelationships()) {
            relationship.accept(visitor);
        }
        for (RelationshipInstance instance : deployment.getRelationshipInstances()) {
            instance.accept(visitor);
        }
        for(ExecuteInstance execute: deployment.getExecuteInstances()) {
            execute.accept(visitor);
        }
        for (Cloud cloud : deployment.getClouds()) {
            cloud.accept(visitor);
        }
    }

    @Override
    public void dispatchTo(Visitor visitor, Provider provider) {
        // Nothing to do: no elements further contained
    }

    @Override
    public void dispatchTo(Visitor visitor, ResourcePoolInstance resourcePoolInstance) {
        // Nothing to do: no elements further contained
    }

    @Override
    public void dispatchTo(Visitor visitor, Cloud cloud) {
        // Nothing to do: no elements further contained
    }

    @Override
    public void dispatchTo(Visitor visitor, InternalComponent internalComponent) {
        dispatchTo(visitor, (Component) internalComponent);
        for (RequiredPort port : internalComponent.getRequiredPorts()) {
            visitor.visitRequiredPort(port);
        }
        visitor.visitRequiredExecutionPlatform(internalComponent.getRequiredExecutionPlatform());
    }

    @Override
    public void dispatchTo(Visitor visitor, ExternalComponent externalComponent) {
        dispatchTo(visitor, (Component) externalComponent);
    }

    @Override
    public void dispatchTo(Visitor visitor, Component component) {
        for (ProvidedPort port : component.getProvidedPorts()) {
            visitor.visitProvidedPort(port); 
        }
        for (ProvidedExecutionPlatform platforms : component.getProvidedExecutionPlatforms()) {
            visitor.visitProvidedExecutionPlatform(platforms);
        }
    }

    @Override
    public void dispatchTo(Visitor visitor, VM vm) {
        dispatchTo(visitor, (ExternalComponent) vm);

    }

    @Override
    public void dispatchTo(Visitor visitor, ProvidedPort port) {
        // Nothing to do: no elements further contained
    }

    @Override
    public void dispatchTo(Visitor visitor, RequiredPort port) {
        // Nothing to do: no elements further contained
    }

    @Override
    public void dispatchTo(Visitor visitor, ProvidedExecutionPlatform executionPlatform) {
        // Nothing to do: no elements further contained
    }

    @Override
    public void dispatchTo(Visitor visitor, RequiredExecutionPlatform executionPlatform) {
        // Nothing to do: no elements further contained
    }

    @Override
    public void dispatchTo(Visitor visitor, InternalComponentInstance internalInstance) { 
        dispatchTo(visitor, (ComponentInstance<? extends Component>) internalInstance);
        for (RequiredPortInstance port : internalInstance.getRequiredPorts()) {
            visitor.visitRequiredPortInstance(port);
        }
        visitor.visitRequiredExecutionPlatformInstance(internalInstance.getRequiredExecutionPlatform());
    }

    @Override
    public void dispatchTo(Visitor visitor, ExternalComponentInstance<? extends ExternalComponent> externalInstance) {
        dispatchTo(visitor, (ComponentInstance<? extends Component>) externalInstance);
    }

    @Override
    public void dispatchTo(Visitor visitor, VMInstance vm) {
        dispatchTo(visitor, (ExternalComponentInstance<? extends ExternalComponent>) vm);
    }

    @Override
    public void dispatchTo(Visitor visitor, ComponentInstance<? extends Component> instance) {
        for (ProvidedPortInstance port : instance.getProvidedPorts()) {
            visitor.visitProvidedPortInstance(port);
        }
        for (ProvidedExecutionPlatformInstance platform : instance.getProvidedExecutionPlatforms()) {
            visitor.visitProvidedExecutionPlatformInstance(platform);
        }
    }

    @Override
    public void dispatchTo(Visitor visitor, Relationship relationship) {
        // Nothing to do: no elements further contained
    }

    @Override
    public void dispatchTo(Visitor visitor, RelationshipInstance relationshipInstance) {
        // Nothing to do: no elements further contained
    }

    @Override
    public void dispatchTo(Visitor visitor, ProvidedPortInstance port) {
        // Nothing to do: no elements further contained
    }

    @Override
    public void dispatchTo(Visitor visitor, RequiredPortInstance port) {
        // Nothing to do: no elements further contained
    }

    @Override
    public void dispatchTo(Visitor visitor, ProvidedExecutionPlatformInstance executionPlatform) {
        // Nothing to do: no elements further contained
    }

    @Override
    public void dispatchTo(Visitor visitor, RequiredExecutionPlatformInstance executionPlatform) {
        // Nothing to do: no elements further contained
    }

    @Override
    public void dispatchTo(Visitor visitor, ExecuteInstance execute) {
        // Nothing to do: no elements further contained
    }
}
