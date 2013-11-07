/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diversify.failure.propagator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * NOT USED!
 * @author huis
 */
public class TargetModel {
    public static final String ON = "onn";
    public static final String OFF = "offf";
    public static final String FAILED = "failed";
    
    public Map<String, String> states = new HashMap<String, String>();
    
    /**
     * dependent(a) is the list of all elements that depends on a. In other 
     * words, if a fails, all elements in dependent(a) fails.
     */
    public Map<String, List<String>> dependent = new HashMap<String, List<String>>();
    
    public Map<String, List<String>> hosting = new HashMap<String, List<String>>();
    
    private Set<String> visited = new HashSet<String>();
    private Set<String> failed = new HashSet<String>();
    
    public void clear(){
        states.clear();
        dependent.clear();
        hosting.clear();
    }
    
    public List<String> fail(String id){
        visited.clear();
        failed.clear();
        return new ArrayList<String>(failed);
    }
    
    public void _fail(String id){
        if(visited.contains(id))
            throw new RuntimeException("dependency loop");
        visited.add(id);
        if(!FAILED.equals(states.get(id))){
            states.put(id, FAILED);
            failed.add(id);
            for(String s : dependent.get(id)){
                _fail(s);
            }
        }
    }
    
}
