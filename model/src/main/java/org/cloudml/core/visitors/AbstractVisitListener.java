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


/**
 * Provides default empty implementation for each event handler
 */
public abstract class AbstractVisitListener implements VisitListener { 

    @Override
    public void onDeploymentEntry(Deployment subject) {
    }

    @Override
    public void onProviderEntry(Provider subject) {
    }

    @Override
    public void onResourcePoolInstanceEntry(ResourcePoolInstance subject) {
    }

    @Override
    public void onVMEntry(VM subject) {
    }

    @Override
    public void onExternalComponentEntry(ExternalComponent subject) {
    }

    @Override
    public void onInternalComponentEntry(InternalComponent subject) {
    }

    @Override
    public void onRequiredPortEntry(RequiredPort subject) {
    }

    @Override
    public void onProvidedPortEntry(ProvidedPort subject) {
    }

    @Override
    public void onProvidedExecutionPlatformEntry(ProvidedExecutionPlatform subject) {
    }

    @Override
    public void onRequiredExecutionPlatformEntry(RequiredExecutionPlatform subject) {
    }

    @Override
    public void onRelationshipEntry(Relationship subject) {
    }

    @Override
    public void onVMInstanceEntry(VMInstance subject) {
    }

    @Override
    public void onExternalComponentInstanceEntry(ExternalComponentInstance subject) {
    }

    @Override
    public void onInternalComponentInstanceEntry(InternalComponentInstance subject) {
    }

    @Override
    public void onRequiredPortInstanceEntry(RequiredPortInstance subject) {
    }

    @Override
    public void onProvidedPortInstanceEntry(ProvidedPortInstance subject) {
    }

    @Override
    public void onProvidedExecutionPlatformInstanceEntry(ProvidedExecutionPlatformInstance subject) {
    }

    @Override
    public void onRequiredExecutionPlatformInstanceEntry(RequiredExecutionPlatformInstance subject) {
    }

    @Override
    public void onRelationshipInstanceEntry(RelationshipInstance subject) {
    }

    @Override
    public void onExecuteInstanceEntry(ExecuteInstance subject) {
    }

    @Override
    public void onCloudEntry(Cloud cloud) {
    }


    @Override
    public void onDeploymentExit(Deployment subject) {
    }


    @Override
    public void onProviderExit(Provider subject) {
    }


    @Override
    public void onVMExit(VM subject) {
    }


    @Override
    public void onExternalComponentExit(ExternalComponent subject) {
    }


    @Override
    public void onInternalComponentExit(InternalComponent subject) {
    }


    @Override
    public void onRequiredPortExit(RequiredPort subject) {
    }


    @Override
    public void onProvidedPortExit(ProvidedPort subject) {
    }


    @Override
    public void onProvidedExecutionPlatformExit(ProvidedExecutionPlatform subject) {
    }


    @Override
    public void onRequiredExecutionPlatformExit(RequiredExecutionPlatform subject) {
    }


    @Override
    public void onRelationshipExit(Relationship subject) {
    }


    @Override
    public void onVMInstanceExit(VMInstance subject) {
    }


    @Override
    public void onExternalComponentInstanceExit(ExternalComponentInstance subject) {
    }


    @Override
    public void onInternalComponentInstanceExit(InternalComponentInstance subject) {
    }


    @Override
    public void onRequiredPortInstanceExit(RequiredPortInstance subject) {
    }


    @Override
    public void onProvidedPortInstanceExit(ProvidedPortInstance subject) {
    }


    @Override
    public void onProvidedExecutionPlatformInstanceExit(ProvidedExecutionPlatformInstance subject) {
    }


    @Override
    public void onRequiredExecutionPlatformInstanceExit(RequiredExecutionPlatformInstance subject) {
    }


    @Override
    public void onRelationshipInstanceExit(RelationshipInstance subject) {
    }


    @Override
    public void onExecuteInstanceExit(ExecuteInstance subject) {
    }


    @Override
    public void onCloudExit(Cloud cloud) {
    }

    @Override
    public void onResourcePoolInstanceExit(ResourcePoolInstance cloud) {
    }



}
