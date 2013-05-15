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

import java.util.HashSet;
import java.util.Set;
import org.cloudml.facade.CloudML;
import org.cloudml.ui.shell.Command;

public class Exit extends Command {
    
    public Exit(CloudML cloudml) {
        super(cloudml);
    }
    
    @SuppressWarnings("serial")
    protected Set<String> getAliases() {
        return new HashSet<String>() {
            { 
                add("q");
                add("quit");
            }
        };
    }
    protected String getName() { return "exit"; }
    
    protected String getShortHelp() {
        return "exit the program (q and quit do the same)";
    }
    
    protected String getUsage() { return "exit"; }
    
    public String execute() {
        System.exit(0);
        return null;
    }
}