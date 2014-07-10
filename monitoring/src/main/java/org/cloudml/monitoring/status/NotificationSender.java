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
import org.cloudml.mrt.PeerStub;
import org.cloudml.mrt.cmd.CmdWrapper;
import org.cloudml.mrt.sample.SystemOutPeerStub;


import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Francesco di Forenza
 */

public class NotificationSender {
    private static final Logger journal = Logger.getLogger(StatusMonitor.class.getName());

    /**
     * Update the status in the model
     *
     * @param name      name of the machine
     * @param newStatus status
     * @param coord     to interact with mrt
     */
    public static void updateUsingFacade(String name, ComponentInstance.State newStatus, Coordinator coord) {
            /*
            //Add a listener which collects and prints the changes every 0.5 second.
            PeerStub observer = new SystemOutPeerStub("Observer");
            Object res;
            res = coord.process("!listenToAny ", observer);
            journal.log(Level.INFO, res.toString());
            */
        updateStatus(name, newStatus, coord, StatusMonitor.class.getName());


    }

    //TODO move this method to MRT and try to remove the need to set up the coordinator
    //A singleton set one time by the deployer should work fine
    public static void updateStatus(String name, ComponentInstance.State newStatus, Coordinator coord, String identity) {
        //A PeerStub identifies who launches the modifications
        PeerStub committer = new SystemOutPeerStub(identity);

        //A wrapper hides the complexity of invoking the coordinator
        CmdWrapper wrapper = new CmdWrapper(coord, committer);

        //Update the value of status
        try {
            wrapper.eSet("/componentInstances[name='" + name + "']", wrapper.makePair("status", "" + newStatus.toString() + ""));
            journal.log(Level.INFO, "Status of: " + name + " changed in: " + newStatus + "");
            journal.log(Level.INFO, "Updating the model..");
        }
        catch (org.apache.commons.jxpath.JXPathNotFoundException e){
           //The exception is triggered when the machine doesn't exist in the model
           // this is because the monitoring can retrieves the status of ALL the machine
           // deployed with the the specific set of credentials and
           // NOT ONLY the ones in the deployment model
           //
           // Extra problems arise with Openstack and Jclouds since the name in the model
           // is not the name of the VMs (that is unique) but the name specified in the json
           // in case the user deploy two times with the same json the system will
           // update the status two times

           // Example
           // First deploy
           // Instance name: my_machine_53f  MetaName: my_machine
           // Second deploy
           // Instance name: my_machine_124  MetaName: my_machine
           // (the name is set by Openstack the MetaName is set by user)
           // Since in the model is used the MetaName the monitoring will consider to times the machine
          ;
        }
    }


    //this method simply print the changes of the status and can be removed if no longer useful for any demo
    public static void commandLinePrinter(String name, ComponentInstance.State newStatus, ComponentInstance.State oldStatus) {
        System.out.println("Server name: " + name + " is " + newStatus + " before was " + oldStatus);

    }
}
