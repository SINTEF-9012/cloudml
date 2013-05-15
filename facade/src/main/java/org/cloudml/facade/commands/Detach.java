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
 * Request for detachment of a service provider from a service consumer.
 * 
 * @see org.cloudml.facade.commands.Attach
 * 
 * @author Franck Chauvel
 * 
 * @since 1.0
 */
public class Detach extends ManageableCommand {

	private final String providerId;

	private final String consumerId;

	/**
	 * Create a new Detach request from the consumer ID and the provider ID
	 * 
	 * @param consumerId
	 *            the ID of the consumer
	 * @param providerId
	 *            teh ID of the provider
	 */
	public Detach(CommandHandler handler, String consumerId, String providerId) {
		super(handler);
		this.consumerId = consumerId;
		this.providerId = providerId;
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
	 * @return the providerId
	 */
	public String getProviderId() {
		return providerId;
	}

	/**
	 * @return the consumerId
	 */
	public String getConsumerId() {
		return consumerId;
	}

}
