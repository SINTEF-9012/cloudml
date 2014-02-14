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

import java.util.Collection;

import org.cloudml.core.CloudMLModel;
import org.cloudml.facade.CloudML;
import org.cloudml.facade.Factory;
import org.cloudml.facade.commands.CloudMlCommand;
import org.cloudml.facade.commands.CommandFactory;

/**
 *
 * @author huis
 */
public class FacadeBridge implements ModelRepo{
    
    CloudML facade = null;
    CommandFactory factory = null;
    
    public FacadeBridge(){
        facade = Factory.getInstance().getCloudML();
        factory = new CommandFactory(facade);
    }

    @Override
    public CloudMLModel getRoot() {
        return facade.getDeploymentModel();
    }
    
    public void handle(String name, Collection<String> params){
        CloudMlCommand command = null;
        if("LoadDeployment".equals(name)){
            command = factory.createLoadDeployment(params.iterator().next());
        }
        else if("Deploy".equals(name)){
            command = factory.createDeploy();
        }
        else if("StartArtefact".equals(name)){
            command = factory.createStartArtifact(params.iterator().next());
        }
        else{
            throw new RuntimeException("Command not defined in facade");
        }
        facade.fireAndForget(command);
    }
    
}
