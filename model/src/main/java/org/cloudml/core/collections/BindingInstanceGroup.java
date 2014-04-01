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
import java.util.List;
import org.cloudml.core.ArtefactPort;
import org.cloudml.core.ArtefactPortInstance;
import org.cloudml.core.Binding;
import org.cloudml.core.BindingInstance;
import org.cloudml.core.ClientPortInstance;
import org.cloudml.core.ServerPortInstance;

public class BindingInstanceGroup extends NamedElementGroup<BindingInstance> {

    public BindingInstanceGroup() {
        super();
    }

    public BindingInstanceGroup(Collection<BindingInstance> content) {
        super(content);
    }

    public BindingInstanceGroup ofType(Binding type) {
        final ArrayList<BindingInstance> selection = new ArrayList<BindingInstance>();
        for (BindingInstance binding : this) {
            if (binding.getType().equals(type)) {
                selection.add(binding);
            }
        }
        return new BindingInstanceGroup(selection);
    }
    
    public BindingInstanceGroup ofType(String typeName) {
       final ArrayList<BindingInstance> selection = new ArrayList<BindingInstance>();
        for (BindingInstance binding : this) {
            if (binding.getType().getName().equals(typeName)) {
                selection.add(binding);
            }
        }
        return new BindingInstanceGroup(selection);
    }

    public BindingInstanceGroup withPort(ArtefactPortInstance<? extends ArtefactPort> port) {
        final ArrayList<BindingInstance> selection = new ArrayList<BindingInstance>();
        for (BindingInstance binding : this) {
            if (binding.eitherEndIs(port)) {
                selection.add(binding);
            }
        }
        return new BindingInstanceGroup(selection);
    }

    public BindingInstanceGroup from(ClientPortInstance cpi) {
        return withPort(cpi);
    }

    public BindingInstanceGroup to(ServerPortInstance cpi) {
        return withPort(cpi);
    }
}
