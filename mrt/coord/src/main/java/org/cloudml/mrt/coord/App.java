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

import java.io.IOException;
import java.net.URI;
import org.cloudml.mrt.coord.ws.CoordWsReception;
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