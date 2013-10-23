/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cloudml.mrt.coord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.cloudml.mrt.coord.cmd.abstracts.Listener;

/**
 *
 * @author huis
 */
public class NodificationCentre {
    Coordinator coordinator;
    Map<Listener, PeerStub> listeners = new HashMap<Listener, PeerStub>();
    long lastCheck = 0;
    
    public void addListener(Listener listener, PeerStub peer){
        listeners.put(listener, peer);
        System.out.printf("----------Listener added: %s", listener);
    }
    
    public void checkAndNotify(){
        int start = Math.abs(Collections.binarySearch(coordinator.changeList, Change.fakeChangeForSearch(lastCheck))+1);
        for(int i = start; i < coordinator.changeList.size(); i ++){
            Change chng = coordinator.changeList.get(i);
            for(Map.Entry<Listener,PeerStub> entry : listeners.entrySet()){
                if(entry.getKey().careFor(chng))
                    entry.getValue().sendMessage(chng.obtainRepr());
            }
            
        }
        lastCheck = System.currentTimeMillis();
    }

    void removeListener(Listener listener) {
        listeners.remove(listener);
    }
    
    
    public static class ListeningTarget{
        PeerStub peer;
        String id;
        Object object;
        String property;
        
        public ListeningTarget(PeerStub peer, String id, Object object, String property){
            this.peer = peer;
            this.id = id;
            this.object = object;
            this.property = property;
            
        }
    }
    
    public void startListening(){
        new Thread(){
            @Override
            public void run(){
                while(true){
                    try {
                        sleep(500);
                        checkAndNotify();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(NodificationCentre.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }
}
