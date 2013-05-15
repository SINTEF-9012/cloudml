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
 * Basic event to convey information from the facade to its followers
 *
 * @author Franck Chauvel
 *
 * @since 1.0
 */
public class Message extends Event {

    public enum Category {

        INFORMATION("Info"), 
        WARNING("Warning"), 
        ERROR("Error");
        
        
        final String label;
        
        private Category(final String label) {
            this.label = label;
        }
        
        public String getLabel() {
            return this.label;
        }
    }
    
    private final Category category;
    private final String body;

    /**
     * Create a new message event, including a text and a level, which is not
     * related to any particular command.
     *
     * @param category the category of this message
     * @param body the message to convey
     */
    public Message(final Category category, final String body) {
        super();
        this.category = category;
        this.body = body;
    }

    /**
     * Create a new message event, including a text and a level, which occurs as
     * a response to a given command.
     *
     * @param category the category of this message
     * @param body the message to convey
     */
    public Message(final CloudMlCommand trigger, final Category category, final String body) {
        super(trigger);
        this.category = category;
        this.body = body;
    }

    /**
     * @return the message associated with this event info
     */
    public String getBody() {
        return this.body;
    }

    /**
     * @return the level associated with this message
     */
    public Category getCategory() {
        return this.category;
    }
    
    
    public void accept(EventHandler handler) {
        handler.handle(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(category.getLabel());
        builder.append(": ");
        builder.append(body);
        return builder.toString();
    }
}
