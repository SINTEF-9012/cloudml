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
package org.cloudml.core.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.cloudml.core.Property;

public class PropertyGroup implements Iterable<Property> {

    private final HashMap<String, Property> properties;

    public PropertyGroup() {
        properties = new HashMap<String, Property>();
    }
    
    public PropertyGroup(Collection<Property> properties) {
        this.properties = new HashMap<String, Property>();
        for (Property p: properties) {
            this.add(p);
        }
    }

    public Property get(String name) {
        return properties.get(name);
    }
    
    public String valueOf(String name) {
        return get(name).getValue();
    }
    
    public boolean containsAll(PropertyGroup other) {
        for (final Property property: other) {
            if (!contains(property)) {
                return false;
            }
        }
        return true;
    }
    
    public void clear() {
        properties.clear();
    }
    
    public int size() {
        return this.properties.size();
    }
    
    public boolean isEmpty() {
        return properties.isEmpty();
    }

    public boolean isDefined(String propertyName) {
        return get(propertyName) != null;
    }
    
    public boolean contains(String name, String value) {
        final Property p = get(name);
        if (p == null) {
            return false;
        }
        return p.hasValue(value); 
    }
    
    public boolean contains(Property property) {
        return contains(property.getName(), property.getValue());
    }

    public final void add(Property property) {
        rejectInvalidProperty(property);
        properties.put(property.getName(), property);
    }

    @Override
    public Iterator<Property> iterator() {
        return properties.values().iterator();
    }

    private void rejectInvalidProperty(Property property) {
        if (property == null) {
            throw new IllegalArgumentException("'null' is not a valid property");
        }
        final String name = property.getName();
        if (isDefined(name)) {
            final String message = String.format("Duplicated key '%s' in properties", name);
            throw new IllegalArgumentException(message);
        }
    }
}
