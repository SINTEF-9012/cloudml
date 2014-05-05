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

import java.util.*;
import org.cloudml.core.collections.PropertyGroup;
import org.cloudml.core.visitors.Visitor;

public class RequiredExecutionPlatform extends ExecutionPlatform {

    public static final Collection<Property> NO_DEMAND = new LinkedList<Property>();
    
    private final PropertyGroup demands;

    
    public RequiredExecutionPlatform(String name) {
        this(name, NO_DEMAND); 
    }

    public RequiredExecutionPlatform(String name, Collection<Property> demands) {
        super(name);
        this.demands = new PropertyGroup(demands);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitRequiredExecutionPlatform(this);
    }

    public PropertyGroup getDemands() {
        return this.demands;
    }

    @Override 
    public RequiredExecutionPlatformInstance instantiate() {
        return new RequiredExecutionPlatformInstance(getName(), this);
    }
}
