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
package org.cloudml.coord;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.cloudml.mrt.coord.Coordinator;
import org.cloudml.mrt.coord.cmd.gen.CloudMLCmds;
import org.cloudml.mrt.coord.ws.CoordWsClient;
import org.cloudml.mrt.coord.ws.CoordWsReception;
import org.java_websocket.WebSocket.READYSTATE;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.yaml.snakeyaml.Yaml;

/**
 * Unit test for simple App.
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
	public void testCoordReception() throws InterruptedException
	{
		int port=9000;
		Coordinator c=new Coordinator();
		c.start(port);
		CoordWsClient client=new CoordWsClient("c1", "ws://127.0.0.1:9000");
		client.connectBlocking();
		assertTrue(client.getReadyState().equals(READYSTATE.OPEN));
		
		client.send("!getSnapshot\n  path : /");
		
		Thread.sleep(2000);
		
		client.send("!listenToAny");
		
		Thread.sleep(2000);
		
		client.close();
	}

}