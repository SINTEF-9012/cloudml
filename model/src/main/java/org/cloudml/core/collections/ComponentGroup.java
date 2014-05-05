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

import java.util.ArrayList;
import java.util.Collection;
import org.cloudml.core.Component;
import org.cloudml.core.ExternalComponent;
import org.cloudml.core.InternalComponent;


public class ComponentGroup extends WithResourceGroup<Component>
{

    public ComponentGroup() {
    }

    public ComponentGroup(Collection<Component> content) {
        super(content);
    }

    public ExternalComponentGroup onlyExternals() {
        final ArrayList<ExternalComponent> selected = new ArrayList<ExternalComponent>();
        for(Component component: this) {
            if (component.isExternal()) {
                selected.add((ExternalComponent) component);
            }
        }
        return new ExternalComponentGroup(selected);
    }
    
    public VMGroup onlyVMs() {
        final VMGroup selection = new VMGroup();
        for (Component component: this) {
            if (component.isExternal()) {
                final ExternalComponent external = component.asExternal();
                if (external.isVM()) {
                    selection.add(external.asVM());
                }
            }
        }
        return selection;
    }
    
    public InternalComponentGroup onlyInternals() {
        final ArrayList<InternalComponent> selected = new ArrayList<InternalComponent>();
        for(Component component: this) {
            if (component.isInternal()) {
                selected.add((InternalComponent) component);
            }
        }
        return new InternalComponentGroup(selected);
    }

  
    
}
