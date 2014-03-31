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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BindingInstanceGroup extends NamedElementGroup<BindingInstance> {

    public BindingInstanceGroup(DeploymentModel context) {
        super(context);
    }

    public BindingInstanceGroup(DeploymentModel context, Collection<BindingInstance> content) {
        super(context, content);
    }

    @Override
    protected void abortIfCannotBeAdded(BindingInstance element) {
        super.abortIfCannotBeAdded(element); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void abortIfCannotBeRemoved(BindingInstance element) {
        super.abortIfCannotBeRemoved(element); //To change body of generated methods, choose Tools | Templates.
    }

    public BindingInstanceGroup ofType(Binding type) {
        List<BindingInstance> selection = new ArrayList<BindingInstance>();
        for (BindingInstance binding : this) {
            if (binding.getType().equals(type)) {
                selection.add(binding);
            }
        }
        return new BindingInstanceGroup(getContext(), selection);
    }

    public BindingInstanceGroup withPort(ArtefactPortInstance<? extends ArtefactPort> port) {
        final ArrayList<BindingInstance> selection = new ArrayList<BindingInstance>();
        for (BindingInstance binding : this) {
            if (binding.eitherEndIs(port)) {
                selection.add(binding);
            }
        }
        return new BindingInstanceGroup(getContext(), selection);
    }

    public BindingInstanceGroup from(ClientPortInstance cpi) {
        return withPort(cpi);
    }

    public BindingInstanceGroup to(ServerPortInstance cpi) {
        return withPort(cpi);
    }
}
