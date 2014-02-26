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
package org.cloudml.codecs;

import org.cloudml.core.*;
import org.cloudml.core.CloudMLElement;
import org.cloudml.core.CloudMLElementWithProperties;
import org.cloudml.core.Component;
import org.cloudml.core.InternalComponent;
import org.cloudml.core.InternalComponentInstance;
import org.cloudml.core.Property;
import org.cloudml.core.ProvidedPort;
import org.cloudml.core.ProvidedPortInstance;
import org.cloudml.core.Relationship;
import org.cloudml.core.RelationshipInstance;
import org.cloudml.core.RequiredPort;
import org.cloudml.core.RequiredPortInstance;
import org.cloudml.core.Resource;
import org.cloudml.core.VMInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nicolas Ferry on 25.02.14.
 */
public class BridgeToPojo {


    private Map<String, VM> nodes = new HashMap<String, VM>();
    private Map<String, Provider> providers = new HashMap<String, Provider>();
    private Map<String, InternalComponent> internalComponents = new HashMap<String, InternalComponent>();
    private Map<String, RequiredPort> requiredPorts = new HashMap<String, RequiredPort>();
    private Map<String, ProvidedPort> providedPorts = new HashMap<String, ProvidedPort>();
    private Map<String, InternalComponentInstance> internalComponentInstances = new HashMap<String, InternalComponentInstance>();
    private Map<String, RequiredPortInstance> requiredPortInstances = new HashMap<String, RequiredPortInstance>();
    private Map<String, ProvidedPortInstance> providedPortInstances = new HashMap<String, ProvidedPortInstance>();
    private Map<String, VMInstance> vmInstances = new HashMap<String, VMInstance>();
    private Map<String, Relationship> relationships = new HashMap<String, Relationship>();
    private CloudMLModel model=new CloudMLModel();

    public CloudMLModel getCloudMLModel(){
        return model;
    }

    public BridgeToPojo(){}

    public void checkForNull(Object obj, String message){
        if(obj == null)
            throw new IllegalArgumentException(message);
    }


    public void providersToPOJO(Iterable<net.cloudml.core.Provider> kproviders){
        checkForNull(kproviders, "Cannot iterate on null!");

        Map<String, Provider> providers = new HashMap<String, Provider>();
        for (net.cloudml.core.Provider kProvider : kproviders) {
            Provider p = new Provider(kProvider.getName(), kProvider.getCredentials());
            initProperties(kProvider, p);
            model.getProviders().add(p);
            providers.put(p.getName(), p);
        }
    }

    public void externalComponentToPOJO(net.cloudml.core.ExternalComponent kExternalComponent){
        checkForNull(kExternalComponent,"Cannot convert null!");

        if(kExternalComponent instanceof net.cloudml.core.VM){
            net.cloudml.core.VM kVM=(net.cloudml.core.VM) kExternalComponent;
            VM vm = new VM(kVM.getName());
            initProperties(kVM, vm);
            initResources(kVM,vm);

            Provider p = providers.get(kVM.getProvider().getName());
            //TODO: extract this to function
            vm.setProvider(p);
            vm.setGroupName(kVM.getGroupName());
            vm.setImageId(kVM.getImageId());
            vm.setIs64os(kVM.getIs64os());
            vm.setLocation(kVM.getLocation());
            vm.setMinCores(kVM.getMinCores());
            vm.setMinStorage(kVM.getMinStorage());
            vm.setMinRam(kVM.getMinRam());
            vm.setOs(kVM.getOs());
            vm.setPrivateKey(kVM.getPrivateKey());
            vm.setSecurityGroup(kVM.getSecurityGroup());
            vm.setSshKey(kVM.getSshKey());

            nodes.put(vm.getName(), vm);

            model.getComponents().put(vm.getName(), vm);
        }
    }

    public void internalComponentToPOJO(net.cloudml.core.InternalComponent kInternalComponent){
        checkForNull(kInternalComponent, "Cannot convert null!");

        InternalComponent a = new InternalComponent(kInternalComponent.getName());
        initProperties(kInternalComponent, a);
        initResources(kInternalComponent, a);
        internalComponents.put(a.getName(), a);

        for (net.cloudml.core.ProvidedPort kap : kInternalComponent.getProvidedPorts()) {//TODO: duplicated code to be rationalized
            ProvidedPort ap = new ProvidedPort(kap.getName(), a, kap.getIsLocal());//TODO
            initProperties(kap, ap);
            ap.setPortNumber(kap.getPortNumber());
            a.getProvidedPorts().add(ap);//!

            providedPorts.put(a.getName() + "_" + ap.getName(), ap);
        }

        for (net.cloudml.core.RequiredPort kap : kInternalComponent.getRequiredPorts()) {//TODO: duplicated code to be rationalized
            RequiredPort ap = new RequiredPort(kap.getName(), a, kap.getIsLocal(), kap.getIsMandatory());//TODO
            initProperties(kap, ap);
            ap.setPortNumber(kap.getPortNumber());
            a.getRequiredPorts().add(ap);//!

            requiredPorts.put(a.getName() + "_" + ap.getName(), ap);
        }

        model.getComponents().put(a.getName(), a);
    }

    public void relationshipsToPOJO(List<net.cloudml.core.Relationship> kRelationships){
        checkForNull(kRelationships, "Cannot iterate on null!");

        for (net.cloudml.core.Relationship kr : kRelationships) {
            relationshipToPOJO(kr);
        }
    }

    public void relationshipToPOJO(net.cloudml.core.Relationship kRelationship){
        checkForNull(kRelationship, "Cannot convert null!");
        RequiredPort rp;
        ProvidedPort pp;

        if(kRelationship.getProvidedPort() == null &&
                kRelationship.getProvidedPort().getName() == null &&
                kRelationship.getProvidedPort().eContainer() == null)
            throw new IllegalArgumentException("A binding need at least a provided port with a name");
        else pp=providedPorts.get(((Component) kRelationship.getProvidedPort().eContainer()).getName() + "_" + kRelationship.getProvidedPort().getName());

        if(kRelationship.getRequiredPort() == null &&
                kRelationship.getRequiredPort().getName() == null &&
                kRelationship.getRequiredPort().eContainer() == null)
            throw new IllegalArgumentException("A binding need at least a required port with a name");
        else rp=requiredPorts.get(((InternalComponent)kRelationship.getRequiredPort().eContainer()).getName() + "_" + kRelationship.getRequiredPort().getName());

        Relationship b = new Relationship(rp, pp);
        b.setName(kRelationship.getName());
        if (kRelationship.getRequiredPortResource() != null) {
            Resource cr = new Resource(kRelationship.getRequiredPortResource().getName());
            if (kRelationship.getRequiredPortResource().getInstallCommand() != null) {
                cr.setInstallCommand(kRelationship.getRequiredPortResource().getInstallCommand());
            }
            if (kRelationship.getRequiredPortResource().getDownloadCommand() != null) {
                cr.setRetrieveCommand(kRelationship.getRequiredPortResource().getDownloadCommand());
            }
            if (kRelationship.getRequiredPortResource().getConfigureCommand() != null) {
                cr.setConfigureCommand(kRelationship.getRequiredPortResource().getConfigureCommand());
            }
            if (kRelationship.getRequiredPortResource().getStartCommand() != null) {
                cr.setStartCommand(kRelationship.getRequiredPortResource().getStartCommand());
            }
            if (kRelationship.getRequiredPortResource().getStopCommand() != null) {
                cr.setStopCommand(kRelationship.getRequiredPortResource().getStopCommand());
            }
            b.setClientResource(cr);
        }
        if (kRelationship.getProvidedPortResource() != null) {
            Resource cr = new Resource(kRelationship.getProvidedPortResource().getName());
            if (kRelationship.getProvidedPortResource().getInstallCommand() != null) {
                cr.setInstallCommand(kRelationship.getProvidedPortResource().getInstallCommand());
            }
            if (kRelationship.getProvidedPortResource().getDownloadCommand() != null) {
                cr.setRetrieveCommand(kRelationship.getProvidedPortResource().getDownloadCommand());
            }
            if (kRelationship.getProvidedPortResource().getConfigureCommand() != null) {
                cr.setConfigureCommand(kRelationship.getProvidedPortResource().getConfigureCommand());
            }
            if (kRelationship.getProvidedPortResource().getStartCommand() != null) {
                cr.setStartCommand(kRelationship.getProvidedPortResource().getStartCommand());
            }
            if (kRelationship.getProvidedPortResource().getStopCommand() != null) {
                cr.setStopCommand(kRelationship.getProvidedPortResource().getStopCommand());
            }
            b.setServerResource(cr);
        }
        model.getRelationships().put(b.getName(), b);
        relationships.put(b.getName(), b);
    }

    public void externalComponentInstanceToPOJO(net.cloudml.core.ExternalComponentInstance kExternalComponentInstance){
        checkForNull(kExternalComponentInstance, "Cannot convert null!");

        if(kExternalComponentInstance instanceof VMInstance){
            net.cloudml.core.VMInstance kVM=(net.cloudml.core.VMInstance) kExternalComponentInstance;
            VMInstance ni = new VMInstance(kVM.getName(), nodes.get(kVM.getType().getName()));
            ni.setPublicAddress(kVM.getPublicAddress());
            initProperties(kVM, ni);

            vmInstances.put(ni.getName(), ni);

            model.getExternalComponentInstances().add(ni);
        }
    }

    public void internalComponentInstanceToPOJO(net.cloudml.core.InternalComponentInstance kInternalComponentInstance){
        checkForNull(kInternalComponentInstance, "Cannot convert null!");

        InternalComponentInstance ai = new InternalComponentInstance(kInternalComponentInstance.getName(), internalComponents.get(kInternalComponentInstance.getType().getName()));
        initProperties(kInternalComponentInstance, ai);
        internalComponentInstances.put(ai.getName(), ai);

        if (kInternalComponentInstance.getDestination() != null) {
            ai.setDestination(vmInstances.get(kInternalComponentInstance.getDestination().getName()));
        }

        for (net.cloudml.core.ProvidedPortInstance kapi : kInternalComponentInstance.getProvidedPortInstances()) {
            ProvidedPortInstance api = new ProvidedPortInstance(kapi.getName(), providedPorts.get(ai.getType().getName() + "_" + kapi.getType().getName()), ai);
            initProperties(kapi, api);
            ai.getProvidedPortInstances().add(api);
            providedPortInstances.put(api.getName(), api);
        }

        for (net.cloudml.core.RequiredPortInstance kapi : kInternalComponentInstance.getRequiredPortInstances()) {
            RequiredPortInstance api = new RequiredPortInstance(kapi.getName(), requiredPorts.get(ai.getType().getName() + "_" + kapi.getType().getName()), ai);
            initProperties(kapi, api);
            ai.getRequiredPortInstances().add(api);
            requiredPortInstances.put(api.getName(), api);
        }

        model.getComponentInstances().add(ai);
    }

    public void relationshipInstancesToPOJO(List<net.cloudml.core.RelationshipInstance> kRelationshipInstances){
        checkForNull(kRelationshipInstances, "Cannot iterate on null!");

        for(net.cloudml.core.RelationshipInstance kr: kRelationshipInstances){
            relationshipInstanceToPOJO(kr);
        }
    }

    public void relationshipInstanceToPOJO(net.cloudml.core.RelationshipInstance kRelationshipInstance){
        checkForNull(kRelationshipInstance, "Cannot convert null!");

        RelationshipInstance b = new RelationshipInstance(requiredPortInstances.get(kRelationshipInstance.getRequiredPortInstance().getName()),
                providedPortInstances.get(kRelationshipInstance.getProvidedPortInstance().getName()), relationships.get(kRelationshipInstance.getType().getName()));
        b.setName(kRelationshipInstance.getName());
        model.getRelationshipInstances().add(b);
    }

    public CloudMLElement toPOJO(net.cloudml.core.CloudMLModel kDeploy) {
        model.setName(kDeploy.getName());

        providersToPOJO(kDeploy.getProviders());


        for (net.cloudml.core.Component kc : kDeploy.getComponents()) {//first pass on the contained elements
            if(kc instanceof net.cloudml.core.ExternalComponent){
                externalComponentToPOJO((net.cloudml.core.ExternalComponent) kc);
            }else if(kc instanceof net.cloudml.core.InternalComponent){
                internalComponentToPOJO((net.cloudml.core.InternalComponent)kc);
            }else{
                throw new IllegalArgumentException("Unknown subtype of component");
            }
        }

        relationshipsToPOJO(kDeploy.getRelationships());

        for (net.cloudml.core.ComponentInstance bai : kDeploy.getComponentInstances()) {//pass1
            if(bai instanceof net.cloudml.core.ExternalComponentInstance){
                externalComponentInstanceToPOJO((net.cloudml.core.ExternalComponentInstance)bai);
            }
            if(bai instanceof net.cloudml.core.InternalComponentInstance){
                internalComponentInstanceToPOJO((net.cloudml.core.InternalComponentInstance)bai);
            }
        }

        relationshipInstancesToPOJO(kDeploy.getRelationshipInstances());

        return model;
    }

    /**
     * Complements element with the properties (instances of
     * org.cloudml.property.Property) defined in kElement
     *
     * @param kElement
     * @param element
     */
    private void initProperties(net.cloudml.core.CloudMLElementWithProperties kElement, CloudMLElementWithProperties element) {
        for (net.cloudml.core.Property kp : kElement.getProperties()) {
            Property p = new Property(kp.getName(), kp.getValue());
            element.getProperties().add(p);
        }
    }

    private void initResources(net.cloudml.core.CloudMLElementWithProperties kElement, CloudMLElementWithProperties element){
        for(net.cloudml.core.Resource kr: kElement.getResources()){
            Resource r = new Resource(kr.getName(), kr.getInstallCommand(), kr.getDownloadCommand(), kr.getConfigureCommand(), kr.getStartCommand(), kr.getStopCommand());

            Map<String, String> up = new HashMap<String, String>();
            String kup=kr.getUploadCommand();
            String[] ups=kup.split(";");
            for(int i=0; i<ups.length;i++){
                String[] com=ups[i].split(" ");
                if(com.length >= 2){
                    up.put(com[0], com[1]);
                }
            }
            r.setUploadCommand(up);

            element.getResources().add(r);
        }
    }

}
