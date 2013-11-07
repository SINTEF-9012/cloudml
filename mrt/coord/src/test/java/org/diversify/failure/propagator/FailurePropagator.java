/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diversify.failure.propagator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.diversify.failure.common.MrtUtil;
import static org.diversify.failure.generator.Transformer.FAILED;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author huis
 */
public class FailurePropagator {
    public static int INVOCATION_TIMEOUT = 5000;
    public static final String GET_SNAPSHOT_HEAD = "###return of GetSnapshot###";
    
    public String returnValue = null;
            
    MrtCommForProp mrt = null;    
    Map<String, String> hosted = new HashMap<String, String>();
    
    public void createHostingMap(){
        try {
            returnValue = null;
            hosted.clear();
            mrt.send("!getSnapshot \n  path : /artefactInstances");
            synchronized(GET_SNAPSHOT_HEAD){
                GET_SNAPSHOT_HEAD.wait(INVOCATION_TIMEOUT);
            }
            Yaml yaml = new Yaml();
            Map obj = (Map) yaml.loadAs(MrtUtil.removeYamlTag(returnValue), Map.class);
            List<Map> artefacts = (List<Map>) obj.get("content");
            for(Map m : artefacts){
                try{
                    String arteName = m.get("name").toString();
                    Map destination = (Map)m.get("destination");
                    String nodeName = destination.get("name").toString();
                    hosted.put(arteName, nodeName);
                }catch(Exception e){
                    continue;
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(FailurePropagator.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
   
    public void onUpdated(String message){
        Yaml yaml = new Yaml();
        Map change = yaml.loadAs(message, Map.class);
        if("failed".equals(change.get("newValue")) && 
                "value".equals(change.get("property")) &&
                change.get("parent").toString().contains("state")
                ){
            String path = (String) change.get("parent");
            List<String> tofail = new ArrayList<String>();
            String id = StringUtils.substringBetween(path, "Instances[name='", "']");
            for(Map.Entry<String, String> entry : hosted.entrySet()){
                if(id.equals(entry.getValue())){
                    String art = entry.getKey();
                    tofail.add(art);
                }
            }
            if(tofail.size()!=0)
                issueFailCommand(tofail);
        }
    }
    
 
    
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
        sb.append(String.format("      parent: /%s[name='%s']/properties[name='state']\n", "artefactInstances", id));
        sb.append(String.format("      keyValues : \n"));
        sb.append(String.format("        value : %s \n", FAILED));
        return sb.toString();
    }
    
    public void listenToAny(){
        mrt.send("!listenToAny");
    }
    
    public static void main(String[] args) throws IOException, InterruptedException{
        Properties props = new Properties();
        props.load(new FileInputStream("conf.prop"));
        String endPoint = props.getProperty("server.uri");
        MrtCommForProp mrt = new MrtCommForProp(endPoint);
                
        FailurePropagator propagator = new FailurePropagator();
        propagator.mrt = mrt;
        mrt.propagator = propagator;
        
        mrt.connectBlocking();
        
        propagator.createHostingMap();
        propagator.listenToAny();
        
        System.out.println(propagator.hosted);
        
    }
    
    
    
}
