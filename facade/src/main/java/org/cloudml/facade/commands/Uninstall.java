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
 * Request for uninstalling a software from a given environment, both identified
 * by their respective ID.
 * 
 * @author Franck Chauvel
 * 
 * @since 1.0
 */
public class Uninstall extends ManageableCommand {

	private final String environment;

	private final String software;

	/**
	 * Create a new uninstall request from the IDs of the software artifact
	 * which has to be removed and the environment where it is currently running
	 * 
	 * @param environment
	 *            the ID of the environment where the software to remove is
	 *            currently running
	 * @param software
	 *            the ID of the software to remove
	 */
	public Uninstall(CommandHandler handler, final String environment, final String software) {
		super(handler);
		this.environment = environment;
		this.software = software;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.cloudml.facade.commands.Command#execute(org.cloudml.facade.commands
	 * .CommandHandler)
	 */
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

}
