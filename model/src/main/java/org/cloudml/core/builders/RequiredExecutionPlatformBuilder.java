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

import java.util.HashMap;
import org.cloudml.core.InternalComponent;
import org.cloudml.core.Property;
import org.cloudml.core.RequiredExecutionPlatform;

public class RequiredExecutionPlatformBuilder extends WithResourcesBuilder<RequiredExecutionPlatform, RequiredExecutionPlatformBuilder> implements SubPartBuilder<InternalComponent> {

    private final HashMap<String, String> demands;

    public RequiredExecutionPlatformBuilder() {
        demands = new HashMap<String, String>();
    }    
    
    @Override
    public RequiredExecutionPlatform build() {
        final RequiredExecutionPlatform platform = new RequiredExecutionPlatform(getName());
        prepare(platform);
        return platform; 
    }
    
    public RequiredExecutionPlatformBuilder demanding(String key, String value) {
        demands.put(key, value);
        return next();
    }

    @Override
    protected RequiredExecutionPlatformBuilder next() {
        return this;
    }

    @Override
    public void integrateIn(InternalComponent container) {
        final RequiredExecutionPlatform platform = new RequiredExecutionPlatform(getName());
        prepare(platform);
        platform.getOwner().set(container);
        container.setRequiredExecutionPlatform(platform);
    }

    private void setupDemands(final RequiredExecutionPlatform platform) {
        for(String key: demands.keySet()) {
            platform.getDemands().add(new Property(key, demands.get(key)));  
        }
    }

    private void prepare(final RequiredExecutionPlatform platform) {
        setupProperties(platform);
        setupResources(platform);
        setupDemands(platform);
    }
}
