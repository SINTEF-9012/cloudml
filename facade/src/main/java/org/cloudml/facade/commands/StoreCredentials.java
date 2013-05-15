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
 * Command to store the credentials in use in a given file
 *
 * @author Franck Chauvel
 * @since 1.0
 */
public class StoreCredentials extends ManageableCommand {

    private final String destination;

    /**
     * Create a new command to store the credentials in use in a given place on
     * disk
     *
     * @param destination
     */
    public StoreCredentials(CommandHandler handler, final String destination) {
        super(handler);
        this.destination = destination;
    }

    /**
     * @return the destination (a local path) where the credentials are supposed
     * to be stored.
     */
    public String getDestination() {
        return this.destination;
    }

    @Override
    public void execute(CommandHandler handler) {
        handler.handle(this);
    }
}
