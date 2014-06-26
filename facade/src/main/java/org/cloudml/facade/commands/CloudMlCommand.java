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
package org.cloudml.facade.commands;

/**
 * Abstract behaviour of a cloudML command, i.e., a treatment that the CloudML
 * facade is able to perform.
 *
 * The pattern command is used to ensure that the GUI may remain reactive even
 * if some commands take a longer time to complete. The shell for instance, may
 * want to run command as background tasks, as one does on a UNIX terminal.
 *
 * @author Franck Chauvel - SINTEF ICT
 * @author Brice Morin - SINTEF ICT
 * @since 1.0
 */
public abstract class CloudMlCommand {

    public abstract void execute(CommandHandler target);

    /*
     * The equality of commands is computed on the associated string. Two
     * commands that serialize the same are equals.
     */
    @Override
    public final boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof CloudMlCommand) {
            final CloudMlCommand command = (CloudMlCommand) object;
            return toString().equals(command.toString());
        }
        return false;
    }
    
    @Override
    public final int hashCode() {
        return toString().hashCode();
    }
    
    @Override
    public abstract String toString(); 

}
