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
public abstract class CloudMlCommand implements Runnable {

    protected boolean isCompleted = false;
    CommandHandler target;
    protected long timeout = -1;

    public void run() {
        execute(target);
        isCompleted = true;
    }

    public CloudMlCommand(CommandHandler handler) {
        this.target = handler;
    }
    
    
    public abstract void execute(CommandHandler target);

    /**
     * @return true if the command is complete
     */
    public synchronized boolean isCompleted() {
        return this.isCompleted;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
    
    
}
