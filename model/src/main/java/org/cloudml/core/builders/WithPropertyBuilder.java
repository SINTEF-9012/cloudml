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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.cloudml.core.Property;
import org.cloudml.core.WithProperties;


public abstract class WithPropertyBuilder<T extends WithProperties, N extends WithPropertyBuilder<?, ?>> extends NamedElementBuilder<T, N> { 
    
    private final HashMap<String, String> properties;
       
    
    protected Map<String, String> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    protected void prepare(WithProperties underConstruction) {
        setupProperties(underConstruction);
    }
   
            
    public WithPropertyBuilder() {
        super();
        this.properties = new HashMap<String, String>();
    }
    
   
    public N withProperty(String key, String value) {
        this.properties.put(key, value);
        return next();
    }

    protected void setupProperties(WithProperties underConstruction) {
        for(Map.Entry<String, String> entry: properties.entrySet()) {
           underConstruction.getProperties().add(new Property(entry.getKey(), entry.getValue()));
       }
    }
        
}
