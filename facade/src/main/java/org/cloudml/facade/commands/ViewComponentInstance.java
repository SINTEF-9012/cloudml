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
 * Request the detailed information about a given artefact instance
 */
public class ViewComponentInstance extends CloudMlCommand {

    private final String instanceId;

    /**
     * Create a new ViewComponentInstance command from the artefact whose
     * details are needed.
     *
     * @param componentId the ID of the needed artefact
     */
    public ViewComponentInstance(final String componentId) {
        this.instanceId = componentId;
    }

    /**
     * @return the ID of the needed artefact
     */
    public String getComponentId() {
        return this.instanceId;
    }

    @Override
    public void execute(CommandHandler handler) {
        handler.handle(this);
    }

    @Override
    public String toString() {
        return String.format("view instance %s", instanceId);
    }
    
    

}
