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
package org.cloudml.mrt.coord;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.Provider;
import org.cloudml.core.Property;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException, IOException
    {
        int port = 9000;
        if(args.length >= 1)
            port = Integer.parseInt(args[0]);
        
        Coordinator coord = new Coordinator();
        initModel(coord.executor.repo.root);
        coord.start(port);
        
//        TestClient client = new TestClient("c1", "ws://127.0.0.1:9000");
//        client.connect();
//        
//        Thread.sleep(1000);
//        
//        client.send("hello");
//        client.send("get-snapshot");
//        client.send("get-snapshot");
//        
//        
//        Thread.sleep(1000);
        //client.close();
        //reception.stop();
    }
    
    public static void initModel(DeploymentModel dm){
        Provider hugeProvider = new Provider("huge", "./credentials");
        Provider bigsmallProvider = new Provider("bigsmall","./credentials");
        dm.getProviders().add(hugeProvider);
        dm.getProviders().add(bigsmallProvider);
        
        Node huge = new Node("huge");
        huge.setProvider(hugeProvider);
        Node big = new Node("big");
        big.setProvider(bigsmallProvider);
        Node small = new Node("small");
        small.setProvider(bigsmallProvider);
        
        dm.getNodeTypes().put("huge", huge);
        dm.getNodeTypes().put("big", big);
        dm.getNodeTypes().put("small", small);
        
        for(int i = 0; i < 5; i++)
            dm.getNodeInstances().add(huge.instanciates("huge"+i));
        
        for(int i = 0; i < 10; i++)
            dm.getNodeInstances().add(big.instanciates("big"+i));
        
        for(int i = 0; i< 10; i++)
            dm.getNodeInstances().add(small.instanciates("small"+i));
        
        for(NodeInstance ni : dm.getNodeInstances()){
            ni.getProperties().add(new Property("state","on"));
        }
        
    }
    
    
      public static void initWithFaked(DeploymentModel root){
        Provider provider = new Provider("provider","");
        root.getProviders().add(provider);
        Node node1 = new Node("node1");
        node1.setProvider(provider);
        root.getNodeTypes().put("node1",node1);
        root.getNodeInstances().add(node1.instanciates("ni11"));
        root.getNodeInstances().add(node1.instanciates("ni12"));
        
        Node node2 = new Node("node2");
        node2.setProvider(provider);
        root.getNodeTypes().put("node2", node2);
        
        root.getNodeInstances().add(node2.instanciates("ni21"));
        
        
//        Artefact artefact1 = new Artefact("artefact1");
//        root.getArtefactTypes().put("artefact1",artefact1);
//        
//        root.getArtefactInstances().add(artefact1.instanciates("ai11"));
//        root.getArtefactInstances().add(artefact1.instanciates("ai12"));
        
       
        for(NodeInstance ni : root.getNodeInstances()){
            ni.getProperties().add(new Property("state","onn"));
        }
        for(ArtefactInstance ai : root.getArtefactInstances()){
            ai.getProperties().add(new Property("state","onn"));
        }
    }
    
    public static void initWithSenseApp(DeploymentModel root){
        JsonCodec jsonCodec = new JsonCodec();
        try {
            root = (DeploymentModel) jsonCodec.load(new FileInputStream("sensappAdmin.json"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ModelRepo.class.getName()).log(Level.SEVERE, null, ex);
            initWithFaked(root);
        }
        
        for(NodeInstance ni : root.getNodeInstances()){
            ni.getProperties().add(new Property("state","onn"));
        }
        for(ArtefactInstance ai : root.getArtefactInstances()){
            ai.getProperties().add(new Property("state","onn"));
        }
    }



}


class TestClient extends WebSocketClient{

    String name;
    public TestClient(String name, String uri){
        super(URI.create(uri), new Draft_17());
        this.name = name;
    }

    @Override
    public void onOpen(ServerHandshake sh) {

    }

    @Override
    public void onMessage(String string) {
        System.out.printf("I got this: '%s'\n", string);
    }

    @Override
    public void onClose(int i, String string, boolean bln) {

    }

    @Override
    public void onError(Exception excptn) {
        excptn.printStackTrace();
    }

}