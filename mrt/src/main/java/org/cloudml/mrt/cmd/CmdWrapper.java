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

package org.cloudml.mrt.cmd;

import java.util.Arrays;
import java.util.HashMap;

import org.cloudml.mrt.Coordinator;
import org.cloudml.mrt.PeerStub;
import org.cloudml.mrt.cmd.abstracts.Modification;
import org.cloudml.mrt.cmd.abstracts.Property;
import org.cloudml.mrt.cmd.abstracts.XPath;
import org.cloudml.mrt.cmd.gen.Commit;
import org.cloudml.mrt.cmd.gen.GetSnapshot;
import org.cloudml.mrt.cmd.gen.Set;

/**
 *
 * @author huis
 */
public class CmdWrapper {
    protected Coordinator coord = null;
    protected PeerStub stub = null;
    
    public CmdWrapper(Coordinator coord, PeerStub stub){
        this.coord = coord;
        this.stub = stub;
    }
    
    public Object eGet(String path){
        try{
            GetSnapshot gs = new GetSnapshot();
            gs.path = new XPath(path);
            return coord.process(gs, stub);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public Object eGet(String path, KeyImagePair... pair){
        GetSnapshot gs = new GetSnapshot();
        gs.path = new XPath(path);
        gs.multimaps = new HashMap<String, XPath>();
        for(int i = 0; i < pair.length; i++){
            gs.multimaps.put(pair[i].image, new XPath(pair[i].property));
        }
        
        return coord.process(gs, stub);
        
    }
    
    public Object eGet(String path, String... properties){
        KeyImagePair[] params = new KeyImagePair[properties.length];
        
        for(int i = 0; i < properties.length; i++){
            params[i]= makeImagePair(properties[i],properties[i]);
        }
        
        return eGet(path, params);
    }
    
    public void eSet(String path, KeyValuePair... pair){
        
        Set set = new Set();
        set.parent = new XPath(path);
        set.keyValues = new HashMap<Property, Object>();
        for(int i = 0; i < pair.length; i++){
            set.keyValues.put(new Property(pair[i].property), pair[i].value);
        }
        
        Commit commit = new Commit();
        commit.modifications = Arrays.asList((Modification)set);
        
        
        coord.process(commit, stub);
        
    }
    
    public KeyValuePair makePair(String property, Object value){
        return new KeyValuePair(property, value);
    }
    
    public KeyImagePair makeImagePair(String property, String image){
        return new KeyImagePair(property, image);
    }
            
    public static class KeyValuePair{
        String property;
        Object value;
        public KeyValuePair(String property, Object value){
            this.property = property;
            this.value = value;
        }
    }
    
    public static class KeyImagePair{
        String property;
        String image;
        public KeyImagePair(String property, String image){
            this.property = property;
            this.image = image;
        }
    }
}
