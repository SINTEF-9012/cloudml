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

import java.util.Collection;
import org.cloudml.core.Component;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.ExternalComponent;
import org.cloudml.core.ExternalComponentInstance;
import org.cloudml.core.InternalComponentInstance;

public class ComponentInstanceGroup<T extends ComponentInstance<? extends Component>> extends WithResourceGroup<T> {

    public ComponentInstanceGroup() {
    }

    public ComponentInstanceGroup(Collection<T> content) {
        super(content);
    }

    
    public InternalComponentInstanceGroup onlyInternals() {
       final InternalComponentInstanceGroup internalInstances = new InternalComponentInstanceGroup();
        for (final T instance: this) {
            if (instance.isInternal()) {
                internalInstances.add(instance.asInternal());
            }
        }
        return internalInstances;
    }
    
    
    public ExternalComponentInstanceGroup onlyExternals() {
        final ExternalComponentInstanceGroup externalInstances = new ExternalComponentInstanceGroup();
        for (final T instance: this) {
            if (instance.isExternal()) {
                externalInstances.add(instance.asExternal());
            }
        }
        return externalInstances;
    }
    
    public VMInstanceGroup onlyVMs() {
        final VMInstanceGroup selection = new VMInstanceGroup();
        for (final T instance: this) {
            if (instance.isExternal()) {
                final ExternalComponentInstance<? extends ExternalComponent> external = instance.asExternal();
                if (external.isVM()) {
                    selection.add(external.asVM());
                }
            }
        }
        return selection;
    }
    
    public ComponentInstanceGroup<T> ofType(String typeName) {
        final ComponentInstanceGroup<T> selected = new ComponentInstanceGroup<T>();
        for (T instance: this) {
            if (instance.getType().isNamed(typeName)) {
                selected.add(instance);
            }
        }
        return selected;
    }
    
    
    public ComponentInstanceGroup<T> hosting(InternalComponentInstance component) {
        final ComponentInstanceGroup<T> selected = new ComponentInstanceGroup<T>();
        for (T instance: this) {
            if (instance.isHosting(component)) {
                selected.add(instance);
            }
        }
        return selected;
    }
    
   
}
