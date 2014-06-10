package org.cloudml.monitoring.util;
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


import org.cloudml.connectors.JCloudsConnector;
import org.cloudml.core.ComponentInstance;
import org.cloudml.facade.mrt.Coordinator;
import org.cloudml.facade.mrt.PeerStub;
import org.cloudml.facade.mrt.cmd.CmdWrapper;
import org.cloudml.facade.mrt.sample.SystemOutPeerStub;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Francesco di Forenza
 */

public class NotificationSender {
    private static final Logger journal = Logger.getLogger(JCloudsConnector.class.getName());

    /**
     * Update the status in the model
     *
     * @param name name of the machine
     * @param newStatus status
     */
    public static void updateUsingFacade(String name, ComponentInstance.State newStatus) {

        //Start a coordinator, which is the main wrapper of the cloudml model
        Coordinator coord = new Coordinator("sample://sensApp");
        journal.log(Level.INFO, "started");
        coord.start();

        //Add a listener which collects and prints the changes every 0.5 second.
        PeerStub observer = new SystemOutPeerStub("Observer");
        Object res;
        res = coord.process("!listenToAny ", observer);
        journal.log(Level.INFO, res.toString());

        //A PeerStub identifies who launches the modifications
        PeerStub committer = new SystemOutPeerStub("Monitoring");

        //A wrapper hides the complexity of invoking the coordinator
        CmdWrapper wrapper = new CmdWrapper(coord, committer);

        //TODO the machine must exist to set the property

        //Update the value of status
        wrapper.eSet("/componentInstances[name='" + name + "']", wrapper.makePair("status", newStatus));

        //Check the updated value
        res = wrapper.eGet("/componentInstances[name='" + name + "']/status");
        journal.log(Level.INFO, res.toString());

    }

    //this method simply print the changes of the status and can be removed if no longer useful for any demo
    public static void commandLinePrinter(String name, ComponentInstance.State newStatus, ComponentInstance.State oldStatus) {
        System.out.println("Server name: " + name + " is " + newStatus + " before was " + oldStatus);

    }
}
