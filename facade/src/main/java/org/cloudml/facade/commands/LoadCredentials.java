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
 * Request to load a specific file containing credentials
 */
public class LoadCredentials extends CloudMlCommand {
    
    private final String pathToCredentials;

    /**
     * Create a new LoadCredentials command from the path of the file containing
     * the credentials.
     * 
     * @param pathToCredentials the path to file containing credentials
     */
    public LoadCredentials(final String pathToCredentials) {
        this.pathToCredentials = pathToCredentials;
    }
         
    /**
     * @return the path to the file containing the credentials
     */
    public String getPathToCredentials() {
        return this.pathToCredentials;
    }

    @Override
    public void execute(CommandHandler handler) {        
        handler.handle(this);
    }

    @Override
    public String toString() {
        return String.format("load credentials from %s", pathToCredentials);
    }
    
    
    
}
