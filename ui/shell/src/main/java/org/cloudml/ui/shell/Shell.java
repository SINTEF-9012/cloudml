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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudml.facade.CloudML;
import org.cloudml.facade.commands.CloudMlCommand;
import org.cloudml.ui.shell.commands.Script;
import org.cloudml.ui.shell.commands.ShellCommand;
import org.cloudml.ui.shell.commands.ShellCommandHandler;
import org.cloudml.ui.shell.configuration.Command;
import org.cloudml.ui.shell.configuration.Configuration;
import org.cloudml.ui.shell.configuration.Loader;
import org.cloudml.ui.shell.terminal.Formatter;
import org.cloudml.ui.shell.terminal.InputDevice;
import org.cloudml.ui.shell.terminal.OutputDevice;

import static org.cloudml.ui.shell.terminal.Message.*;
import static org.cloudml.ui.shell.terminal.Color.*;

/**
 * The CloudML Shell
 *
 * It allows the user to interact with the models and, in turn, with the
 * application deployed in the Cloud. This shell is based on the well-known
 * "command pattern": the input of the user is first parsed to get a Command
 * object, which can be stored or execute, or forwarded to the cloudML facade.
 *
 */
public class Shell implements ShellCommandHandler {

    private final CloudML proxy;
    private final Configuration configuration;
    private final History history;
    
    private final InputDevice input;
    private final Formatter output;
    
    private final Mailbox mailbox;

    private boolean running;

    public Shell(InputDevice input, OutputDevice output, CloudML proxy) {
        this.proxy = proxy;
        this.input = input;
        this.output = new Formatter(output);
        this.configuration = Loader.getInstance().getConfiguration();
        this.history = new History(output, configuration);   
        this.mailbox = new Mailbox(output);  
        proxy.register(mailbox.new EventHandler());
    }

    /**
     * Start the interaction with the user
     */
    public void start() {
        running = true;
        displayOpening();
        while (running) {
            execute(prompt());
        }
    }

    private void displayOpening() {
        output.print(format(configuration.getVersion()).eol().in(WHITE));
        output.print(format(configuration.getCopyright()).eol().in(WHITE));
        output.print(format(configuration.getLogo()).eol().in(CYAN));
        output.print(format(configuration.getDisclaimer()).eol().in(WHITE));
    }

    private String prompt() {
        if (mailbox.hasNewMessages()) {
            output.print(format("%d new message(s).", mailbox.size()).eol().in(GREEN));
        }
        return input.prompt();
    }

    /**
     * Parse and execute the command whose text is given as input
     *
     * @param text the text of the command to execute
     */
    public void execute(String text) {
        if (!text.isEmpty()) {
            final ShellCommand command = ShellCommand.fromText(text);
            history.record(command);
            command.execute(this);
        } 
    }

    public void unknownCommand() {
        output.print(format("command not supported yet!").in(RED));
    }

    public void exit() {
        running = false;
        output.print(format(configuration.getClosingMessage()).eol());
    }

    public void version() {
        output.print(format(configuration.getVersion()).eol());
    }

    public void history(int depth) {
       output.showHistory(history.selectLast(depth));
    }

    public void help(String subject) {
        if (subject == null) {
            output.showHelp(configuration.getCommands());
        
        } else {
            output.showHelp(findCommandAbout(subject));
        }
    }

    private Command findCommandAbout(String subject) {
        for(Command each: configuration.getCommands()) {
            if (each.getSyntax().contains(subject)) {
                return each;
            }
        }
        return null;
    }

    public void dumpTo(int depth, String destination) {
        try {
            new Script(history.selectLast(depth)).toFile(destination);
            output.success();
            
        } catch (FileNotFoundException ex) {
            output.error(format("Error: Unable to find in file '%s'. Is this path valid?", destination)); 
         
        }
    }

    public void replay(String pathToScript) {
        try {
            ShellCommand script = ShellCommand.fromFile(pathToScript);
            script.execute(this);

        } catch (FileNotFoundException ex) {
            output.print(format("Error: Unable to open the script at '%s'. Is this path valid?", pathToScript).eol().in(RED));

        } catch (IOException ex) {
            output.print(format("Error: Unexpected I/O error while reading the script at '%s'", pathToScript).eol().in(RED));

        }
    }

    public void showMessages(int depth) {
       mailbox.showMessages(depth);
    }

    public void delegate(CloudMlCommand command, boolean runInBackground) {
        if (runInBackground) {
            proxy.fireAndForget(command);

        } else {
            mailbox.followUp(command);
            proxy.fireAndWait(command);
            mailbox.discard(command);
        }
    }

}
