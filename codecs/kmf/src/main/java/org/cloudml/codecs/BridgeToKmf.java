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

import net.cloudml.core.VM;
import org.cloudml.core.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nicolas Ferry on 25.02.14.
 */
public class BridgeToKmf {

    private Map<String, net.cloudml.core.VM> vms = new HashMap<String, VM>();
    private Map<String, net.cloudml.core.Provider> providers = new HashMap<String, net.cloudml.core.Provider>();
    private Map<String, net.cloudml.core.InternalComponent> internalComponents = new HashMap<String, net.cloudml.core.InternalComponent>();
    private Map<String, net.cloudml.core.RequiredPort> requiredPorts = new HashMap<String, net.cloudml.core.RequiredPort>();
    private Map<String, net.cloudml.core.ProvidedPort> providedPorts = new HashMap<String, net.cloudml.core.ProvidedPort>();
    private Map<String, net.cloudml.core.InternalComponentInstance> internalComponentInstances = new HashMap<String, net.cloudml.core.InternalComponentInstance>();
    private Map<String, net.cloudml.core.RequiredPortInstance> requiredPortInstances = new HashMap<String, net.cloudml.core.RequiredPortInstance>();
    private Map<String, net.cloudml.core.ProvidedPortInstance> providedPortInstances = new HashMap<String, net.cloudml.core.ProvidedPortInstance>();
    private Map<String, net.cloudml.core.VMInstance> VMInstances = new HashMap<String, net.cloudml.core.VMInstance>();
    private Map<String, net.cloudml.core.Relationship> relationships = new HashMap<String, net.cloudml.core.Relationship>();

    private net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
    private net.cloudml.core.CloudMLModel kDeploy = factory.createCloudMLModel();

    public BridgeToKmf(){}

    /**
     * Complements kElement with the properties (instances of
     * org.cloudml.property.Property) defined in element
     *
     * @param element
     * @param kElement
     */
    private void initProperties(CloudMLElementWithProperties element, net.cloudml.core.CloudMLElementWithProperties kElement, net.cloudml.core.CoreFactory factory) {
        for (Property p : element.getProperties()) {
            net.cloudml.core.Property kp = factory.createProperty();
            kp.setName(p.getName());
            kp.setValue(p.getValue());
            kElement.addProperties(kp);
        }
    }

    private void initResources(CloudMLElementWithProperties element, net.cloudml.core.CloudMLElementWithProperties kElement, net.cloudml.core.CoreFactory factory){
        for(Resource r:element.getResources()){
            net.cloudml.core.Resource kr = factory.createResource();
            kr.setName(r.getName());
            kr.setInstallCommand(r.getInstallCommand());
            kr.setDownloadCommand(r.getRetrieveCommand());
            kr.setConfigureCommand(r.getConfigureCommand());
            kr.setStartCommand(r.getStartCommand());
            kr.setStopCommand(r.getStopCommand());
            kElement.addResources(kr);

            String kup="";
            for(Map.Entry<String, String> up : r.getUploadCommand().entrySet()){
                kup+=up.getKey()+" "+up.getValue()+";";
            }
            kr.setUploadCommand(kup);
        }
    }


    public void providersToKmf(List<Provider> providers){
        for (Provider p : providers) {
            net.cloudml.core.Provider kProvider = factory.createProvider();
            initProperties(p, kProvider, factory);
            kProvider.setName(p.getName());
            kProvider.setCredentials(p.getCredentials());
            kDeploy.addProviders(kProvider);

            this.providers.put(kProvider.getName(), kProvider);
        }
    }

    public void externalComponentToKmf(List<ExternalComponent> ExternalComponents){
        //TODO: continue cloning and conversion...
        for (ExternalComponent ec : ExternalComponents) {
            if(ec instanceof org.cloudml.core.VM){
                org.cloudml.core.VM vm=(org.cloudml.core.VM)ec;
                net.cloudml.core.VM kNode = factory.createVM();
                initProperties(vm, kNode, factory);
                initResources(vm, kNode, factory);
                kNode.setName(vm.getName());

                kNode.setProvider(providers.get(vm.getProvider().getName()));
                kNode.setGroupName(vm.getGroupName());
                kNode.setImageId(vm.getImageId());
                kNode.setIs64os(vm.getIs64os());
                kNode.setLocation(vm.getLocation());
                kNode.setMinCores(vm.getMinCores());
                kNode.setMinStorage(vm.getMinStorage());
                kNode.setMinRam(vm.getMinRam());
                kNode.setOs(vm.getOs());
                kNode.setPrivateKey(vm.getPrivateKey());
                kNode.setSecurityGroup(vm.getSecurityGroup());
                kNode.setSshKey(vm.getSshKey());

                vms.put(kNode.getName(), kNode);

                kDeploy.addComponents(kNode);
            }
        }
    }

    public void internalComponentToKmf(List<InternalComponent> internalComponents){
        for (Component ca : internalComponents) {//first pass on the contained elements
            InternalComponent a=(InternalComponent)ca;
            net.cloudml.core.InternalComponent ka = factory.createInternalComponent();
            ka.setName(a.getName());
            initProperties(a, ka, factory);
            initResources(a,ka,factory);

            this.internalComponents.put(ka.getName(), ka);

            for (ProvidedPort ap : a.getProvidedPorts()) {//TODO: duplicated code to be rationalized
                net.cloudml.core.ProvidedPort kap = factory.createProvidedPort();
                kap.setName(ap.getName());
                initProperties(ap, kap, factory);
                kap.setPortNumber(ap.getPortNumber());
                kap.setIsLocal(ap.getIsLocal());
                ka.addProvidedPorts(kap);//!

                providedPorts.put(ka.getName() + "_" + kap.getName(), kap);
            }
            for (RequiredPort ap : a.getRequiredPorts()) {
                net.cloudml.core.RequiredPort kap = factory.createRequiredPort();
                kap.setName(ap.getName());
                initProperties(ap, kap, factory);
                kap.setPortNumber(ap.getPortNumber());
                kap.setIsLocal(ap.getIsLocal());
                kap.setIsMandatory(ap.getIsMandatory());
                ka.addRequiredPorts(kap);

                requiredPorts.put(ka.getName() + "_" + kap.getName(), kap);
            }

            kDeploy.addComponents(ka);
        }
    }


    public void relationshipToKmf(Iterable<Relationship> relationships){
        for (Relationship b : relationships) {
            net.cloudml.core.Relationship kb = factory.createRelationship();
            kb.setName(b.getName());
            kb.setRequiredPort(requiredPorts.get(b.getRequiredPort().getComponent().getName() + "_" + b.getRequiredPort().getName()));
            kb.setProvidedPort(providedPorts.get(b.getProvidedPort().getComponent().getName() + "_" + b.getProvidedPort().getName()));

            if (b.getClientResource() != null) {
                net.cloudml.core.Resource cr = factory.createResource();
                cr.setName(b.getClientResource().getName());
                if (b.getClientResource().getInstallCommand() != null) {
                    cr.setInstallCommand(b.getClientResource().getInstallCommand());
                }
                if (b.getClientResource().getRetrieveCommand() != null) {
                    cr.setDownloadCommand(b.getClientResource().getRetrieveCommand());
                }
                if (b.getClientResource().getConfigureCommand() != null) {
                    cr.setConfigureCommand(b.getClientResource().getConfigureCommand());
                }
                if (b.getClientResource().getStartCommand() != null) {
                    cr.setStartCommand(b.getClientResource().getStartCommand());
                }
                if (b.getClientResource().getStopCommand() != null) {
                    cr.setStopCommand(b.getClientResource().getStopCommand());
                }
                kb.setRequiredPortResource(cr);
            }

            if (b.getServerResource() != null) {
                net.cloudml.core.Resource cr = factory.createResource();
                cr.setName(b.getServerResource().getName());
                if (b.getServerResource().getInstallCommand() != null) {
                    cr.setInstallCommand(b.getServerResource().getInstallCommand());
                }
                if (b.getServerResource().getRetrieveCommand() != null) {
                    cr.setDownloadCommand(b.getServerResource().getRetrieveCommand());
                }
                if (b.getServerResource().getConfigureCommand() != null) {
                    cr.setConfigureCommand(b.getServerResource().getConfigureCommand());
                }
                if (b.getServerResource().getStartCommand() != null) {
                    cr.setStartCommand(b.getServerResource().getStartCommand());
                }
                if (b.getServerResource().getStopCommand() != null) {
                    cr.setStopCommand(b.getServerResource().getStopCommand());
                }
                kb.setProvidedPortResource(cr);
            }


            kDeploy.addRelationships(kb);
            this.relationships.put(kb.getName(), kb);
        }
    }

    public void externalComponentInstanceToKmf(List<ExternalComponentInstance> externalComponentInstances){
        for (ExternalComponentInstance eni : externalComponentInstances) {
            if(eni instanceof VMInstance){
                VMInstance ni=(VMInstance)eni;
                net.cloudml.core.VMInstance kni = factory.createVMInstance();
                kni.setName(ni.getName());
                kni.setPublicAddress(ni.getPublicAddress());
                kni.setType(vms.get(ni.getType().getName()));
                initProperties(ni, kni, factory);

                VMInstances.put(kni.getName(), kni);

                kDeploy.addComponentInstances(kni);
            }
        }
    }

    public void internalComponentInstances(List<InternalComponentInstance> internalComponentInstances){
        for (InternalComponentInstance bai : internalComponentInstances) {//pass 1
            InternalComponentInstance ai=(InternalComponentInstance)bai;
            net.cloudml.core.InternalComponentInstance kai = factory.createInternalComponentInstance();
            kai.setName(ai.getName());
            kai.setType(internalComponents.get(ai.getType().getName()));
            initProperties(ai, kai, factory);

            this.internalComponentInstances.put(kai.getName(), kai);

            if (ai.getDestination() != null) {
                kai.setDestination(VMInstances.get(ai.getDestination().getName()));
            }


            for (RequiredPortInstance api : ai.getRequiredPortInstances()) {
                net.cloudml.core.RequiredPortInstance kapi = factory.createRequiredPortInstance();
                kapi.setName(api.getName());
                kapi.setType(requiredPorts.get(kai.getType().getName() + "_" + api.getType().getName()));
                initProperties(api, kapi, factory);
                kai.addRequiredPortInstances(kapi);
                requiredPortInstances.put(kapi.getName(), kapi);
            }

            for (ProvidedPortInstance api : ai.getProvidedPortInstances()) {
                net.cloudml.core.ProvidedPortInstance kapi = factory.createProvidedPortInstance();
                kapi.setName(api.getName());
                kapi.setType(providedPorts.get(kai.getType().getName() + "_" + api.getType().getName()));
                initProperties(api, kapi, factory);
                kai.addProvidedPortInstances(kapi);
                providedPortInstances.put(kapi.getName(), kapi);
            }

            kDeploy.addComponentInstances(kai);
        }
    }

    public void relationshipInstanceToKmf(List<RelationshipInstance> relationshipInstances){
        for (RelationshipInstance b : relationshipInstances) {
            net.cloudml.core.RelationshipInstance kb = factory.createRelationshipInstance();
            kb.setName(b.getName());
            kb.setRequiredPortInstance(requiredPortInstances.get(b.getRequiredPortInstance().getName()));
            kb.setProvidedPortInstance(providedPortInstances.get(b.getProvidedPortInstance().getName()));
            kb.setType(relationships.get(b.getType().getName()));

            kDeploy.addRelationshipInstances(kb);
        }
    }

    public net.cloudml.core.CloudMLModel toKMF(CloudMLModel deploy) {
        kDeploy.setName(deploy.getName());
        providersToKmf(deploy.getProviders());
        externalComponentToKmf(deploy.getExternalComponents());
        internalComponentToKmf(deploy.getInternalComponents());
        relationshipToKmf(deploy.getRelationships().values());
        externalComponentInstanceToKmf(deploy.getExternalComponentInstances());
        internalComponentInstances(deploy.getInternalComponentInstances());
        relationshipInstanceToKmf(deploy.getRelationshipInstances());
        return kDeploy;
    }

}
