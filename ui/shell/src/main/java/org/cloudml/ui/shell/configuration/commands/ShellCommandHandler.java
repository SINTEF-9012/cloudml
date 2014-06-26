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
package org.cloudml.ui.shell.configuration.commands;

import org.cloudml.facade.commands.CloudMlCommand;

/**
 * Interface required to handle shell commands
 */
public interface ShellCommandHandler {

    /**
     * Default handler, when no other matches.
     */
    public void unknownCommand();

    /**
     * Exit the CloudML
     */
    public void exit();

    /**
     * Show the version number of the CloudML shell
     */
    public void version();

    /**
     * Show the explanation about how to use the CloudML shell
     *
     * @param subject the subject on which help is needed
     */
    public void help(String subject);

    /**
     * Dump the local history into the selected. Only the n-first command of the
     * history are stored.
     *
     * @param depth the depth of the history
     * @param destination the file where the history must be stored
     */
    public void dumpTo(int depth, String destination);

    /**
     * Show the history of commands entered by th user
     *
     * @param depth the depth to reach in the history
     */
    public void history(int depth);

    /**
     * Replay the command stored in the given file
     *
     * @param pathToScript the path to the script to replay
     */
    public void replay(String pathToScript);

    /**
     * Show the last n messages received by the Shell
     *
     * @param depth the number of messages to display
     */
    public void showMessages(int depth);

    /**
     * Delegate the execution to the given CloudML command, either synchronously
     * or asynchronously
     *
     * @param command the CloudML command to run
     * @param runInBackground a flag to run in background or not
     */
    public void delegate(CloudMlCommand command, boolean runInBackground);

}
