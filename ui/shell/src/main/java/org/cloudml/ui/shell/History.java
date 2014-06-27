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
package org.cloudml.ui.shell;

import org.cloudml.ui.shell.terminal.Terminal;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.cloudml.ui.shell.commands.ShellCommand;
import org.cloudml.ui.shell.configuration.Configuration;
import org.cloudml.ui.shell.terminal.OutputDevice;

import static org.cloudml.ui.shell.terminal.Color.RED;
import static org.cloudml.ui.shell.terminal.Message.format;

/**
 * Hold the list of commands executed through the shell
 */
class History {

    private final Configuration configuration;
    private final OutputDevice terminal;
    private final List<ShellCommand> history;

    History(OutputDevice terminal, Configuration configuration) {
        this.configuration = configuration;
        this.terminal = terminal;
        this.history = new ArrayList<ShellCommand>();
    }

    int length() {
        return this.history.size();
    }

    List<ShellCommand> selectLast(int count) {
        if (count < 0) {
            count = length();
        }
        final int width = Math.min(count, length());
        final ArrayList<ShellCommand> selection = new ArrayList<ShellCommand>(history.subList(length() - width, length()));
        Collections.reverse(selection);
        return selection;
    }

    void record(ShellCommand command) {
        this.history.add(command);
    }

}
