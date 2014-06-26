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
 * Request for installing/deploying a given software on a given environment.
 * Both the software and the environment are identified by their ID
 */
public class Install extends CloudMlCommand {

    private final String environment;

    private final String software;

    /**
     * Create a new Install request from the id of the target environment and
     * the ID of the software to install
     *
     * @param environment the ID of the target environment to populate
     * @param software the ID of the target software to install
     */
    public Install(String environment, String software) {
        this.environment = environment;
        this.software = software;
    }

    @Override
    public void execute(CommandHandler target) {
        target.handle(this);
    }

    /**
     * @return the environment
     */
    public String getEnvironment() {
        return environment;
    }

    /**
     * @return the software
     */
    public String getSoftware() {
        return software;
    }

    @Override
    public String toString() {
        return String.format("install %s on %s", software, environment);
    }
    
    

}
