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

/**
 * Dump a part of history into the given file
 */
public class DumpTo extends ShellCommand {

    private final int depth;
    private final String destination;

    public DumpTo(String destination) {
        this(ALL, destination);
    }

    public DumpTo(int depth, String destination) {
        super(VOLATILE);
        this.depth = depth;
        this.destination = destination;
    }

    public boolean hasDepth() {
        return depth != ALL;
    }

    private static final int ALL = -1;

    @Override
    public void execute(ShellCommandHandler handler) {
        handler.dumpTo(depth, destination);
    }

    @Override
    public String toString() {
        if (hasDepth()) {
            return String.format("dump %d to %s", depth, destination);
        }
        return String.format("dump to %s", destination);
    }

}
