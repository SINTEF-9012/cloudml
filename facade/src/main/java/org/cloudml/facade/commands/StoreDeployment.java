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
 * Request for storing the deployment model currently in use in a given format,
 * at a given destination (on the local disk)
 * 
 * @author Franck Chauvel
 * @since 1.0
 */
public class StoreDeployment extends ManageableCommand {

	private final String destination;

	/**
	 * Create a new Storing request, specifying the format to use and the
	 * destination on disk
	 * 
	 * @param format
	 *            the format to use
	 * @param destination
	 *            the destination where to store the file
	 */
	public StoreDeployment(CommandHandler handler, final String destination) {
		super(handler);
		this.destination = destination;
	}

	/*
	 * (non-Javadoc)
	 * @see org.cloudml.facade.commands.CloudMlCommand#execute(org.cloudml.facade.commands.CommandHandler)
	 */
	public void execute(CommandHandler target) {
		target.handle(this);

	}

	/**
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}

}
