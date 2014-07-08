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

package org.cloudml.monitoring.status.modules.util;

/**
 * @author Francesco di Forenza
 */

import org.cloudml.monitoring.status.MonitoredVm;
import org.cloudml.monitoring.status.NotificationSender;
import org.cloudml.core.ComponentInstance.State;
import org.cloudml.mrt.Coordinator;

import java.util.ArrayList;
import java.util.List;

public class ListManager {

    public static void listManager(List<MonitoredVm> flexiant, List<MonitoredVm> VMs, Coordinator coord) {

        List<MonitoredVm> running;
        List<MonitoredVm> deleted;
        running = new ArrayList<MonitoredVm>();
        for (MonitoredVm temp : flexiant) {
            int position = indexOf(temp, VMs);
            //if machine is new
            if (position == -1) {
                VMs.add(temp);
                running.add(temp);
                NotificationSender.updateUsingFacade(temp.getName(), temp.getStatus(), coord);
            }
            //if already exists and changed
            else {
                running.add(temp);
                State oldStatus = VMs.get(position).getStatus();
                State newStatus = temp.getStatus();
                if (oldStatus != newStatus) {
                    VMs.get(position).setStatus(newStatus);
                    NotificationSender.updateUsingFacade(temp.getName(), newStatus, coord);
                }
            }
        }
        deleted = new ArrayList<MonitoredVm>();
        deleted.addAll(VMs);
        for (MonitoredVm alive : running) {
            remove(alive, deleted);
        }
        for (MonitoredVm toDelete : deleted) {
            State newStatus = State.UNRECOGNIZED;
            NotificationSender.updateUsingFacade(toDelete.getName(), newStatus, coord);
            VMs.remove(toDelete);
        }
    }

    private static void remove(MonitoredVm toDelete, List<MonitoredVm> list) {
        int index = indexOf(toDelete, list);
        if (index != -1) {
            list.remove(index);
        }
    }

    private static int indexOf(MonitoredVm toTest, List<MonitoredVm> list) {
        for (MonitoredVm i : list) {
            if (i.getId().equals(toTest.getId())) {
                return list.indexOf(i);
            }
        }
        return -1;
    }

}
