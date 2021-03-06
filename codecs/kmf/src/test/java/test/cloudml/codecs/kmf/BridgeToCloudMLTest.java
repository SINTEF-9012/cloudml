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
import org.cloudml.codecs.BridgeToCloudML;
import org.cloudml.codecs.KMFBridge;
import org.cloudml.core.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Created by nicolasf on 26.02.14.
 */
@RunWith(JUnit4.class)
public class BridgeToCloudMLTest extends TestCase {

    @Test
    public void testFromEmptyModel() {
        Deployment model = new Deployment();
        KMFBridge bridge = new KMFBridge();
        net.cloudml.core.CloudMLModel kmodel = bridge.toKMF(model);
        assertTrue(kmodel.getClouds().isEmpty());
        assertTrue(kmodel.getComponentInstances().isEmpty());
        assertTrue(kmodel.getComponents().isEmpty());
        assertTrue(kmodel.getClouds().isEmpty());
        assertTrue(kmodel.getRelationshipInstances().isEmpty());
        assertTrue(kmodel.getRelationships().isEmpty());
        assertTrue(kmodel.getProviders().isEmpty());
    }
    
    
    @Test
    public void offersShouldBeConvertedToCloudML() {
        final KMFSamplesBuilder kmfSamples = new KMFSamplesBuilder();
        final String vmName = "Foo VM";
        net.cloudml.core.CloudMLModel kDeployment = kmfSamples.aVmOffering(vmName, "OS", "Linux");
        
        final KMFBridge bridge = new KMFBridge();
        final Deployment deployment = (Deployment) bridge.toPOJO(kDeployment);
        final Property offer = deployment
                .getComponents()
                .onlyVMs().firstNamed(vmName)
                .getProvidedExecutionPlatforms().toList().get(0).getOffers().get("OS");
        
        assertThat("offer", offer, is(not(nullValue())));
        assertThat("offer's name", offer.getName(), is(equalTo("OS")));
        assertThat("offer's value", offer.getValue(), is(equalTo("Linux")));
    }
    
    @Test
    public void demandsShouldBeConvertedToCloudML() {
        final KMFSamplesBuilder kmfSamples = new KMFSamplesBuilder(); 
        final String appName = "Foo App";
        net.cloudml.core.CloudMLModel kDeployment = kmfSamples.anInternalComponentDemanding(appName, "OS", "Linux");
        
        final KMFBridge bridge = new KMFBridge();
        final Deployment deployment = (Deployment) bridge.toPOJO(kDeployment);
        final Property demand = deployment
                .getComponents()
                .onlyInternals().firstNamed(appName)
                .getRequiredExecutionPlatform().getDemands().get("OS");
        
        assertThat("demand", demand, is(not(nullValue())));
        assertThat("demand's name", demand.getName(), is(equalTo("OS")));
        assertThat("demand's value", demand.getValue(), is(equalTo("Linux")));
    }

    /**
     * Tests providers
     */
    @Test(expected = IllegalArgumentException.class)
    public void testProviderToPojoWithNull() {
        BridgeToCloudML bridge = new BridgeToCloudML();
        bridge.providersToPOJO(null);
    }

    @Test
    public void testProviderToPojoEmpty() {
        BridgeToCloudML bridge = new BridgeToCloudML();
        bridge.providersToPOJO(new ArrayList<net.cloudml.core.Provider>());
        assertTrue(bridge.getCloudMLModel().getProviders().isEmpty());
    }

    @Test
    public void testProviderToPojoWithElements() {
        BridgeToCloudML bridge = new BridgeToCloudML();
        List<net.cloudml.core.Provider> providers = new ArrayList<net.cloudml.core.Provider>();

        KMFSamplesBuilder kmfSamples = new KMFSamplesBuilder();

        providers.add(kmfSamples.getProviderA());
        bridge.providersToPOJO(providers);
        Provider actual = bridge.getCloudMLModel().getProviders().toList().get(0);

        Matcher matcher = new Matcher();
        assertTrue(new Matcher().match(kmfSamples.getProviderA(), actual));
    }

    /**
     * Tests on external components
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExternalComponentToPojoWithNull() {
        BridgeToCloudML bridge = new BridgeToCloudML();
        bridge.externalComponentToPOJO(null);
    }

    @Test
    public void testExternalComponentToPojoWithElements() {
        BridgeToCloudML bridge = new BridgeToCloudML();

        net.cloudml.core.VM input = new KMFSamplesBuilder().getVMA();
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.CloudMLModel cm = factory.createCloudMLModel();
        cm.addProviders(input.getProvider());
        cm.addVms(input);
     
        KMFSamplesBuilder kmfSamples = new KMFSamplesBuilder();

        bridge.toPOJO(cm);
        List<org.cloudml.core.ExternalComponent> output = bridge.getCloudMLModel().getComponents().onlyExternals().toList();
        assertFalse(output.isEmpty());
        VM actual = (VM) output.get(0);

        assertTrue(new Matcher().matchVM(kmfSamples.getVMA(), actual));
    }

    /**
     * Tests on internal components
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInternalComponentToPojoWithNull() {
        BridgeToCloudML bridge = new BridgeToCloudML();
        bridge.internalComponentToPOJO(null);
    }

    @Test
    public void testInternalComponentToPojoWithElements() {
        BridgeToCloudML bridge = new BridgeToCloudML();

        KMFSamplesBuilder kmfSamples = new KMFSamplesBuilder();

        bridge.internalComponentToPOJO(kmfSamples.getInternalComponentA());
        InternalComponent result = bridge.getCloudMLModel().getComponents().onlyInternals().toList().get(0);

        assertTrue(new Matcher().matchIC(kmfSamples.getInternalComponentA(), result));
    }

    /**
     * Tests on relationships
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRelationshipToPojoWithNull() {
        BridgeToCloudML bridge = new BridgeToCloudML();
        bridge.relationshipToPOJO(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRelationshipToPojoWithInvalidElements() {
        BridgeToCloudML bridge = new BridgeToCloudML();
        net.cloudml.core.Relationship kic = createRelationship("SensAppBinding");
        bridge.relationshipToPOJO(kic);
    }

    @Test
    public void testRelationshipToPojoWithElements() {
        BridgeToCloudML bridge = new BridgeToCloudML();

        net.cloudml.core.Relationship input = new KMFSamplesBuilder().getRelationshipA();

        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.CloudMLModel cm = factory.createCloudMLModel();
        cm.addRelationships(input);
        cm.addInternalComponents((net.cloudml.core.InternalComponent)input.getProvidedPort().getComponent());
        cm.addInternalComponents((net.cloudml.core.InternalComponent)input.getRequiredPort().getComponent());
        bridge.toPOJO(cm);

        Relationship output = bridge.getCloudMLModel().getRelationships().firstNamed(input.getName());

        assertTrue(new Matcher().matchRelationship(input, output));
    }

    /**
     * Tests on VM instances
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExternalComponentInstanceToPojoWithNull() {
        BridgeToCloudML bridge = new BridgeToCloudML();
        bridge.externalComponentInstanceToPOJO(null);
    }

    @Test
    public void testExternalComponentInstanceToPojoWithElement() {
        BridgeToCloudML bridge = new BridgeToCloudML();

        net.cloudml.core.VMInstance input = new KMFSamplesBuilder().getVMInstanceA();
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.CloudMLModel cm = factory.createCloudMLModel();
        cm.addProviders(((net.cloudml.core.VM) input.getType()).getProvider());
        cm.addVms((net.cloudml.core.VM)input.getType());
        cm.addVmInstances(input);
        bridge.toPOJO(cm);

        assertFalse(bridge.getCloudMLModel().getComponentInstances().onlyExternals().isEmpty());
        VMInstance output = (VMInstance) (bridge.getCloudMLModel().getComponentInstances().onlyExternals().toList().get(0));
        assertNotNull(output);
        assertNotNull(output.getName());
        assertNotNull(output.getType());

        assertTrue(new Matcher().matchVMInstance(input, output));
    }

    /**
     * Tests in internal component instance
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInternalComponentInstanceToPojoWithNull() {
        BridgeToCloudML bridge = new BridgeToCloudML();
        bridge.internalComponentInstanceToPOJO(null);
    }

    @Test
    public void testInternalComponentInstanceToPojoWithElement() {
        BridgeToCloudML bridge = new BridgeToCloudML();

        net.cloudml.core.InternalComponentInstance input = new KMFSamplesBuilder().getInternalComponentInstanceA();
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.CloudMLModel cm = factory.createCloudMLModel();
        cm.addInternalComponents((net.cloudml.core.InternalComponent)input.getType());
        cm.addInternalComponentInstances(input);
        bridge.toPOJO(cm);

        List<InternalComponentInstance> ici = bridge.getCloudMLModel().getComponentInstances().onlyInternals().toList();
        assertFalse(ici.isEmpty());

        InternalComponentInstance output = ici.get(0);

        assertNotNull(output);
        assertNotNull(output.getName());
        assertNotNull(output.getType());
        assertNotNull(output.getRequiredExecutionPlatform());

        assertTrue(new Matcher().matchICI(input, output));
    }

    /**
     * Tests relationship instances
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRelationshipInstanceToPojoWithNull() {
        BridgeToCloudML bridge = new BridgeToCloudML();
        bridge.relationshipInstanceToPOJO(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRelationshipInstancesToPojoWithNull() {
        BridgeToCloudML bridge = new BridgeToCloudML();
        bridge.relationshipInstancesToPOJO(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRelationshipInstanceToPojoWithNotValidElement() {
        BridgeToCloudML bridge = new BridgeToCloudML();
        net.cloudml.core.RelationshipInstance kvi = createRelationshipInstanceNotValid("DBS");
        bridge.relationshipInstanceToPOJO(kvi);
    }

    @Test
    public void testRelationshipInstanceToPojoWithElement() {
        BridgeToCloudML bridge = new BridgeToCloudML();

        net.cloudml.core.RelationshipInstance input = new KMFSamplesBuilder().getRelationshipInstanceA();

        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.CloudMLModel cm = factory.createCloudMLModel();

        cm.addRelationships(input.getType());
        cm.addInternalComponents((net.cloudml.core.InternalComponent)input.getType().getProvidedPort().getComponent());
        //cm.addComponents(input.getProvidedPortInstance().getComponentInstance().getDestination().getType());
        cm.addInternalComponents((net.cloudml.core.InternalComponent)input.getType().getRequiredPort().getComponent());
        //cm.addComponents(input.getRequiredPortInstance().getComponentInstance().getDestination().getType());

        cm.addRelationshipInstances(input);
        cm.addInternalComponentInstances((net.cloudml.core.InternalComponentInstance)input.getProvidedPortInstance().getComponentInstance());
        //cm.addComponentInstances(input.getProvidedPortInstance().getComponentInstance().getDestination());
        cm.addInternalComponentInstances((net.cloudml.core.InternalComponentInstance)input.getRequiredPortInstance().getComponentInstance());
        //cm.addComponentInstances(input.getRequiredPortInstance().getComponentInstance().getDestination());

        bridge.toPOJO(cm);

        RelationshipInstance output = bridge.getCloudMLModel().getRelationshipInstances().toList().get(0);

        assertTrue(new Matcher().matchRelationshipInstance(input, output));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteInstanceToKmfWithNull() {
        BridgeToCloudML bridge = new BridgeToCloudML();
        bridge.relationshipInstancesToPOJO(null);
    }

    @Test
    public void testExecuteInstanceToKmfWithValidElement() {
        BridgeToCloudML bridge = new BridgeToCloudML();
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.CloudMLModel cm = factory.createCloudMLModel();


        bridge.toPOJO(cm);
    }

    public net.cloudml.core.RelationshipInstance createRelationshipInstanceNotValid(String name) {
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.RelationshipInstance kcomp = factory.createRelationshipInstance();
        kcomp.setName(name);
        kcomp.setType(factory.createRelationship());
        return kcomp;
    }

    public net.cloudml.core.RelationshipInstance createRelationshipInstance(String name) {
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.RelationshipInstance kri = factory.createRelationshipInstance();
        kri.setName(name);
        kri.setType(factory.createRelationship());
        net.cloudml.core.ProvidedPortInstance pp = factory.createProvidedPortInstance();
        net.cloudml.core.RequiredPortInstance rp = factory.createRequiredPortInstance();
        pp.setName("plop");
        rp.setName("plip");
        pp.setComponentInstance(factory.createComponentInstance());
        rp.setComponentInstance(factory.createComponentInstance());
        return kri;
    }

    public net.cloudml.core.InternalComponentInstance createInternalComponentInstance(String name) {
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.InternalComponentInstance kcomp = factory.createInternalComponentInstance();
        //kcomp.setDestination(factory.createVMInstance());
        kcomp.setRequiredExecutionPlatformInstance(factory.createRequiredExecutionPlatformInstance());
        kcomp.setName(name);
        return kcomp;
    }

    public net.cloudml.core.VMInstance createVMInstance(String name, String ip) {
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.VMInstance kcomp = factory.createVMInstance();
        kcomp.setPublicAddress(ip);
        kcomp.setName(name);
        return kcomp;
    }

    public net.cloudml.core.Relationship createRelationshipWithPorts(String name) {
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.Relationship kcomp = factory.createRelationship();
        kcomp.setName(name);
        net.cloudml.core.ProvidedPort pp = factory.createProvidedPort();
        pp.setName(name + "pp");
        net.cloudml.core.Component c = factory.createComponent();
        c.setName(name + "c");
        pp.setComponent(c);
        net.cloudml.core.RequiredPort rp = factory.createRequiredPort();
        rp.setName(name + "rp");
        net.cloudml.core.Component c2 = factory.createComponent();
        c2.setName(name + "c");
        rp.setComponent(c2);
        kcomp.setProvidedPort(pp);
        kcomp.setRequiredPort(rp);
        return kcomp;
    }

    public net.cloudml.core.Relationship createRelationship(String name) {
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.Relationship kcomp = factory.createRelationship();
        kcomp.setName(name);
        kcomp.setProvidedPort(factory.createProvidedPort());
        return kcomp;
    }

    public net.cloudml.core.InternalComponent createInternalComponent(String name) {
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.InternalComponent kcomp = factory.createInternalComponent();
        //TODO: to be completed
        kcomp.setName(name);
        return kcomp;
    }

    public net.cloudml.core.VM createVM(String name, String endPoint) {
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.VM kcomp = factory.createVM();
        //TODO: to be completed
        kcomp.setName(name);
        kcomp.setEndPoint(endPoint);
        return kcomp;
    }

    public net.cloudml.core.Provider createProvider(String name, String credentials) {
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.Provider kProvider = factory.createProvider();
        kProvider.setName(name);
        kProvider.setCredentials(credentials);
        return kProvider;
    }
}
