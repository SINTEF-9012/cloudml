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
package org.cloudml.core.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.cloudml.core.NamedElement;

public abstract class NamedElementGroup<T extends NamedElement> implements Collection<T> {

    private final HashMap<String, T> content;

    public NamedElementGroup() {
        this.content = new HashMap<String, T>();
    }

    public NamedElementGroup(Collection<T> content) {
        this.content = new HashMap<String, T>();
        for (T element : content) {
            this.content.put(element.getName(), element);
        }
    }

    @Override
    public boolean add(T element) {
        abortIfCannotBeAdded(element);
        this.content.put(element.getName(), element);
        return true;
    }

    protected void abortIfCannotBeAdded(T element) {
        // by default we do nothing
    }

    public boolean remove(T element) {
        abortIfCannotBeRemoved(element);
        this.content.remove(element.getName());
        return true;
    }

    protected void abortIfCannotBeRemoved(T element) {
        // By default we do nothing
    }

    public boolean contains(T element) {
        return content.containsKey(element.getName());
    }

    @Override
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

    @Override
    public int size() {
        return this.content.size();
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        if (this.getClass().isAssignableFrom(o.getClass())) {
            T element = (T) o;
            return contains(element);
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        return this.content.values().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.content.values().toArray(a);
    }
    
    public List<T> toList() {
        return new ArrayList<T>(this.content.values());
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        if (this.getClass().isAssignableFrom(o.getClass())) {
            return remove((T) o);
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.content.values().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean overall = true;
        for (T element : c) {
            overall &= add(element);
        }
        return overall;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean overall = true;
        for (Object element : c) {
            overall &= remove(element);
        }
        return overall;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean overall = true;
        for (T element : this) {
            if (!c.contains(element)) {
                overall &= remove(element);
            }
        }
        return overall;
    }

    @Override
    public void clear() {
        this.content.clear();
    }
    
    
}
