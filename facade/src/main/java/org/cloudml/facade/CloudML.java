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
package org.cloudml.facade;

import org.cloudml.core.Deployment;
import org.cloudml.facade.commands.CloudMlCommand;
import org.cloudml.facade.commands.CommandHandler;
import org.cloudml.facade.events.EventHandler;

/**
 * The general interface of the CloudML facade
 */
public interface CloudML extends CommandHandler{

    /**
     * Require the facade to perform a new command, without blocking on it
     *
     * @param command the command to execute
     * 
     * @return the associated execution object
     */
    public Execution fireAndForget(CloudMlCommand command);

    /**
     * Require the facade to perform a command, and wait for the command to be
     * complete. This is a blocking operation.
     *
     * @param command the command to execute
     * 
     * @return the associated execution object
     */
    public Execution fireAndWait(CloudMlCommand command);

    /**
     * Register a new event handler.
     *
     * @param handler the new handler to register
     */
    public void register(EventHandler handler);

    
    /**
     * Properly free the resources allocated by the facade
     */
    public void terminate();
    
    /**
     * In order to monitor the changes from outside
     * @return 
     */
    public Deployment getDeploymentModel();

    public Deployment merge(String path);
    
}