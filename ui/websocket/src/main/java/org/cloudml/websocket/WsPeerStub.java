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

package org.cloudml.websocket;

import org.cloudml.facade.mrt.PeerStub;
import org.cloudml.mrt.cmd.gen.CloudMLCmds;
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
        try{
        webSocket.send(yaml.dump(message));
        }
        catch(Exception ex){
            
        }
    }
    
}
