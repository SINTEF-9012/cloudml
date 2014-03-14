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

/**
 * Created by Nicolas Ferry & Franck Chauvel on 03.03.14.
 */
public class RequiredExecutionPlatform extends ExecutionPlatform{

    //TODO: provide abstract class
    private final ArrayList<Property> demands;

    public RequiredExecutionPlatform(){
        demands =new ArrayList<Property>();
    }

    public RequiredExecutionPlatform(String name){
        super(name);
        demands =new ArrayList<Property>();
    }

    public RequiredExecutionPlatform(String name, List<Property> properties){
        super(name,properties);
        demands =new ArrayList<Property>();
    }

    public RequiredExecutionPlatform(String name, List<Property> properties, Component owner){
        super(name,properties, owner);
        demands = new ArrayList<Property>();
    }

    public RequiredExecutionPlatform(String name, List<Property> properties, Component owner, Collection<Property> demands){
        super(name, properties, owner);
        this.demands = new ArrayList<Property>();
        this.demands.addAll(demands);
    }


    public List<Property> getDemands(){
        return this.demands;
    }

    public void setDemands(Collection<Property> demands){
        this.demands.clear();
        this.demands.addAll(demands);
    }

    public void addDemand(String key, String value) {
        unlessNewKey(key);
        this.demands.add(new Property(key, value));
    }

    private void unlessNewKey(String key){
        for(Property p : this.demands){
            if(p.getName().equals(key))
                throw new IllegalArgumentException("Duplicated key for demand (" +key+ ")");
        }
    }

    public RequiredExecutionPlatformInstance instantiates(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Illegal: null name");
        }
        if (name.equals("")) {
            throw new IllegalArgumentException("Illegal: empty name");
        }
        return new RequiredExecutionPlatformInstance(name, this);
    }

}
