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

import org.cloudml.core.collections.PropertyGroup;

public abstract class WithProperties extends NamedElement {
    
    private final PropertyGroup properties;

    public WithProperties() {
        this(DEFAULT_NAME);
    }

    public WithProperties(String name) {
        super(name);
        properties = new PropertyGroup();
    }

    public PropertyGroup getProperties() {
        return this.properties; 
    }
   
    
    public boolean hasProperty(String propertyName) {
        return this.properties.isDefined(propertyName);
    }
    
    public boolean hasProperty(String key, String value) {
        return this.properties.contains(key, value);
    } 

  
}
