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

public interface Dispatcher {

    void dispatchTo(Visitor visitor, Deployment deployment);

    void dispatchTo(Visitor visitor, Provider provider);

    void dispatchTo(Visitor visitor, ResourcePoolInstance resourcePoolInstance);

    void dispatchTo(Visitor visitor, Cloud cloud);

    void dispatchTo(Visitor visitor, Component instance); 
    
    void dispatchTo(Visitor visitor, InternalComponent internalComponent);

    void dispatchTo(Visitor visitor, ExternalComponent internalComponent);

    void dispatchTo(Visitor visitor, VM vm);

    void dispatchTo(Visitor visitor, ProvidedPort port);

    void dispatchTo(Visitor visitor, RequiredPort port);

    void dispatchTo(Visitor visitor, ProvidedExecutionPlatform executionPlatform);

    void dispatchTo(Visitor visitor, RequiredExecutionPlatform executionPlatform);

    void dispatchTo(Visitor visitor, ComponentInstance<? extends Component> instance); 
    
    void dispatchTo(Visitor visitor, InternalComponentInstance internalInstance);

    void dispatchTo(Visitor visitor, ExternalComponentInstance<? extends ExternalComponent> externalInstance);
    
    void dispatchTo(Visitor visitor, VMInstance vm);

    void dispatchTo(Visitor visitor, Relationship relationship);

    void dispatchTo(Visitor visitor, RelationshipInstance relationshipInstance);

    void dispatchTo(Visitor visitor, ProvidedPortInstance port);

    void dispatchTo(Visitor visitor, RequiredPortInstance port);

    void dispatchTo(Visitor visitor, ProvidedExecutionPlatformInstance executionPlatform);

    void dispatchTo(Visitor visitor, RequiredExecutionPlatformInstance executionPlatform);

    void dispatchTo(Visitor visitor, ExecuteInstance execute);
}
