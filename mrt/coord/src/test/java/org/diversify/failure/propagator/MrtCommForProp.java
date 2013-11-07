/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diversify.failure.propagator;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

/**
 *
 * @author huis
 */
public class MrtCommForProp extends WebSocketClient{
    public FailurePropagator propagator = null;

    public MrtCommForProp(String endpoint) {
        super(URI.create(endpoint), new Draft_17());
    }

    @Override
    public void onOpen(ServerHandshake sh) {
        System.out.printf("%s opened", sh.getHttpStatus());
    }
    
    @Override
    public void send(String text){
        System.out.println("I want to send this:");
        System.out.println(text + "\n");
        super.send(text);
    }

    @Override
    public void onMessage(String string) {
        System.out.println("I got this:");
        System.out.println(string + "\n");
        
        propagator.returnValue = string;
        if(string.startsWith(FailurePropagator.GET_SNAPSHOT_HEAD)){
            synchronized(FailurePropagator.GET_SNAPSHOT_HEAD){
                FailurePropagator.GET_SNAPSHOT_HEAD.notifyAll();
            }
        }
        else if(string.trim().startsWith("!updated")){
            propagator.onUpdated(string);
        }
    }

    @Override
    public void onClose(int i, String string, boolean bln) {
        
    }

    @Override
    public void onError(Exception excptn) {
        Logger.getLogger(FailurePropagator.class.getName()).log(Level.SEVERE, null, excptn);
       
    }
    
    
}
