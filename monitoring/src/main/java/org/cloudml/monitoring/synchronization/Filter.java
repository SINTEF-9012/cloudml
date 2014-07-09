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
    public static ModelUpdates fromCloudmlToModaMP(Deployment deployment) {
        //prepare the lists
        List<Component> toReturnComponent = new ArrayList<Component>();
        List<ExternalComponent> toReturnExternalComponent = new ArrayList<ExternalComponent>();
        List<VM> toReturnVM = new ArrayList<VM>();

        //get the relevant part of the model
        ComponentInstanceGroup instances = deployment.getComponentInstances();

        //go top down to remove the synched ones

        //prepare the VMs list
        VMInstanceGroup VMs = instances.onlyVMs();
        for (VMInstance i : VMs) {
            toReturnVM.add(fromCloudmlToModaMP(i));
            instances.remove(i);
        }

        //prepare the ExternalComponents list
        ExternalComponentInstanceGroup components = instances.onlyExternals();
        for (ExternalComponentInstance i : components) {
            toReturnExternalComponent.add(fromCloudmlToModaMP(i));
            instances.remove(i);
        }

        //prepare the components list
        //REALLY NECESSARY?

        return new ModelUpdates(toReturnComponent, toReturnExternalComponent, toReturnVM);
    }

    private static ExternalComponent fromCloudmlToModaMP(ExternalComponentInstance toTranslate) {
        ExternalComponent toReturn = new ExternalComponent();
        toReturn.setId(toTranslate.getName());
        //TODO add other fields
        return toReturn;
    }

    private static VM fromCloudmlToModaMP(VMInstance toTranslate) {
        VM toReturn = new VM();
        toReturn.setId(toTranslate.getName());
        toReturn.setNumberOfCpus(toTranslate.getType().getMinCores());
        toReturn.setCloudProvider(toTranslate.getType().getProvider().getName());
        //TODO add other fields and check the current ones
        return toReturn;
    }


}
