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
 * 
 * @author Franck Chauvel
 * @since 1.0
 */
public class LoadCredentials extends ManageableCommand {

    
    private final String pathToCredentials;
    
    
    /**
     * Create a new LoadCredentials command from the path of the file containing
     * the credentials.
     * 
     * @param pathToCredentials the path to file containing credentials
     */
    public LoadCredentials(CommandHandler handler, final String pathToCredentials) {
        super(handler);
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
    
}
