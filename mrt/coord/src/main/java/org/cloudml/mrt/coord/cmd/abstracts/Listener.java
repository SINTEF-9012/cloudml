/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cloudml.mrt.coord.cmd.abstracts;

/**
 *
 * @author huis
 */
public class Listener {
    
    public String id;
    public boolean cancel = false;
    
    public String getID(){
        return id;
    }
    
    public boolean careFor(Change change){
        return _careFor(change);
    }
    
    protected boolean _careFor(Change change){
        return false;
    }
    
        @Override
    public boolean equals(Object other){
        try{
            return getID().equals(((Listener) other).getID());
        }
        catch(Exception e){
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getID().hashCode();
    }
    
}
