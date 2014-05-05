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
package org.cloudml.core.builders;

import org.cloudml.core.ExternalComponent;
import org.cloudml.core.ExternalComponentInstance;

/**
 * Ease importing all static factory methods used for building deployment models
 */
public class Commons {

    public static DeploymentBuilder aDeployment() {
        return new DeploymentBuilder();
    }

    public static ResourceBuilder aResource() {
        return new ResourceBuilder();
    }

    public static VMBuilder aVM() {
        return new VMBuilder();
    }

    public static ExternalComponentBuilder<? extends ExternalComponent, ExternalComponentBuilder<?, ?>> anExternalComponent() {
        return new ExternalComponentBuilder<ExternalComponent, ExternalComponentBuilder<?, ?>>(); 
    }

    public static InternalComponentBuilder anInternalComponent() {
        return new InternalComponentBuilder();
    }

    public static ProviderBuilder aProvider() {
        return new ProviderBuilder();
    }

    public static RequiredPortBuilder aRequiredPort() {
        return new RequiredPortBuilder();
    }

    public static ProvidedPortBuilder aProvidedPort() {
        return new ProvidedPortBuilder();
    }

    public static ProvidedExecutionPlatformBuilder aProvidedExecutionPlatform() {
        return new ProvidedExecutionPlatformBuilder();
    }

    public static RequiredExecutionPlatformBuilder aRequiredExecutionPlatform() {
        return new RequiredExecutionPlatformBuilder();
    }

    public static RequiredPortInstanceBuilder aClientPortInstance() {
        return new RequiredPortInstanceBuilder();
    }

    public static ProvidedPortInstanceBuilder aServerPortInstance() {
        return new ProvidedPortInstanceBuilder();
    }

    public static RelationshipBuilder aRelationship() {
        return new RelationshipBuilder();
    }

    public static ExternalComponentInstanceBuilder<ExternalComponentInstance<ExternalComponent>, ExternalComponentInstanceBuilder<?, ?>> anExternalComponentInstance() {
        return new ExternalComponentInstanceBuilder<ExternalComponentInstance<ExternalComponent>, ExternalComponentInstanceBuilder<?, ?>>();
    }

    public static VMInstanceBuilder aVMInstance() {
        return new VMInstanceBuilder();
    }

    public static InternalComponentInstanceBuilder anInternalComponentInstance() {
        return new InternalComponentInstanceBuilder();
    }

    public static RelationshipInstanceBuilder aRelationshipInstance() {
        return new RelationshipInstanceBuilder();
    }
}
