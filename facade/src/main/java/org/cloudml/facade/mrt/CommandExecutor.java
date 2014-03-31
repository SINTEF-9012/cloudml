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
package org.cloudml.facade.mrt;

import java.io.ByteArrayOutputStream;
import java.util.List;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.Node;
import org.cloudml.facade.mrt.cmd.abstracts.Change;
import org.cloudml.facade.mrt.cmd.abstracts.Instruction;
import org.cloudml.facade.mrt.cmd.gen.Extended;

/**
 * This is the one who finally execute the command, one by one.
 * It is also the only one who knows what exactly each command means.
 * But in the same, it only knows the behavioural meaning of a command, without
 * any knowledge about where it is from, how it is scheduled, etc.
 * 
 * @author Hui Song
 */
public class CommandExecutor {
    
    ModelRepo repo = null;
    public CommandExecutor(ModelRepo repo){
        this.repo = repo;
    }

    
    public synchronized String getSnapshotInJson(){
        JsonCodec jsonCodec = new JsonCodec();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        jsonCodec.save(repo.getRoot(), baos);
        return baos.toString();   
    }
    
    public synchronized String getSnapshotInString(){
        return repo.getRoot().toString();
    }
    
    public synchronized void commitModifications(List<String> modifications){
        for(String modi : modifications){
            String[] parsed = modi.split("\\s+");
            if("add".equals(parsed[0])){
               if("in".equals(parsed[4]) && "root".equals(parsed[5]) && "nodeTypes".equals(parsed[6]))
                   repo.getRoot().getNodeTypes().add(new Node(parsed[3]));
            }
        }
    }
    
    /**
     * The thing that really matters to this method is its "synchronized" keywork
     * @param inst
     * @return 
     */
    public synchronized Object execute(Instruction inst, List<Change> changes){
        int curr = changes.size();
        
        if(inst instanceof Extended){
            inst.execute(repo, changes);
            return null;
        }
        
        
        Object obj = inst.execute(repo.getRoot(), changes);
        for(;curr<changes.size();curr++){
            changes.get(curr).fromPeer = inst.fromPeer;
        }
        return obj;
    }
    
    
    
}
