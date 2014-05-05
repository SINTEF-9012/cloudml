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
import org.cloudml.core.visitors.VisitListener;

/**
 * On any deployment model object, append the result of the local validation.
 */
public class ValidationCollector implements VisitListener {

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
    public void onDeployment(Deployment subject) {
        collect(subject);
    }

    @Override
    public void onProvider(Provider subject) {
        collect(subject);
    }

    @Override
    public void onVM(VM subject) {
        collect(subject);
    }

    @Override
    public void onExternalComponent(ExternalComponent subject) {
        collect(subject);
    }

    @Override
    public void onInternalComponent(InternalComponent subject) {
        collect(subject);
    }

    @Override
    public void onRequiredPort(RequiredPort subject) {
        collect(subject);
    }

    @Override
    public void onProvidedPort(ProvidedPort subject) {
        collect(subject);
    }

    @Override
    public void onProvidedExecutionPlatform(ProvidedExecutionPlatform subject) {
        collect(subject);
    }

    @Override
    public void onRequiredExecutionPlatform(RequiredExecutionPlatform subject) {
        collect(subject);
    }

    @Override
    public void onRelationship(Relationship subject) {
           collect(subject);
    }

    @Override
    public void onVMInstance(VMInstance subject) {
        collect(subject);
    }

    @Override
    public void onExternalComponentInstance(ExternalComponentInstance subject) {
        collect(subject);
    }

    @Override
    public void onInternalComponentInstance(InternalComponentInstance subject) {
        collect(subject);
    }

    @Override
    public void onRequiredPortInstance(RequiredPortInstance subject) {
        collect(subject);
    }

    @Override
    public void onProvidedPortInstance(ProvidedPortInstance subject) {
        collect(subject);
    }

    @Override
    public void onProvidedExecutionPlatformInstance(ProvidedExecutionPlatformInstance subject) {
        collect(subject);
    }

    @Override
    public void onRequiredExecutionPlatformInstance(RequiredExecutionPlatformInstance subject) {
        collect(subject);
    }

    @Override
    public void onRelationshipInstance(RelationshipInstance subject) {
        collect(subject);
    }

    @Override
    public void onExecuteInstance(ExecuteInstance subject) {
        collect(subject);
    }

    @Override
    public void onCloud(Cloud subject) {
        collect(subject);
    }
   
}
