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

import java.io.*;
import java.util.Arrays;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.cloudml.facade.commands.CloudMlCommand;
import org.cloudml.ui.shell.commands.builder.ShellCommandsLexer;
import org.cloudml.ui.shell.commands.builder.ShellCommandsParser;

import static org.cloudml.ui.shell.terminal.Color.RED;
import static org.cloudml.ui.shell.terminal.Message.format;

/**
 * General interface of the shell commands such as exit, help, deploy, etc.
 *
 * Commands are either volatile or persistent. Volatile commands are only there
 * for the convenience of the user, such as help, version, copyright, etc. and
 * are therefore never saved in scripts. By contrast, persistent commands are
 * stored in scripts.
 */
public abstract class ShellCommand {

    public static final boolean PERSISTENT = true;
    public static final boolean VOLATILE = !PERSISTENT;

    private final boolean persistent;

    public ShellCommand() {
        this(PERSISTENT);
    }

    public ShellCommand(boolean persistent) {
        this.persistent = persistent;
    }

    public final boolean isPersistent() {
        return this.persistent;
    }

    public void execute(ShellCommandHandler handler) {
        handler.unknownCommand();
    }

    @Override
    public final boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof ShellCommand) {
            return object.toString().equals(toString());
        }
        return false;
    }

    @Override
    public abstract String toString();

    /**
     * Write this command into the given text file. The file will be be created
     * if does not already exists, and overridden if it exists.
     *
     * @param pathToFile the file to create
     *
     * @throws FileNotFoundException
     */
    public void toFile(String pathToFile) throws FileNotFoundException {
        PrintStream script = null;
        try {
            script = new PrintStream(new File(pathToFile));
            
            script.println("#");
            script.println("# Generated script. Please edit carefully");
            script.println("#");
            script.println(toString());

        } catch (FileNotFoundException ex) {
            throw ex;

        } finally {
            script.close();

        }
    }

    /*
     * Commands factory method
     */
    public static ShellCommand fromText(String text) {
        return parse(new ANTLRInputStream(text));
    }

    public static ShellCommand fromFile(String pathToFile) throws FileNotFoundException, IOException {
        final FileInputStream file = new FileInputStream(pathToFile);
        return parse(new ANTLRInputStream(file));
    }

    private static ShellCommand parse(ANTLRInputStream input) throws RecognitionException {
        ShellCommandsLexer lexer = new ShellCommandsLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ShellCommandsParser parser = new ShellCommandsParser(tokens);
        ParseTree tree = parser.script();
        return tree.accept(new ShellCommandBuilder());
    }

    public static ShellCommand script(ShellCommand... commands) {
        return new Script(Arrays.asList(commands));
    }

    public static ShellCommand exit() {
        return new Exit();
    }

    public static ShellCommand version() {
        return new Version();
    }

    public static ShellCommand help() {
        return new Help(null);
    }

    public static ShellCommand help(String subject) {
        return new Help(subject);
    }

    public static ShellCommand history() {
        return new History();
    }

    public static ShellCommand history(int depth) {
        return new History(depth);
    }

    public static ShellCommand dumpTo(String location) {
        return new DumpTo(location);
    }

    public static ShellCommand dumpTo(int depth, String location) {
        return new DumpTo(depth, location);
    }

    public static ShellCommand replay(String pathToScript) {
        return new Replay(pathToScript);
    }

    public static ShellCommand showMessages() {
        return new ShowMessages();
    }

    public static ShellCommand showMessages(int depth) {
        return new ShowMessages(depth);
    }

    public static ShellCommand delegate(CloudMlCommand command, boolean runInBackground) {
        return new DelegatedCommand(command, runInBackground);
    }
}
