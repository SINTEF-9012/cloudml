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
public abstract class ExecutionPlatformInstance<T extends ExecutionPlatform> extends CloudMLElementWithProperties {

    private T type;
    private ComponentInstance owner;


    public ExecutionPlatformInstance(){}

    public ExecutionPlatformInstance(String name, T type){
        super(name);
        this.type=type;
    }

    public ExecutionPlatformInstance(String name, List<Property> properties, T type){
        super(name, properties);
        this.type=type;
    }

    public T getType(){
        return this.type;
    }

    public void setType(T type){
        this.type=type;
    }

    public ComponentInstance getOwner(){
        return this.owner;
    }

    public void setOwner(ComponentInstance owner){
        this.owner=owner;
    }

}
