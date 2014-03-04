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

import java.util.List;

/**
 * Created by Nicolas Ferry & Franck Chauvel on 03.03.14.
 */
public class ProvidedExecutionPlatformInstance extends ExecutionPlatformInstance<ProvidedExecutionPlatform> {

    public ProvidedExecutionPlatformInstance(){}

    public ProvidedExecutionPlatformInstance(String name, ProvidedExecutionPlatform type){
        super(name,type);
    }

    public ProvidedExecutionPlatformInstance(String name, List<Property> properties, ProvidedExecutionPlatform type){
        super(name, properties, type);
    }

    @Override
    public String toString() {
        return "ProvidedExecutionPlatformInstance " + name + " component:" + this.getOwner().getName();
    }

    @Override
    public boolean equals(Object other) {
        if(other == null)
            return false;

        if (other instanceof ProvidedExecutionPlatformInstance) {
            ProvidedExecutionPlatformInstance otherNode = (ProvidedExecutionPlatformInstance) other;
            return name.equals(otherNode.getName()) && this.getOwner().equals(otherNode.getOwner());
        } else {
            return false;
        }
    }

}
