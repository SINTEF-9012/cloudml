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
package org.cloudml.facade.events;

import org.cloudml.facade.commands.CloudMlCommand;

/**
 * Abstract behaviour of the events sent by the CloudML facade.
 *
 * @author Franck Chauvel
 * @since 1.0
 */
public abstract class Event {

    private final CloudMlCommand trigger;

    /**
     * Create an event which is not a response to particular command
     */
    public Event() {
        this.trigger = null;
    }

    /**
     * Create a new event, which was triggered as a response to a specific
     * command
     *
     * @param trigger the CloudML command that triggered this particular event
     */
    public Event(CloudMlCommand trigger) {
        this.trigger = trigger;
    }

    /**
     * @return the command that triggered this event, or null if this event was
     * not the response to a command
     */
    public CloudMlCommand getTrigger() {
        return this.trigger;
    }

    /**
     * Check whether this very event was triggered by a given command
     *
     * @param command the command of interest
     * @return true if the command triggered this event
     */
    public boolean wasTriggeredBy(CloudMlCommand command) {
        return (this.trigger != null) ? (this.trigger == command) : false;
    }
    
    /**
     * Default switch method
     * @param handler the event handler
     */
    public void accept(EventHandler handler) {
        handler.handle(this);
    }
}
