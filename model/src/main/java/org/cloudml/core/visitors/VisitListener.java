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
 * Required behaviour for a reacting to visit events
 */
public interface VisitListener {

    public void onDeploymentEntry(Deployment subject);

    public void onDeploymentExit(Deployment subject);

    public void onProviderEntry(Provider subject);

    public void onProviderExit(Provider subject);

    public void onVMEntry(VM subject);

    public void onVMExit(VM subject);

    public void onExternalComponentEntry(ExternalComponent subject);

    public void onExternalComponentExit(ExternalComponent subject);

    public void onInternalComponentEntry(InternalComponent subject);

    public void onInternalComponentExit(InternalComponent subject);

    public void onRequiredPortEntry(RequiredPort subject);

    public void onRequiredPortExit(RequiredPort subject);

    public void onProvidedPortEntry(ProvidedPort subject);

    public void onProvidedPortExit(ProvidedPort subject);

    public void onProvidedExecutionPlatformEntry(ProvidedExecutionPlatform subject);

    public void onProvidedExecutionPlatformExit(ProvidedExecutionPlatform subject);

    public void onRequiredExecutionPlatformEntry(RequiredExecutionPlatform subject);

    public void onRequiredExecutionPlatformExit(RequiredExecutionPlatform subject);

    public void onRelationshipEntry(Relationship subject);

    public void onRelationshipExit(Relationship subject);

    public void onVMInstanceEntry(VMInstance subject);

    public void onVMInstanceExit(VMInstance subject);

    public void onExternalComponentInstanceEntry(ExternalComponentInstance subject);

    public void onExternalComponentInstanceExit(ExternalComponentInstance subject);

    public void onInternalComponentInstanceEntry(InternalComponentInstance subject);

    public void onInternalComponentInstanceExit(InternalComponentInstance subject);

    public void onRequiredPortInstanceEntry(RequiredPortInstance subject);

    public void onRequiredPortInstanceExit(RequiredPortInstance subject);

    public void onProvidedPortInstanceEntry(ProvidedPortInstance subject);

    public void onProvidedPortInstanceExit(ProvidedPortInstance subject);

    public void onProvidedExecutionPlatformInstanceEntry(ProvidedExecutionPlatformInstance subject);

    public void onProvidedExecutionPlatformInstanceExit(ProvidedExecutionPlatformInstance subject);

    public void onRequiredExecutionPlatformInstanceEntry(RequiredExecutionPlatformInstance subject);

    public void onRequiredExecutionPlatformInstanceExit(RequiredExecutionPlatformInstance subject);

    public void onRelationshipInstanceEntry(RelationshipInstance subject);

    public void onRelationshipInstanceExit(RelationshipInstance subject);

    public void onExecuteInstanceEntry(ExecuteInstance subject);

    public void onExecuteInstanceExit(ExecuteInstance subject);

    public void onCloudEntry(Cloud cloud);

    public void onCloudExit(Cloud cloud);

    public void onResourcePoolInstanceEntry(ResourcePoolInstance resourcePoolInstance);

    public void onResourcePoolInstanceExit(ResourcePoolInstance resourcePoolInstance);
}
