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

package org.cloudml.deployer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathNotFoundException;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.Deployment;
import org.cloudml.core.NamedElement;
import org.cloudml.core.VM;
import org.cloudml.core.VMInstance;
import org.cloudml.core.WithProperties;
import org.cloudml.core.samples.SensApp;

/**
 *
 * @author huis
 */
public class CloudMLElementComparator {
    
    public static class ElementUpdate{
        public Object element = null;
        public String path = null;
        public Object oldValue = null;
        public Object newValue = null;
        
        public ElementUpdate(Object element, String path, Object oldValue, Object newValue){
            this.element = element;
            this.path = path;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }
        
        @Override
        public String toString(){
            Object name = null;
            if(element instanceof NamedElement){
                name = ((NamedElement) element).getName();
            }
            else
                name = element;
            return String.format("%s(%s)::%s is changed from %s to %s", 
                    name, 
                    element.getClass().getSimpleName(),
                    path,
                    oldValue,
                    newValue
            );
        }
        
    }
    
    public static final Map<String, Collection<String>> toCompareDefault = 
            new HashMap<String, Collection<String>>();
    static{
        toCompareDefault.put(
                "VMInstance", 
                Arrays.asList("ips", "status", "type/os", "type/location", 
                        "type/minRam", "type/maxRam", "type/minStorage", 
                        "type/maxStorage", "type/minCores", "type/maxCores"
                ));
        toCompareDefault.put("InternalComponentInstance", 
                Arrays.asList("status"));
        
    }
    
    
    private boolean compareProperties = true;
    private boolean compareResources = false; //Not Supported yet
    private Collection<String> toCompare = null;
    private ArrayList<ElementUpdate> updates = new ArrayList<ElementUpdate>();
    private Object current = null;
    private Object target = null;
    
    public boolean isEmpty(){
        return updates.isEmpty();
    }
    
    public Collection<ElementUpdate> getUpdates(){
        return updates;
    }
    
    public void clean(){
        updates.clear();
    }
    
    public CloudMLElementComparator(Object current, Object target){
        this.current = current;
        this.target = target;
        this.toCompare = toCompareDefault.get(current.getClass().getSimpleName());
        if(compareProperties){
            ArrayList<String> newList = new ArrayList<String>();
            newList.addAll(this.toCompare);
            this.toCompare = newList;
            try{
                for(String s : ((WithProperties)current).getProperties().keySet())
                    if(!toCompare.contains(s))
                        toCompare.add("properties/"+s);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            try{
                for(String s : ((WithProperties)target).getProperties().keySet())
                    if(!toCompare.contains(s))
                        toCompare.add("properties/"+s);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        
        if(compareResources){
            ArrayList<String> newList = new ArrayList<String>();
            newList.addAll(this.toCompare);
            this.toCompare = newList;
            
            
        }
    }
    
    public Collection<ElementUpdate> compare(){
        for(String attr : toCompare){
            Object c = query(current, attr);
            Object t = query(target, attr);
            
            if(c == null){
                if(t == null)
                    continue;
            }
            else if(c.equals(t)){
                continue;
            }
            updates.add(new ElementUpdate(current, attr, c, t));
        }
        return this.updates;
    }
    
    public static Object query(Object obj, String path){
        try{
            JXPathContext jxpc = JXPathContext.newContext(obj);
            return jxpc.getValue(path);
        }
        catch(NullPointerException e){
            return null;
        }
        catch(JXPathNotFoundException e){
            return null;
        }
    }
    
    public static void main(String[] args){
        
        
        
        
    }
    
}
