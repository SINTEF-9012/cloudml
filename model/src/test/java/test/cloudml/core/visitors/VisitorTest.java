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
/*
 */
package test.cloudml.core.visitors;

import junit.framework.TestCase;
import org.cloudml.core.*;
import org.cloudml.core.visitors.ContainmentDispatcher;
import org.cloudml.core.visitors.VisitListener;
import org.cloudml.core.visitors.Visitor;
import static org.cloudml.core.samples.SshClientServer.*;
import org.junit.Test;
import org.jmock.Expectations;
import static org.jmock.Expectations.any;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;

public class VisitorTest extends TestCase {

    private final Mockery context = new JUnit4Mockery();

    // TODO: Break this test in many smaller ones
    @Test
    public void testListenersAreInvoked() {
        final Deployment model = getOneClientConnectedToOneServer().build();
        final VisitListener processor = context.mock(VisitListener.class);

        context.checking(new Expectations() {
            {
                oneOf(processor).onDeploymentEntry(with(any(Deployment.class)));
                oneOf(processor).onDeploymentExit(with(any(Deployment.class)));
                
                oneOf(processor).onProviderEntry(with(any(Provider.class)));
                oneOf(processor).onProviderExit(with(any(Provider.class)));
                
                exactly(2).of(processor).onVMEntry(with(any(VM.class)));
                exactly(2).of(processor).onVMExit(with(any(VM.class)));
                
                exactly(2).of(processor).onInternalComponentEntry(with(any(InternalComponent.class)));
                exactly(2).of(processor).onInternalComponentExit(with(any(InternalComponent.class)));
                
                oneOf(processor).onProvidedPortEntry(with(any(ProvidedPort.class)));
                oneOf(processor).onProvidedPortExit(with(any(ProvidedPort.class)));
                
                exactly(2).of(processor).onProvidedExecutionPlatformEntry(with(any(ProvidedExecutionPlatform.class)));
                exactly(2).of(processor).onProvidedExecutionPlatformExit(with(any(ProvidedExecutionPlatform.class)));

                oneOf(processor).onRequiredPortEntry(with(any(RequiredPort.class)));
                oneOf(processor).onRequiredPortExit(with(any(RequiredPort.class)));
                
                exactly(2).of(processor).onRequiredExecutionPlatformEntry(with(any(RequiredExecutionPlatform.class)));
                exactly(2).of(processor).onRequiredExecutionPlatformExit(with(any(RequiredExecutionPlatform.class)));
                
                oneOf(processor).onRelationshipEntry(with(any(Relationship.class)));
                oneOf(processor).onRelationshipExit(with(any(Relationship.class)));
                       
                exactly(2).of(processor).onVMInstanceEntry(with(any(VMInstance.class)));
                exactly(2).of(processor).onVMInstanceExit(with(any(VMInstance.class)));
                
                exactly(2).of(processor).onInternalComponentInstanceEntry(with(any(InternalComponentInstance.class)));
                exactly(2).of(processor).onInternalComponentInstanceExit(with(any(InternalComponentInstance.class)));
                
                oneOf(processor).onRequiredPortInstanceEntry(with(any(RequiredPortInstance.class)));
                oneOf(processor).onRequiredPortInstanceExit(with(any(RequiredPortInstance.class)));
                
                exactly(2).of(processor).onRequiredExecutionPlatformInstanceEntry(with(any(RequiredExecutionPlatformInstance.class)));
                exactly(2).of(processor).onRequiredExecutionPlatformInstanceExit(with(any(RequiredExecutionPlatformInstance.class)));
                
                oneOf(processor).onProvidedPortInstanceEntry(with(any(ProvidedPortInstance.class)));
                oneOf(processor).onProvidedPortInstanceExit(with(any(ProvidedPortInstance.class)));
                
                exactly(2).of(processor).onProvidedExecutionPlatformInstanceEntry(with(any(ProvidedExecutionPlatformInstance.class)));
                exactly(2).of(processor).onProvidedExecutionPlatformInstanceExit(with(any(ProvidedExecutionPlatformInstance.class)));
                
                oneOf(processor).onRelationshipInstanceEntry(with(any(RelationshipInstance.class)));
                oneOf(processor).onRelationshipInstanceExit(with(any(RelationshipInstance.class)));
                
                exactly(2).of(processor).onExecuteInstanceEntry(with(any(ExecuteInstance.class)));
                exactly(2).of(processor).onExecuteInstanceExit(with(any(ExecuteInstance.class)));
                
            }
        });

        Visitor visitor = new Visitor(new ContainmentDispatcher(), processor);
        model.accept(visitor);
    }
}
