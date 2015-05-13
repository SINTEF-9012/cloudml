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

import java.util.List;

/**
 * Stop the component whose ID is given
 */
public class StopComponent extends CloudMlCommand {

    private final List<String> componentId;

    /**
     * Create a new StopComponent request from the ID of the artifact to stop
     *
     * @param componentId the ID of the artifact to stop
     */
    public StopComponent(List<String> componentId) {
        this.componentId = componentId;
    }

    /**
     * @return the componentId to stop
     */
    public List<String> getComponentId() {
        return componentId;
    }

    @Override
    public void execute(CommandHandler target) {
        target.handle(this);
    }

    @Override
    public String toString() {
        return String.format("stop %s", componentId);
    }
    
    

}
