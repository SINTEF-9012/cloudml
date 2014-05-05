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

package org.cloudml.core.builders;

import java.util.ArrayList;
import org.cloudml.core.Deployment;
import org.cloudml.core.InternalComponent;
import org.cloudml.core.RequiredExecutionPlatform;


public class InternalComponentBuilder extends ComponentBuilder<InternalComponent, InternalComponentBuilder> { 

    private final ArrayList<RequiredPortBuilder> requiredPorts;
    private RequiredExecutionPlatformBuilder requiredExecutionPlatform;
 
    public InternalComponentBuilder() {
        requiredExecutionPlatform = new RequiredExecutionPlatformBuilder();
        requiredPorts = new ArrayList<RequiredPortBuilder>();
    }
        
    public InternalComponentBuilder with(RequiredPortBuilder requiredPort) {
        requiredPorts.add(requiredPort);
        return next();
    }
    
    public InternalComponentBuilder with(RequiredExecutionPlatformBuilder executionPlatform) {
        requiredExecutionPlatform = executionPlatform;
        return next();
    }
    
    @Override
    public InternalComponent build() {
        final RequiredExecutionPlatform platform = requiredExecutionPlatform.build();
        final InternalComponent component = new InternalComponent(getName(), platform);
        super.prepare(component);
        for(RequiredPortBuilder port: requiredPorts) {
            port.integrateIn(component);
        }
        
        return component;
    }

    @Override
    protected InternalComponentBuilder next() {
        return this;
    }

    @Override
    public void integrateIn(Deployment container) {
        final InternalComponent component = build();
        container.getComponents().add(component);
    }

    
    
}
