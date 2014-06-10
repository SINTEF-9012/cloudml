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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cloudml.facade.mrt.sample;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudml.connectors.JCloudsConnector;
import org.cloudml.facade.mrt.Coordinator;
import org.cloudml.facade.mrt.PeerStub;
import org.cloudml.facade.mrt.cmd.CmdWrapper;

/**
 *
 * @author huis
 */
public class MrtModificationSample {
    private static final Logger journal = Logger.getLogger(JCloudsConnector.class.getName());
    
    public static void main(String[] args){
        
        sampleProgrammatic();
    }
    
    public static void sampleProgrammatic(){
        //Start a coordinator, which is the main wrapper of the cloudml model
        Coordinator coord = new Coordinator("sample://sensApp");
        journal.log(Level.INFO, "started");
        coord.start();
        
        //Add a listener which collects and prints the changes every 0.5 second.
        PeerStub observer = new SystemOutPeerStub("Observer");
        Object res = null;
        res = coord.process("!listenToAny ", observer);
        journal.log(Level.INFO, res.toString());
        
        
        // Now starts the code to get and set attributes and properties.
        
        //A PeerStub identifies who launches the modifications
        PeerStub committer = new SystemOutPeerStub("Committer");
        
        //A wrapper hides the complexity of invoking the coordinator
        CmdWrapper wrapper = new CmdWrapper(coord, committer);
        
        //Get a single property: Check the value before update
        res = wrapper.eGet("/componentInstances[name='sensapp-sl1']/status");
        journal.log(Level.INFO, res.toString());
        
        //Set a single property
        wrapper.eSet("/componentInstances[name='sensapp-sl1']", wrapper.makePair("status", "RUNNING"));
        
        //Check the updated value
        res = wrapper.eGet("/componentInstances[name='sensapp-sl1']/status");
        journal.log(Level.INFO, res.toString());
        
        //Set multiple properties together
        wrapper.eSet(
                    "/componentInstances[name='sensapp-sl1']", 
                    wrapper.makePair("properties/cpu", "80"),
                    wrapper.makePair("properties/cpu-timestamp", "13439083")
                );
        
        //Get multiple properties together
        res = wrapper.eGet(
                    "/componentInstances[name='sensapp-sl1']",
                    "properties/cpu",
                    "properties/cpu-timestamp",
                    "status"
                );
        journal.log(Level.INFO, res.toString());
        
        
    }
    
    public static void sampleByString(){
        //Start a coordinator, which is the main wrapper of the cloudml model
        Coordinator coord = new Coordinator("sample://sensApp");
        journal.log(Level.INFO, "started");
        coord.start();
        
        //Add a listener which collects and prints the changes every 0.5 second.
        PeerStub observer = new SystemOutPeerStub("Observer");
        Object res = null;
        res = coord.process("!listenToAny ", observer);
        journal.log(Level.INFO, res.toString());
        
        
        PeerStub stub = new SystemOutPeerStub("SampleUser");
        
        //Query the status of a component instance named 'sensapp-sl1'
        res = coord.process("!getSnapshot { path : \"/componentInstances[name='sensapp-sl1']/status\" } ", stub);
        journal.log(Level.INFO, res.toString());
        
        //Update the value of status
        res = coord.process(
               "!commit { "
                + "modifications: ["
                +   "!set { "
                    + "parent : \"/componentInstances[name='sensapp-sl1']\", "
                    + "keyValues : { status : RUNNING } "
                  + "}  "
                + "] "
             + "}", 
                stub
        );
        
        //Query again
        res = coord.process("!getSnapshot { path : \"/componentInstances[name='sensapp-sl1']/status\" } ", stub);
        journal.log(Level.INFO, res.toString());
        
        // Add or update (if exists) two properties: cpu and cpu-timestamp
        res = coord.process("!commit { modifications: [!set { parent : \"/componentInstances[name='sensapp-sl1']\", keyValues : { cpu : 80, cpu-timestamp : 12343480 } }  ] }", stub);
        
        // Query three properties together
        res = coord.process("!getSnapshot { path : \"/componentInstances[name='sensapp-sl1']\" , multimaps : { status : status, cpu : properties/cpu, timestamp : properties/cpu-timestamp } }", stub);
        journal.log(Level.INFO, res.toString());
        
        
        journal.log(Level.INFO, "finished");
    }
    
    
    
    
}

