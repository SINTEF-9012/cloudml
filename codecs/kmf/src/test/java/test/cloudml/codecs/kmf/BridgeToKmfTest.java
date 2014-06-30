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
package test.cloudml.codecs.kmf;

import junit.framework.TestCase;

import org.cloudml.codecs.BridgeToKmf;
import org.cloudml.core.*;
import org.cloudml.core.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;
import static org.cloudml.core.builders.Commons.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@RunWith(JUnit4.class)
public class BridgeToKmfTest extends TestCase {

    @Test
    public void offersShouldBeConvertedInKmf() {
        final Deployment cloudml = aDeployment()
                .with(aProvider()
                        .named("EC2"))
                .with(aVM()
                        .named("VM")
                        .providedBy("EC2")
                        .with(aProvidedExecutionPlatform()
                                .offering("OS", "Linux")))
                .build();

        final BridgeToKmf bridge = new BridgeToKmf();
        final net.cloudml.core.CloudMLModel kmf = bridge.toKMF(cloudml);

        net.cloudml.core.VM vm = null;
        for (net.cloudml.core.VM each: kmf.getVms()) {
            if (each.getName().equals("VM")) {
                vm = each;
                break;
            }
        }
        assertThat("missing vm", vm, is(not(nullValue())));

        assertThat("provided execution platform", vm.getProvidedExecutionPlatforms().size(), is(equalTo(1)));

        net.cloudml.core.ProvidedExecutionPlatform pep = vm.getProvidedExecutionPlatforms().get(0);
        assertThat(pep, is(not(nullValue())));

        assertThat("offering", pep.getOffers().size(), is(equalTo(1)));
        net.cloudml.core.Property property = pep.getOffers().get(0);

        assertThat(property.getName(), is(equalTo("OS")));
        assertThat(property.getValue(), is(equalTo("Linux")));

    }

    @Test
    public void demandsShouldBeConvertedToKmf() {
        final Deployment cloudml = aDeployment()
                .with(anInternalComponent()
                        .named("foo")
                        .with(aRequiredExecutionPlatform()
                                .demanding("OS", "Linux")))
                .build();

        final BridgeToKmf bridge = new BridgeToKmf();
        final net.cloudml.core.CloudMLModel kmf = bridge.toKMF(cloudml);

        net.cloudml.core.InternalComponent cpt = null;
        for (net.cloudml.core.InternalComponent each: kmf.getInternalComponents()) {
            if (each.getName().equals("foo")) {
                cpt = each;
                break;
            }
        }

        assertThat(cpt, is(not(nullValue())));
        assertThat("required execution platform", cpt.getRequiredExecutionPlatform(), is(not(nullValue())));

        net.cloudml.core.RequiredExecutionPlatform rep = cpt.getRequiredExecutionPlatform();
        assertThat("demands count", rep.getDemands().size(), is(equalTo(1)));

        net.cloudml.core.Property property = rep.getDemands().get(0);
        assertThat("demand's key", property.getName(), is(equalTo("OS")));
        assertThat("demand's value", property.getValue(), is(equalTo("Linux")));
    }

    /**
     * Tests providers
     */
    @Test(expected = IllegalArgumentException.class)
    public void testProviderToKmfWithNull() {
        BridgeToKmf bridge = new BridgeToKmf();
        bridge.providersToKmf(null);
    }

    @Test
    public void testProviderToKmfEmpty() {
        BridgeToKmf bridge = new BridgeToKmf();
        bridge.providersToKmf(new ArrayList<Provider>());
        assertTrue(bridge.getKmfModel().getProviders().isEmpty());
    }

    @Test
    public void testProviderToKmfWithElements() {
        BridgeToKmf bridge = new BridgeToKmf();
        List<Provider> providers = new ArrayList<Provider>();

        CloudMLSamplesBuilder cloudMLSamples = new CloudMLSamplesBuilder();

        providers.add(cloudMLSamples.getProviderA());
        bridge.providersToKmf(providers);
        net.cloudml.core.Provider actual = bridge.getKmfModel().getProviders().get(0);

        Matcher matcher = new Matcher();
        assertTrue(new Matcher().match(actual, cloudMLSamples.getProviderA()));
    }

    /**
     * Tests on external components
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExternalComponentToKmfWithNull() {
        BridgeToKmf bridge = new BridgeToKmf();
        bridge.externalComponentToKmf(null);
    }

    @Test
    public void testExternalComponentToKmfWithElements() {
        BridgeToKmf bridge = new BridgeToKmf();

        CloudMLSamplesBuilder cloudMLSamples = new CloudMLSamplesBuilder();

        VM input = cloudMLSamples.getVMA();
        Deployment model = new Deployment();
        model.getProviders().add(input.getProvider());
        model.getComponents().add(input);
        bridge.toKMF(model);

        List<net.cloudml.core.VM> output = bridge.getKmfModel().getVms();
        assertFalse(output.isEmpty());
        net.cloudml.core.VM actual = output.get(0);

        assertTrue(new Matcher().matchVM(actual, cloudMLSamples.getVMA()));
    }

    /**
     * Tests on internal components
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInternalComponentToKmfWithNull() {
        BridgeToKmf bridge = new BridgeToKmf();
        bridge.internalComponentToKmf(null);
    }

    @Test
    public void testInternalComponentToKmfWithElements() {
        BridgeToKmf bridge = new BridgeToKmf();

        CloudMLSamplesBuilder cloudMLSamples = new CloudMLSamplesBuilder();

        bridge.internalComponentToKmf(cloudMLSamples.getInternalComponentA());
        net.cloudml.core.InternalComponent result = bridge.getKmfModel().getInternalComponents().get(0);

        assertTrue(new Matcher().matchIC(result, cloudMLSamples.getInternalComponentA()));
    }

    /**
     * Tests on relationships
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRelationshipToKmfWithNull() {
        BridgeToKmf bridge = new BridgeToKmf();
        bridge.relationshipToKmf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRelationshipToKmfWithInvalidElements() {
        BridgeToKmf bridge = new BridgeToKmf();
        Relationship rel = new Relationship("Sensapp", null, null);
        bridge.relationshipToKmf(rel);
    }

    @Test
    public void testRelationshipToKmfWithElements() {
        BridgeToKmf bridge = new BridgeToKmf();

        CloudMLSamplesBuilder cloudMLSamples = new CloudMLSamplesBuilder();
        Relationship input = cloudMLSamples.getRelationshipB();

        Deployment cm = new Deployment();
        cm.getRelationships().add(input);
        Component ownerProvided = input.getProvidedEnd().getOwner().get();
        cm.getComponents().add(ownerProvided);
        Component ownerRequired = input.getRequiredEnd().getOwner().get();
        cm.getComponents().add(ownerRequired);
        bridge.toKMF(cm);

        net.cloudml.core.Relationship output = bridge.getKmfModel().getRelationships().get(0);

        assertTrue(new Matcher().matchRelationship(output, input));
    }

    /**
     * Tests on VM instances
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExternalComponentInstanceToKmfWithNull() {
        BridgeToKmf bridge = new BridgeToKmf();
        bridge.externalComponentInstanceToKmf(null);
    }

    @Test
    public void testExternalComponentInstanceToKmfWithElement() {
        BridgeToKmf bridge = new BridgeToKmf();

        CloudMLSamplesBuilder cloudMLSamples = new CloudMLSamplesBuilder();
        Deployment cm = new Deployment();

        VMInstance input = cloudMLSamples.getVMInstanceA();
        cm.getProviders().add(input.getType().getProvider());
        cm.getComponents().add(input.getType());
        cm.getComponentInstances().add(input);
        bridge.toKMF(cm);

        assertFalse(bridge.getKmfModel().getVmInstances().isEmpty());
        net.cloudml.core.VMInstance output = bridge.getKmfModel().getVmInstances().get(0);
        assertNotNull(output);
        assertNotNull(output.getName());
        assertNotNull(output.getType());

        assertTrue(new Matcher().matchVMInstance(output, input));
    }

    /**
     * Tests in internal component instance
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInternalComponentInstanceToKmfWithNull() {
        BridgeToKmf bridge = new BridgeToKmf();
        bridge.internalComponentInstancesToKmf(null);
    }

    @Test
    public void testInternalComponentInstanceToKmfWithElement() {
        BridgeToKmf bridge = new BridgeToKmf();

        CloudMLSamplesBuilder cloudMLSamples = new CloudMLSamplesBuilder();
        Deployment cm = new Deployment();

        InternalComponentInstance input = cloudMLSamples.getInternalComponentInstanceA();
        cm.getComponents().add(input.getType());
        // TODO: Handle execute object when we build example models. (so as to mirror the behaviour using destination)
        cm.getComponentInstances().add(input);

        bridge.toKMF(cm);

        List<net.cloudml.core.InternalComponentInstance> ici = bridge.getKmfModel().getInternalComponentInstances();
        assertFalse(ici.isEmpty());

        net.cloudml.core.InternalComponentInstance output = ici.get(0);

        assertNotNull(output);
        assertNotNull(output.getName());
        assertNotNull(output.getType());
        assertNotNull(((net.cloudml.core.InternalComponentInstance) output).getRequiredExecutionPlatformInstance());

        assertTrue(new Matcher().matchICI((net.cloudml.core.InternalComponentInstance) output, input));
    }

    /**
     * Tests relationship instances
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRelationshipInstanceToKmfWithNull() {
        BridgeToKmf bridge = new BridgeToKmf();
        bridge.relationshipInstancesToKmf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRelationshipInstancesToKmfWithNull() {
        BridgeToKmf bridge = new BridgeToKmf();
        bridge.relationshipInstancesToKmf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRelationshipInstanceToKmfWithNotValidElement() {
        BridgeToKmf bridge = new BridgeToKmf();
        bridge.relationshipInstanceToKmf(new RelationshipInstance("ri", null, null, null));
    }

    @Test
    public void testRelationshipInstanceToKmfWithElement() {
        BridgeToKmf bridge = new BridgeToKmf();

        CloudMLSamplesBuilder cloudMLSamples = new CloudMLSamplesBuilder();
        Deployment cm = new Deployment();

        RelationshipInstance input = cloudMLSamples.getRelationshipInstanceB();

        //Adding the types
        cm.getRelationships().add(input.getType());
        Component ownerProvided = input.getProvidedEnd().getOwner().get().getType();
        cm.getComponents().add(
                ownerProvided);
        Component ownerRequired = input.getRequiredEnd().getOwner().get().getType();
        cm.getComponents().add(ownerRequired);

        //Adding the instances
        cm.getRelationshipInstances().add(input);
        cm.getComponentInstances().add(input.getProvidedEnd().getOwner().get());
        cm.getComponentInstances().add(input.getRequiredEnd().getOwner().get());

        bridge.toKMF(cm);

        net.cloudml.core.RelationshipInstance output = bridge.getKmfModel().getRelationshipInstances().get(0);

        assertTrue(new Matcher().matchRelationshipInstance(output, input));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteInstanceToKmfWithNull() {
        BridgeToKmf bridge = new BridgeToKmf();
        bridge.relationshipInstancesToKmf(null);
    }

    @Test
    public void testExecuteInstanceToKmfWithValidElement() {
        BridgeToKmf bridge = new BridgeToKmf();

        CloudMLSamplesBuilder cloudMLSamples = new CloudMLSamplesBuilder();
        Deployment cm = new Deployment();

        ExecuteInstance ei = cloudMLSamples.getExecuteInstanceA();
        cm.getExecuteInstances().add(ei);
        Component ownerProvided = ei.getProvidedEnd().getOwner().get().getType();
        cm.getComponents().add(ownerProvided);
        Component ownerRequired = ei.getRequiredEnd().getOwner().get().getType();
        cm.getComponents().add(ownerRequired);

        cm.getComponentInstances().add(ei.getProvidedEnd().getOwner().get());
        cm.getComponentInstances().add(ei.getRequiredEnd().getOwner().get());

        bridge.toKMF(cm);
        net.cloudml.core.ExecuteInstance output = bridge.getKmfModel().getExecutesInstances().get(0);
        assertTrue(new Matcher().matchExecuteInstance(output, ei));
    }

}
