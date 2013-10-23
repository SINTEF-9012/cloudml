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

package org.cloudml.mrt.coord;


import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.DeploymentModel;
import org.cloudml.mrt.coord.cmd.abstracts.Change;
import org.cloudml.mrt.coord.cmd.abstracts.Instruction;
import org.cloudml.mrt.coord.cmd.abstracts.Listener;
import org.cloudml.mrt.coord.cmd.gen.CloudMLCmds;
import org.cloudml.mrt.coord.ws.CoordWsReception;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author Hui Song
 */
public class Coordinator {
    
    CoordWsReception wsReception = null;
    DeploymentModel root = null;
    //JsonCodec jsonCodec = new JsonCodec();
    CommandExecutor executor = new CommandExecutor();    
    List<Change> changeList = new ArrayList<Change>();
    NodificationCentre notificationCentre = new NodificationCentre();
    JsonCodec jsonCodec = new JsonCodec();
    
    public void start(int port){
        startWsReception(port);
        notificationCentre.coordinator = this;
        notificationCentre.startListening();
    }
    
    public void startWsReception(int port){
        wsReception = new CoordWsReception(port, this);
        wsReception.start();
    }
    
    public Object process(Instruction inst, PeerStub from){
        //Do something before, such as record every instruction
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
            if(obj!=null)
                ret += String.format("---return of %s---\n%s\n", cmd.getClass().getSimpleName(), codec(obj));
        }
        return ret;
    }
    
    public String codec(Object object){
        if(object instanceof DeploymentModel){
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                jsonCodec.save((DeploymentModel)object, baos);
                return baos.toString("UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Coordinator.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        else
            return object.toString();
    }
    
}
