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
package org.cloudml.facade.util;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nicolasf on 19.02.15.
 */
public class WSClient extends WebSocketClient {

    private Boolean connected=false;
    private static final Logger journal = Logger.getLogger(WSClient.class.getName());

    public WSClient(URI serverURI) throws InterruptedException {
        super(serverURI,new Draft_17());
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        journal.log(Level.INFO, ">> Connected to the CloudML server");
        connected=true;
    }

    @Override
    public void onMessage(String s) {
        journal.log(Level.INFO, ">> "+s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        journal.log(Level.INFO, ">> Disconnected from the CloudML server "+s);
        connected=false;
        this.close();
    }

    @Override
    public void onError(Exception e) {

    }

    public Boolean getConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }
}
