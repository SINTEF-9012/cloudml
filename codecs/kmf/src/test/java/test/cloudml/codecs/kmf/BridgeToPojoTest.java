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
import net.cloudml.core.InternalComponent;
import net.cloudml.core.Provider;
import org.cloudml.codecs.BridgeToPojo;
import org.cloudml.codecs.KMFBridge;
import org.cloudml.core.CloudMLModel;
import org.cloudml.core.VMInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicolasf on 26.02.14.
 */

@RunWith(JUnit4.class)
public class BridgeToPojoTest extends TestCase {

    @Test
    public void testFromEmptyModel(){
        CloudMLModel model=new CloudMLModel();
        KMFBridge bridge=new KMFBridge();
        net.cloudml.core.CloudMLModel kmodel=bridge.toKMF(model);
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
        CloudMLModel model=new CloudMLModel();
        BridgeToPojo bridge=new BridgeToPojo();
        bridge.providersToPOJO(null);
    }

    @Test
    public void testProviderToPojoEmpty(){
        CloudMLModel model=new CloudMLModel();
        BridgeToPojo bridge=new BridgeToPojo();
        bridge.providersToPOJO(new ArrayList<net.cloudml.core.Provider>());
        assertTrue(bridge.getCloudMLModel().getProviders().isEmpty());
    }

    @Test
    public void testProviderToPojoWithElements(){
        CloudMLModel model=new CloudMLModel();
        BridgeToPojo bridge=new BridgeToPojo();
        List<net.cloudml.core.Provider> providers=new ArrayList<net.cloudml.core.Provider>();

        providers.add(createProvider("aws","./credentials"));
        providers.add(createProvider("flexiant","./credentials"));
        bridge.providersToPOJO(providers);
        assertEquals(2,bridge.getCloudMLModel().getProviders().size());
    }

    /**
     * Tests on external components
     */

    @Test(expected=IllegalArgumentException.class)
    public void testExternalComponentToPojoWithNull(){
        CloudMLModel model=new CloudMLModel();
        BridgeToPojo bridge=new BridgeToPojo();
        bridge.externalComponentToPOJO(null);
    }


    @Test
    public void testExternalComponentToPojoWithElements(){
        CloudMLModel model=new CloudMLModel();
        BridgeToPojo bridge=new BridgeToPojo();
        net.cloudml.core.VM kExternalComponent= createVM("vm1","chauvel");
        bridge.externalComponentToPOJO(kExternalComponent);
        //assertTrue(compare(kExternalComponent,model.getExternalComponents().get(0)));
    }


    /**
     * Tests on internal components
     */

    @Test(expected=IllegalArgumentException.class)
    public void testInternalComponentToPojoWithNull(){
        CloudMLModel model=new CloudMLModel();
        BridgeToPojo bridge=new BridgeToPojo();
        bridge.internalComponentToPOJO(null);
    }

    @Test
    public void testInternalComponentToPojoWithElements(){
        CloudMLModel model=new CloudMLModel();
        BridgeToPojo bridge=new BridgeToPojo();
        net.cloudml.core.InternalComponent kic=createInternalComponent("SensApp");
        bridge.internalComponentToPOJO(kic);
        //assertTrue(compare(kic,model.getInternalComponents().get(0)));
    }

    /**
     * Tests on relationships
     */
    @Test(expected=IllegalArgumentException.class)
    public void testRelationshipToPojoWithNull(){
        CloudMLModel model=new CloudMLModel();
        BridgeToPojo bridge=new BridgeToPojo();
        bridge.relationshipToPOJO(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRelationshipToPojoWithNotValidElements(){
        CloudMLModel model=new CloudMLModel();
        BridgeToPojo bridge=new BridgeToPojo();
        net.cloudml.core.Relationship kic=createRelationship("SensAppBinding");
        bridge.relationshipToPOJO(kic);
    }

    @Test
    public void testRelationshipToPojoWithElements(){
        CloudMLModel model=new CloudMLModel();
        BridgeToPojo bridge=new BridgeToPojo();
        net.cloudml.core.Relationship kic=createRelationshipWithPorts("SensAppBinding");
        bridge.relationshipToPOJO(kic);
        //assertTrue();
    }

    /**
     * Tests on VM instances
     */
    @Test(expected=IllegalArgumentException.class)
    public void testExternalComponentInstanceToPojoWithNull(){
        CloudMLModel model=new CloudMLModel();
        BridgeToPojo bridge=new BridgeToPojo();
        bridge.externalComponentInstanceToPOJO(null);
    }

    @Test
    public void testExternalComponentInstanceToPojoWithElement(){
        CloudMLModel model=new CloudMLModel();
        BridgeToPojo bridge=new BridgeToPojo();
        net.cloudml.core.VMInstance kvi=createVMInstance("SensAppInstance","i");
        bridge.externalComponentInstanceToPOJO(kvi);
        //assertTrue(compare(kvi,model.getExternalComponentInstances().get(0)));
    }

    /**
     * Tests in internal component instance
     */
    @Test(expected=IllegalArgumentException.class)
    public void testInternalComponentInstanceToPojoWithNull(){
        CloudMLModel model=new CloudMLModel();
        BridgeToPojo bridge=new BridgeToPojo();
        bridge.internalComponentInstanceToPOJO(null);
    }

    @Test
    public void testInternalComponentInstanceToPojoWithElement(){
        CloudMLModel model=new CloudMLModel();
        BridgeToPojo bridge=new BridgeToPojo();
        net.cloudml.core.InternalComponentInstance kvi=createInternalComponentInstance("DBS");
        bridge.internalComponentInstanceToPOJO(kvi);
        //assertTrue(compare(kvi,model.getInternalComponentInstances().get(0)));
    }

    /**
     * Tests relationship instances
     */
    @Test(expected=IllegalArgumentException.class)
    public void testRelationshipInstanceToPojoWithNull(){
        CloudMLModel model=new CloudMLModel();
        BridgeToPojo bridge=new BridgeToPojo();
        bridge.relationshipInstanceToPOJO(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRelationshipInstancesToPojoWithNull(){
        CloudMLModel model=new CloudMLModel();
        BridgeToPojo bridge=new BridgeToPojo();
        bridge.relationshipInstancesToPOJO(null);
    }

    @Test
    public void testRelationshipInstanceToPojoWithElement(){
        CloudMLModel model=new CloudMLModel();
        BridgeToPojo bridge=new BridgeToPojo();
        net.cloudml.core.RelationshipInstance kvi=createRelationshipInstance("DBS");
        bridge.relationshipInstanceToPOJO(kvi);
        //assertTrue(compare(kvi,model.getRelationshipInstances().get(0)));
    }




    public net.cloudml.core.RelationshipInstance createRelationshipInstance(String name){
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.RelationshipInstance kcomp=factory.createRelationshipInstance();
        kcomp.setName(name);
        kcomp.setType(factory.createRelationship());
        return kcomp;
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
