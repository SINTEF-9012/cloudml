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
package org.cloudml.monitoring.synchronization;

import org.cloudml.core.*;
import org.cloudml.core.collections.ComponentInstanceGroup;
import org.cloudml.core.collections.VMInstanceGroup;

import java.util.List;
import java.util.logging.Level;

/**
 * @author Lorenzo Cianciaruso
 */
public class MonitoringSynch {

    /**
     * This method activates the transformation from CloudML's model to the Monitoring Platform's model
     * and then send the current deployment to the MP
     * @param monitoringAddress address of the monitoring platform
     * @param currentDeployment current deployment model
     */
    public static void sendCurrentDeployment(String monitoringAddress, Deployment currentDeployment){
        Model model = Filter.fromCloudmlToModaMP(currentDeployment);

        MonitoringAPI request = new MonitoringAPI(monitoringAddress);
        request.uploadDeployment(model);
    }

    /**
     * This method activates the transformation from CloudML's model to the Monitoring Platform's model
     * and then send the added components to the MP
     * @param monitoringAddress address of the monitoring platform
     * @param addedECs added components
     */
    public static void sendAddedComponents(String monitoringAddress, List<ExternalComponentInstance<? extends ExternalComponent>> addedECs,
                                           List<InternalComponentInstance> addedICs) {
        Model added = Filter.fromCloudmlToModaMP(addedECs, addedICs);
        MonitoringAPI request = new MonitoringAPI(monitoringAddress);
        request.addInstances(added);
    }

    /**
     * This method activates the transformation from CloudML's model to the Monitoring Platform's model
     * and then send the removed components to the MP
     * @param monitoringAddress address of the monitoring platform
     * @param removedECs removed components
     * @return boolean, true if the models are synch, false if there is a mismatch
     */
    public static boolean sendRemovedComponents(String monitoringAddress, List<ExternalComponentInstance<? extends ExternalComponent>> removedECs,
                                                List<InternalComponentInstance> removedICs) {
        MonitoringAPI request = new MonitoringAPI(monitoringAddress);
        boolean modelMatching = true;

        //this cycles send the delete request for each component removed
        //to the monitoring manager. In case the connection terminates
        //with a NOT FOUND error means that there is a mismatch between the
        //two models. In this case the whole model should be resent.
        ComponentInstanceGroup supportList = new ComponentInstanceGroup();
        supportList.addAll(removedECs);
        VMInstanceGroup removedVMs = supportList.onlyVMs();
        for(int i = 0; i<removedVMs.size() && modelMatching;i++){
            int code = request.deleteInstances(removedECs.get(i).getName());
            if(code == MonitoringAPI.CLIENT_ERROR_NOT_FOUND) {
                modelMatching = false;
            }
        }

        for(int i = 0; i<removedICs.size() && modelMatching;i++){
            int code = request.deleteInstances(removedICs.get(i).getName());
            if(code == MonitoringAPI.CLIENT_ERROR_NOT_FOUND) {
                modelMatching = false;
            }
        }
        return modelMatching;
    }

}
