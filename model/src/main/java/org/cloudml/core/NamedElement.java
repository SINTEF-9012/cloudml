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

import java.util.Collection;
import java.util.Iterator;

public abstract class NamedElement implements CloudMLElement {

    public static final String DEFAULT_NAME = "no name";

    private String name;

    public NamedElement() {
        this.name = DEFAULT_NAME;
    }

    public NamedElement(String name) {
        abortIfNameIsInvalid(name);
        this.name = name;
    }

    private void abortIfNameIsInvalid(String name) {
        if (name == null) {
            throw new IllegalArgumentException("'null' is not a valid name");
        }
        else if (name.isEmpty()) {
            throw new IllegalArgumentException("The empty string '' is not a valid name");
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        abortIfNameIsInvalid(name);
        this.name = name;
    }
    
    
    public static <T extends NamedElement> T findByName(String name, Collection<T> collection) {
        T selected = null;
        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext() && selected == null) {
            T current = iterator.next();
            if (name.equals(current.getName())) {
                selected = current;
            }
        }
        return selected;
    }
}
