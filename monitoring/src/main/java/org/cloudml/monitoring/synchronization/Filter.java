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

import it.polimi.modaclouds.qos_models.monitoring_ontology.Component;
import it.polimi.modaclouds.qos_models.monitoring_ontology.ExternalComponent;
import it.polimi.modaclouds.qos_models.monitoring_ontology.VM;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.Deployment;
import org.cloudml.core.ExternalComponentInstance;
import org.cloudml.core.VMInstance;
import org.cloudml.core.collections.ComponentInstanceGroup;
import org.cloudml.core.collections.ExternalComponentInstanceGroup;
import org.cloudml.core.collections.VMInstanceGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Francesco di Forenza
 *         This class receive the model changes and tranform them
 *         to ensure compatibility with third parties
 */

public class Filter {

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

        //call the actual translator
        return getModelUpdates(instances);

    }

    /**
     * Convert a list of ExternalCOmpnents model from CloudMl in a format
     * compatible with MODAClouds Monitoring Platform's APIs
     *
     * @param listToConvert the list
     */
    public static Model fromCloudmlToModaMP(List<ExternalComponentInstance<? extends org.cloudml.core.ExternalComponent>> listToConvert) {
        //create a new list for avoid changes in the original one
        ComponentInstanceGroup supportList = new ComponentInstanceGroup();
        supportList.addAll(listToConvert);

        //call the actual translator
        return getModelUpdates(supportList);
    }

    //this is the core method the other one are just to prepare the lists for this one
    private static Model getModelUpdates(ComponentInstanceGroup instances) {
        //prepare the lists
        List<Component> toReturnComponent = new ArrayList<Component>();
        List<ExternalComponent> toReturnExternalComponent = new ArrayList<ExternalComponent>();
        List<VM> toReturnVM = new ArrayList<VM>();

        Model model = new Model();

        //go top down to remove the synched ones

        //prepare the VMs list
        VMInstanceGroup VMs = instances.onlyVMs();
        for (VMInstance i : VMs) {
            //toReturnVM.add(fromCloudmlToModaMP(i)); will no more use lists
            model.add(fromCloudmlToModaMP(i));
            instances.remove(i);
        }

        //prepare the ExternalComponents list
        ExternalComponentInstanceGroup components = instances.onlyExternals();
        for (ExternalComponentInstance i : components) {
            //toReturnExternalComponent.add(fromCloudmlToModaMP(i)); will no more use lists
            //model.add(fromCloudmlToModaMP(i)); no external component add function
            instances.remove(i);
        }

        //prepare the components list
        //REALLY NECESSARY?

        //return new Model(toReturnComponent, toReturnExternalComponent, toReturnVM);
        return model;
    }


    //translate a single ExternalComponent
    private static ExternalComponent fromCloudmlToModaMP(ExternalComponentInstance toTranslate) {
        ExternalComponent toReturn = new ExternalComponent();
        //KB entity field
        String uri = "http://www.modaclouds.eu/rdfs/1.0/monitoring/"+toTranslate.getName()+"-1";
        toReturn.setId(uri);
        //Component field
        //toReturn.setId(toTranslate.getName());
        //External components fields
        //toReturn.setUrl(toTranslate.getPublicAddress());  there is no more url attribute
        boolean started = false;
        if (toTranslate.getStatus() == ComponentInstance.State.RUNNING) {
            started = true;
        }
        //toReturn.setStarted(started); there is no more started attribute
        toReturn.setCloudProvider(toTranslate.getType().asExternal().getProvider().getName());
        return toReturn;
    }

    //Translate a single VM
    private static VM fromCloudmlToModaMP(VMInstance toTranslate) {
        VM toReturn = new VM();
        //KB entity field
        String uri = "http://www.modaclouds.eu/rdfs/1.0/monitoring/"+toTranslate.getName()+"-1";
        toReturn.setId(uri);
        //Component field
        //toReturn.setId(toTranslate.getName());
        //External component fields
        //toReturn.setUrl(toTranslate.getPublicAddress()); url is equal to location?? next line
        toReturn.setLocation(toTranslate.getPublicAddress());
        boolean started = false;
        if (toTranslate.getStatus() == ComponentInstance.State.RUNNING) {
            started = true;
        }
        //toReturn.setStarted(started); no more started attribute
        toReturn.setCloudProvider(toTranslate.getType().getProvider().getName());
        //VM fields
        toReturn.setNumberOfCPUs(toTranslate.getType().getMinCores());
        return toReturn;
    }



}
