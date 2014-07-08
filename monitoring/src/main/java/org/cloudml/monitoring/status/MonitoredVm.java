package org.cloudml.monitoring.status; /**
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

import org.cloudml.core.ComponentInstance;
/**
 * @author Francesco di Forenza
 */
public class MonitoredVm {
    private String id;
    private String name;
    private ComponentInstance.State status;

    public MonitoredVm(String id, String name, ComponentInstance.State status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ComponentInstance.State getStatus() {
        return status;
    }

    /**
     * Set the status
     *
     * @param status
     */
    public void setStatus(ComponentInstance.State status) {
        this.status = status;
    }
}
