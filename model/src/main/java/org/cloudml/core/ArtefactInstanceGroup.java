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
package org.cloudml.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArtefactInstanceGroup extends NamedElementGroup<ArtefactInstance> {

    public ArtefactInstanceGroup(DeploymentModel context) {
        super(context);
    }

    public ArtefactInstanceGroup(DeploymentModel context, Collection<ArtefactInstance> content) {
        super(context, content);
    }
    
    

    @Override
    protected void abortIfCannotBeAdded(ArtefactInstance instance) {
        if (!getContext().getArtefactTypes().contains(instance.getType())) {
            String message = String.format("artefact type '%s' associated with instance '%s' is not part of the model", instance.getType().getName(), instance.getName());
            throw new IllegalArgumentException(message);
        }
        if (instance.hasDestination() && !getContext().getNodeInstances().contains(instance.getDestination())) {
            String message = String.format("destination '%s' of artefact instance '%s' is not part of the model", instance.getDestination().getName(), instance.getName());
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    protected void abortIfCannotBeRemoved(ArtefactInstance element) {
        super.abortIfCannotBeRemoved(element); //To change body of generated methods, choose Tools | Templates.
    }

    public ArtefactInstanceGroup ofType(Artefact type) {
        ArrayList<ArtefactInstance> selection = new ArrayList<ArtefactInstance>();
        for (ArtefactInstance instance : this) {
            if (instance.getType().equals(type)) {
                selection.add(instance);
            }
        }
        return new ArtefactInstanceGroup(getContext(), selection);
    }
    
    public ArtefactInstanceGroup ofType(String typeName) {
        final Artefact type = getContext().getArtefactTypes().named(typeName);
        return ofType(type);
    }

    public ArtefactInstanceGroup hostedBy(NodeInstance destination) {
        final ArrayList<ArtefactInstance> selection = new ArrayList<ArtefactInstance>();
        for (ArtefactInstance artefact : this) {
            if (artefact.getDestination().equals(destination)) {
                selection.add(artefact);
            }
        }
        return new ArtefactInstanceGroup(getContext(), selection);
    }
}
