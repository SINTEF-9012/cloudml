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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.cloudml.ui.shell.commands.ShellCommand;
import org.cloudml.ui.shell.terminal.InputDevice;

/**
 * A test specific input device, which play a sequence of action 
 */
public class Scenario implements InputDevice {

    private final List<ShellCommand> scenario;
    private final List<ShellCommand> executed; 
    
    public Scenario(ShellCommand... commands) {
        this.scenario = new ArrayList<ShellCommand>(Arrays.asList(commands));
        this.executed = new ArrayList<ShellCommand>(this.scenario);
    }
    
    public String prompt() {
        if (scenario.isEmpty()) {
            return ShellCommand.exit().toString();
        }
        final ShellCommand command = scenario.remove(0);
        executed.add(command);
        return command.toString();
    }
   
    
}
