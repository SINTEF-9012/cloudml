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
import java.util.Iterator;
import java.util.List;
import org.cloudml.core.NamedElement;

public abstract class NamedElementGroup<T extends NamedElement> implements Collection<T> {

    private final ArrayList<T> content;

    public NamedElementGroup() {
        this.content = new ArrayList<T>();
    }

    public NamedElementGroup(Collection<T> content) {
        this.content = new ArrayList<T>();
        for (T element : content) {
            this.content.add(element);
        }
    }

    public T firstNamed(String name) {
        for (T namedElement : content) {
            if (namedElement.isNamed(name)) {
                return namedElement;
            }
        }
        return null;
    }

    @Override
    public final boolean isEmpty() {
        return content.isEmpty();
    }

    @Override
    public final Iterator<T> iterator() {
        return content.iterator();
    }

    public boolean replace(T o){
        if (o == null) {
            return false;
        }
        if (o instanceof NamedElement) {
            final NamedElement namedElement = (NamedElement) o;
            content.remove(o);
            content.add(o);
            return true;
        }
        return false;
    }

    @Override
    public boolean add(T e) {
        content.add(e);
        return true;
    }

    @Override
    public final int size() {
        return this.content.size();
    }

    @Override
    public final boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof NamedElement) {
            T element = (T) o;
            return content.contains(element);
        }
        return false;
    }

    @Override
    public final Object[] toArray() {
        return this.content.toArray();
    }

    @Override
    public final <T> T[] toArray(T[] a) {
        return this.content.toArray(a);
    }

    public final List<T> toList() {
        return new ArrayList<T>(this.content);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof NamedElement) {
            final NamedElement namedElement = (NamedElement) o;
            content.remove(namedElement);
            return true;
        }
        return false;
    }

    public Boolean replaceAll(Collection<? extends T> c){
        boolean overall = true;
        for (T element : c) {
            overall &= replace(element);
        }
        return overall;
    }

    @Override
    public final boolean containsAll(Collection<?> c) {
        return this.content.containsAll(c);
    }

    @Override
    public final boolean addAll(Collection<? extends T> c) {
        boolean overall = true;
        for (T element : c) {
            overall &= add(element);
        }
        return overall;
    }

    @Override
    public final boolean removeAll(Collection<?> c) {
        boolean overall = true;
        for (Object element : c) {
            overall &= remove(element);
        }
        return overall;
    }

    @Override
    public final boolean retainAll(Collection<?> c) {
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

    public Collection<String> onlyNames() {
        final ArrayList<String> names = new ArrayList<String>();
        for (T element: this) {
            names.add(element.getName());
        }
        return names;
    }
}
