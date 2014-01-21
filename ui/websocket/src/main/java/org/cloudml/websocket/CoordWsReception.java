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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudml.facade.mrt.CommandReception;
import org.cloudml.facade.mrt.Coordinator;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

/**
 *
 * @author huis
 */
public class CoordWsReception extends WebSocketServer implements CommandReception{
    
    Coordinator coord = null;
    List<WebSocket> activeComponents = new ArrayList<WebSocket>();

    public CoordWsReception(int port){
        this(port, null);
    }
    
    public CoordWsReception(int port, Coordinator coord){
        super(
                new InetSocketAddress(port),
                Collections.singletonList((Draft)new Draft_17())
              );
        this.coord = coord;
    }
    
    @Override
    public void onOpen(WebSocket ws, ClientHandshake ch) {
        System.out.printf("A component is connected from: %s\n", ws.getRemoteSocketAddress());
        activeComponents.add(ws);
        ws.send(String.format("!connected\n  yourID : %s\n", ws.getRemoteSocketAddress()));
    }

    @Override
    public void onClose(WebSocket ws, int i, String string, boolean bln) {
        System.out.printf("%s left.\n", ws.getRemoteSocketAddress());
        activeComponents.remove(ws);
    }

    @Override
    public void onMessage(WebSocket ws, String string) {
            System.out.printf("%s said: %s\n", ws.getRemoteSocketAddress(), string);
            Object ret = coord.process(string, new WsPeerStub(ws));
            if(ret != null)
                ws.send(ret.toString());
    }

    @Override
    public void onError(WebSocket ws, Exception excptn) {
        excptn.printStackTrace();
    }
    
    public Coordinator getCoordinator(){
        return coord;
    }
    
}
