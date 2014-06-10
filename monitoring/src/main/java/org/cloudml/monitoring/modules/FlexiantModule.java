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

package org.cloudml.monitoring.modules;

/**
 * @author Francesco di Forenza
 */

import net.flexiant.extility.Server;
import net.flexiant.extility.ServerStatus;
import org.cloudml.monitoring.MonitoredVm;
import org.cloudml.connectors.Connector;
import org.cloudml.connectors.FlexiantConnector;
import org.cloudml.core.ComponentInstance;
import org.cloudml.monitoring.modules.util.ListManager;

import java.util.ArrayList;
import java.util.List;

public class FlexiantModule implements Module {

    private List<MonitoredVm> VMs;
    private FlexiantConnector connector;
    private Type type;

    public FlexiantModule(FlexiantConnector connector) {
        VMs = new ArrayList<MonitoredVm>();
        this.connector = connector;
        this.type = Type.FLEXIANT_MONITOR;
    }

    public void exec() {
        List<MonitoredVm> list = new ArrayList<MonitoredVm>();
        //Get all servers
        List<Object> flexiantServers;
        flexiantServers = connector.getListServer();
        if (flexiantServers != null) {
            for (Object o : flexiantServers) {
                Server server = (Server) o;
                MonitoredVm temp = new MonitoredVm(server.getResourceUUID(), server.getResourceName(), toState(server.getStatus()));
                list.add(temp);
            }
        }
        ListManager.listManager(list, VMs);
    }

    //map provider syntax on CloudMl one
    private ComponentInstance.State toState(ServerStatus status){
        switch (status){
            case ERROR:
                return ComponentInstance.State.ERROR;
            case RUNNING:
                return ComponentInstance.State.RUNNING;
            case STOPPED:
                return ComponentInstance.State.STOPPED;
            case RECOVERY:
                return ComponentInstance.State.RECOVERY;
            default:
                return ComponentInstance.State.PENDING;
        }
    }

    public Type getType() {
        return type;
    }

    public Connector getConnector() {
        return connector;
    }

}

