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

import net.cloudml.core.ResourcesPool;
import org.cloudml.core.*;
import org.cloudml.core.NamedElement;
import org.cloudml.core.WithProperties;
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

import org.cloudml.core.collections.ProvidedExecutionPlatformInstanceGroup;
import org.cloudml.core.collections.ProvidedPortInstanceGroup;
import org.cloudml.core.collections.RequiredPortInstanceGroup;
import org.cloudml.core.credentials.FileCredentials;
import org.cloudml.core.util.ModelUtils;
import org.cloudml.core.util.OwnedBy;
import java.util.*;

/**
 * Created by Nicolas Ferry on 25.02.14.
 */
public class BridgeToCloudML {

    private Map<String, VM> vms = new HashMap<String, VM>();
    private Map<String, ExternalComponent> externalComponents = new HashMap<String, ExternalComponent>();
    private Map<String, Provider> providers = new HashMap<String, Provider>();
    private Map<String, InternalComponent> internalComponents = new HashMap<String, InternalComponent>();
    private Map<String, RequiredPort> requiredPorts = new HashMap<String, RequiredPort>();
    private Map<String, ProvidedPort> providedPorts = new HashMap<String, ProvidedPort>();
    private Map<String, InternalComponentInstance> internalComponentInstances = new HashMap<String, InternalComponentInstance>();
    private Map<String, RequiredPortInstance> requiredPortInstances = new HashMap<String, RequiredPortInstance>();
    private Map<String, ProvidedPortInstance> providedPortInstances = new HashMap<String, ProvidedPortInstance>();
    private Map<String, VMInstance> vmInstances = new HashMap<String, VMInstance>();
    private Map<String, ExternalComponentInstance> externalComponentInstances = new HashMap<String, ExternalComponentInstance>();
    private Map<String, Relationship> relationships = new HashMap<String, Relationship>();
    private Map<String, ProvidedExecutionPlatformInstance> providedExecutionPlatformInstances = new HashMap<String, ProvidedExecutionPlatformInstance>();
    private Map<String, RequiredExecutionPlatformInstance> requiredExecutionPlatformInstances = new HashMap<String, RequiredExecutionPlatformInstance>();
    private Deployment model = new Deployment();

    public BridgeToCloudML() {

    }

    //TODO: delete this method
    public Deployment getCloudMLModel() {
        return model;
    }

    public NamedElement toPOJO(net.cloudml.core.CloudMLModel kDeploy) {
        model.setName(kDeploy.getName() + " ");
        convertProperties(kDeploy,model);

        providersToPOJO(kDeploy.getProviders());
        externalComponentsToPOJO(kDeploy.getExternalComponents());
        internalComponentsToPOJO(kDeploy.getInternalComponents());
        externalComponentInstancesToPOJO(kDeploy.getExternalComponentInstances());
        internalComponentInstancesToPOJO(kDeploy.getInternalComponentInstances());
        vmsToPOJO(kDeploy.getVms());
        vmInstancesToPOJO(kDeploy.getVmInstances());
        resourcePoolsToPOJO(kDeploy.getResourcePools());
        relationshipsToPOJO(kDeploy.getRelationships());
        relationshipInstancesToPOJO(kDeploy.getRelationshipInstances());
        executeInstancesToPOJO(kDeploy.getExecutesInstances());
        resourcePoolsToPOJO(kDeploy.getResourcePools());
        return model;
    }

    private void resourcePoolsToPOJO(List<net.cloudml.core.ResourcesPool> kResourcePools) {
        checkForNull(kResourcePools, "Cannot iterate on null!");
        for (net.cloudml.core.ResourcesPool kResourcePool: kResourcePools) {
            resourcePoolToPOJO(kResourcePool);
        }
    }

    private void resourcePoolToPOJO(net.cloudml.core.ResourcesPool kResourcePools){
        
    }

    public void checkForNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public void providersToPOJO(Collection<net.cloudml.core.Provider> kproviders) {
        checkForNull(kproviders, "Cannot iterate on null!");

        for (net.cloudml.core.Provider kProvider: kproviders) {
            Provider p = new Provider(kProvider.getName(), new FileCredentials(kProvider.getCredentials()));
            convertProperties(kProvider, p);
            model.getProviders().add(p);
            providers.put(p.getName(), p);
        }
        assert kproviders.isEmpty() == providers.isEmpty();
    }

    public void vmsToPOJO(List<net.cloudml.core.VM> vms) {
        for (net.cloudml.core.VM c: vms) {
            vmToPOJO(c);
        }
    }

    public void vmToPOJO(net.cloudml.core.VM kvm) {
        net.cloudml.core.VM kVM = kvm;
        VM vm = new VM(kVM.getName(), new Provider("Dummy provider"));
        convertProperties(kVM, vm);
        convertResources(kVM, vm);
        convertPuppetResources(kVM, vm);

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
        vm.setRegion(kVM.getRegion());
        vm.setLogin(kVM.getLogin());
        vm.setPasswd(kVM.getPasswd());
        vm.setProviderSpecificTypeName(kVM.getProviderSpecificTypeName());

        initProvidedExecutionPlatforms(kVM, vm);
        vms.put(vm.getName(), vm);

        model.getComponents().add(vm);
    }

    public void externalComponentsToPOJO(List<net.cloudml.core.ExternalComponent> components) {
        int counter = 0;
        for (net.cloudml.core.ExternalComponent c: components) {
            externalComponentToPOJO(c);
            counter++;
        }
        assert counter == model.getComponents().onlyExternals().size();
        assert counter == externalComponents.size();
    }

    public void externalComponentToPOJO(net.cloudml.core.ExternalComponent kExternalComponent) {
        checkForNull(kExternalComponent, "Cannot convert null!");

        if (kExternalComponent instanceof net.cloudml.core.ExternalComponent) {

            ExternalComponent ec = new ExternalComponent(kExternalComponent.getName(), new Provider("Dummy provider"));
            convertProperties(kExternalComponent, ec);
            convertResources(kExternalComponent, ec);

            Provider p = providers.get(kExternalComponent.getProvider().getName());
            ec.setProvider(p);
            if (kExternalComponent.getLogin() != null) {
                ec.setLogin(kExternalComponent.getLogin());
            }
            if (kExternalComponent.getPasswd() != null) {
                ec.setPasswd(kExternalComponent.getPasswd());
            }
            if (kExternalComponent.getLocation() != null) {
                ec.setLocation(kExternalComponent.getLocation());
            }
            if(kExternalComponent.getRegion() != null){
                ec.setRegion(kExternalComponent.getRegion());
            }
            if (kExternalComponent.getServiceType() != null) {
                ec.setServiceType(kExternalComponent.getServiceType());
            }

            convertAndAddProvidedPortsToPOJO(kExternalComponent.getProvidedPorts(), ec);
            initProvidedExecutionPlatforms(kExternalComponent, ec);
            externalComponents.put(ec.getName(), ec);

            model.getComponents().add(ec);
        } else {
            throw new IllegalArgumentException("Unknown subtype of ExternalComponent " + kExternalComponent.getClass().getName());
        }
    }

    private void initProvidedExecutionPlatforms(net.cloudml.core.Component kc, Component c) {
        List<net.cloudml.core.ProvidedExecutionPlatform> providedExecutionPlatforms = kc.getProvidedExecutionPlatforms();
        if (providedExecutionPlatforms != null) {
            for (net.cloudml.core.ProvidedExecutionPlatform kpep: providedExecutionPlatforms) {
                c.getProvidedExecutionPlatforms().add(initProvidedExecutionPlatform(kpep, c));
            }
        }
    }

    private ProvidedExecutionPlatform initProvidedExecutionPlatform(net.cloudml.core.ProvidedExecutionPlatform kpep, Component c) {
        if (kpep != null) {
            ProvidedExecutionPlatform pep = new ProvidedExecutionPlatform(kpep.getName());
            pep.getOwner().set(c);
            convertProperties(kpep, pep);
            convertResources(kpep, pep);
            convertOffers(kpep, pep);
            return pep;
        }
        return null;
    }

    private void convertOffers(net.cloudml.core.ProvidedExecutionPlatform kpep, ProvidedExecutionPlatform pep) {
        for (net.cloudml.core.Property eachKOffer: kpep.getOffers()) {
            pep.getOffers().add(new Property(eachKOffer.getName(), eachKOffer.getValue()));
        }
    }

    public void internalComponentsToPOJO(List<net.cloudml.core.InternalComponent> components) {
        int counter = 0;
        for (net.cloudml.core.InternalComponent c: components) {
            internalComponentToPOJO(c);
            counter++;
        }
        assert counter == model.getComponents().onlyInternals().size();
        assert counter == internalComponents.size();
    }

    public String calculatePortIdentifier(net.cloudml.core.Port kp) {
        return String.format("%s_%s", kp.getComponent().getName(), kp.getName());
    }

    public void convertAndAddProvidedPortsToPOJO(List<net.cloudml.core.ProvidedPort> pps, Component ic) {
        for (net.cloudml.core.ProvidedPort kpp: pps) {
            ProvidedPort pp = new ProvidedPort(kpp.getName(), kpp.getIsLocal());
            pp.getOwner().set(ic);
            convertProperties(kpp, pp);
            pp.setPortNumber(kpp.getPortNumber());
            ic.getProvidedPorts().add(pp);
            providedPorts.put(calculatePortIdentifier(kpp), pp);
        }
        assert providedPorts.size() >= pps.size();
        assert ic.getProvidedPorts().size() == pps.size();
    }

    public void convertAndAddRequiredPortsToPOJO(List<net.cloudml.core.RequiredPort> rps, InternalComponent ic) {
        for (net.cloudml.core.RequiredPort krp: rps) {
            RequiredPort rp = new RequiredPort(krp.getName(), krp.getIsLocal(), !krp.getIsMandatory());
            rp.getOwner().set(ic);
            convertProperties(krp, rp);
            rp.setPortNumber(krp.getPortNumber());
            ic.getRequiredPorts().add(rp);
            requiredPorts.put(calculatePortIdentifier(krp), rp);
        }
        if (!(requiredPorts.size() >= rps.size())) {
            throw new IllegalArgumentException("All required ports have not been cloned!");
        }
        assert ic.getRequiredPorts().size() == rps.size();
    }

    public void internalComponentToPOJO(net.cloudml.core.InternalComponent kInternalComponent) {
        checkForNull(kInternalComponent, "Cannot convert null!");

        InternalComponent ic = new InternalComponent(kInternalComponent.getName(), new RequiredExecutionPlatform("to be replaced"));
        convertProperties(kInternalComponent, ic);
        convertResources(kInternalComponent, ic);
        convertPuppetResources(kInternalComponent,ic);
        internalComponents.put(ic.getName(), ic);

        initRequiredExecutionPlatform(kInternalComponent, ic);
        initProvidedExecutionPlatforms(kInternalComponent, ic);

        convertAndAddProvidedPortsToPOJO(kInternalComponent.getProvidedPorts(), ic);
        convertAndAddRequiredPortsToPOJO(kInternalComponent.getRequiredPorts(), ic);

        model.getComponents().add(ic);
    }

    private void initRequiredExecutionPlatform(net.cloudml.core.InternalComponent kInternalComponent, InternalComponent ic) {
        if (kInternalComponent.getRequiredExecutionPlatform() != null) {
            //assert !vmInstances.isEmpty();
            RequiredExecutionPlatform rep = new RequiredExecutionPlatform(kInternalComponent.getRequiredExecutionPlatform().getName());
            rep.getOwner().set(ic);
            convertProperties(kInternalComponent.getRequiredExecutionPlatform(), rep);
            convertResources(kInternalComponent.getRequiredExecutionPlatform(), rep);
            convertDemands(kInternalComponent, rep);
            ic.setRequiredExecutionPlatform(rep);
        }
    }

    private void convertDemands(net.cloudml.core.InternalComponent kInternalComponent, RequiredExecutionPlatform rep) {
        for (net.cloudml.core.Property eachKDemand: kInternalComponent.getRequiredExecutionPlatform().getDemands()) {
            rep.getDemands().add(new Property(eachKDemand.getName(), eachKDemand.getValue()));
        }
    }

    public void relationshipsToPOJO(List<net.cloudml.core.Relationship> kRelationships) {
        checkForNull(kRelationships, "Cannot iterate on null!");
        for (net.cloudml.core.Relationship kr: kRelationships) {
            relationshipToPOJO(kr);
        }
    }

    public void checkValidPort(net.cloudml.core.Port p) {
        if (p == null) {
            throw new IllegalArgumentException("Port is null! ");
        }
        if (p.getName() == null) {
            throw new IllegalArgumentException("Port name is null! " + p.getClass().getName());
        }
        if (p.getComponent() == null) {
            throw new IllegalArgumentException("Port has no container! " + p.getClass().getName());
        }
    }

    public void relationshipToPOJO(net.cloudml.core.Relationship kRelationship) {
        checkForNull(kRelationship, "Cannot convert null!");

        checkValidPort(kRelationship.getProvidedPort());
        String providedPortIdentifier = calculatePortIdentifier(kRelationship.getProvidedPort());
        ProvidedPort pp = providedPorts.get(providedPortIdentifier);
        assert pp != null;

        checkValidPort(kRelationship.getRequiredPort());
        String requiredPortIdentifier = calculatePortIdentifier(kRelationship.getRequiredPort());
        RequiredPort rp = requiredPorts.get(requiredPortIdentifier);

        if (rp == null) {
            throw new IllegalArgumentException("Required port is null!" + kRelationship.getName());
        }

        Relationship b = new Relationship(kRelationship.getName(), rp, pp);

        convertProperties(kRelationship, b);
        convertResources(kRelationship, b);

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
        model.getRelationships().add(b);
        relationships.put(b.getName(), b);
    }

    public void vmInstancesToPOJO(List<net.cloudml.core.VMInstance> vminstances) {
        for (net.cloudml.core.VMInstance kc: vminstances) {
            vmInstanceToPOJO(kc);
        }
    }

    public void vmInstanceToPOJO(net.cloudml.core.VMInstance kVmInstance) {
        net.cloudml.core.VMInstance kVM = (net.cloudml.core.VMInstance) kVmInstance;
        assert vms.containsKey(kVM.getType().getName());
        VMInstance ni = new VMInstance(kVM.getName(), vms.get(kVM.getType().getName()));
        ni.setPublicAddress(kVM.getPublicAddress());
        convertProperties(kVM, ni);

        initProvidedExecutionPlatformInstances(kVmInstance, ni);

        vmInstances.put(ni.getName(), ni);

        model.getComponentInstances().add(ni);
    }

    public void externalComponentInstancesToPOJO(List<net.cloudml.core.ExternalComponentInstance> componentInstances) {
        int counter = 0;
        for (net.cloudml.core.ExternalComponentInstance kc: componentInstances) {
            externalComponentInstanceToPOJO((net.cloudml.core.ExternalComponentInstance) kc);
            counter++;
        }
        assert counter == model.getComponentInstances().onlyExternals().size();
        assert externalComponentInstances.size() == counter;
    }

    public void externalComponentInstanceToPOJO(net.cloudml.core.ExternalComponentInstance kExternalComponentInstance) {
        checkForNull(kExternalComponentInstance, "Cannot convert null!");

        if (kExternalComponentInstance instanceof net.cloudml.core.ExternalComponentInstance) {
            assert externalComponents.containsKey(kExternalComponentInstance.getType().getName());
            ExternalComponentInstance ni = new ExternalComponentInstance(kExternalComponentInstance.getName(), externalComponents.get(kExternalComponentInstance.getType().getName()));
            //ni.setPublicAddress(kExternalComponentInstance.getPublicAddress());
            convertProperties(kExternalComponentInstance, ni);

            initProvidedExecutionPlatformInstances(kExternalComponentInstance, ni);
            convertAndAddProvidedPortInstances(kExternalComponentInstance.getProvidedPortInstances(), ni);
            externalComponentInstances.put(ni.getName(), ni);

            model.getComponentInstances().add(ni);
        } else {
            throw new IllegalArgumentException("Unknown subtype of ExternalComponentInstance '" + kExternalComponentInstance.getClass().getName());
        }
    }

    public void internalComponentInstancesToPOJO(List<net.cloudml.core.InternalComponentInstance> componentInstances) {
        int counter = 0;
        for (net.cloudml.core.InternalComponentInstance ici: componentInstances) {
            internalComponentInstanceToPOJO((net.cloudml.core.InternalComponentInstance) ici);
            counter++;
        }
        assert counter == model.getComponentInstances().onlyInternals().size();
        assert counter == internalComponentInstances.size();
    }

    public void convertAndAddProvidedPortInstances(List<net.cloudml.core.ProvidedPortInstance> ppi, ComponentInstance ai) {
        ProvidedPortInstanceGroup ppig=new ProvidedPortInstanceGroup();
        for (net.cloudml.core.ProvidedPortInstance kapi: ppi) {
            ProvidedPortInstance api = new ProvidedPortInstance(kapi.getName(), providedPorts.get(ai.getType().getName() + "_" + kapi.getType().getName()));
            api.getOwner().set(ai);
            convertProperties(kapi, api);
            //ai.getProvidedPorts().add(api);
            ppig.add(api);
            providedPortInstances.put(api.getName(), api);
        }
        ai.setProvidedPorts(ppig);
    }

    public void internalComponentInstanceToPOJO(net.cloudml.core.InternalComponentInstance kInternalComponentInstance) {
        checkForNull(kInternalComponentInstance, "Cannot convert null!");

        InternalComponentInstance ai = new InternalComponentInstance(kInternalComponentInstance.getName(), internalComponents.get(kInternalComponentInstance.getType().getName()));
        convertProperties(kInternalComponentInstance, ai);
        internalComponentInstances.put(ai.getName(), ai);

        initRequiredExecutionPlatformInstance(kInternalComponentInstance, ai);
        initProvidedExecutionPlatformInstances(kInternalComponentInstance, ai);
        //TODO: destination

        convertAndAddProvidedPortInstances(kInternalComponentInstance.getProvidedPortInstances(), ai);

        RequiredPortInstanceGroup rpig=new RequiredPortInstanceGroup();
        for (net.cloudml.core.RequiredPortInstance kapi: kInternalComponentInstance.getRequiredPortInstances()) {
            RequiredPortInstance api = new RequiredPortInstance(kapi.getName(), requiredPorts.get(ai.getType().getName() + "_" + kapi.getType().getName()));
            api.getOwner().set(ai);
            convertProperties(kapi, api);
            //ai.getRequiredPorts().add(api);
            rpig.add(api);
            requiredPortInstances.put(api.getName(), api);
        }
        ai.setRequiredPorts(rpig);

        model.getComponentInstances().add(ai);
    }

    private void initRequiredExecutionPlatformInstance(net.cloudml.core.InternalComponentInstance kInternalComponentInstance, InternalComponentInstance ai) {
        if (kInternalComponentInstance.getRequiredExecutionPlatformInstance() != null) {
            //assert !vmInstances.isEmpty();
            RequiredExecutionPlatformInstance repi
                    = new RequiredExecutionPlatformInstance(kInternalComponentInstance.getRequiredExecutionPlatformInstance().getName(),
                                                            ai.getType().getRequiredExecutionPlatform());
            repi.getOwner().set(ai);
            convertProperties(kInternalComponentInstance.getRequiredExecutionPlatformInstance(), repi);
            convertResources(kInternalComponentInstance.getRequiredExecutionPlatformInstance(), repi);
            requiredExecutionPlatformInstances.put(repi.getName(), repi);
            ai.setRequiredExecutionPlatform(repi);
        }
    }

    private void initProvidedExecutionPlatformInstances(net.cloudml.core.ComponentInstance kInternalComponentInstance, ComponentInstance ai) {
        if (kInternalComponentInstance.getProvidedExecutionPlatformInstances() != null) {
            ProvidedExecutionPlatformInstanceGroup pepig=new ProvidedExecutionPlatformInstanceGroup();
            for (net.cloudml.core.ProvidedExecutionPlatformInstance kpepi: kInternalComponentInstance.getProvidedExecutionPlatformInstances()) {
                pepig.add(initProvidedExecutionPlatformInstance(kpepi, ai));
            }
            ai.setProvidedExecutionPlatforms(pepig);
        }
    }

    private ProvidedExecutionPlatformInstance initProvidedExecutionPlatformInstance(net.cloudml.core.ProvidedExecutionPlatformInstance kpepi, ComponentInstance ai) {
        if (kpepi != null) {
            ProvidedExecutionPlatform pepType = null;
            for (ProvidedExecutionPlatform pep: ai.getType().getProvidedExecutionPlatforms()) {
                if (pep.getName().equals(kpepi.getType().getName())) {
                    pepType = pep;
                }
            }
            ProvidedExecutionPlatformInstance pepi = new ProvidedExecutionPlatformInstance(kpepi.getName(), pepType);
            pepi.getOwner().set(ai);
            convertProperties(kpepi, pepi);
            convertResources(kpepi, pepi);
            providedExecutionPlatformInstances.put(pepi.getName(), pepi);
            return pepi;
        }
        return null;
    }

    public void relationshipInstancesToPOJO(List<net.cloudml.core.RelationshipInstance> kRelationshipInstances) {
        checkForNull(kRelationshipInstances, "Cannot iterate on null!");

        for (net.cloudml.core.RelationshipInstance kr: kRelationshipInstances) {
            relationshipInstanceToPOJO(kr);
        }
    }

    public void relationshipInstanceToPOJO(net.cloudml.core.RelationshipInstance kRelationshipInstance) {
        checkForNull(kRelationshipInstance, "Cannot convert null!");

        if (kRelationshipInstance.getRequiredPortInstance() == null) {
            throw new IllegalArgumentException("a relationship instance required at least a required port instance");
        }
        if (kRelationshipInstance.getProvidedPortInstance() == null) {
            throw new IllegalArgumentException("a relationship instance required at least a provided port instance");
        }

        net.cloudml.core.RequiredPortInstance r = kRelationshipInstance.getRequiredPortInstance();
        net.cloudml.core.ProvidedPortInstance p = kRelationshipInstance.getProvidedPortInstance();

        if (r.getName() == null) {
            throw new IllegalArgumentException("Required port need a name");
        }
        if (p.getName() == null) {
            throw new IllegalArgumentException("Provided port need a name");
        }

        RelationshipInstance b = new RelationshipInstance(kRelationshipInstance.getName(), requiredPortInstances.get(r.getName()),
                                                          providedPortInstances.get(p.getName()), relationships.get(kRelationshipInstance.getType().getName()));
        model.getRelationshipInstances().add(b);
    }

    /**
     * Complements element with the properties (instances of
     * org.cloudml.property.Property) defined in kElement
     *
     * @param kElement
     * @param element
     */
    private void convertProperties(net.cloudml.core.CloudMLElementWithProperties kElement, WithProperties element) {
        for (net.cloudml.core.Property kp: kElement.getProperties()) {
            Property p = new Property(kp.getName(), kp.getValue());
            element.getProperties().add(p);
        }
    }

    public void executeInstancesToPOJO(List<net.cloudml.core.ExecuteInstance> kexecuteInstances) {
        if (kexecuteInstances != null) {
            for (net.cloudml.core.ExecuteInstance kei: kexecuteInstances) {
                executeInstanceToPOJO(kei);
            }
        }
    }

    public void executeInstanceToPOJO(net.cloudml.core.ExecuteInstance kei) {
        if (kei != null) {

            ExecuteInstance ei = new ExecuteInstance(kei.getName(),
                                                     requiredExecutionPlatformInstances.get(kei.getRequiredExecutionPlatformInstance().getName()),
                                                     providedExecutionPlatformInstances.get(kei.getProvidedExecutionPlatformInstance().getName()));
            model.getExecuteInstances().add(ei);
        }
    }


    private void convertPuppetResources(net.cloudml.core.CloudMLElementWithProperties kElement, WithResources element){
        for (net.cloudml.core.PuppetResource kr: kElement.getPuppetResources()) {
            PuppetResource pr = new PuppetResource(kr.getName(), kr.getInstallCommand(), kr.getDownloadCommand(), kr.getConfigureCommand(), kr.getStartCommand(), kr.getStopCommand());
            pr.setRequireCredentials(kr.getRequireCredentials());
            pr.setExecuteLocally(kr.getExecuteLocally());

            Map<String, String> up = new HashMap<String, String>();
            String kup = kr.getUploadCommand();
            String[] ups = kup.split(";");
            for (int i = 0; i < ups.length; i++) {
                String[] com = ups[i].split(" ");
                if (com.length >= 2) {
                    up.put(com[0], com[1]);
                }
            }
            pr.setUploadCommand(up);
            convertProperties(kr, pr);
            pr.setMaster(kr.getMasterEndpoint());
            pr.setRepo(kr.getRepositoryEndpoint());
            pr.setConfigureHostnameCommand(kr.getConfigureHostnameCommand());
            pr.setConfigurationFile(kr.getConfigurationFile());
            pr.setRepositoryKey(kr.getRepositoryKey());
            pr.setUsername(kr.getUsername());
            pr.setManifestEntry(kr.getManifestEntry());
            element.getResources().add(pr);
        }
    }

    private void convertResources(net.cloudml.core.CloudMLElementWithProperties kElement, WithResources element) {
        for (net.cloudml.core.Resource kr: kElement.getResources()) {
            Resource r = new Resource(kr.getName(), kr.getInstallCommand(), kr.getDownloadCommand(), kr.getConfigureCommand(), kr.getStartCommand(), kr.getStopCommand());

            r.setRequireCredentials(kr.getRequireCredentials());
            r.setExecuteLocally(kr.getExecuteLocally());

            Map<String, String> up = new HashMap<String, String>();
            String kup = kr.getUploadCommand();
            String[] ups = kup.split(";");
            for (int i = 0; i < ups.length; i++) {
                String[] com = ups[i].split(" ");
                if (com.length >= 2) {
                    up.put(com[0], com[1]);
                }
            }
            r.setUploadCommand(up);
            convertProperties(kr, r);

            if(kr instanceof net.cloudml.core.PuppetResource){
                break;
            }else{

            element.getResources().add(r);
            }
        }
    }
}
