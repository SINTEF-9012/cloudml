package org.cloudml.monitoring.synchronization;
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

import it.polimi.modaclouds.qos_models.monitoring_ontology.CloudProvider;
import it.polimi.modaclouds.qos_models.monitoring_ontology.Location;
import it.polimi.modaclouds.qos_models.monitoring_ontology.VM;
import it.polimi.modaclouds.qos_models.monitoring_ontology.InternalComponent;
import org.cloudml.core.*;
import org.cloudml.core.Provider;
import org.cloudml.core.collections.ComponentInstanceGroup;
import org.cloudml.core.collections.InternalComponentInstanceGroup;
import org.cloudml.core.collections.ProviderGroup;
import org.cloudml.core.collections.VMInstanceGroup;
import java.util.List;
import java.util.logging.Level;

/**
 * @author Francesco di Forenza
 *         This class receive the model changes and tranform them
 *         to ensure compatibility with third parties
 */

public class Filter {

    private static final java.util.logging.Logger journal = java.util.logging.Logger.getLogger(Filter.class.getName());


    /**
     * Convert a Deployment model from CloudMl in a format
     * compatible with MODAClouds Monitoring Platform's APIs
     *
     * @param deployment the deployment model to convert
     */
    public static Model fromCloudmlToModaMP(Deployment deployment) {
        //get the relevant part of the model
        //create a new list for avoid changes in the original one
        ComponentInstanceGroup instances = new ComponentInstanceGroup();
        instances.addAll(deployment.getComponentInstances());

        ProviderGroup providers = new ProviderGroup();
        providers.addAll(deployment.getProviders());
        //call the actual translator
        return getModelUpdates(instances, providers);

    }

    /**
     * Convert a list of ExternalCOmpnents model from CloudMl in a format
     * compatible with MODAClouds Monitoring Platform's APIs
     *
     * @param addedECs the list
     */
    public static Model fromCloudmlToModaMP(List<ExternalComponentInstance<? extends org.cloudml.core.ExternalComponent>> addedECs,
                                            List<InternalComponentInstance> addedICs) {
        //create a new list for avoid changes in the original one
        ComponentInstanceGroup supportList = new ComponentInstanceGroup();
        supportList.addAll(addedECs);
        supportList.addAll(addedICs);

        //call the actual translator
        return getModelUpdates(supportList, null);
    }

    //this is the core method the other one are just to prepare the lists for this one
    private static Model getModelUpdates(ComponentInstanceGroup instances, ProviderGroup providers) {

        Model model = new Model();

        //go top down to remove the synched ones

        //prepare the VMs list
        VMInstanceGroup VMs = instances.onlyVMs();
        for (VMInstance i : VMs) {
            String location = i.getType().getLocation();
            if(!location.isEmpty()) {
                model.add(fromCloudmlToModaMP(location));
            }
            model.add(fromCloudmlToModaMP(i));
            instances.remove(i);
        }

        //prepare the InternalComponents list
        InternalComponentInstanceGroup internalComponents = instances.onlyInternals();
        for (InternalComponentInstance i : internalComponents) {
            model.add(fromCloudmlToModaMP(i));
            instances.remove(i);
        }

        //prepare for providers list
         if(providers!=null){
             for (Provider i : providers) {
                 model.add(fromCloudmlToModaMP(i));
                 instances.remove(i);
             }
         }

        return model;
    }

    //Translate a single VM from cloudML to Monitoring Platform QoS-model
    private static VM fromCloudmlToModaMP(VMInstance toTranslate) {
        VM toReturn = new VM();
        //KB entity field
        String id = toTranslate.getName();
        toReturn.setId(id);
        toReturn.setType(String.valueOf(toTranslate.getType().getName()));
        toReturn.setCloudProvider(toTranslate.getType().getProvider().getName());
        toReturn.setLocation(toTranslate.getType().getLocation());
        toReturn.setNumberOfCPUs(toTranslate.getCore());
        return toReturn;
    }
    //Translate an internal component from cloudML to Monitoring Platform QoS-model
    private static InternalComponent fromCloudmlToModaMP(InternalComponentInstance toTranslate){
        InternalComponent toReturn = new InternalComponent();
        String id = toTranslate.getName();
        toReturn.setId(id);
        toReturn.setType(String.valueOf(toTranslate.getType().getName()));
        toReturn.addRequiredComponent(toTranslate.externalHost().getName());
        return toReturn;
    }
    //Translate a cloud provider from cloudML to Monitoring Platform QoS-model
    private static CloudProvider fromCloudmlToModaMP(Provider toTranslate){
        CloudProvider toReturn = new CloudProvider();
        String id = toTranslate.getName();
        toReturn.setId(id);
        toReturn.setType(String.valueOf(toTranslate.getName()));
        return toReturn;
    }
    //Translate a location from cloudML to Monitoring Platform QoS-model
    private static Location fromCloudmlToModaMP(String location){
        Location toReturn = new Location();
        toReturn.setId(location);
        toReturn.setType(String.valueOf(location));
        toReturn.setLocation(location);
        return toReturn;
    }

}
