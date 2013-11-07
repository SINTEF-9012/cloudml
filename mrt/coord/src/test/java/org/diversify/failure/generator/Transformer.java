/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diversify.failure.generator;

import static java.lang.Thread.sleep;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.diversify.failure.common.MrtUtil;
import static org.diversify.failure.common.MrtUtil.removeYamlTag;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author huis
 */
public class Transformer {
    public static final String ON = "onn";
    public static final String OFF = "offf";
    public static final String FAILED = "failed";
    
    public static final String NODE_INSTANCE = "nodeInstances";
    public static final String ARTEFACT_INSTANCE = "artefactInstances";
    
    Map<String, String> elemTypes = new HashMap<String, String>();
    public MrtComm mrt = null;
    String returnValue = null;
    
    public void issueFailCommand(List<String> ids){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("!commit\n"));
        sb.append(String.format("  modifications: \n"));
        for(String id : ids){
            sb.append(failElementModification(id));
        }
        mrt.send(sb.toString());
    }
    
    
    public String failElementModification(String id){
        StringBuilder sb = new StringBuilder();
        
        sb.append(String.format("    - !set\n"));
        sb.append(String.format("      parent: /%s[name='%s']/properties[name='state']\n", elemTypes.get(id), id));
        sb.append(String.format("      keyValues : \n"));
        sb.append(String.format("        value : %s \n", FAILED));
        return sb.toString();
    }
    
    public Map<String, String> checkState(int timeout){
         Map<String, String> states = new HashMap<String, String>();
         elemTypes.clear();
         _checkNodeOrArtefactStates(timeout, states, NODE_INSTANCE);
         _checkNodeOrArtefactStates(timeout,states, ARTEFACT_INSTANCE);
         return states;
    }
    
    void _checkNodeOrArtefactStates(int timeout, Map<String,String> states, String nodeOrArtefact){       
        
        returnValue = null;
        mrt.send(String.format("!getSnapshot \n  path : /%s", nodeOrArtefact));
        
        while(timeout-- > 0){
            try {
                sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Transformer.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(returnValue == null)
                continue;
            Yaml yaml = new Yaml();
            if(!returnValue.startsWith("###return of GetSnapshot###"))
                continue;
            Map<String, List<Map>> result = (Map<String, List<Map>>) yaml.loadAs(removeYamlTag(returnValue), Map.class);
            for(Map m : result.get("content")){
                String name = m.get("name").toString();
                String state = null;
                List<Map> properties = (List<Map>) m.get("properties");
                for(Map p: properties){
                    if("state".equals(p.get("name").toString()))
                        state = p.get("value").toString();
                }
                states.put(name, state);
                elemTypes.put(name, nodeOrArtefact);
            }
            break;
        }
    }
    

    
    public void receiveReturnMessage(String s){
        returnValue = s;
    }
}
