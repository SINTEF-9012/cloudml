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

package org.cloudml.ui.shell.commands;

import org.cloudml.facade.commands.CloudMlCommand;

/**
 * Encapsulate a command object accepted by the facade. Also specifies whether
 * the inner facade command is to be run synchronously or asynchronously
 */
public class DelegatedCommand extends ShellCommand {

    public static final boolean IN_BACKGROUND = true;
    
    private final CloudMlCommand command;
    private final boolean inBackground;

    
    public DelegatedCommand(CloudMlCommand command) {
        this(command, IN_BACKGROUND);
    }
    
    public DelegatedCommand(CloudMlCommand proxy, boolean background) {
        this.command = proxy;
        this.inBackground = background;
    } 

    @Override
    public void execute(ShellCommandHandler handler) {
        handler.delegate(command, inBackground);
    }
    
    
    public boolean runInBackground() {
        return inBackground;
    }

    @Override
    public String toString() {
        final String text = command.toString();
        if (runInBackground()) {
            return String.format("%s &", text);
        }
        return text;
    }
    
    
    
    
}
