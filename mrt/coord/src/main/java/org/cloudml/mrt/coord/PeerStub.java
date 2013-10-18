/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cloudml.mrt.coord;

/**
 *
 * @author huis
 */
public abstract class PeerStub {
    
    public abstract String getID();
    
    @Override
    public boolean equals(Object other){
        try{
            return getID().equals(((PeerStub) other).getID());
        }
        catch(Exception e){
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getID().hashCode();
    }
    
    public abstract void sendMessage(Object message);    
    
}
