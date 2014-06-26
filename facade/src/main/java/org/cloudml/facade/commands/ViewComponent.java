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
package org.cloudml.facade.commands;

/**
 * Request detailed information about a selected artefact type
 */
public class ViewComponent extends CloudMlCommand {

    private final String componentId;

    /**
     * Create a new ViewComponent from the ID of the component type whose
     * details are needed
     *
     * @param ComponentId the ID of the needed component type.
     */
    public ViewComponent(final String ComponentId) {
        this.componentId = ComponentId;
    }

    /**
     * @return the ID of the needed component type.
     */
    public String getComponentId() {
        return this.componentId;
    }

    @Override
    public void execute(CommandHandler handler) {
        handler.handle(this);
    }

    @Override
    public String toString() {
        return String.format("view type %s", componentId);
    }
    
    

}
