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

    @Test
    public void testListenersAreInvoked() {
        final Deployment model = getOneClientConnectedToOneServer().build();
        final VisitListener processor = context.mock(VisitListener.class);

        context.checking(new Expectations() {
            {
                oneOf(processor).onDeployment(with(any(Deployment.class)));
                oneOf(processor).onProvider(with(any(Provider.class)));
                exactly(2).of(processor).onVM(with(any(VM.class)));
                exactly(2).of(processor).onInternalComponent(with(any(InternalComponent.class)));
                oneOf(processor).onProvidedPort(with(any(ProvidedPort.class)));
                exactly(2).of(processor).onProvidedExecutionPlatform(with(any(ProvidedExecutionPlatform.class)));
                oneOf(processor).onRequiredPort(with(any(RequiredPort.class)));
                exactly(2).of(processor).onRequiredExecutionPlatform(with(any(RequiredExecutionPlatform.class)));
                oneOf(processor).onRelationship(with(any(Relationship.class)));
                exactly(2).of(processor).onVMInstance(with(any(VMInstance.class)));
                exactly(2).of(processor).onInternalComponentInstance(with(any(InternalComponentInstance.class)));
                oneOf(processor).onRequiredPortInstance(with(any(RequiredPortInstance.class)));
                exactly(2).of(processor).onRequiredExecutionPlatformInstance(with(any(RequiredExecutionPlatformInstance.class)));
                oneOf(processor).onProvidedPortInstance(with(any(ProvidedPortInstance.class)));
                exactly(2).of(processor).onProvidedExecutionPlatformInstance(with(any(ProvidedExecutionPlatformInstance.class)));
                oneOf(processor).onRelationshipInstance(with(any(RelationshipInstance.class)));
                exactly(2).of(processor).onExecuteInstance(with(any(ExecuteInstance.class)));
            }
        });

        Visitor visitor = new Visitor(new ContainmentDispatcher(), processor);
        model.accept(visitor);
    }
}
