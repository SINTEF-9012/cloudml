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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nicolas Ferry & Franck Chauvel on 03.03.14.
 */
public class ProvidedExecutionPlatform extends ExecutionPlatform{

    private final Map<String, Object> offers;

    public ProvidedExecutionPlatform(){
        offers =new HashMap<String, Object>();
    }

    public ProvidedExecutionPlatform(String name){
        super(name);
        offers =new HashMap<String, Object>();
    }

    public ProvidedExecutionPlatform(String name, List<Property> properties){
        super(name,properties);
        offers =new HashMap<String, Object>();
    }

    public ProvidedExecutionPlatform(String name, List<Property> properties, Component owner){
        super(name,properties, owner);
        offers =new HashMap<String, Object>();
    }

    public ProvidedExecutionPlatform(String name, List<Property> properties, Component owner, Map<String, Object> offers){
        super(name,properties, owner);
        this.offers =new  HashMap<String, Object>();
        this.offers.putAll(offers);
    }

    public Map<String, Object> getOffers(){
        return this.offers;
    }

    public void setOffers(Map<String, Object> offers){
        this.offers.clear();
        this.offers.putAll(offers);
    }

    public void addOffer(String key, Object value) {
        this.offers.put(key, value);
    }


    public ProvidedExecutionPlatformInstance instantiates(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Illegal: null name");
        }
        if (name.equals("")) {
            throw new IllegalArgumentException("Illegal: empty name");
        }
        return new ProvidedExecutionPlatformInstance(name, this);
    }

}
