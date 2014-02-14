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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudml.facade.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.cloudml.core.ComponentInstance;
import org.cloudml.facade.commands.CloudMlCommand;

/**
 * Data object to convey a list of artefact instances
 * 
 * @author Franck Chauvel
 * @since 1.0
 */
public class ComponentInstanceList extends Data {

    private final ArrayList<ComponentInstance> components;

    /**
     * Create a new event containing list of components
     *
     * @param components the list of components
     */
    public ComponentInstanceList(CloudMlCommand command, final Collection<ComponentInstance> components) {
        super(command);
        this.components = new ArrayList<ComponentInstance>(components.size());
        this.components.addAll(components);
    }

    /**
     * @return the list of component instances
     */
    public List<ComponentInstance> getComponentInstances() {
        return Collections.unmodifiableList(this.components);
    }

    @Override
    public void accept(EventHandler handler) {
        handler.handle(this);
    }
}
