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

package org.cloudml.facade.mrt;


import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.CloudMLModel;
import org.cloudml.facade.mrt.cmd.abstracts.Change;
import org.cloudml.facade.mrt.cmd.abstracts.Instruction;
import org.cloudml.facade.mrt.cmd.abstracts.Listener;
import org.cloudml.facade.mrt.cmd.gen.CloudMLCmds;
import org.cloudml.facade.mrt.cmd.gen.Snapshot;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author Hui Song
 */
public class Coordinator {
    
    CommandReception reception = null;
    //JsonCodec jsonCodec = new JsonCodec();
    CommandExecutor executor = null;    
    List<Change> changeList = new ArrayList<Change>();
    NodificationCentre notificationCentre = new NodificationCentre();
    JsonCodec jsonCodec = new JsonCodec();
    
    public Coordinator(){
        FacadeBridge  bridge = new FacadeBridge();
        executor = new CommandExecutor(bridge);
        
    }
    
    public void start(){
        reception.start();
        notificationCentre.coordinator = this;
        notificationCentre.startListening();
    }
    
    public void setReception(CommandReception reception){
        this.reception = reception;
    }
    
    public Object process(Instruction inst, PeerStub from){
        //Do something before, such as record every instruction
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
        if(object instanceof CloudMLModel){
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                jsonCodec.save((CloudMLModel)object, baos);
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
