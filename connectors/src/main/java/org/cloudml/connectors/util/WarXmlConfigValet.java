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

package org.cloudml.connectors.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudml.connectors.ConnectorFactory;
import org.cloudml.core.Component;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.ExternalComponent;
import org.cloudml.core.ExternalComponentInstance;
import org.cloudml.core.Property;
import org.cloudml.core.RelationshipInstance;
import org.cloudml.core.Resource;

/**
 * This configuration agent works on war files
 * 
 * It takes a target "WAR" component, a source component that contains the
 * required configuration value (such as a standalone database), and a resource
 * that contains the configuration pairs (usually carried by a {@link Relationship).
 * 
 * The properties carried by the configuration resource are in the following format:
 * 
 * 1. path_{name} -> {entry}::{xpath} : config the given xml element by {xpath} inside
 *    the entry {entry}, the value with be specified in value_{name} with the same name
 * 2. value_{name} -> string_with_embedded queries: where the queries obtain the
 *    values from the any cloudml elements that are reachable from the resource or
 *    the relationship instance (usually from the component instance
 *    e.g., "jdbc:mysql://@instance{providedEnd/owner/value/publicAddress}"
 * 
 * @author Hui Song
 */
public class WarXmlConfigValet extends ConfigValet {
    
    private ComponentInstance warCompInst;
    private Resource configResource;
    private RelationshipInstance relationshipInstance;
    
    public WarXmlConfigValet(RelationshipInstance relationshipInstance, Resource configResource){
        this.relationshipInstance = relationshipInstance;
        this.warCompInst = relationshipInstance.getRequiredEnd().getOwner().get();
        this.configResource = configResource;
    }

    @Override
    public void config() {
        
        ComponentInstance warCompi = this.warCompInst;
        Component warComp = warCompi.getType();
        
        
        ZipModifier zipModifier = new ZipModifier(
                warComp.getProperties().valueOf("warfile"), 
                warComp.getProperties().valueOf("temp-warfile")
        );
        
        Map<String,Map<String, String>> entryKv = new HashMap<String,Map<String,String>>();
        for(Property prop : configResource.getProperties()){
            String name = prop.getName();
            if("valet".equals(name) || "path".equals(name))  // they are preserved keywords
                continue;
            if(name.startsWith("path")){
                String entry_path_value = CloudMLQueryUtil.cloudmlStringRecover(
                        prop.getValue(), 
                        configResource, 
                        relationshipInstance
                );
                String[] entry_path = entry_path_value.split("::");
                if(!entryKv.containsKey(entry_path[0]))
                    entryKv.put(entry_path[0], new HashMap<String,String>());
                Map<String,String> kv = entryKv.get(entry_path[0]);
                
                String value_value = configResource.getProperties()
                        .valueOf(name.replaceFirst("path","value"));
                String value = CloudMLQueryUtil.cloudmlStringRecover(
                                    value_value, 
                                    configResource, 
                                    relationshipInstance
                                );
                
                if(value != null){
                    kv.put(entry_path[1], value);
                }
            }
        }

        try{
           
            zipModifier.updateXMLElement(entryKv);  

        }catch(Exception ex){
            ex.printStackTrace();
            throw new RuntimeException("Error in writing XML configurations in the WAR file", ex);
        }
    }
    
}
