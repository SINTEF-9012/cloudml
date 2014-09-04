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

import it.polimi.modaclouds.qos_models.monitoring_ontology.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Francesco di Forenza and Lorenzo Cianciaruso
 */
public class Model {

    private Set<CloudProvider> cloudProviders;
    private Set<Location> locations;
    private Set<VM> vMs;
    private Set<PaaSService> paaSServices;
    private Set<InternalComponent> internalComponents;
    private Set<Method> methods;

    public void add(CloudProvider cloudProvider) {
        if (cloudProviders == null) cloudProviders = new HashSet<CloudProvider>();
        cloudProviders.add(cloudProvider);
    }
    public void add(Location location) {
        if (locations == null) locations = new HashSet<Location>();
        locations.add(location);
    }
    public void add(VM vM) {
        if (vMs == null) vMs = new HashSet<VM>();
        vMs.add(vM);
    }
    public void add(PaaSService paaSService) {
        if (paaSServices == null) paaSServices = new HashSet<PaaSService>();
        paaSServices.add(paaSService);
    }
    public void add(InternalComponent internalComponent) {
        if (internalComponents == null) internalComponents = new HashSet<InternalComponent>();
        internalComponents.add(internalComponent);
    }
    public void add(Method method) {
        if (methods == null) methods = new HashSet<Method>();
        methods.add(method);
    }

    public Set<CloudProvider> getCloudProviders() {
        return cloudProviders;
    }
    public void setCloudProviders(Set<CloudProvider> cloudProviders) {
        this.cloudProviders = cloudProviders;
    }
    public Set<Location> getLocations() {
        return locations;
    }
    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }
    public Set<VM> getvMs() {
        return vMs;
    }
    public void setvMs(Set<VM> vMs) {
        this.vMs = vMs;
    }
    public Set<PaaSService> getPaaSServices() {
        return paaSServices;
    }
    public void setPaaSServices(Set<PaaSService> paaSServices) {
        this.paaSServices = paaSServices;
    }
    public Set<InternalComponent> getInternalComponents() {
        return internalComponents;
    }
    public void setInternalComponents(Set<InternalComponent> internalComponents) {
        this.internalComponents = internalComponents;
    }
    public Set<Method> getMethods() {
        return methods;
    }
    public void setMethods(Set<Method> methods) {
        this.methods = methods;
    }

    public Set<Resource> getResources() {
        Set<Resource> resources = new HashSet<Resource>();
        resources.addAll(nullable(cloudProviders));
        resources.addAll(nullable(locations));
        resources.addAll(nullable(vMs));
        resources.addAll(nullable(paaSServices));
        resources.addAll(nullable(internalComponents));
        resources.addAll(nullable(methods));
        return resources;
    }

    private <T> Collection<T> nullable(
            Set<T> collection) {
        return collection == null? new HashSet<T>() : collection;
    }

    /* class body before the modifications in the MP. Before using qos models 2.1.2

    private List<VM> vms;
    private List<Component> components;
    private List<ExternalComponent> externalComponents;

    public ModelUpdates(List<Component> components, List<ExternalComponent> externalComponents, List<VM> vms) {
        this.vms = vms;
        this.components = components;
        this.externalComponents = externalComponents;
    }

    public List<VM> getVms() {
        return vms;
    }

    public List<Component> getComponents() {
        return components;
    }

    public List<ExternalComponent> getExternalComponents() {
        return externalComponents;
    }
    */


}
