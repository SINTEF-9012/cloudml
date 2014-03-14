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

import java.util.ArrayList;
import java.util.List;


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
        InternalComponent ic=createInternalComponent(INTERNAL_COMPONENT_A_NAME);
        RequiredExecutionPlatform rep=getRequiredExecutionPlatformA();
        rep.setOwner(ic);
        ic.setRequiredExecutionPlatform(rep);
        ProvidedExecutionPlatform pep=getProvidedExecutionPlatformA();
        pep.setOwner(ic);
        List<ProvidedExecutionPlatform> peps=new ArrayList<ProvidedExecutionPlatform>();
        peps.add(pep);
        ic.setProvidedExecutionPlatforms(peps);
        return ic;
    }

    public InternalComponent getInternalComponentB(){
        InternalComponent ic=createInternalComponent(INTERNAL_COMPONENT_B_NAME);
        RequiredExecutionPlatform rep=getRequiredExecutionPlatformB();
        rep.setOwner(ic);
        ic.setRequiredExecutionPlatform(rep);
        ProvidedExecutionPlatform pep=getProvidedExecutionPlatformB();
        pep.setOwner(ic);
        List<ProvidedExecutionPlatform> peps=new ArrayList<ProvidedExecutionPlatform>();
        peps.add(pep);
        ic.setProvidedExecutionPlatforms(peps);
        return ic;
    }

    private InternalComponent createInternalComponent(String name){
        InternalComponent ic=new InternalComponent();
        ic.setName(name);
        return ic;
    }

    public RequiredExecutionPlatform getRequiredExecutionPlatformA(){
        return createRequiredExecutionPlatform(REQUIRED_EXECUTION_PLATFORM_A_NAME);
    }


    public RequiredExecutionPlatform getRequiredExecutionPlatformB(){
        return createRequiredExecutionPlatform(REQUIRED_EXECUTION_PLATFORM_B_NAME);
    }

    private RequiredExecutionPlatform createRequiredExecutionPlatform(String name){
        RequiredExecutionPlatform rep=new RequiredExecutionPlatform(name);
        return rep;
    }

    public ProvidedExecutionPlatform getProvidedExecutionPlatformA(){
        return createProvidedExecutionPlatform(PROVIDED_EXECUTION_PLATFORM_A_NAME);
    }


    public ProvidedExecutionPlatform getProvidedExecutionPlatformB(){
        return createProvidedExecutionPlatform(PROVIDED_EXECUTION_PLATFORM_B_NAME);
    }

    private ProvidedExecutionPlatform createProvidedExecutionPlatform(String name){
        ProvidedExecutionPlatform pep=new ProvidedExecutionPlatform(name);
        return pep;
    }

    public InternalComponentInstance getInternalComponentInstanceA(){
        InternalComponent icA=getInternalComponentA();
        return createInternalComponentInstance(INTERNAL_COMPONENT_INSTANCE_A_NAME, getRequiredExecutionPlatformInstanceA(icA), getProvidedExecutionPlatformInstanceA(icA), icA);
    }

    public InternalComponentInstance getInternalComponentInstanceB(){
        InternalComponent icB= getInternalComponentB();
        return createInternalComponentInstance(INTERNAL_COMPONENT_INSTANCE_B_NAME, getRequiredExecutionPlatformInstanceB(icB),getProvidedExecutionPlatformInstanceB(icB), icB);
    }

    public InternalComponentInstance getInternalComponentInstanceAWithType( InternalComponent icA){
        return createInternalComponentInstance(INTERNAL_COMPONENT_INSTANCE_A_NAME, getRequiredExecutionPlatformInstanceA(icA), getProvidedExecutionPlatformInstanceA(icA), icA);
    }

    public InternalComponentInstance getInternalComponentInstanceBWithType( InternalComponent icB){
        return createInternalComponentInstance(INTERNAL_COMPONENT_INSTANCE_B_NAME, getRequiredExecutionPlatformInstanceB(icB), getProvidedExecutionPlatformInstanceB(icB), icB);
    }

    private InternalComponentInstance createInternalComponentInstance(String name, RequiredExecutionPlatformInstance requiredExecutionPlatformInstance,
                                                                      ProvidedExecutionPlatformInstance providedExecutionPlatformInstance, InternalComponent type){
        InternalComponentInstance ici=new InternalComponentInstance();
        ici.setName(name);
        ici.setRequiredExecutionPlatformInstance(requiredExecutionPlatformInstance);
        requiredExecutionPlatformInstance.setOwner(ici);
        providedExecutionPlatformInstance.setOwner(ici);
        List<ProvidedExecutionPlatformInstance> pepis=new ArrayList<ProvidedExecutionPlatformInstance>();
        pepis.add(providedExecutionPlatformInstance);
        ici.setProvidedExecutionPlatformInstances(pepis);
        ici.setType(type);
        return ici;
    }

    public RequiredExecutionPlatformInstance getRequiredExecutionPlatformInstanceA(InternalComponent icA) {
        return createRequiredExecutionPlatformInstance(REQUIRED_EXECUTION_PLATFORM_INSTANCE_NAME_A, icA.getRequiredExecutionPlatform());
    }

    public RequiredExecutionPlatformInstance getRequiredExecutionPlatformInstanceB(InternalComponent icB) {
        return createRequiredExecutionPlatformInstance(REQUIRED_EXECUTION_PLATFORM_INSTANCE_NAME_B, icB.getRequiredExecutionPlatform());
    }

    public RequiredExecutionPlatformInstance createRequiredExecutionPlatformInstance(String name, RequiredExecutionPlatform type) {
        RequiredExecutionPlatformInstance repi = new RequiredExecutionPlatformInstance(name, type);
        return repi;
    }

    public ProvidedExecutionPlatformInstance getProvidedExecutionPlatformInstanceA(InternalComponent icA){
        return createProvidedExecutionPlatformInstance(PROVIDED_EXECUTION_PLATFORM_INSTANCE_NAME_A, icA.getProvidedExecutionPlatforms().get(0));
    }

    public ProvidedExecutionPlatformInstance getProvidedExecutionPlatformInstanceB(InternalComponent icB){
        return createProvidedExecutionPlatformInstance(PROVIDED_EXECUTION_PLATFORM_INSTANCE_NAME_A, icB.getProvidedExecutionPlatforms().get(0));
    }

    private ProvidedExecutionPlatformInstance createProvidedExecutionPlatformInstance(String name, ProvidedExecutionPlatform type){
        ProvidedExecutionPlatformInstance pepi=new ProvidedExecutionPlatformInstance(name, type);
        return pepi;
    }

    public Relationship getRelationshipA() {
        return createRelationship(RELATIONSHIP_A_NAME);
    }

    public Relationship getRelationshipB() {
        return createRelationship(RELATIONSHIP_B_NAME);
    }


    private Relationship createRelationship(String name) {
        Relationship relationship = new Relationship();
        relationship.setName(name);

        ProvidedPort providedPort = new ProvidedPort();
        providedPort.setName(name + PROVIDED_PORT_NAME);
        Component ownerProvided = getInternalComponentA();
        ownerProvided.getProvidedPorts().add(providedPort);
        providedPort.setComponent(ownerProvided);
        relationship.setProvidedPort(providedPort);

        RequiredPort requiredPort = new RequiredPort();
        requiredPort.setName(name + REQUIRED_PORT_NAME);
        InternalComponent ownerRequired = getInternalComponentB();
        ownerRequired.getRequiredPorts().add(requiredPort);
        requiredPort.setComponent(ownerRequired);
        relationship.setRequiredPort(requiredPort);

        return relationship;
    }

    public RelationshipInstance getRelationshipInstanceA() {
        return createRelationshipInstance(RELATIONSHIP_INSTANCE_A_NAME, getRelationshipA());
    }

    public RelationshipInstance getRelationshipInstanceB() {
        return createRelationshipInstance(RELATIONSHIP_INSTANCE_B_NAME, getRelationshipB());
    }

    private RelationshipInstance createRelationshipInstance(String name, Relationship relationship) {
        RelationshipInstance relationshipInstance = new RelationshipInstance(name, relationship);

        InternalComponentInstance ownerOfProvidedPort = getInternalComponentInstanceAWithType((InternalComponent) relationship.getProvidedPort().getComponent());
        ProvidedPortInstance providedPortInstance = new ProvidedPortInstance(name + PROVIDED_PORT_INSTANCE_NAME, relationship.getProvidedPort(), ownerOfProvidedPort);
        ownerOfProvidedPort.getProvidedPortInstances().add(providedPortInstance);
        relationshipInstance.setProvidedPortInstance(providedPortInstance);

        InternalComponentInstance ownerOfRequiredPort = getInternalComponentInstanceBWithType((InternalComponent) relationship.getRequiredPort().getComponent());

        RequiredPortInstance requiredPortInstance = new RequiredPortInstance(name + REQUIRED_PORT_INSTANCE_NAME, relationship.getRequiredPort(), ownerOfRequiredPort);
        ownerOfRequiredPort.getRequiredPortInstances().add(requiredPortInstance);
        relationshipInstance.setRequiredPortInstance(requiredPortInstance);

        return relationshipInstance;
    }
 
    public ExecuteInstance getExecuteInstanceA(){
        return createExecuteInstance(EXECUTE_INSTANCE_NAME_A);
    }

    public ExecuteInstance getExecuteInstanceB(){
        return createExecuteInstance(EXECUTE_INSTANCE_NAME_B);
    }

    private ExecuteInstance createExecuteInstance(String name){
        InternalComponentInstance requiredOwner = getInternalComponentInstanceA();
        InternalComponentInstance providedOwner = getInternalComponentInstanceB();

        getInternalComponentInstanceB();
        ExecuteInstance ei=new ExecuteInstance(name, providedOwner.getProvidedExecutionPlatformInstances().get(0), requiredOwner.getRequiredExecutionPlatformInstance());

        return ei;
    }


}
