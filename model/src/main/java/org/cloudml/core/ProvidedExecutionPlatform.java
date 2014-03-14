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
public class ProvidedExecutionPlatform extends ExecutionPlatform{

    private final List<Property> offers;

    public ProvidedExecutionPlatform(){
        offers =new ArrayList<Property>();
    }

    public ProvidedExecutionPlatform(String name){
        super(name);
        offers =new ArrayList<Property>();
    }

    public ProvidedExecutionPlatform(String name, List<Property> properties){
        super(name,properties);
        offers =new ArrayList<Property>();
    }

    public ProvidedExecutionPlatform(String name, List<Property> properties, Component owner){
        super(name,properties, owner);
        offers =new ArrayList<Property>();
    }

    public ProvidedExecutionPlatform(String name, List<Property> properties, Component owner, Collection<Property> offers){
        super(name,properties, owner);
        this.offers =new  ArrayList<Property>();
        this.offers.addAll(offers);
    }

    public List<Property> getOffers(){
        return this.offers;
    }

    public void setOffers(Collection<Property> offers){
        this.offers.clear();
        this.offers.addAll(offers);
    }

    public void addOffer(String key, String value) {
        unlessNewKey(key);
        this.offers.add(new Property(key, value));
    }

    private void unlessNewKey(String key){
        if(getOfferByKey(key) != null){
            throw new IllegalArgumentException("Duplicated Offer Id (" + key + ")");
        }
    }

    public Property getOfferByKey(String key){
        for(Property p : this.offers){
            if(p.getName().equals(key))
                return p;
        }
        return null;
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
