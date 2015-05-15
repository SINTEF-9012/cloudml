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
package org.cloudml.monitoring.status.modules;

import net.cloudml.core.InternalComponentInstance;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudml.connectors.*;
import org.cloudml.core.ComponentInstance;
import org.cloudml.monitoring.status.MonitoredVm;
import org.cloudml.monitoring.status.modules.util.ListManager;
import org.cloudml.mrt.Coordinator;
import org.jclouds.compute.domain.NodeMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicolasf on 07.05.15.
 */
public class CloudFoundyModule implements Module {

    private List<MonitoredVm> VMs;
    private CloudFoundryConnector connector;
    private Type type;
    private Coordinator coord;


    public CloudFoundyModule(CloudFoundryConnector connector, Coordinator coord) {
        VMs = new ArrayList<MonitoredVm>();
        this.connector = connector;
        this.type = Type.CLOUDFOUNDRY_MONITOR;
        this.coord=coord;
    }

    @Override
    public void exec() {
        List<MonitoredVm> list = new ArrayList<MonitoredVm>();
        //Get all servers
        List<CloudApplication> cloudFoundryApps;
        cloudFoundryApps = connector.listApplications();
        if (cloudFoundryApps != null) {
            for (CloudApplication o : cloudFoundryApps) {
                MonitoredVm temp = new MonitoredVm(o.getUris().get(0), o.getName(), toState(o.getState()));
                list.add(temp);
            }
        }
        ListManager.listManager(list, VMs, coord);

    }

    //map provider syntax on CloudMl one
    private ComponentInstance.State toState(CloudApplication.AppState status){
        switch (status){
            case STARTED:
                return ComponentInstance.State.RUNNING;
            case STOPPED:
                return ComponentInstance.State.STOPPED;
            case UPDATING:
                return ComponentInstance.State.PENDING;
            default:
                return ComponentInstance.State.UNRECOGNIZED;
        }

    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public PaaSConnector getConnector() {
        return connector;
    }
}
