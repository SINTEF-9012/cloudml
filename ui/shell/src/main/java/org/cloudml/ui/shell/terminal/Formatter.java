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

import java.util.List;
import org.cloudml.ui.shell.commands.ShellCommand;
import org.cloudml.ui.shell.configuration.Command;
import org.cloudml.ui.shell.configuration.Usage;

import static org.cloudml.ui.shell.terminal.Message.*;
import static org.cloudml.ui.shell.terminal.Color.*;

/**
 * Format the messages before delegating to another output device
 */
public class Formatter implements OutputDevice {

    private final OutputDevice delegate;

    public Formatter(OutputDevice delegate) {
        this.delegate = delegate;
    }

    public void showHistory(List<ShellCommand> history) {
        print(format("Last commands:").eol());
        for (int i = 1; i <= history.size(); i++) {
            print(format(" %03d: %s", i, history.get(i-1)).eol());
        }
    }

    public void showHelp(List<Command> commands) {
        print(format("Available commands:").eol());
        for (Command eachCommand: commands) {
            print(format(" - %s", eachCommand.getSyntax()).eol());
        }
    }

    public void showHelp(Command command) {
        if (command == null) {
            print(format("No related command.").eol());
        
        } else {
            print(format(command.getSyntax()).eol());
            print(format(command.getDescription()).eol());
            for (Usage eachExample: command.getExamples()) {
                print(format(" - %s ", eachExample.getSyntax()).eol());
                print(format("   %s ", eachExample.getDescription()).eol());
            }
        }
    }

    public void info(Message message) {
        print(message);
    }

    public void success() {
        print(format("OK").in(GREEN).eol());
    }

    public void error(Message message) {
        print(message.prepend("Error: ").in(RED));
    }

    public void print(Message message) {
        delegate.print(message);
    }

}
