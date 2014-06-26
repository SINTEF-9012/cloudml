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
 * Request for the connection of a provided service with a required service. By
 * contrast, with install (resp. uninstall) which target deployment links,
 * attach/detach target software dependencies.
 */
public class Attach extends CloudMlCommand {

    private final String providerId;

    private final String consumerId;

    /**
     * Create a new Attach request from the providerId, and the consumerID
     *
     * @param providerId the ID of the provider
     * @param consumerId the ID of the consumer
     */
    public Attach(String providerId, String consumerId) {
        this.consumerId = consumerId;
        this.providerId = providerId;
    }

    /**
     * @return the provider ID
     */
    public String getProviderId() {
        return providerId;
    }

    /**
     * @return the consumer ID
     */
    public String getConsumerId() {
        return consumerId;
    }

    @Override
    public void execute(CommandHandler target) {
        target.handle(this);
    }

    @Override
    public String toString() {
        return String.format("connect %s to %s", consumerId, providerId);
    }

}
