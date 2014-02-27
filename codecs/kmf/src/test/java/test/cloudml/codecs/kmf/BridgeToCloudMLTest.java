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

/**
 * Created by nicolasf on 26.02.14.
 */

@RunWith(JUnit4.class)
public class BridgeToCloudMLTest extends TestCase {

    @Test
    public void testFromEmptyModel(){
        CloudMLModel model = new CloudMLModel();
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

    /**
     * Tests providers
     */

    @Test(expected=IllegalArgumentException.class)
    public void testProviderToPojoWithNull(){
        BridgeToCloudML bridge = new BridgeToCloudML();
        bridge.providersToPOJO(null);
    }

    @Test
    public void testProviderToPojoEmpty(){
        BridgeToCloudML bridge = new BridgeToCloudML();
        bridge.providersToPOJO(new ArrayList<net.cloudml.core.Provider>());
        assertTrue(bridge.getCloudMLModel().getProviders().isEmpty());
    }

    @Test
    public void testProviderToPojoWithElements(){
        BridgeToCloudML bridge = new BridgeToCloudML();
        List<net.cloudml.core.Provider> providers=new ArrayList<net.cloudml.core.Provider>();

        KMFSamplesBuilder kmfSamples = new KMFSamplesBuilder();

        providers.add(kmfSamples.getProviderA());
        bridge.providersToPOJO(providers);
        Provider actual = bridge.getCloudMLModel().getProviders().get(0);

        Matcher matcher = new Matcher();
        assertTrue(new Matcher().match(kmfSamples.getProviderA(), actual));
    }

    /**
     * Tests on external components
     */

    @Test(expected=IllegalArgumentException.class)
    public void testExternalComponentToPojoWithNull(){
        BridgeToCloudML bridge = new BridgeToCloudML();
        bridge.externalComponentToPOJO(null);
    }


    @Test
    public void testExternalComponentToPojoWithElements(){
        BridgeToCloudML bridge = new BridgeToCloudML();

        net.cloudml.core.VM input = new KMFSamplesBuilder().getVMA();
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.CloudMLModel cm=factory.createCloudMLModel();
        cm.addProviders(input.getProvider());
        cm.addComponents(input);
        bridge.toPOJO(cm);

        KMFSamplesBuilder kmfSamples = new KMFSamplesBuilder();

        bridge.toPOJO(cm);
        List<org.cloudml.core.ExternalComponent> output= bridge.getCloudMLModel().getExternalComponents();
        assertFalse(output.isEmpty());
        VM actual = (VM) output.get(0);

        assertTrue(new Matcher().matchVM(kmfSamples.getVMA(), actual));
    }


    /**
     * Tests on internal components
     */

    @Test(expected=IllegalArgumentException.class)
    public void testInternalComponentToPojoWithNull(){
        BridgeToCloudML bridge = new BridgeToCloudML();
        bridge.internalComponentToPOJO(null);
    }

    @Test
    public void testInternalComponentToPojoWithElements(){
        BridgeToCloudML bridge = new BridgeToCloudML();

        KMFSamplesBuilder kmfSamples = new KMFSamplesBuilder();

        bridge.internalComponentToPOJO(kmfSamples.getInternalComponentA());
        InternalComponent result = bridge.getCloudMLModel().getInternalComponents().get(0);

        assertTrue(new Matcher().matchIC(kmfSamples.getInternalComponentA(), result));
    }

    /**
     * Tests on relationships
     */
    @Test(expected=IllegalArgumentException.class)
    public void testRelationshipToPojoWithNull(){
        BridgeToCloudML bridge = new BridgeToCloudML();
        bridge.relationshipToPOJO(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRelationshipToPojoWithInvalidElements(){
        BridgeToCloudML bridge = new BridgeToCloudML();
        net.cloudml.core.Relationship kic=createRelationship("SensAppBinding");
        bridge.relationshipToPOJO(kic);
    }

    @Test
    public void testRelationshipToPojoWithElements(){
        BridgeToCloudML bridge = new BridgeToCloudML();

        net.cloudml.core.Relationship input = new KMFSamplesBuilder().getRelationshipA();

        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.CloudMLModel cm=factory.createCloudMLModel();
        cm.addRelationships(input);
        cm.addComponents(input.getProvidedPort().getComponent());
        cm.addComponents(input.getRequiredPort().getComponent());
        bridge.toPOJO(cm);
        
        Relationship output = bridge.getCloudMLModel().getRelationships().get(input.getName());

        assertTrue(new Matcher().matchRelationship(input, output));
    }

    /**
     * Tests on VM instances
     */
    @Test(expected=IllegalArgumentException.class)
    public void testExternalComponentInstanceToPojoWithNull(){
        BridgeToCloudML bridge = new BridgeToCloudML();
        bridge.externalComponentInstanceToPOJO(null);
    }

    @Test
    public void testExternalComponentInstanceToPojoWithElement(){
        BridgeToCloudML bridge = new BridgeToCloudML();

        net.cloudml.core.VMInstance input = new KMFSamplesBuilder().getVMInstanceA();
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.CloudMLModel cm=factory.createCloudMLModel();
        cm.addProviders(((net.cloudml.core.VM) input.getType()).getProvider());
        cm.addComponents(input.getType());
        cm.addComponentInstances(input);
        bridge.toPOJO(cm);

        assertFalse(bridge.getCloudMLModel().getExternalComponentInstances().isEmpty());
        VMInstance output = (VMInstance) (bridge.getCloudMLModel().getExternalComponentInstances().get(0));
        assertNotNull(output);
        assertNotNull(output.getName());
        assertNotNull(output.getType());

        assertTrue(new Matcher().matchVMInstance(input, output));
    }

    /**
     * Tests in internal component instance
     */
    @Test(expected=IllegalArgumentException.class)
    public void testInternalComponentInstanceToPojoWithNull(){
        BridgeToCloudML bridge = new BridgeToCloudML();
        bridge.internalComponentInstanceToPOJO(null);
    }

    @Test
    public void testInternalComponentInstanceToPojoWithElement(){
        BridgeToCloudML bridge = new BridgeToCloudML();

        net.cloudml.core.InternalComponentInstance input = new KMFSamplesBuilder().getInternalComponentInstanceA();
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.CloudMLModel cm=factory.createCloudMLModel();
        cm.addComponents(input.getType());
        cm.addComponents(input.getDestination().getType());
        cm.addComponentInstances(input.getDestination());
        cm.addProviders(((net.cloudml.core.VM)input.getDestination().getType()).getProvider());
        cm.addComponentInstances(input);
        bridge.toPOJO(cm);

        List<InternalComponentInstance> ici= bridge.getCloudMLModel().getInternalComponentInstances();
        assertFalse(ici.isEmpty());

        bridge.internalComponentInstanceToPOJO(input);
        InternalComponentInstance output = ici.get(0);

        assertNotNull(output);
        assertNotNull(output.getName());
        assertNotNull(output.getType());
        assertNotNull(output.getDestination());

        assertTrue(new Matcher().matchICI(input, output));
    }

    /**
     * Tests relationship instances
     */
    @Test(expected=IllegalArgumentException.class)
    public void testRelationshipInstanceToPojoWithNull(){
        BridgeToCloudML bridge = new BridgeToCloudML();
        bridge.relationshipInstanceToPOJO(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRelationshipInstancesToPojoWithNull(){
        BridgeToCloudML bridge = new BridgeToCloudML();
        bridge.relationshipInstancesToPOJO(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRelationshipInstanceToPojoWithNotValidElement(){
        BridgeToCloudML bridge = new BridgeToCloudML();
        net.cloudml.core.RelationshipInstance kvi = createRelationshipInstanceNotValid("DBS");
        bridge.relationshipInstanceToPOJO(kvi);
    }

    @Test
    public void testRelationshipInstanceToPojoWithElement(){
        BridgeToCloudML bridge = new BridgeToCloudML();

        net.cloudml.core.RelationshipInstance input = new KMFSamplesBuilder().getRelationshipInstanceA();

        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.CloudMLModel cm=factory.createCloudMLModel();

        cm.addRelationships(input.getType());
        cm.addComponents(input.getType().getProvidedPort().getComponent());
        cm.addComponents(input.getProvidedPortInstance().getComponentInstance().getDestination().getType());
        cm.addComponents(input.getType().getRequiredPort().getComponent());
        cm.addComponents(input.getRequiredPortInstance().getComponentInstance().getDestination().getType());

        cm.addRelationshipInstances(input);
        cm.addComponentInstances(input.getProvidedPortInstance().getComponentInstance());
        cm.addComponentInstances(input.getProvidedPortInstance().getComponentInstance().getDestination());
        cm.addComponentInstances(input.getRequiredPortInstance().getComponentInstance());
        cm.addComponentInstances(input.getRequiredPortInstance().getComponentInstance().getDestination());

        bridge.toPOJO(cm);

        bridge.relationshipInstanceToPOJO(input);
        RelationshipInstance output = bridge.getCloudMLModel().getRelationshipInstances().get(0);

        assertTrue(new Matcher().matchRelationshipInstance(input, output));
    }


    public net.cloudml.core.RelationshipInstance createRelationshipInstanceNotValid(String name){
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.RelationshipInstance kcomp=factory.createRelationshipInstance();
        kcomp.setName(name);
        kcomp.setType(factory.createRelationship());
        return kcomp;
    }

    public net.cloudml.core.RelationshipInstance createRelationshipInstance(String name){
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.RelationshipInstance kri=factory.createRelationshipInstance();
        kri.setName(name);
        kri.setType(factory.createRelationship());
        net.cloudml.core.ProvidedPortInstance pp=factory.createProvidedPortInstance();
        net.cloudml.core.RequiredPortInstance rp=factory.createRequiredPortInstance();
        pp.setName("plop");
        rp.setName("plip");
        pp.setComponentInstance(factory.createComponentInstance());
        rp.setComponentInstance(factory.createComponentInstance());
        return kri;
    }

    public net.cloudml.core.InternalComponentInstance createInternalComponentInstance(String name){
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.InternalComponentInstance kcomp=factory.createInternalComponentInstance();
        kcomp.setDestination(factory.createVMInstance());
        kcomp.setName(name);
        return kcomp;
    }


    public net.cloudml.core.VMInstance createVMInstance(String name, String ip){
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.VMInstance kcomp=factory.createVMInstance();
        kcomp.setPublicAddress(ip);
        kcomp.setName(name);
        return kcomp;
    }

    public net.cloudml.core.Relationship createRelationshipWithPorts(String name){
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.Relationship kcomp=factory.createRelationship();
        kcomp.setName(name);
        net.cloudml.core.ProvidedPort pp=factory.createProvidedPort();
        pp.setName(name+"pp");
        net.cloudml.core.Component c=factory.createComponent();
        c.setName(name+"c");
        pp.setComponent(c);
        net.cloudml.core.RequiredPort rp=factory.createRequiredPort();
        rp.setName(name+"rp");
        net.cloudml.core.Component c2=factory.createComponent();
        c2.setName(name+"c");
        rp.setComponent(c2);
        kcomp.setProvidedPort(pp);
        kcomp.setRequiredPort(rp);
        return kcomp;
    }

    public net.cloudml.core.Relationship createRelationship(String name){
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.Relationship kcomp=factory.createRelationship();
        kcomp.setName(name);
        kcomp.setProvidedPort(factory.createProvidedPort());
        return kcomp;
    }

    public net.cloudml.core.InternalComponent createInternalComponent(String name){
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.InternalComponent kcomp=factory.createInternalComponent();
        //TODO: to be completed
        kcomp.setName(name);
        return kcomp;
    }

    public net.cloudml.core.VM createVM(String name, String endPoint){
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.VM kcomp=factory.createVM();
        //TODO: to be completed
        kcomp.setName(name);
        kcomp.setEndPoint(endPoint);
        return kcomp;
    }

    public net.cloudml.core.Provider createProvider(String name, String credentials){
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.Provider kProvider = factory.createProvider();
        kProvider.setName(name);
        kProvider.setCredentials(credentials);
        return kProvider;
    }
}
