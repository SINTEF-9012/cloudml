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

import java.util.Arrays;
import java.util.List;
import org.cloudml.facade.Factory;

/**
 * The CloudML shell can be run in two different modes: batch or interactive.
 * When running in batch mode, it takes a command to be executed.
 */
public abstract class ShellMode { 

    /**
     * @return a string explaining how to start the CloudML shell
     */
    public static String usage() {
        String result = "";
        result += "Usage: java -jar cloudml-shell.jar [-i | -b <command>]" + EOL;
        result += "where:" + EOL;
        result += "   -b, --batch:        run the given command in 'batch' mode." + EOL ;
        result += "   -i, --interactive:  run in 'interactive' mode.  " + EOL;
        return result;
    }
    
    private static final String EOL = System.lineSeparator();
    
    public static ShellMode from(String... arguments) {
        if (runInBatchMode(arguments)) {
            return new BatchMode(extractCommand(arguments));
        }
        return new InteractiveMode();
    }

    public static ShellMode batch(String command) {
        return new BatchMode(command);
    }

    public static ShellMode interactive() {
        return new InteractiveMode();
    }

    private static boolean runInBatchMode(String[] arguments) {
        final List<String> batchFlags
                = Arrays.asList(new String[]{"-b",
                                             "--batch"});
        final List<String> interactiveFlags
                = Arrays.asList(new String[]{"-i",
                                             "--interactive"});
        for (String any: arguments) {
            if (any.startsWith("-")) {
                if (batchFlags.contains(any)) {
                    return BATCH;
                }
                if (interactiveFlags.contains(any)) {
                    return INTERACTIVE;
                }
                throw new IllegalArgumentException(String.format("Unknown option '%s'", any));
            }
        }
        return INTERACTIVE;
    }

    private static final boolean BATCH = true;
    private static final boolean INTERACTIVE = false;

    private static String extractCommand(String[] arguments) {
        final StringBuilder buffer = new StringBuilder();
        for (String each: arguments) {
            if (isNotFlag(each)) {
                buffer.append(each).append(" ");
            }
        }
        return buffer.toString();
    }

    private static boolean isNotFlag(String each) {
        return !each.startsWith("-");
    }

    public abstract void start();

    @Override
    public final boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof ShellMode) {
            ShellMode mode = (ShellMode) object;
            return toString().trim().equals(mode.toString().trim());
        }
        return false;
    }

    @Override
    public abstract String toString();
}

/**
 * The 'batch' mode, where the CloudML shell simply runs the given command until
 * completion.
 */
class BatchMode extends ShellMode {

    private final String command;

    public BatchMode(String command) {
        this.command = command;
    }

    @Override
    public void start() {
        final Shell shell = new Shell(Factory.getInstance().getCloudML());
        shell.execute(command);
    }

    @Override
    public String toString() {
        return String.format("-b %s", command);
    }

}

/**
 * The 'interactive' mode where the CloudML Shell prompts the user for commands
 * to be executed.
 */
class InteractiveMode extends ShellMode {

    public InteractiveMode() {
    }

    @Override
    public void start() {
        new Shell(Factory.getInstance().getCloudML()).start();
    }

    @Override
    public String toString() {
        return "-i";
    }

}
