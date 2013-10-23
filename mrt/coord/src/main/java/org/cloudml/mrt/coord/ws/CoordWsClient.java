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
