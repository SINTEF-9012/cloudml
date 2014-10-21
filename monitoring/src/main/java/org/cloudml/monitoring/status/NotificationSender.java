package org.cloudml.monitoring.status;
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


import org.cloudml.core.ComponentInstance;
import org.cloudml.mrt.Coordinator;

/**
 * @author Francesco di Forenza
 * this class contatins all the possible way in which a change in the status can be notified
 */

public class NotificationSender {

    /**
     * Update the status in the model
     *
     * @param name      name of the machine
     * @param newStatus status
     * @param coord     to interact with mrt
     */
    public static void updateUsingFacade(String name, ComponentInstance.State newStatus, Coordinator coord) {
        coord.updateStatus(name, newStatus.toString(), StatusMonitor.class.getName());
    }

    //this method simply print the changes of the status and can be removed if no longer useful for any demo
    public static void commandLinePrinter(String name, ComponentInstance.State newStatus, ComponentInstance.State oldStatus) {
        System.out.println("Server name: " + name + " is " + newStatus + " before was " + oldStatus);

    }
}
