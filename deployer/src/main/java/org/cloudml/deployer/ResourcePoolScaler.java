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
package org.cloudml.deployer;


import org.cloudml.connectors.Connector;
import org.cloudml.connectors.ConnectorFactory;
import org.cloudml.core.*;
import org.cloudml.mrt.Coordinator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ferrynico on 07/04/15.
 */
public class ResourcePoolScaler extends Scaler {
    private static final Logger journal = Logger.getLogger(ResourcePoolScaler.class.getName());


    public ResourcePoolScaler(Deployment currentModel, Coordinator coordinator, CloudAppDeployer dep) {
        super(currentModel, coordinator, dep);
    }

    public void scaleOut(ResourcePoolInstance resourcePoolInstance, int nb){
        for(int i=0; i<nb; i++){
            scaleOut(resourcePoolInstance);
        }
    }

    public void scaleOut(ResourcePoolInstance resourcePoolInstance){
        for(VMInstance i:resourcePoolInstance.getBaseInstances()){
            scaleOut(resourcePoolInstance,i);
        }
    }

    public void scaleOut(ResourcePoolInstance resourcePoolInstance, VMInstance vmi){
        VM temp = findVMGenerated(vmi.getType().getName(), "fromImage");
        Map<InternalComponentInstance, InternalComponentInstance> duplicatedGraph=resourcePoolInstance.replicate(vmi,currentModel);

        if(temp == null){
            Connector c = ConnectorFactory.createIaaSConnector(vmi.getType().getProvider());
            String ID=c.createImage(vmi);
            c.closeConnection();
            vmi.getType().setImageId(ID);
        }else{
            vmi.getType().setImageId(temp.getImageId());
        }

        provisionVM(vmi);
        allConfiguration(duplicatedGraph);

        journal.log(Level.INFO, ">> Scaling completed!");
    }

    private void allConfiguration(Map<InternalComponentInstance, InternalComponentInstance> duplicatedGraph){
        //execute the configuration bindings
        Set<ComponentInstance> listOfAllComponentImpacted= new HashSet<ComponentInstance>();
        configureBindingOfImpactedComponents(listOfAllComponentImpacted,duplicatedGraph);
        //execute configure commands on the components
        configureImpactedComponents(listOfAllComponentImpacted,duplicatedGraph);
        //execute start commands on the components
        startImpactedComponents(listOfAllComponentImpacted, duplicatedGraph);
        //restart components on the VM scaled
        restartHostedComponents(ci);
    }

    private void provisionVM(VMInstance vmi){
        Connector c2=ConnectorFactory.createIaaSConnector(vmi.getType().getProvider());
        HashMap<String,String> result=c2.createInstance(ci);
        c2.closeConnection();
        coordinator.updateStatusInternalComponent(ci.getName(), result.get("status"), CloudAppDeployer.class.getName());
        coordinator.updateStatus(vmi.getName(), ComponentInstance.State.RUNNING.toString(), CloudAppDeployer.class.getName());
        coordinator.updateIP(ci.getName(),result.get("publicAddress"),CloudAppDeployer.class.getName());
    }
}
