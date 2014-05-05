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

import org.cloudml.core.*;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.cloudml.core.builders.Commons.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import static org.cloudml.core.samples.SshClientServer.*;

@RunWith(JUnit4.class)
public class DeploymentTest extends WithPropertiesTest {

    @Override
    public WithProperties aSampleWithProperties(String name) {
        return aDeployment().named(name).build();
    }

    @Test(expected = IllegalStateException.class)
    public void rejectDuplicatedProviderNames() {
        final Deployment sut = aDeployment()
                .with(amazonEc2())
                .build();

        final Provider duplicatedProvider = aProvider().named(AMAZON_EC2).build();

        sut.getProviders().add(duplicatedProvider);
    }

    @Test(expected = IllegalStateException.class)
    public void rejectDuplicatedComponentNames() {
        final Deployment sut = aDeployment()
                .with(amazonEc2())
                .with(ec2LargeLinux())
                .build();

        sut.getComponents().add(ec2LargeLinux().build());
    }

    @Test(expected = IllegalStateException.class)
    public void rejectDuplicatedComponentInstanceNames() {
        final String vmName = "VM #1";

        final Deployment sut = aDeployment()
                .with(amazonEc2())
                .with(ec2LargeLinux())
                .with(aVMInstance()
                .named(vmName)
                .ofType(EC2_LARGE_LINUX))
                .build();

        final VMInstance duplicatedVM = aVMInstance()
                .named(vmName)
                .ofType(EC2_LARGE_LINUX)
                .build();

        sut.getComponentInstances().add(duplicatedVM);
    }

    @Test(expected = IllegalStateException.class)
    public void rejectDuplicatedRelationshipNames() {
        final Deployment sut = aDeployment()
                .with(sshClient())
                .with(sshServer())
                .with(sshConnection())
                .build();

        sut.getRelationships().add(sshConnection().build());
    }

    @Test(expected = IllegalStateException.class)
    public void rejectDuplicatedRelationshipInstanceNames() {
        final Deployment sut = aDeployment()
                .with(amazonEc2())
                .with(ec2LargeLinux())
                .with(ec2XLargeWindows7())
                .with(sshClient())
                .with(sshServer())
                .with(sshConnection())
                .with(aVMInstance()
                .named("client host")
                .ofType(EC2_LARGE_LINUX))
                .with(aVMInstance()
                .named("server host")
                .ofType(EC2_XLARGE_WINDOWS_7))
                .with(anInternalComponentInstance()
                .named("client")
                .ofType(SSH_CLIENT)
                .hostedBy("client host"))
                .with(anInternalComponentInstance()
                .named("server")
                .ofType(SSH_SERVER)
                .hostedBy("server host"))
                .with(aRelationshipInstance()
                .named("ssh connection")
                .ofType(SSH_CONNECTION)
                .from("client", CLIENT_PORT)
                .to("server", SERVER_PORT))
                .build();

        final RelationshipInstance duplicated = aRelationshipInstance()
                .named("ssh connection")
                .build();

        sut.getRelationshipInstances().add(duplicated);
    }

    @Test
    public void addedProvidersAreContained() {
        final Deployment model = aDeployment().build();
        final Provider provider = new Provider("ec2");

        model.getProviders().add(provider);

        assertThat("provider included", model.getProviders(), contains(provider));
        assertThat("provider owner", provider.getOwner().get(), is(sameInstance(model)));
    }

    @Test
    public void removedProviderAreNotContainedAnymore() {
        final Deployment model = aDeployment()
                .with(amazonEc2())
                .build();

        final Provider provider = model.getProviders().firstNamed(AMAZON_EC2);

        model.getProviders().remove(provider);

        assertThat("provider not contained", model.getProviders(), not(contains(provider)));
        assertThat("no owner", provider.getOwner().isUndefined());
    }

    @Test(expected = IllegalStateException.class)
    public void rejectRemovingProvidersThatAreStillInUse() {
        final Deployment model = aDeployment()
                .with(amazonEc2())
                .with(ec2LargeLinux())
                .build();

        final Provider provider = model.getProviders().firstNamed(AMAZON_EC2);

        model.getProviders().remove(provider);
    }

    @Test
    public void addedVMsAreContained() {
        final Deployment model = aDeployment()
                .with(amazonEc2())
                .build();
        final Component largeLinux = ec2LargeLinux().build();

        model.getComponents().add(largeLinux);

        assertThat("is contained", model.getComponents(), contains(largeLinux));
        assertThat("is owner", largeLinux.getDeployment(), is(sameInstance(model)));
    }

    @Test
    public void removedVMAreNotContainedAnymore() {
        final Deployment model = aDeployment()
                .with(amazonEc2())
                .with(ec2LargeLinux())
                .build();
        final Component largeLinux = model.getComponents().onlyVMs().firstNamed(EC2_LARGE_LINUX);   

        model.getComponents().remove(largeLinux);

        assertThat("not contained", model.getComponents(), not(contains(largeLinux)));
        assertThat("no owner", largeLinux.getOwner().isUndefined());
    }

    @Test(expected = IllegalStateException.class)
    public void rejectRemoveVMThatStillHaveInstances() {
        final Deployment model = aDeployment()
                .with(amazonEc2())
                .with(ec2LargeLinux())
                .with(aVMInstance()
                .named("a VM instance")
                .ofType(EC2_LARGE_LINUX))
                .build();

        final VM vm = model.getComponents().onlyVMs().firstNamed(EC2_LARGE_LINUX);

        model.getComponents().remove(vm);
    }

    @Test
    public void addedInternalComponentAreContained() {
        final Deployment model = aDeployment().build();
        final InternalComponent client = sshClient().build();

        model.getComponents().add(client);

        assertThat("is contained", model.getComponents().onlyInternals(), contains(client));
        assertThat("has owner", client.getOwner().get(), is(sameInstance(model)));
    }

    @Test
    public void removedInternalComponentsAreNotContainedAnymore() {
        final Deployment model = aDeployment()
                .with(sshClient())
                .build();

        final InternalComponent client = model.getComponents().onlyInternals().firstNamed(SSH_CLIENT);

        model.getComponents().remove(client);

        assertThat("not contained anymore", model.getComponents().onlyInternals(), not(contains(client)));
        assertThat("no owner", client.getOwner().isUndefined());
    }

    @Test(expected = IllegalStateException.class)
    public void rejectRemovingArtefactsThatAreStillInUse() {
        final Deployment model = aDeployment()
                .with(amazonEc2())
                .with(ec2LargeLinux())
                .with(aVMInstance()
                .named("a vm instance")
                .ofType(EC2_LARGE_LINUX))
                .with(sshClient())
                .with(anInternalComponentInstance()
                .named("The client")
                .ofType(SSH_CLIENT)
                .hostedBy("a vm instance"))
                .build();
        final InternalComponent client = model.getComponents().onlyInternals().firstNamed(SSH_CLIENT);

        model.getComponents().remove(client);
    }

    @Test
    public void validateWithoutWarningsWhenValid() {
        Deployment model = aDeployment()
                .with(amazonEc2())
                .build();

        Report validation = model.validate();

        assertThat("no warnings", validation.pass(Report.WITHOUT_WARNING));
    }

    @Test
    public void validationReportsEmptyModel() {
        Deployment model = aDeployment().build();

        Report validation = model.validate();

        assertThat("empty model detected", validation.hasWarningAbout("empty", "deployment", "model"));
    }
}
