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
package test.cloudml.codecs.kmf;

import static test.cloudml.codecs.kmf.Samples.*;
import net.cloudml.core.*;
import net.cloudml.factory.*;

/**
 * Provide samples of KMF/POJO objects used during testing
 */
class KMFSamplesBuilder {

   private final CoreFactory factory;


   public KMFSamplesBuilder() {
       factory = new MainFactory().getCoreFactory();
   }

    public Provider getProviderA() {
        return createProvider(PROVIDER_A_NAME);
    }
    
    public Provider getProviderB() {
        return createProvider(PROVIDER_B_NAME);
    }

    private Provider createProvider(String name) {
        Provider provider = factory.createProvider();
        provider.setName(name);
        provider.setCredentials(CREDENTIALS);
        return provider;
    }

    public VM getVMA(){
        return createVM(VM_A_NAME, ENDPOINT_A_NAME, getProviderA());
    }

    public VM getVMB(){
        return createVM(VM_B_NAME, ENDPOINT_B_NAME, getProviderB());
    }

    public net.cloudml.core.VM createVM(String name, String endPoint, Provider p){
        net.cloudml.core.VM kcomp=factory.createVM();
        //TODO: to be completed
        kcomp.setProvider(p);
        kcomp.setName(name);
        kcomp.setEndPoint(endPoint);
        return kcomp;
    }

    public VMInstance getVMInstanceA(){
        return createVMInstance(VM_INSTANCE_A, IP_ADDRESS_A, getVMA());
    }

    public VMInstance getVMInstanceB(){
        return createVMInstance(VM_INSTANCE_B, IP_ADDRESS_B, getVMB());
    }

    public net.cloudml.core.VMInstance createVMInstance(String name, String ip, VM type){
        net.cloudml.core.VMInstance kcomp=factory.createVMInstance();
        //TODO: to be completed
        kcomp.setPublicAddress(ip);
        kcomp.setName(name);
        kcomp.setType(type);
        return kcomp;
    }

    public InternalComponent getInternalComponentA(){
        return createInternalComponent(INTERNAL_COMPONENT_A_NAME, getRequiredExecutionPlatformA());
    }

    public InternalComponent getInternalComponentB(){
        return createInternalComponent(INTERNAL_COMPONENT_B_NAME, getRequiredExecutionPlatformB());
    }

    public RequiredExecutionPlatform getRequiredExecutionPlatformA() {
        return createRequiredExecutionPlatform(REQUIRED_EXECUTION_PLATFORM_A_NAME);
    }

    public RequiredExecutionPlatform getRequiredExecutionPlatformB() {
        return createRequiredExecutionPlatform(REQUIRED_EXECUTION_PLATFORM_B_NAME);
    }

    public RequiredExecutionPlatform createRequiredExecutionPlatform(String name) {
        RequiredExecutionPlatform krep = factory.createRequiredExecutionPlatform();
        krep.setName(name);
        return krep;
    }


    public net.cloudml.core.InternalComponent createInternalComponent(String name, RequiredExecutionPlatform krep){
        InternalComponent internalComponent = factory.createInternalComponent();
        //TODO: to be completed
        internalComponent.setName(name);
        internalComponent.setRequiredExecutionPlatform(krep);
        krep.setOwner(internalComponent);
        return internalComponent;
    }


    public RequiredExecutionPlatformInstance getRequiredExecutionPlatformInstanceA(InternalComponent icA) {
        return createRequiredExecutionPlatformInstance(REQUIRED_EXECUTION_PLATFORM_INSTANCE_NAME_A, icA.getRequiredExecutionPlatform());
    }

    public RequiredExecutionPlatformInstance getRequiredExecutionPlatformInstanceB(InternalComponent icB) {
        return createRequiredExecutionPlatformInstance(REQUIRED_EXECUTION_PLATFORM_INSTANCE_NAME_B, icB.getRequiredExecutionPlatform());
    }

    private RequiredExecutionPlatformInstance createRequiredExecutionPlatformInstance(String name, RequiredExecutionPlatform krep) {
        RequiredExecutionPlatformInstance kInstance = factory.createRequiredExecutionPlatformInstance();
        kInstance.setName(name);
        kInstance.setType(krep);
        return kInstance;
    }


    public InternalComponentInstance getInternalComponentInstanceA(){
        InternalComponent icA=getInternalComponentA();
        return createInternalComponentInstance(INTERNAL_COMPONENT_INSTANCE_A_NAME, getRequiredExecutionPlatformInstanceA(icA), icA);
    }

    public InternalComponentInstance getInternalComponentInstanceB(){
        InternalComponent icB=getInternalComponentB();
        return createInternalComponentInstance(INTERNAL_COMPONENT_INSTANCE_B_NAME, getRequiredExecutionPlatformInstanceB(icB), icB);
    }

    public InternalComponentInstance createInternalComponentInstance(String name, RequiredExecutionPlatformInstance krepi, InternalComponent type){
        InternalComponentInstance internalComponentInstance = factory.createInternalComponentInstance();
        internalComponentInstance.setRequiredExecutionPlatformInstances(krepi);
        krepi.setOwner(internalComponentInstance);
        internalComponentInstance.setName(name);
        internalComponentInstance.setType(type);
        return internalComponentInstance;
    }

    public Relationship getRelationshipA() {
        return createRelationship(RELATIONSHIP_A_NAME);
    }

    public Relationship getRelationshipB() {
        return createRelationship(RELATIONSHIP_B_NAME);
    }

    private Relationship createRelationship(String name){
        net.cloudml.core.Relationship krel=factory.createRelationship();
        krel.setName(name);

        net.cloudml.core.ProvidedPort pp=factory.createProvidedPort();
        pp.setName(name + PROVIDED_PORT_NAME);
        net.cloudml.core.Component c=getInternalComponentA();
        c.addProvidedPorts(pp);
        pp.setComponent(c);
        krel.setProvidedPort(pp);


        net.cloudml.core.RequiredPort rp=factory.createRequiredPort();
        rp.setName(name + REQUIRED_PORT_NAME);
        net.cloudml.core.InternalComponent c2 = getInternalComponentB();
        c2.addRequiredPorts(rp);
        rp.setComponent(c2);

        krel.setRequiredPort(rp);

        return krel;
    }

    public RelationshipInstance getRelationshipInstanceA() {
        return createRelationshipInstance(RELATIONSHIP_INSTANCE_A_NAME, getRelationshipA());
    }

    public RelationshipInstance getRelationShipInstanceB() {
        return createRelationshipInstance(RELATIONSHIP_INSTANCE_B_NAME, getRelationshipB());
    }

    private RelationshipInstance createRelationshipInstance(String name, Relationship relationship){
        RelationshipInstance relationshipInstance = factory.createRelationshipInstance();

        relationshipInstance.setName(name);

        relationshipInstance.setType(relationship);

        ProvidedPortInstance providedPort = factory.createProvidedPortInstance();
        providedPort.setName(name + PROVIDED_PORT_INSTANCE_NAME);
        providedPort.setType(relationship.getProvidedPort());
        ComponentInstance ownerProvider = getInternalComponentInstanceA();
        ownerProvider.addProvidedPortInstances(providedPort);
        providedPort.setComponentInstance(ownerProvider);

        relationshipInstance.setProvidedPortInstance(providedPort);

        RequiredPortInstance requiredPort = factory.createRequiredPortInstance();
        requiredPort.setName(name + REQUIRED_PORT_INSTANCE_NAME);
        requiredPort.setType(relationship.getRequiredPort());
        InternalComponentInstance ownerRequired = getInternalComponentInstanceB();
        ownerRequired.addRequiredPortInstances(requiredPort);
        requiredPort.setComponentInstance(ownerRequired);

        relationshipInstance.setRequiredPortInstance(requiredPort);

        return relationshipInstance;
    }
}
