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


package org.cloudml.facade;

import java.util.Collection;

import org.cloudml.core.Deployment;
import org.cloudml.facade.commands.CloudMlCommand;
import org.cloudml.facade.commands.CommandFactory;
import org.cloudml.mrt.ModelRepo;

/**
 *
 * @author huis
 */
public class FacadeBridge implements ModelRepo {
    
    CloudML facade = null;
    CommandFactory factory = null;
    String longContent = null;
    
    public FacadeBridge(){
        facade = Factory.getInstance().getCloudML();
        factory = new CommandFactory();
        
    }

    @Override
    public Deployment getRoot() {
        return facade.getDeploymentModel();
    } 
    
    public Object handle(String name, Collection<String> params){
        CloudMlCommand command = null;
        if("LoadDeployment".equals(name)){
            
            if(params == null || params.isEmpty()){ 
                return "###Waiting for Deployment Content";
            }
            String param = params.iterator().next();
            command = factory.loadDeployment(param);
        }
        else if("Deploy".equals(name)){
            command = factory.deploy();
        }
        else if("StartArtefact".equals(name)){
            command = factory.startComponent(params.iterator().next());
        }
        else if("ScaleOut".equals(name)){
            command = factory.scaleOut(params.iterator().next());
        }
        else if("Image".equals(name)){
            command = factory.image(params.iterator().next());
        }
        else if("Snapshot".equals(name)){
            command = factory.snapshot(params.iterator().next());
        }
        else{
            throw new RuntimeException("Command not defined in facade");
        }
        facade.fireAndForget(command);
        return null;
    }


    
}
