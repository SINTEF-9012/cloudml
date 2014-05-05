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

import org.cloudml.core.Component; 
import org.cloudml.core.InternalComponent;
import org.cloudml.facade.commands.CloudMlCommand;

/**
 * A data object that conveys a single artefact type.
 * 
 * @author Franck Chauvel
 * @since 1.0
 */
public class ComponentData extends Data {
    
    private final Component type;
    
    /**
     * Create a new data that can convey a artefact type.
     * @param command the command which ask for this data
     * @param type the related artefact type
     */
    public ComponentData(final CloudMlCommand command, Component type) {
        super(command);
        this.type = type;
    }
    
    /**
     * @return the associated artefact type
     */
    public Component getComponent() {
        return this.type;
    }
    
    @Override
    public void accept(EventHandler handler) {
        handler.handle(this);
    }
    
}
