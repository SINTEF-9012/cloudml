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
import org.cloudml.core.WithProperties;

public class WithPropertyGroup<T extends WithProperties> extends NamedElementGroup<T> {

    public WithPropertyGroup() {
    }

    public WithPropertyGroup(Collection<T> content) {
        super(content);
    }

    public WithPropertyGroup<T> withProperty(String propertyName) {
        final ArrayList<T> selected = new ArrayList<T>();
        for (T wp: this) {
            if (wp.hasProperty(propertyName)) {
                selected.add(wp);
            }
        }
        return new WithPropertyGroup<T>(selected);
    }
    
     public WithPropertyGroup<T> withProperty(String key, String value) {
        final ArrayList<T> selected = new ArrayList<T>();
        for (T wp: this) {
            if (wp.hasProperty(key, value)) {
                selected.add(wp);
            }
        }
        return new WithPropertyGroup<T>(selected);
    }
    
}
