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
 * Request the loading of a given deployment model. The format will be inferred
 * from the extension of the given file.
 */
public class LoadDeployment extends CloudMlCommand {

    private final String pathToModel;

    /**
     * Create a new request for loading a deployment model
     *
     * @param pathToModel the local path to the deployment model
     */
    public LoadDeployment(final String pathToModel) {
        this.pathToModel = pathToModel;
    }

    public void execute(CommandHandler target) {
        target.handle(this);
    }

    /**
     * @return the pathToModel
     */
    public String getPathToModel() {
        return pathToModel;
    }

    @Override
    public String toString() {
        return String.format("load deployment from %s", pathToModel);
    }
    
        

}
