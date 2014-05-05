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

import org.cloudml.core.validation.Report;
import org.cloudml.core.visitors.Visitor;

public class RequiredExecutionPlatformInstance extends ExecutionPlatformInstance<RequiredExecutionPlatform> {

    public RequiredExecutionPlatformInstance(String name, RequiredExecutionPlatform type) { 
        super(name, type);
    }

    @Override
    public void accept(Visitor visitor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    } 

    @Override
    public String toString() {
        return "RequiredExecutionPlatformInstance " + getQualifiedName();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (other instanceof RequiredExecutionPlatformInstance) {
            RequiredExecutionPlatformInstance otherNode = (RequiredExecutionPlatformInstance) other;
            return getName().equals(otherNode.getName()) && this.getOwner().equals(otherNode.getOwner());
        }
        else {
            return false;
        }
    }
}
