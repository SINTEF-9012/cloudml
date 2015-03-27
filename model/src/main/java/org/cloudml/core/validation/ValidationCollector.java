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

package org.cloudml.core.validation;

import org.cloudml.core.*;
import org.cloudml.core.visitors.AbstractVisitListener;

/**
 * On any deployment model object, append the result of the local validation.
 */
public class ValidationCollector extends AbstractVisitListener {
 
    private final Report validation;

    public ValidationCollector() {
        this.validation = new Report();
    }

    public void collect(CanBeValidated toValidate) {
        validation.append(toValidate.validate());
    }

    public Report getOverallReport() {
        return this.validation;
    }

    @Override
    public void onDeploymentEntry(Deployment subject) {
        collect(subject);
    }

    @Override
    public void onProviderEntry(Provider subject) {
        collect(subject);
    }

    @Override
    public void onResourcePoolInstanceEntry(ResourcePoolInstance subject) {
        collect(subject);
    }


    @Override
    public void onVMEntry(VM subject) {
        collect(subject);
    }

    @Override
    public void onExternalComponentEntry(ExternalComponent subject) {
        collect(subject);
    }

    @Override
    public void onInternalComponentEntry(InternalComponent subject) {
        collect(subject);
    }

    @Override
    public void onRequiredPortEntry(RequiredPort subject) {
        collect(subject);
    }

    @Override
    public void onProvidedPortEntry(ProvidedPort subject) {
        collect(subject);
    }

    @Override
    public void onProvidedExecutionPlatformEntry(ProvidedExecutionPlatform subject) {
        collect(subject);
    }

    @Override
    public void onRequiredExecutionPlatformEntry(RequiredExecutionPlatform subject) {
        collect(subject);
    }

    @Override
    public void onRelationshipEntry(Relationship subject) {
           collect(subject);
    }

    @Override
    public void onVMInstanceEntry(VMInstance subject) {
        collect(subject);
    }

    @Override
    public void onExternalComponentInstanceEntry(ExternalComponentInstance subject) {
        collect(subject);
    }

    @Override
    public void onInternalComponentInstanceEntry(InternalComponentInstance subject) {
        collect(subject);
    }

    @Override
    public void onRequiredPortInstanceEntry(RequiredPortInstance subject) {
        collect(subject);
    }

    @Override
    public void onProvidedPortInstanceEntry(ProvidedPortInstance subject) {
        collect(subject);
    }

    @Override
    public void onProvidedExecutionPlatformInstanceEntry(ProvidedExecutionPlatformInstance subject) {
        collect(subject);
    }

    @Override
    public void onRequiredExecutionPlatformInstanceEntry(RequiredExecutionPlatformInstance subject) {
        collect(subject);
    }

    @Override
    public void onRelationshipInstanceEntry(RelationshipInstance subject) {
        collect(subject);
    }

    @Override
    public void onExecuteInstanceEntry(ExecuteInstance subject) {
        collect(subject);
    }

    @Override
    public void onCloudEntry(Cloud subject) {
        collect(subject);
    }
   
}
