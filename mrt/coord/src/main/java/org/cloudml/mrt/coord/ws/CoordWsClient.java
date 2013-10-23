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
package org.cloudml.mrt.coord.ws;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

/**
 * Generic web socket client
 * @author nicolasf
 *
 */
public class CoordWsClient extends WebSocketClient {

	private String name;

	public CoordWsClient(String name,String endPoint){
		super(URI.create(endPoint), new Draft_17());
		this.name=name;
	}

	@Override
	public void onClose(int arg0, String arg1, boolean arg2) {

	}

	@Override
	public void onError(Exception arg0) {
		arg0.printStackTrace();
	}

	@Override
	public void onMessage(String msg) {
		System.out.println("I got this:"+msg);
	}

	@Override
	public void onOpen(ServerHandshake sh) {
		System.out.println("Connection open: "+sh.getHttpStatus());
	}

}
