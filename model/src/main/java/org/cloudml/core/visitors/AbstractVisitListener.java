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

import org.cloudml.core.Cloud;
import org.cloudml.core.Deployment;
import org.cloudml.core.ExecuteInstance;
import org.cloudml.core.ExternalComponent;
import org.cloudml.core.ExternalComponentInstance;
import org.cloudml.core.InternalComponent;
import org.cloudml.core.InternalComponentInstance;
import org.cloudml.core.ProvidedExecutionPlatform;
import org.cloudml.core.ProvidedExecutionPlatformInstance;
import org.cloudml.core.ProvidedPort;
import org.cloudml.core.ProvidedPortInstance;
import org.cloudml.core.Provider;
import org.cloudml.core.Relationship;
import org.cloudml.core.RelationshipInstance;
import org.cloudml.core.RequiredExecutionPlatform;
import org.cloudml.core.RequiredExecutionPlatformInstance;
import org.cloudml.core.RequiredPort;
import org.cloudml.core.RequiredPortInstance;
import org.cloudml.core.VM;
import org.cloudml.core.VMInstance;



/**
 * Provides default empty implementation for each event handler
 */
public abstract class AbstractVisitListener implements VisitListener {

    @Override
    public void onDeployment(Deployment subject) {
    }

    @Override
    public void onProvider(Provider subject) {
    }

    @Override
    public void onVM(VM subject) {
    }

    @Override
    public void onExternalComponent(ExternalComponent subject) {
    }

    @Override
    public void onInternalComponent(InternalComponent subject) {
    }

    @Override
    public void onRequiredPort(RequiredPort subject) {
    }

    @Override
    public void onProvidedPort(ProvidedPort subject) {
    }

    @Override
    public void onProvidedExecutionPlatform(ProvidedExecutionPlatform subject) {
    }

    @Override
    public void onRequiredExecutionPlatform(RequiredExecutionPlatform subject) {
    }

    @Override
    public void onRelationship(Relationship subject) {
    }

    @Override
    public void onVMInstance(VMInstance subject) {
    }

    @Override
    public void onExternalComponentInstance(ExternalComponentInstance subject) {
    }

    @Override
    public void onInternalComponentInstance(InternalComponentInstance subject) {
    }

    @Override
    public void onRequiredPortInstance(RequiredPortInstance subject) {
    }

    @Override
    public void onProvidedPortInstance(ProvidedPortInstance subject) {
    }

    @Override
    public void onProvidedExecutionPlatformInstance(ProvidedExecutionPlatformInstance subject) {
    }

    @Override
    public void onRequiredExecutionPlatformInstance(RequiredExecutionPlatformInstance subject) {
    }

    @Override
    public void onRelationshipInstance(RelationshipInstance subject) {
    }

    @Override
    public void onExecuteInstance(ExecuteInstance subject) {
    }

    @Override
    public void onCloud(Cloud cloud) {
    }

}
