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
 * 1. path -> entry : the particular xml file name inside the war file
 * 2. {name} -> {xpath} : config the given {xpath} with the value obtained from {name}, in particular,
 *    the value is carried as a property in the source component, under the same {name}, i.e., 
 *    value = sourceComponent.getProperty("{name}");
 * 3. #{method} -> {xpath} : similar as above, the the value is obtained by 
 *    invoking the {method} of the source component instance
 * 4. ##{method} -> {xpath} : the value is obtained by invoking {method} on the
 *    type of the source component.
 * 5. (@{name} | @#{method} | @##{method})-prefix -> {value} : use value as the prefix of a 
 *    particular configuration item.
 * 
 * @author Hui Song
 */
public class WarXmlConfigValet extends ConfigValet {
    
    private ComponentInstance warCompInst;
    private Resource configResource;
    private ComponentInstance valueSourceCompInst;
    

    
    public WarXmlConfigValet(ComponentInstance warCompInst, Resource configResource, ComponentInstance valueSourceCompInst){
        this.warCompInst = warCompInst;
        this.configResource = configResource;
        this.valueSourceCompInst = valueSourceCompInst;
    }

    @Override
    public void config() {
        
        ComponentInstance warCompi = this.warCompInst;
        Component warComp = warCompi.getType();
        ExternalComponentInstance dbCompi = (ExternalComponentInstance) this.valueSourceCompInst;
        ExternalComponent dbComp = (ExternalComponent) dbCompi.getType();
        
        ZipModifier zipModifier = new ZipModifier(
                warComp.getProperties().valueOf("warfile"), 
                warComp.getProperties().valueOf("temp-warfile")
        );
        
        Map<String, String> kv = new HashMap<String,String>();
        for(Property prop : configResource.getProperties()){
            String name = prop.getName();
            if("valet".equals(name) || "path".equals(name))  // they are preserved keywords
                continue;
            if(!name.startsWith("@")){
                String value = null;
                if(name.startsWith("#")){
                    try {
                        
                        if(name.startsWith("##"))
                            value = dbComp.getClass().getMethod(name.substring(2))
                                    .invoke(dbComp).toString();
                        else
                            value = dbCompi.getClass().getMethod(name.substring(1))
                                    .invoke(dbCompi).toString();
                    } catch (Exception ex) {
                        Logger.getLogger(WarXmlConfigValet.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                }
                else{
                    value = valueSourceCompInst.hasProperty(name) ? 
                            valueSourceCompInst.getProperties().valueOf(name) :
                            null;
                }
                String prefixName = "@"+name+"-prefix";

                if(configResource.hasProperty(prefixName) && value != null)
                    value = configResource.getProperties().valueOf(prefixName) + value;
                if(value != null){
                    kv.put(prop.getValue(), value);
                }
            }
        }

        try{
            zipModifier.updateXMLElement(configResource.getProperties().valueOf("path"),kv);
        }catch(Exception ex){
            ex.printStackTrace();
            throw new RuntimeException("Error in writing XML configurations in the WAR file", ex);
        }
    }
    
}
