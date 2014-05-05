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
 * Required behaviour for a reacting to visit events
 */
public interface VisitListener {

    public void onDeployment(Deployment subject);

    public void onProvider(Provider subject);

    public void onVM(VM subject);

    public void onExternalComponent(ExternalComponent subject);

    public void onInternalComponent(InternalComponent subject);

    public void onRequiredPort(RequiredPort subject);

    public void onProvidedPort(ProvidedPort subject);

    public void onProvidedExecutionPlatform(ProvidedExecutionPlatform subject);

    public void onRequiredExecutionPlatform(RequiredExecutionPlatform subject);

    public void onRelationship(Relationship subject);

    public void onVMInstance(VMInstance subject);

    public void onExternalComponentInstance(ExternalComponentInstance subject);

    public void onInternalComponentInstance(InternalComponentInstance subject);

    public void onRequiredPortInstance(RequiredPortInstance subject);

    public void onProvidedPortInstance(ProvidedPortInstance subject);

    public void onProvidedExecutionPlatformInstance(ProvidedExecutionPlatformInstance subject);

    public void onRequiredExecutionPlatformInstance(RequiredExecutionPlatformInstance subject);

    public void onRelationshipInstance(RelationshipInstance subject);

    public void onExecuteInstance(ExecuteInstance subject);
    
    public void onCloud(Cloud cloud);
}
