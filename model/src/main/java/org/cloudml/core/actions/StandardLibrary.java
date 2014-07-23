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
package org.cloudml.core.actions;

import org.cloudml.core.*;
import org.cloudml.core.collections.InternalComponentInstanceGroup;

import java.util.List;
import java.util.Map;


public class StandardLibrary {

    private final NamingStrategy naming; 

    public StandardLibrary() {
        this.naming = new NamingStrategy();
    }
    
    public StandardLibrary(NamingStrategy naming) {
        this.naming = naming;
    }   
    
    public ComponentInstance<? extends Component> provision(Deployment deployment, Component type) {
        return new Provision(this, type).applyTo(deployment);
    }

    public void terminate(Deployment deployment, VMInstance instance) {
        new Terminate(this, instance).applyTo(deployment);  
    }

    public InternalComponentInstance install(Deployment deployment, InternalComponent type, ComponentInstance<? extends Component> host) {
        return new Install(this, type, host).applyTo(deployment); 
    }

    public void uninstall(Deployment deployment, InternalComponentInstance artefactInstance) {
        new Uninstall(this, artefactInstance).applyTo(deployment);
    }

    public InternalComponentInstance migrate(Deployment deployment, InternalComponentInstance artefactInstance) {
        return new Migrate(this, artefactInstance).applyTo(deployment);
    }

    public void bind(Deployment deployment, RequiredPortInstance clientPort) {
        new Bind(this, clientPort).applyTo(deployment);
    }

    public Relationship findRelationshipFor(Deployment deployment, RequiredPortInstance clientPort) {
        return new FindRelationship(this, clientPort).applyTo(deployment);
    }

    public ProvidedPortInstance findProvidedPortFor(Deployment deployment, Relationship relationshipType) {
        return new FindProvidedPortInstance(this, relationshipType).applyTo(deployment);
    }

    public ComponentInstance<? extends Component> findDestinationFor(Deployment deployment, InternalComponent component) {
        return new FindHost(this, component).applyTo(deployment);
    }

    public ComponentInstance<? extends Component> findAlternativeDestinationFor(Deployment deployment, InternalComponentInstance artefact) {
        return new FindHost(this, artefact.getType(), artefact.getHost()).applyTo(deployment);
    }

    public String createUniqueComponentInstanceName(Deployment deployment, Component type) {
        return naming.createUniqueComponentInstanceName(deployment, type); 
    }
    
    public String createUniqueRelationshipInstanceName(Deployment deployment, Relationship type) {
        return naming.createUniqueRelationshipInstanceName(deployment, type);  
    }

    public void unbind(Deployment deployment, PortInstance<? extends Port> port) {
        new Unbind(this, port).applyTo(deployment);
    }

    public void stop(Deployment deployment, ComponentInstance<? extends Component> instance) {
        new Stop(this, instance).applyTo(deployment);
    }

    public Component findHostType(Deployment deployment, InternalComponent type) {
        return new FindHostType(this, type).applyTo(deployment); 
    }

    public Component findComponentProviding(Deployment deployment, Relationship bindingType) {
        return new FindComponentTypeProviding(this, bindingType).applyTo(deployment);
    }

    public ComponentInstance replicateComponentInstance(Deployment deployment, ComponentInstance c, ExternalComponentInstance eci){
        return new ReplicateComponentInstance(this, c, eci).applyTo(deployment);
    }

    public Map<InternalComponentInstance, InternalComponentInstance> replicateSubGraph(Deployment deployment, InternalComponentInstanceGroup list, VMInstance vmi){
        return new ReplicateSubGraph(this, list, vmi).applyTo(deployment);
    }
}
