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
package org.cloudml.websocket;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.Deployment;
import org.cloudml.core.builders.DeploymentBuilder;
import org.cloudml.core.samples.PaasCloudBees;
import org.cloudml.core.samples.SensApp;
import org.cloudml.facade.mrt.Coordinator;
import org.cloudml.facade.mrt.PeerStub;
import org.cloudml.facade.mrt.cmd.gen.Extended;

/**
 * Hello world!
 *
 */
public class Daemon 
{
    public static void main( String[] args )
    {
        int port = 9000;
        if(args.length >= 1)
            port = Integer.parseInt(args[0]);
        
        Coordinator coord = new Coordinator();
        
        CoordWsReception reception = new CoordWsReception(port, coord);
        coord.setReception(reception);
        //coord.setCloudMLRoot(initWithSenseApp());
        //coord.executor.repo.root = initWithMdms();
        
        //initWithSample(coord);

        coord.start();
    }
    
    public static Deployment initWithSenseApp(){
        Deployment root = null;
        JsonCodec jsonCodec = new JsonCodec();
        try {
            root = (Deployment) jsonCodec.load(new FileInputStream("sensappAdmin.json"));
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Daemon.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
//        for(VMInstance ni : root.getExternalComponentInstances()){
//            ni.getProperties().add(new Property("state","onn"));
//        }
//        for(InternalComponentInstance ai : root.getComponentInstances()){
//            ai.getProperties().add(new Property("state","onn"));
//        }
        
    }
    
    public static void initWithSample(Coordinator coord){
        Extended extended = new Extended();
        extended.name = "LoadDeployment";
        extended.params = Arrays.asList("sample://sensApp");
        coord.process(extended, new PeerStub(){

            @Override
            public String getID() {
                return "Root User";
            }

            @Override
            public void sendMessage(Object message) {
                
            }
            
        });
        //coord.
    }
}
