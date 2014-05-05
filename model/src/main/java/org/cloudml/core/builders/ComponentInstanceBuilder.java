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

import org.cloudml.core.Component;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.Deployment;

public abstract class ComponentInstanceBuilder< T extends ComponentInstance<? extends Component>, N extends ComponentInstanceBuilder<?, ?>> extends WithResourcesBuilder<T, N> implements SubPartBuilder<Deployment>{
    
    private static final String DEFAULT_TYPE_NAME = "default type name";

    private String typeName;

    public ComponentInstanceBuilder() {
        typeName = DEFAULT_TYPE_NAME;
    }

    public N ofType(String typeName) {
        this.typeName = typeName;
        return next();
    }
    
    protected String getTypeName() {
        return typeName;
    } 

  
    
}
