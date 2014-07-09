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

/**
 * @author Francesco di Forenza
 */


import org.cloudml.monitoring.status.MonitoredVm;
import org.cloudml.connectors.Connector;
import org.cloudml.connectors.OpenStackConnector;
import org.cloudml.core.ComponentInstance;
import org.cloudml.monitoring.status.modules.util.ListManager;
import org.cloudml.mrt.Coordinator;
import org.jclouds.compute.domain.NodeMetadata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class OpenStackModule implements Module {

    private List<MonitoredVm> VMs;
    private OpenStackConnector connector;
    private Type type;
    private Coordinator coord;


    public OpenStackModule(OpenStackConnector connector, Coordinator coord) {
        VMs = new ArrayList<MonitoredVm>();
        this.connector = connector;
        this.type = Type.OPENSTACK_MONITOR;
        this.coord=coord;

    }

    public void exec() {
        List<MonitoredVm> list = new ArrayList<MonitoredVm>();
        //get all servers
        Set<NodeMetadata> jCloudsNodes = (Set<NodeMetadata>) connector.listOfVMs();
        Iterator<NodeMetadata> iterator = jCloudsNodes.iterator();
        while (iterator.hasNext()) {
            NodeMetadata current = iterator.next();
            String name = (current.getUserMetadata().get("Name"));
            MonitoredVm temp = new MonitoredVm(current.getId(), name, toState(current.getStatus()));
            list.add(temp);
        }
        ListManager.listManager(list, VMs, coord);
    }

    //map provider syntax on CloudMl one
    private ComponentInstance.State toState(NodeMetadata.Status status){
        switch (status){
            case ERROR:
                return ComponentInstance.State.ERROR;
            case RUNNING:
                return ComponentInstance.State.RUNNING;
            case SUSPENDED:
                return ComponentInstance.State.STOPPED;
            case TERMINATED:
                return ComponentInstance.State.TERMINATED;
            case PENDING:
                return ComponentInstance.State.PENDING;
            default:
                return ComponentInstance.State.UNRECOGNIZED;
        }

    }

    public Type getType() {
        return type;
    }

    public Connector getConnector() {
        return connector;
    }


}

