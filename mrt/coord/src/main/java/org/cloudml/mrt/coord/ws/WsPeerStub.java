/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cloudml.mrt.coord.ws;

import org.cloudml.mrt.coord.PeerStub;
import org.cloudml.mrt.coord.cmd.gen.CloudMLCmds;
import org.java_websocket.WebSocket;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author huis
 */
public class WsPeerStub extends PeerStub{
    
    WebSocket webSocket = null;
    String id = null;
    
    public WsPeerStub(WebSocket webSocket){
        this.webSocket = webSocket;
        id = webSocket.getRemoteSocketAddress().toString();
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public void sendMessage(Object message) {
        Yaml yaml = CloudMLCmds.INSTANCE.getYaml();
        webSocket.send(yaml.dump(message));
    }
    
}
