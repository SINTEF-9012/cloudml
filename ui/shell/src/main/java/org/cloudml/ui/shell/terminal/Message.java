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


package org.cloudml.ui.shell.terminal;

import org.cloudml.ui.shell.terminal.Color;

/**
 * A message displayed on a terminal
 */
public class Message { 
    
    
    public static Message format(String text, Object... arguments) {
        return new Message(String.format(text, arguments));
    } 
    
    private final String text;
    
    public Message(String text) {
        this.text = text;
    }
    
    public Message prepend(String prefix) {
        return new Message(prefix + text);
    }
    
    public Message in(Color color) {
        return new Message(color.paint(text));
    }
    
    public Message eol() {
        return new Message(text + System.lineSeparator());
    }
    
    @Override
    public String toString() {
        return text;
    }

}
