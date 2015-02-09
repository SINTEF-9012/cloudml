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

import com.google.common.collect.FluentIterable;
import org.cloudml.connectors.CloudSigmaConnector;
import org.cloudml.connectors.Connector;
import org.cloudml.core.ComponentInstance;
import org.cloudml.monitoring.status.MonitoredVm;
import org.cloudml.monitoring.status.modules.util.ListManager;
import org.cloudml.mrt.Coordinator;
import org.jclouds.cloudsigma2.domain.ServerInfo;
import org.jclouds.cloudsigma2.domain.ServerStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicolasf on 09.02.15.
 */
public class CloudSigmaModule implements Module {

    private List<MonitoredVm> VMs;
    private CloudSigmaConnector connector;
    private Type type;
    private Coordinator coord;

    public CloudSigmaModule(CloudSigmaConnector connector, Coordinator coord) {
        VMs = new ArrayList<MonitoredVm>();
        this.connector = connector;
        this.type = Type.CLOUDSIGMA_MONITOR;
        this.coord=coord;
    }



    @Override
    public void exec() {
        List<MonitoredVm> list = new ArrayList<MonitoredVm>();
        //Get all servers
        FluentIterable<ServerInfo> cloudsigmaServers;
        cloudsigmaServers = connector.listOfServers();
        if (cloudsigmaServers != null) {
            for (ServerInfo o : cloudsigmaServers) {
                MonitoredVm temp = new MonitoredVm(o.getUuid(), o.getName(), toState(o.getStatus()));
                list.add(temp);
            }
        }
        ListManager.listManager(list, VMs, coord);
    }

    private ComponentInstance.State toState(ServerStatus status){
        switch (status){
            case UNAVAILABLE:
                return ComponentInstance.State.ERROR;
            case RUNNING:
                return ComponentInstance.State.RUNNING;
            case STOPPED:
                return ComponentInstance.State.STOPPED;
            case STARTING:
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
    public Connector getConnector() {
        return connector;
    }
}
