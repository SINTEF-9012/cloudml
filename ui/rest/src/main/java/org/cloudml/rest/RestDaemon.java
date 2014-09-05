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
package org.cloudml.rest;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.UriBuilder;
import org.cloudml.facade.FacadeBridge;
import org.cloudml.mrt.Coordinator;
import org.cloudml.mrt.PeerStub;
import org.cloudml.mrt.sample.SystemOutPeerStub;
import org.cloudml.rest.resources.CommitResource;
import org.cloudml.rest.resources.QueryResource;
import org.cloudml.rest.resources.UniversalResource;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author Hui Song
 */
public class RestDaemon {
    
    public static PeerStub commonStub = new SystemOutPeerStub("test");
    
    public static void main(String[] args){
        
        int port = 9002;
        if(args.length >= 1)
            port = Integer.parseInt(args[0]);
        
        if(Coordinator.SINGLE_INSTANCE == null){
            Coordinator coord = new Coordinator();
            coord.setModelRepo(new FacadeBridge());
            Coordinator.SINGLE_INSTANCE = coord;
            coord.start();
            
            //Only for test purpose
            coord.process("!extended { name : LoadDeployment, params : ['sample://sensapp'] }", commonStub);
        }
                
        
        
        URI uri = UriBuilder.fromUri("http://0.0.0.0/").port(port).build();
        ResourceConfig resourceConfig = new ResourceConfig(QueryResource.class);
        resourceConfig.register(UniversalResource.class);
        resourceConfig.register(CommitResource.class);
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, resourceConfig);
        
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RestDaemon.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
