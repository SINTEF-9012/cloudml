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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jline.console.ConsoleReader;
import jline.console.completer.ArgumentCompleter;
import jline.console.completer.Completer;
import jline.console.completer.FileNameCompleter;
import jline.console.completer.StringsCompleter;
import jline.console.history.MemoryHistory;
import org.cloudml.ui.shell.configuration.Command;
import org.cloudml.ui.shell.configuration.Configuration;

/**
 * Wrap the concept of feature from which one can read or paint
 */
public class Terminal implements InputDevice, OutputDevice {

    private final Configuration configuration;
    private final ConsoleReader reader;

    public Terminal(Configuration configuration) {
        try {
            this.configuration = configuration;

            jline.TerminalFactory.configure("auto");

            reader = new ConsoleReader();
            reader.setHistory(new MemoryHistory());
            reader.addCompleter(new ArgumentCompleter(selectCompleters()));
        
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private List<Completer> selectCompleters() {
        final List<Completer> selection = new ArrayList<Completer>();
        for (Command eachCommand: configuration.getCommands()) {
            selection.add(new StringsCompleter(eachCommand.getSyntax()));
        }
        selection.add(new FileNameCompleter());
        return selection;
    }

    /**
     * Prompt the user for a new CloudML command
     *
     * @return the command entered by the user
     */
    @Override
    public String prompt() {
        try {
            reader.println("");
            return reader.readLine(Color.CYAN.paint(configuration.getPrompt()));

        } catch (IOException ex) {
            throw new RuntimeException(ex);

        } 
    }

    @Override
    public void print(Message message) {
        try {
            reader.print(message.toString());
            reader.flush();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
