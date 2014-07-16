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
 * Created by user on 10.07.14.
 * @author Lorenzo Cianciaruso
 */
public class MonitoringSynch {

    public static void sendCurrentDeployment(String monitoringAddress, Deployment currentDeployment){
        ModelUpdates model = Filter.fromCloudmlToModaMP(currentDeployment);
        MonitoringAPI request = new MonitoringAPI(monitoringAddress);
        request.uploadDeployment(model);

    }

    public static void sendAddedComponents(String monitoringAddress, List<ExternalComponentInstance<? extends ExternalComponent>> addedECs) {
        ModelUpdates added = Filter.fromCloudmlToModaMP(addedECs);
        MonitoringAPI request = new MonitoringAPI(monitoringAddress);
        request.addInstances(added);

    }

    public static void sendRemovedComponents(String monitoringAddress, List<ExternalComponentInstance<? extends ExternalComponent>> removedECs) {
        ModelUpdates removed = Filter.fromCloudmlToModaMP(removedECs);
        //MonitoringAPI request = new MonitoringAPI(monitoringAddress);
        //request.deleteInstances(removed);

    }
}
