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
package org.cloudml.facade;

import org.cloudml.facade.commands.CloudMlCommand;
import org.cloudml.facade.commands.CommandHandler;
import org.cloudml.mrt.Coordinator;

/**
 * Execute a command in a separate thread
 */
public class Execution implements Runnable {

    public static final long NO_TIMEOUT = -1L;

    private final CloudMlCommand command;
    private final CommandHandler handler;
    private boolean completed;
    private final long timeout;

    public Execution(CloudMlCommand command, CommandHandler handler) {
        this(command, handler, NO_TIMEOUT);
    }

    public Execution(CloudMlCommand command, CommandHandler handler, long timeout) {
        this.command = command;
        this.handler = handler;
        this.completed = false;
        this.timeout = timeout;
    }


    public synchronized boolean isCompleted() {
        return completed;
    }

    private synchronized void markAsCompleted() {
        Coordinator coordinator=handler.getCoordinator();
        if(coordinator != null) {
            coordinator.ack("completed", command.getClass().getName());
        }
        completed = true;
        notifyAll();
    }

    public long getTimeout() {
        return timeout;
    }

    @Override
    public void run() {
        command.execute(handler);
        markAsCompleted();
    }

}
