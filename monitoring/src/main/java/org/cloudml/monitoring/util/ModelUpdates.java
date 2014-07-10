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
package org.cloudml.monitoring.util;

/**
 * Created by user on 07.07.14.
 */
import it.polimi.modaclouds.qos_models.monitoring_ontology.Component;
import it.polimi.modaclouds.qos_models.monitoring_ontology.ExternalComponent;
import it.polimi.modaclouds.qos_models.monitoring_ontology.VM;

import java.util.List;

public class ModelUpdates {

    private List<VM> vms;

    private List<Component> components;

    private List<ExternalComponent> externalComponents;

    public ModelUpdates(List<VM> vms, List<Component> components,
                        List<ExternalComponent> externalComponents) {
        this.vms = vms;
        this.components = components;
        this.externalComponents = externalComponents;
    }

    public List<VM> getVms() {
        return vms;
    }

    public void setVms(List<VM> vms) {
        this.vms = vms;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public List<ExternalComponent> getExternalComponents() {
        return externalComponents;
    }

    public void setExternalComponents(List<ExternalComponent> externalComponents) {
        this.externalComponents = externalComponents;
    }

}
