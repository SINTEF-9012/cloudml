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


package org.cloudml.mrt;


import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.Deployment;
import org.cloudml.mrt.cmd.abstracts.Change;
import org.cloudml.mrt.cmd.abstracts.Instruction;
import org.cloudml.mrt.cmd.abstracts.Listener;
import org.cloudml.mrt.cmd.gen.CloudMLCmds;
import org.cloudml.mrt.cmd.gen.Extended;
import org.cloudml.mrt.cmd.gen.Snapshot;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author Hui Song
 */
public class Coordinator {
    
    public static final String ADDITIONAL_PREFIX = "!additional";
    
    CommandReception reception = null;
    //JsonCodec jsonCodec = new JsonCodec();
    CommandExecutor executor = null;    
    List<Change> changeList = new ArrayList<Change>();
    NodificationCentre notificationCentre = new NodificationCentre();
    JsonCodec jsonCodec = new JsonCodec();
    
    Instruction lastInstruction = null;
    
    public Coordinator(){
        FacadeBridge  bridge = new FacadeBridge();
        executor = new CommandExecutor(bridge);
        
    }
    
    public Coordinator(String initModel){
        this();
        Extended extended = new Extended();
        extended.name = "LoadDeployment";
        extended.params = Arrays.asList("sample://sensApp");
        this.process(extended, new PeerStub(){
            @Override
            public String getID() {
                return "RootUser";
            }
            @Override
            public void sendMessage(Object message) {
                System.out.println(String.format("RootUser:>> %s", message));
            }
            
        });
    }
    
    public void start(){
        if(reception !=null)
            reception.start();
        notificationCentre.coordinator = this;
        notificationCentre.startListening();
    }
    
    public void setReception(CommandReception reception){
        this.reception = reception;
    }
    
    public Object process(Instruction inst, PeerStub from){
        //Do something before, such as record every instruction
        this.lastInstruction = inst;
        inst.fromPeer = from.getID();
        return executor.execute(inst, changeList);
        //Do something after, such as...
    }
    
    public Object process(Listener listener, PeerStub from){
        listener.id = listener.id + from.getID();
        if(listener.cancel){
            notificationCentre.removeListener(listener);
        }
        else{
            listener.root = executor.repo.getRoot();
            notificationCentre.addListener(listener, from);
        }
        return null;
    }
    
    public String process(String cmdLiteral, PeerStub from){
        
        if(cmdLiteral.startsWith(ADDITIONAL_PREFIX)){
            String additional = cmdLiteral.substring(ADDITIONAL_PREFIX.length());
            lastInstruction.addAdditional(additional);
            return (String) process(lastInstruction, from);
        }
            
        
        Yaml yaml = CloudMLCmds.INSTANCE.getYaml();

        String ret = "";
        for (Object cmd : yaml.loadAll(cmdLiteral)) {
            Object obj = null;
            if(cmd instanceof Instruction)
                obj = process((Instruction) cmd, from);
            else if(cmd instanceof Listener)
                obj = process((Listener) cmd, from);
            if(obj!=null){
                ret += String.format("###return of %s###\n%s\n", cmd.getClass().getSimpleName(), codec(obj));
            }
        }
        return ret;
    }
    
    public String codec(Object object){
        if(object instanceof Deployment){
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                jsonCodec.save((Deployment)object, baos);
                return baos.toString("UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Coordinator.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        else{
             Snapshot snapshot = new Snapshot();
             snapshot.content = object;
             return CloudMLCmds.INSTANCE.getYaml().dump(snapshot);
        }
            
    }


    
}
