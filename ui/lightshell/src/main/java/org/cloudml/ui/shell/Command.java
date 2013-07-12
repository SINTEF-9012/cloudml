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

import java.util.Map;
import java.util.Set;

/**
 * An abstract class that shell-commands must extend.
 * <p>
 * The return values from the abstract values decide how the help-output of
 * a command looks like. A typical help output might look like this:
 * <pre>
 * Usage: output from getUsage()
 *  Here, the output from getLongHelp() will be. If getLongHelp() returns null,
 *  then the output from getShortHelp() will be shown here. Note that if
 *  getShortHelp() returns null, then this whole line will only say 'null'.
 * SUBCOMMANDS
 *  getName() subcmd.getName()    Output from getShortHelp from subcmd is here
 *  getName() anothersubcmd       Short help from another subcmd
 * EXAMPLES
 *  key from getExamples()        This text here is the key's corresponding
 *                                value. This is a description of the command
 *                                (the key).
 *  another key                   Another description here. Only one line.
 * </pre>
 *
 * @author  Andreas Halle
 * @version 0.1.0
 * @see     lightshell.Shell
 */
public abstract class Command {
    
    private String arg;
    
    /**
     * Return a {@code String} containing results from executing code. The code
     * takes a {@code String} as an argument. The argument is given by using
     * the command {@code setArg(String)} before running {@code execute()}.
     *
     * @return result from code execution.
     */
    public abstract String execute();

    /**
     * Return argument set with {@code setArg(String)}.
     *
     * @return argument.
     */
    public final String getArg() { return arg; }

    /**
     * Return the help text of a command as a {@code String}.
     *
     * @return help text.
     */
    public final String getHelp() {
        StringBuilder sb = new StringBuilder();
        sb.append("Usage: ");
        sb.append(getUsage());
        sb.append("\n");
        sb.append(prettyLongHelp(67));
        sb.append(getSubCmdList());
        sb.append(getExampleHelp());
        return sb.toString();
    }

    /**
     * Set an argument to be executed later with {@code execute()}.
     *
     * @param arg argument to be given to the command before execution.
     */
    public final void setArg(String arg) { this.arg = arg; }

    /**
     * Return a {@code Set} of aliases for a command.
     *
     * @return a {@code Set} of {@code Strings}.
     */
    protected Set<String> getAliases() { return null; }

    /**
     * Return a {@code Map} where the keys are example command usage, and the
     * values are an explanation of the corresponding usage.
     * <p>
     * Return a {@code LinkedHashMap} if you want the list to be printed in the
     * order that you insert them.
     * <p>
     * Return {@code null} if you don't want any help examples.
     *
     * @return a {@code Map} of examples.
     */
    protected Map<String, String> getExamples() { return null; }

    /**
     * Return a long help text. This text is shown in the top of the command
     * help, and it should describe the command in detail.
     *
     * @return a long help text.
     */
    protected String getLongHelp() { return null; }

    /**
     * Return a {@code String} containing the name of the command. This name,
     * unlike those in the alias set, will be shown in the command list from
     * the 'help'-command.
     * <p>
     * The name can also exist in the alias set, but it is not needed.
     * <p>
     * Commands that return {@code null} here will be ignored by the shell.
     *
     * @return the name of the command.
     */
    protected abstract String getName();

    /**
     * Return a {@code Set} that contains {@code Command} objects that are
     * used as sub commands for this {@code Command} object.
     * <p>
     * Return a {@code LinkedHashSet} if you want the list of sub commands in
     * help to be printed in the order that you insert them.
     * <p>
     * Return {@code null} if the command doesn't have any sub commands.
     *
     * @return a {@code Set} of sub commands.
     */
    protected Set<Command> getSubCommands() { return null; };

    /**
     * Return a short help text. This help text is shown next to each command
     * in the command list.
     *
     * @return a short help text.
     */
    protected abstract String getShortHelp();

    /**
     * Return command syntax, i.e. the ordering of command arguments.
     *
     * @return command syntax.
     */
    protected abstract String getUsage();

    /*
     * Return a String with a list of examples for a given command. Includes
     * leading newline. Return a blank String if there are no examples.
     */
    private String getExampleHelp() {
        Map<String, String> ex = getExamples();
        if (ex == null) return "";

        StringBuilder sb = new StringBuilder();
        sb.append("\nEXAMPLE USAGE\n");
        Set<String> keys= ex.keySet();
        String delim = "";
        for (String key : keys) {
            String str = ex.get(key);
            sb.append(delim).append(Shell.indent(" " + key, str, 20, 47));
            delim = System.getProperty("line.separator");
        }
        return sb.toString();
    }



    /*
     * Return a String with a list of sub commands for a given command.
     * Includes leading newline. Return a blank String if there are no sub
     * commands.
     */
    private String getSubCmdList() {
        Set<Command> subCmds = getSubCommands();
        if (subCmds == null) return "";

        StringBuilder sb = new StringBuilder();
        sb.append("\nSUB COMMANDS\n");

        String delim = "";
        for (Command c : subCmds) {
            String str = " " + c.getName();
            sb.append(delim);
            sb.append(Shell.indent(str, c.getShortHelp(), 20, 47));
            delim = System.getProperty("line.separator");
        }

        return sb.toString();
    }



    /* Cuts each line into a maximum length. Indent each line with a space. */
    private String prettyLongHelp(int lim) {
        String lhelp = getLongHelp();
        if (lhelp == null) return " " + getShortHelp();

        StringBuilder sb = new StringBuilder();

        String delim = "";
        String[] lines = Shell.cut(lhelp, lim);
        for (String s : lines) {
            sb.append(delim).append(" ").append(s);
            delim = System.getProperty("line.separator");
        }
        return sb.toString();
    }
}
