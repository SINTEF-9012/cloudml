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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.CloudMLElement;

import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.Property;
import org.cloudml.core.Provider;

/**
 * Unit test for simple 
 */
public class AppTest 
extends TestCase
{
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public AppTest( String testName )
	{
		super( testName );
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite()
	{
		return new TestSuite( AppTest.class );
	}

	/**
	 * Rigourous Test :-)
	 */
	public static void main(String[] args) throws InterruptedException, FileNotFoundException
	{
//		int port=9001;
//		Coordinator c=new Coordinator();
//		c.start(port);
//		
//		CoordWsClient client=new CoordWsClient("c1", "ws://127.0.0.1:9001");
//		client.connectBlocking();
//		System.out.println(client.getReadyState()+"");
//		assertTrue(client.getReadyState().equals(READYSTATE.OPEN));
//
//		
//		client.send("!getSnapshot\n  path : /");
//		
//		client.send("!listenToAny");
//		Thread.sleep(500);
//		client.close();
        
        JsonCodec codec = new JsonCodec();   
        
        
        DeploymentModel dm = new DeploymentModel("root");
        Provider provider = new Provider("provider");
        provider.setCredentials("");
        dm.getProviders().add(provider);
        //dm.getProviders()
		Node node1 = new Node("node1");
        node1.setProvider(provider);
		dm.getNodeTypes().put("node1", node1);
		NodeInstance ni = new NodeInstance("ni",node1);
		
        //NodeInstance ni = node1.instanciates("ni");
        dm.getNodeInstances().add(ni);
		//Artefact arte = new Artefact("at1");
		
		//dm.getArtefactTypes().put("at1", arte);
		//dm.getArtefactInstances().add(new ArtefactInstance("atr1", arte, ni));
		//System.out.println(dm);
        System.out.println("-------------");
        codec.save(dm, System.out);
        System.out.println("-------------");
        
        
        //CloudMLElement elem = codec.load(new FileInputStream("C:\\temp\\sensappAdmin.json"));
        //System.out.println(elem);
       // codec.save(elem, System.out);
	}

}