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

import org.cloudml.core.Deployment;
import org.cloudml.core.ExternalComponent;
import org.cloudml.core.ExternalComponentInstance;

import java.util.List;

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
    public static void sendAddedComponents(String monitoringAddress, List<ExternalComponentInstance<? extends ExternalComponent>> addedECs) {
        Model added = Filter.fromCloudmlToModaMP(addedECs);
        MonitoringAPI request = new MonitoringAPI(monitoringAddress);
        request.addInstances(added);

    }

    /**
     * This method activates the transformation from CloudML's model to the Monitoring Platform's model
     * and then send the removed components to the MP
     * @param monitoringAddress address of the monitoring platform
     * @param removedECs removed components
     */
    public static void sendRemovedComponents(String monitoringAddress, List<ExternalComponentInstance<? extends ExternalComponent>> removedECs) {
        Model removed = Filter.fromCloudmlToModaMP(removedECs);
        //MonitoringAPI request = new MonitoringAPI(monitoringAddress);
        //request.deleteInstances(removed);

    }
}
