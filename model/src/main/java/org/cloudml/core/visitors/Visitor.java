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
package org.cloudml.core.visitors;

import org.cloudml.core.*;

public class Visitor {

    private final Dispatcher dispatcher;
    private final ListenerGroup listeners;

    public Visitor(VisitListener... listeners) {
        this(new ContainmentDispatcher(), listeners);
    }

    public Visitor(Dispatcher dispatcher, VisitListener... listeners) {
        this.dispatcher = dispatcher;
        this.listeners = new ListenerGroup(listeners);
    }

    public void visitDeployment(Deployment deployment) {
        listeners.enter(deployment);
        dispatcher.dispatchTo(this, deployment);
        listeners.exit(deployment);
    }

    public void visitProvider(Provider subject) {
        listeners.enter(subject);
        dispatcher.dispatchTo(this, subject);
        listeners.exit(subject);
    }

    public void visitInternalComponent(InternalComponent component) {
        listeners.enter(component);
        dispatcher.dispatchTo(this, component);
        listeners.exit(component);
    }

    public void visitExternalComponent(ExternalComponent component) {
        listeners.enter(component);
        dispatcher.dispatchTo(this, component);
        listeners.exit(component);
    }

    public void visitVM(VM subject) {
        listeners.enter(subject);
        dispatcher.dispatchTo(this, subject);
        listeners.exit(subject);
    }

    public void visitRelationship(Relationship relationship) {
        listeners.enter(relationship);
        dispatcher.dispatchTo(this, relationship);
        listeners.exit(relationship); 
    }

    public void visitRequiredPort(RequiredPort subject) {
        listeners.enter(subject);
        dispatcher.dispatchTo(this, subject);
        listeners.exit(subject);
    }

    public void visitProvidedPort(ProvidedPort subject) {
        listeners.enter(subject);
        dispatcher.dispatchTo(this, subject);
        listeners.exit(subject); 
    }

    public void visitRequiredExecutionPlatform(RequiredExecutionPlatform subject) {
        listeners.enter(subject);
        dispatcher.dispatchTo(this, subject);
        listeners.exit(subject); 
    }

    public void visitProvidedExecutionPlatform(ProvidedExecutionPlatform subject) {
        listeners.enter(subject);
        dispatcher.dispatchTo(this, subject);
        listeners.exit(subject);
    }

    public void visitVMInstance(VMInstance subject) {
        listeners.enter(subject);
        dispatcher.dispatchTo(this, subject);
        listeners.exit(subject); 
    }

    public void visitExternalComponentInstance(ExternalComponentInstance subject) {
        listeners.enter(subject);
        dispatcher.dispatchTo(this, subject);
        listeners.exit(subject);
    }

    public void visitInternalComponentInstance(InternalComponentInstance subject) {
        listeners.enter(subject);
        dispatcher.dispatchTo(this, subject);
        listeners.exit(subject); 
    }

    public void visitRelationshipInstance(RelationshipInstance subject) {
        listeners.enter(subject);
        dispatcher.dispatchTo(this, subject);
        listeners.exit(subject);
    }

    public void visitRequiredPortInstance(RequiredPortInstance subject) {
        listeners.enter(subject);
        dispatcher.dispatchTo(this, subject);
        listeners.exit(subject);  
    }

    public void visitProvidedPortInstance(ProvidedPortInstance subject) {
        listeners.enter(subject);
        dispatcher.dispatchTo(this, subject);
        listeners.exit(subject);
    }
    
    public void visitRequiredExecutionPlatformInstance(RequiredExecutionPlatformInstance subject) {
        listeners.enter(subject);
        dispatcher.dispatchTo(this, subject);
        listeners.exit(subject);  
    }

    public void visitProvidedExecutionPlatformInstance(ProvidedExecutionPlatformInstance subject) {
        listeners.enter(subject);
        dispatcher.dispatchTo(this, subject);
        listeners.exit(subject);
    }
    
    public void visitExecuteInstance(ExecuteInstance subject) {
        listeners.enter(subject);
        dispatcher.dispatchTo(this, subject); 
        listeners.exit(subject);
    }

    public void visitCloud(Cloud subject) {
        listeners.enter(subject);
        dispatcher.dispatchTo(this, subject);
        listeners.exit(subject);
    }

    public void visitResourcePoolInstance(ResourcePoolInstance subject){
        listeners.enter(subject);
        dispatcher.dispatchTo(this, subject);
        listeners.exit(subject);
    }
}