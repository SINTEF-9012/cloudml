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
import java.util.HashMap;
import java.util.Iterator;

public abstract class NamedElementGroup<T extends NamedElement> implements Iterable<T> {

    private final DeploymentModel context;
    private final HashMap<String, T> content;

    public NamedElementGroup(DeploymentModel context) {
        this.context = context;
        this.content = new HashMap<String, T>();
    }

    public NamedElementGroup(DeploymentModel context, Collection<T> content) {
        this.context = context;
        this.content = new HashMap<String, T>();
        for(T element: content) {
            this.content.put(element.getName(), element);
        }
    }

    protected DeploymentModel getContext() {
        return context;
    }

    public Collection<T> getContent() {
        return content.values();
    }

    public boolean add(T element) {
        abortIfCannotBeAdded(element);
        this.content.put(element.getName(), element);
        return true;
    }

    protected void abortIfCannotBeAdded(T element) {
        // by default we do nothing
    }

    public T remove(T element) {
        abortIfCannotBeRemoved(element);
        return this.content.remove(element.getName());
    }

    protected void abortIfCannotBeRemoved(T element) {
        // By default we do nothing
    }

    public boolean contains(T element) {
        return content.containsKey(element.getName());
    }

    public boolean isEmpty() {
        return content.isEmpty();
    }

    public T named(String elementName) {
        return content.get(elementName);
    }

    @Override
    public Iterator<T> iterator() {
        return this.content.values().iterator();
    }
    
    
}
