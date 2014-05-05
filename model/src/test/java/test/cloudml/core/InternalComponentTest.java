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
package test.cloudml.core;

import junit.framework.TestCase;
import org.cloudml.core.Deployment;
import org.cloudml.core.InternalComponent;
import org.cloudml.core.InternalComponentInstance;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.cloudml.core.builders.Commons.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import static org.cloudml.core.samples.SshClientServer.*;

@RunWith(JUnit4.class)
public class InternalComponentTest extends TestCase {

    @Test
    public void testValidationPassesWhenValid() {
        final InternalComponent internalComponent = aSampleInternalComponent();

        final Report validation = internalComponent.validate();

        assertThat("Validation shall not detect any issue in a valid internal component",
                   validation.pass(Report.WITHOUT_WARNING));
    }

    private InternalComponent aSampleInternalComponent() {
        final Deployment model = aDeployment()
                .with(amazonEc2())
                .with(ec2LargeLinux())
                .with(sshClient())
                .build();

        return model.getComponents().onlyInternals().firstNamed(SSH_CLIENT);
    }

    @Test
    public void testInstantiates() {
        final InternalComponent sut = aSampleInternalComponent();
        
        final InternalComponentInstance instance = sut.instantiate();
        
        Verify.correctInternalComponentInstance(sut, instance);
    }
    
    @Test
    public void validationDetectsMissingPorts() {
        final InternalComponent internalComponent = aSampleInternalComponent();
        internalComponent.getRequiredPorts().clear();
        internalComponent.getProvidedPorts().clear();

        final Report validation = internalComponent.validate();

        assertThat("missing port not detected", validation.hasWarningAbout("no", "required", "provided", "port"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRejectsNullAsRequiredExecutionPlatform() {
        final InternalComponent internalComponent = aSampleInternalComponent();
        internalComponent.setRequiredExecutionPlatform(null);
    }

    @Test
    public void validationDetectsMissingOwner() {
        final InternalComponent internalComponent = aSampleInternalComponent();
        internalComponent.getOwner().discard();

        final Report validation = internalComponent.validate();

        assertThat("missing owner not detected", validation.hasErrorAbout("no", "owner"));
    }

    @Test
    public void testContainementInDeploymentModel() {
        final InternalComponent internalComponent = aSampleInternalComponent();

        final Deployment owner = internalComponent.getOwner().get();

        assertThat("no owner", owner, is(not(nullValue())));
        assertThat("is contained by its owner", owner.getComponents().onlyInternals(), contains(internalComponent));
    }
}
