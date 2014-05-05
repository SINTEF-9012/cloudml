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

import java.util.Collection;
import org.cloudml.core.Component;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.InternalComponentInstance;
import org.cloudml.core.Port;
import org.cloudml.core.PortInstance;
import org.cloudml.core.RelationshipInstance;

public class RelationshipInstanceGroup extends NamedElementGroup<RelationshipInstance> {

    public RelationshipInstanceGroup() {
    }

    public RelationshipInstanceGroup(Collection<RelationshipInstance> content) {
        super(content);
    }

    public RelationshipInstanceGroup whereEitherEndIs(PortInstance<? extends Port> port) {
        final RelationshipInstanceGroup selection = new RelationshipInstanceGroup();
        for (RelationshipInstance instance: this) {
            if (instance.eitherEndIs(port)) { 
                selection.add(instance);
            }
        }
        return selection;
    }
    
    
    public RelationshipInstanceGroup ofType(String name) {
        final RelationshipInstanceGroup selection = new RelationshipInstanceGroup();
        for (RelationshipInstance instance: this) {
            if (instance.getType().isNamed(name)) {
                selection.add(instance);
            }
        }
        return selection;
    }
    
    public InternalComponentInstanceGroup clientsOf(ComponentInstance<? extends Component> server) {
        final InternalComponentInstanceGroup selection = new InternalComponentInstanceGroup();
        for (RelationshipInstance relationship: this) {
            if (relationship.isProvidedBy(server)) {
                selection.add(relationship.getClientComponent());
            }
        }
        return selection;
    }
}
