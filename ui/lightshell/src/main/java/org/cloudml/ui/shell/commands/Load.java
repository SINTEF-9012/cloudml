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

import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudml.facade.commands.CloudMlCommand;
import org.cloudml.ui.shell.Command;
import org.cloudml.ui.shell.Shell;

public class Load extends Command {

    protected String getName() {
        return "load";
    }

    protected String getShortHelp() {
        return "loads a CloudML in memory, to enable further actions";
    }

    protected String getUsage() {
        return "load [credentials | deployment] from <path-to-CloudML-model>";
    }

    public String execute() {
        if (getArg() != null) {
            System.out.println("args = " + getArg());
            final StringTokenizer tokenizer = new StringTokenizer(getArg(), " ");
            boolean deploy = false;

            int i = 0;   
            //String token = tokenizer.nextToken();
            while (tokenizer.hasMoreElements() && i < 3) {
                String token = tokenizer.nextToken();
                if (i == 0) {
                    if ("credentials".equals(token)) {
                        deploy = false;
                    } else if ("deployment".equals(token)) {
                        deploy = true;
                    } else {
                        Logger.getLogger(Load.class.getName()).log(Level.WARNING, "\"credentials\" or \"deployment\" expected but \"" + token + "\" was found");
                        return getUsage();
                    }
                }

                if (i == 1) {
                    if (! "from".equals(token)) {
                        Logger.getLogger(Load.class.getName()).log(Level.WARNING, "\"from\" expected but \"" + token + "\" was found");
                        return getUsage();
                    }
                }

                if (i == 2) {
                    CloudMlCommand cmd;
                    if (deploy) {
                        cmd = Shell.factory.createLoadDeployment(token);
                    } else {
                        cmd = Shell.factory.createLoadCredentials(token);
                    }
                    Shell.cloudML.fireAndForget(cmd);
                    return null;
                }
                i++;
            }
            return null;
        } 
        return getUsage();
    }
}