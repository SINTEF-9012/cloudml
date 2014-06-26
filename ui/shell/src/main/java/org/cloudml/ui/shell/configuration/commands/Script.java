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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Captures a sequences of commands
 */
public class Script extends ShellCommand implements Iterable<ShellCommand> {

    private final List<ShellCommand> commands;
    
    public Script(List<ShellCommand> commands) {
        this.commands = new ArrayList<ShellCommand>(commands);
    }
    
    @Override
    public void execute(ShellCommandHandler handler) {
        for(ShellCommand each: commands) {
            each.execute(handler);
        }
    }

    public Iterator<ShellCommand> iterator() {
        return commands.iterator();
    }
    
   
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.commands != null ? this.commands.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        for (ShellCommand each: commands) {
            result.append(each);
            if (commands.indexOf(each) != commands.size() - 1) {
                result.append(" ");
            }
        }
        return result.toString();
    }
    
}
