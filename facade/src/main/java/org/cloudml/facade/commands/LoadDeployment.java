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
 * Request the loading of a given deployment model. The format will be infered
 * from the extension of the given file.
 * 
 * @author Franck Chauvel
 * @since 1.0
 */
public class LoadDeployment extends ManageableCommand {

	private final String pathToModel;

	/**
	 * Create a new request for loading a deployment model
	 * 
	 * @param pathToModel
	 *            the local path to the deployment model
	 */
	public LoadDeployment(CommandHandler handler, final String pathToModel) {
		super(handler);
		this.pathToModel = pathToModel;
	}

	/*
	 * (non-Javadoc)
	 * @see org.cloudml.facade.commands.CloudMlCommand#execute(org.cloudml.facade.commands.CommandHandler)
	 */
	public void execute(CommandHandler target) {
		target.handle(this);
	}

	/**
	 * @return the pathToModel
	 */
	public String getPathToModel() {
		return pathToModel;
	}

}
