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
import org.cloudml.core.Component;
import org.cloudml.core.Property;
import org.cloudml.core.ProvidedExecutionPlatform;


public class ProvidedExecutionPlatformBuilder extends WithResourcesBuilder<ProvidedExecutionPlatform, ProvidedExecutionPlatformBuilder> implements SubPartBuilder<Component> {

    private final HashMap<String, String> offers;

    
    public ProvidedExecutionPlatformBuilder() {
        offers = new HashMap<String, String>();
    }
      
    
    @Override
    public ProvidedExecutionPlatform build() {
        ProvidedExecutionPlatform platform = new ProvidedExecutionPlatform(getName());
        super.prepare(platform);
        setupOffers(platform);
        return platform;
    }
    
    public ProvidedExecutionPlatformBuilder offering(String key, String value) {
        offers.put(key, value);
        return next();
    }

    @Override
    public ProvidedExecutionPlatformBuilder next() {
        return this;
    }

    @Override
    public void integrateIn(Component container) {
        ProvidedExecutionPlatform platform = new ProvidedExecutionPlatform(getName());
        prepare(platform);
        setupOffers(platform);
        platform.getOwner().set(container);
        container.getProvidedExecutionPlatforms().add(platform);
    }

    private void setupOffers(ProvidedExecutionPlatform platform) {
        for(String key: offers.keySet()) {
            platform.getOffers().add(new Property(key, offers.get(key)));
        }
    }
   
    
    
    
}
