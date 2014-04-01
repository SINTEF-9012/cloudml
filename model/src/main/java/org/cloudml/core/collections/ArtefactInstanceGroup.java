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
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.NodeInstance;

public class ArtefactInstanceGroup extends NamedElementGroup<ArtefactInstance> {

    public ArtefactInstanceGroup() {
        super();
    }

    public ArtefactInstanceGroup(Collection<ArtefactInstance> content) {
        super(content);
    }

    public ArtefactInstanceGroup ofType(Artefact type) {
        final ArrayList<ArtefactInstance> selection = new ArrayList<ArtefactInstance>();
        for (ArtefactInstance instance : this) {
            if (instance.getType().equals(type)) {
                selection.add(instance);
            }
        }
        return new ArtefactInstanceGroup(selection);
    }

    public ArtefactInstanceGroup ofType(String typeName) {
        final ArrayList<ArtefactInstance> selection = new ArrayList<ArtefactInstance>();
        for (ArtefactInstance instance : this) {
            if (instance.getType().getName().equals(typeName)) {
                selection.add(instance);
            }
        }
        return new ArtefactInstanceGroup(selection);
    }

    public ArtefactInstanceGroup hostedBy(NodeInstance destination) {
        final ArrayList<ArtefactInstance> selection = new ArrayList<ArtefactInstance>();
        for (ArtefactInstance artefact : this) {
            if (artefact.getDestination().equals(destination)) {
                selection.add(artefact);
            }
        }
        return new ArtefactInstanceGroup(selection);
    }
}
