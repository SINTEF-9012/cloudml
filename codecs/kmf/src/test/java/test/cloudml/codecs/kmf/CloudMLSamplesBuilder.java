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
import org.cloudml.core.*;


/**
 * Create sample CloudML objects used during testing
 */
class CloudMLSamplesBuilder {

    public Provider getProviderA() {
        return createProvider(PROVIDER_A_NAME);
    }

    public Provider getSampleProviderB() {
        return createProvider(PROVIDER_B_NAME);
    }

    private Provider createProvider(String name) {
        Provider provider = new Provider();
        provider.setName(name);
        return provider;
    }

    public VM getVMA(){
        return createVM(VM_A_NAME, ENDPOINT_A_NAME, getProviderA());
    }

    public VM getSampleVMB(){
        return createVM(VM_B_NAME, ENDPOINT_B_NAME, getSampleProviderB());
    }


    private VM createVM(String name,String endPoint, Provider p){
        VM vm=new VM();
        vm.setName(name);
        vm.setEndPoint(endPoint);
        vm.setProvider(p);
        return vm;
    }

    public VMInstance getVMInstanceA(){
        return createVMInstance(VM_INSTANCE_A, IP_ADDRESS_A, getVMA());
    }

    public VMInstance getSampleVMInstanceB(){
        return createVMInstance(VM_INSTANCE_B, IP_ADDRESS_B, getSampleVMB());
    }

    private VMInstance createVMInstance(String name, String ip, VM type){
        VMInstance vmi=new VMInstance();
        vmi.setType(type);
        vmi.setPublicAddress(ip);
        vmi.setName(name);
        return vmi;
    }

    public InternalComponent getInternalComponentA(){
        return createInternalComponent(INTERNAL_COMPONENT_A_NAME);
    }

    public InternalComponent getSampleInternalComponentB(){
        return createInternalComponent(INTERNAL_COMPONENT_B_NAME);
    }

    private InternalComponent createInternalComponent(String name){
        InternalComponent ic=new InternalComponent();
        ic.setName(name);
        return ic;
    }

    public InternalComponentInstance getInternalComponentInstanceA(){
        return createInternalComponentInstance(INTERNAL_COMPONENT_INSTANCE_A_NAME, getVMInstanceA(), getInternalComponentA());
    }

    public InternalComponentInstance getInternalComponentInstanceB(){
        return createInternalComponentInstance(INTERNAL_COMPONENT_INSTANCE_B_NAME, getSampleVMInstanceB(), getSampleInternalComponentB());
    }

    private InternalComponentInstance createInternalComponentInstance(String name, VMInstance dest, InternalComponent type){
        InternalComponentInstance ici=new InternalComponentInstance();
        ici.setName(name);
        ici.setDestination(dest);
        ici.setType(type);
        return ici;
    }

    public Relationship getRelationshipA() {
        return createRelationship(RELATIONSHIP_A_NAME);
    }

    public Relationship getSampleRelationshipB() {
        return createRelationship(RELATIONSHIP_B_NAME);
    }


    private Relationship createRelationship(String name) {
        Relationship relationship = new Relationship();
        relationship.setName(name);

        ProvidedPort providedPort = new ProvidedPort();
        providedPort.setName(name + PROVIDED_PORT_NAME);
        Component ownerProvided = new InternalComponent();
        ownerProvided.getProvidedPorts().add(providedPort);
        ownerProvided.setName(name + "cp");
        providedPort.setComponent(ownerProvided);
        relationship.setProvidedPort(providedPort);

        RequiredPort requiredPort = new RequiredPort();
        requiredPort.setName(name + REQUIRED_PORT_NAME);
        InternalComponent ownerRequire = new InternalComponent();
        ownerRequire.getRequiredPorts().add(requiredPort);
        ownerRequire.setName(name + "cr");
        requiredPort.setComponent(ownerRequire);
        relationship.setRequiredPort(requiredPort);

        return relationship;
    }

    public RelationshipInstance getRelationshipInstanceA() {
        return createRelationshipInstance(RELATIONSHIP_INSTANCE_A_NAME, getRelationshipA());
    }

    public RelationshipInstance getSampleRelationshipInstanceB() {
        return createRelationshipInstance(RELATIONSHIP_INSTANCE_B_NAME, getSampleRelationshipB());
    }

    private RelationshipInstance createRelationshipInstance(String name, Relationship relationship) {
        RelationshipInstance relationshipInstance = new RelationshipInstance(name, relationship);

        InternalComponentInstance ownerOfProvidedPort = getInternalComponentInstanceA();
        ProvidedPortInstance providedPortInstance = new ProvidedPortInstance(name + PROVIDED_PORT_INSTANCE_NAME, relationship.getProvidedPort(), ownerOfProvidedPort);
        ownerOfProvidedPort.getProvidedPortInstances().add(providedPortInstance);
        relationshipInstance.setProvidedPortInstance(providedPortInstance);

        InternalComponentInstance ownerOfRequiredPort = getInternalComponentInstanceB();
        RequiredPortInstance requiredPortInstance = new RequiredPortInstance(name + REQUIRED_PORT_INSTANCE_NAME, relationship.getRequiredPort(), ownerOfRequiredPort);
        ownerOfRequiredPort.getRequiredPortInstances().add(requiredPortInstance);
        relationshipInstance.setRequiredPortInstance(requiredPortInstance);

        return relationshipInstance;
    }


}
