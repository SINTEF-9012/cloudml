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

/**
 * Show the the last messages received by the shell
 */
public class ShowMessages extends ShellCommand {

    private static final int ALL = -1;

    private final int depth;

    public ShowMessages() {
        this(ALL);
    }

    public ShowMessages(int depth) {
        super(VOLATILE);
        this.depth = depth;
    }

    @Override
    public void execute(ShellCommandHandler handler) {
        handler.showMessages(depth);
    }

    public boolean hasDepth() {
        return depth != ALL;
    }

    @Override
    public String toString() {
        if (hasDepth()) {
            return String.format("messages %d", depth);
        }
        return String.format("messages");
    }

}
