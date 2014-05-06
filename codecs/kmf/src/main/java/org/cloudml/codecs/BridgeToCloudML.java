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

import java.util.*;
import org.cloudml.core.credentials.FileCredentials;

/**
 * Created by Nicolas Ferry on 25.02.14.
 */
public class BridgeToCloudML {

    private Map<String, VM> externalComponents = new HashMap<String, VM>();
    private Map<String, Provider> providers = new HashMap<String, Provider>();
    private Map<String, InternalComponent> internalComponents = new HashMap<String, InternalComponent>();
    private Map<String, RequiredPort> requiredPorts = new HashMap<String, RequiredPort>();
    private Map<String, ProvidedPort> providedPorts = new HashMap<String, ProvidedPort>();
    private Map<String, InternalComponentInstance> internalComponentInstances = new HashMap<String, InternalComponentInstance>();
    private Map<String, RequiredPortInstance> requiredPortInstances = new HashMap<String, RequiredPortInstance>();
    private Map<String, ProvidedPortInstance> providedPortInstances = new HashMap<String, ProvidedPortInstance>();
    private Map<String, VMInstance> vmInstances = new HashMap<String, VMInstance>();
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

        providersToPOJO(kDeploy.getProviders());
        externalComponentsToPOJO(kDeploy.getComponents());
        internalComponentsToPOJO(kDeploy.getComponents());
        externalComponentInstancesToPOJO(kDeploy.getComponentInstances());
        internalComponentInstancesToPOJO(kDeploy.getComponentInstances());
        relationshipsToPOJO(kDeploy.getRelationships());
        relationshipInstancesToPOJO(kDeploy.getRelationshipInstances());
        executeInstancesToPOJO(kDeploy.getExecutesInstances());
        return model;
    }

    public void checkForNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public void providersToPOJO(Collection<net.cloudml.core.Provider> kproviders) {
        checkForNull(kproviders, "Cannot iterate on null!");

        for (net.cloudml.core.Provider kProvider : kproviders) {
            Provider p = new Provider(kProvider.getName(), new FileCredentials(kProvider.getCredentials()));
            convertProperties(kProvider, p);
            model.getProviders().add(p);
            providers.put(p.getName(), p);
        }
        assert kproviders.isEmpty() == providers.isEmpty();
    }

    public void externalComponentsToPOJO(List<net.cloudml.core.Component> components) {
        int counter = 0;
        for (net.cloudml.core.Component c : components) {
            if (c instanceof net.cloudml.core.ExternalComponent) {
                externalComponentToPOJO((net.cloudml.core.ExternalComponent) c);
                counter++;
            }
            else if (c instanceof net.cloudml.core.InternalComponent) {
                //Will be processed in the next pass
            }
            else {
                throw new IllegalArgumentException("Unknown subtype of component: " + c.getClass().getName() + " (" + c.getName() + ")");
            }
        }
        assert counter == model.getComponents().onlyExternals().size();
        assert counter == externalComponents.size();
    }

    public void externalComponentToPOJO(net.cloudml.core.ExternalComponent kExternalComponent) {
        checkForNull(kExternalComponent, "Cannot convert null!");

        if (kExternalComponent instanceof net.cloudml.core.VM) {
            net.cloudml.core.VM kVM = (net.cloudml.core.VM) kExternalComponent;
            VM vm = new VM(kVM.getName(), new Provider("Dummy provider"));
            convertProperties(kVM, vm);
            convertResources(kVM, vm);

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

            initProvidedExecutionPlatforms(kVM, vm);
            externalComponents.put(vm.getName(), vm);

            model.getComponents().add(vm);
        }
        else {
            throw new IllegalArgumentException("Unknown subtype of ExternalComponent " + kExternalComponent.getClass().getName());
        }
    }

    private void initProvidedExecutionPlatforms(net.cloudml.core.Component kc, Component c) {
        List<net.cloudml.core.ProvidedExecutionPlatform> providedExecutionPlatforms = kc.getProvidedExecutionPlatforms();
        if (providedExecutionPlatforms != null) {
            for (net.cloudml.core.ProvidedExecutionPlatform kpep : providedExecutionPlatforms) {
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
            return pep;
        }
        return null;
    }

    public void internalComponentsToPOJO(List<net.cloudml.core.Component> components) {
        int counter = 0;
        for (net.cloudml.core.Component c : components) {
            if (c instanceof net.cloudml.core.InternalComponent) {
                internalComponentToPOJO((net.cloudml.core.InternalComponent) c);
                counter++;
            }
            else if (c instanceof net.cloudml.core.ExternalComponent) {
                //Should have been converted in the previous pass
            }
            else {
                throw new IllegalArgumentException("Unknown subtype of component: " + c.getClass().getName());
            }
        }
        assert counter == model.getComponents().onlyInternals().size();
        assert counter == internalComponents.size();
    }

    public String calculatePortIdentifier(net.cloudml.core.Port kp) {
        return String.format("%s_%s", kp.getComponent().getName(), kp.getName());
    }

    public void convertAndAddProvidedPortsToPOJO(List<net.cloudml.core.ProvidedPort> pps, InternalComponent ic) {
        for (net.cloudml.core.ProvidedPort kpp : pps) {
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
        for (net.cloudml.core.RequiredPort krp : rps) {
            RequiredPort rp = new RequiredPort(krp.getName(), krp.getIsLocal(), krp.getIsMandatory());
            rp.getOwner().set(ic);
            convertProperties(krp, rp);
            rp.setPortNumber(krp.getPortNumber());
            ic.getRequiredPorts().add(rp);
            requiredPorts.put(calculatePortIdentifier(krp), rp);
        }
        assert requiredPorts.size() >= rps.size();
        assert ic.getRequiredPorts().size() == rps.size();
    }

    public void internalComponentToPOJO(net.cloudml.core.InternalComponent kInternalComponent) {
        checkForNull(kInternalComponent, "Cannot convert null!");

        InternalComponent ic = new InternalComponent(kInternalComponent.getName(), new RequiredExecutionPlatform("to be replaced"));
        convertProperties(kInternalComponent, ic);
        convertResources(kInternalComponent, ic);
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
            RequiredExecutionPlatform rep =
                    new RequiredExecutionPlatform(kInternalComponent.getRequiredExecutionPlatform().getName());
            rep.getOwner().set(ic);
            convertProperties(kInternalComponent.getRequiredExecutionPlatform(), rep);
            convertResources(kInternalComponent.getRequiredExecutionPlatform(), rep);

            ic.setRequiredExecutionPlatform(rep);
        }
    }

    public void relationshipsToPOJO(List<net.cloudml.core.Relationship> kRelationships) {
        checkForNull(kRelationships, "Cannot iterate on null!");
        for (net.cloudml.core.Relationship kr : kRelationships) {
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
        assert rp != null;

        Relationship b = new Relationship(kRelationship.getName(), rp, pp);

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

    public void externalComponentInstancesToPOJO(List<net.cloudml.core.ComponentInstance> componentInstances) {
        int counter = 0;
        for (net.cloudml.core.ComponentInstance kc : componentInstances) {
            if (kc instanceof net.cloudml.core.ExternalComponentInstance) {
                externalComponentInstanceToPOJO((net.cloudml.core.ExternalComponentInstance) kc);
                counter++;
            }
            else if (kc instanceof net.cloudml.core.InternalComponentInstance) {
                //Will be processed in the next pass
            }
            else {
                throw new IllegalArgumentException("Unknown subtype of component: " + kc.getClass().getName());
            }
        }
        assert counter == model.getComponentInstances().onlyExternals().size();
        assert vmInstances.size() == counter;
    }

    public void externalComponentInstanceToPOJO(net.cloudml.core.ExternalComponentInstance kExternalComponentInstance) {
        checkForNull(kExternalComponentInstance, "Cannot convert null!");

        if (kExternalComponentInstance instanceof net.cloudml.core.VMInstance) {
            net.cloudml.core.VMInstance kVM = (net.cloudml.core.VMInstance) kExternalComponentInstance;
            assert externalComponents.containsKey(kVM.getType().getName());
            VMInstance ni = new VMInstance(kVM.getName(), externalComponents.get(kVM.getType().getName()));
            ni.setPublicAddress(kVM.getPublicAddress());
            convertProperties(kVM, ni);

            initProvidedExecutionPlatformInstances(kExternalComponentInstance, ni);

            vmInstances.put(ni.getName(), ni);

            model.getComponentInstances().add(ni);
        }
        else {
            throw new IllegalArgumentException("Unknown subtype of ExternalComponentInstance '" + kExternalComponentInstance.getClass().getName());
        }
    }

    public void internalComponentInstancesToPOJO(List<net.cloudml.core.ComponentInstance> componentInstances) {
        int counter = 0;
        for (net.cloudml.core.ComponentInstance ici : componentInstances) {
            if (ici instanceof net.cloudml.core.InternalComponentInstance) {
                internalComponentInstanceToPOJO((net.cloudml.core.InternalComponentInstance) ici);
                counter++;
            }
            else if (ici instanceof net.cloudml.core.ExternalComponentInstance) {
                //Should have been processed in the previous pass
            }
            else {
                throw new IllegalArgumentException("Unknown subtype of component" + ici.getClass().getName());
            }
        }
        assert counter == model.getComponentInstances().onlyInternals().size();
        assert counter == internalComponentInstances.size();
    }

    public void internalComponentInstanceToPOJO(net.cloudml.core.InternalComponentInstance kInternalComponentInstance) {
        checkForNull(kInternalComponentInstance, "Cannot convert null!");

        InternalComponentInstance ai = new InternalComponentInstance(kInternalComponentInstance.getName(), internalComponents.get(kInternalComponentInstance.getType().getName()));
        convertProperties(kInternalComponentInstance, ai);
        internalComponentInstances.put(ai.getName(), ai);

        initRequiredExecutionPlatformInstance(kInternalComponentInstance, ai);
        initProvidedExecutionPlatformInstances(kInternalComponentInstance, ai);
        //TODO: destination

        for (net.cloudml.core.ProvidedPortInstance kapi : kInternalComponentInstance.getProvidedPortInstances()) {
            ProvidedPortInstance api = new ProvidedPortInstance(kapi.getName(), providedPorts.get(ai.getType().getName() + "_" + kapi.getType().getName()));
            api.getOwner().set(ai);
            convertProperties(kapi, api);
            //ai.getProvidedPorts().add(api);
            providedPortInstances.put(api.getName(), api);
        }

        for (net.cloudml.core.RequiredPortInstance kapi : kInternalComponentInstance.getRequiredPortInstances()) {
            RequiredPortInstance api = new RequiredPortInstance(kapi.getName(), requiredPorts.get(ai.getType().getName() + "_" + kapi.getType().getName()));
            api.getOwner().set(ai);
            convertProperties(kapi, api);
            //ai.getRequiredPorts().add(api);
            requiredPortInstances.put(api.getName(), api);
        }

        model.getComponentInstances().add(ai);
    }

    private void initRequiredExecutionPlatformInstance(net.cloudml.core.InternalComponentInstance kInternalComponentInstance, InternalComponentInstance ai) {
        if (kInternalComponentInstance.getRequiredExecutionPlatformInstance() != null) {
            //assert !vmInstances.isEmpty();
            RequiredExecutionPlatformInstance repi =
                    new RequiredExecutionPlatformInstance(kInternalComponentInstance.getRequiredExecutionPlatformInstance().getName(),
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
            for (net.cloudml.core.ProvidedExecutionPlatformInstance kpepi : kInternalComponentInstance.getProvidedExecutionPlatformInstances()) {
                initProvidedExecutionPlatformInstance(kpepi, ai);
            }
        }
    }

    private void initProvidedExecutionPlatformInstance(net.cloudml.core.ProvidedExecutionPlatformInstance kpepi, ComponentInstance ai) {
        if (kpepi != null) {
            ProvidedExecutionPlatform pepType = null;
            for (ProvidedExecutionPlatform pep : ai.getType().getProvidedExecutionPlatforms()) {
                if (pep.getName().equals(kpepi.getType().getName())) {
                    pepType = pep;
                }
            }
            ProvidedExecutionPlatformInstance pepi = new ProvidedExecutionPlatformInstance(kpepi.getName(), pepType);
            pepi.getOwner().set(ai);
            convertProperties(kpepi, pepi);
            convertResources(kpepi, pepi);
            providedExecutionPlatformInstances.put(pepi.getName(), pepi);
            // ai.getProvidedExecutionPlatforms().add(pepi);
        }
    }

    public void relationshipInstancesToPOJO(List<net.cloudml.core.RelationshipInstance> kRelationshipInstances) {
        checkForNull(kRelationshipInstances, "Cannot iterate on null!");

        for (net.cloudml.core.RelationshipInstance kr : kRelationshipInstances) {
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
        for (net.cloudml.core.Property kp : kElement.getProperties()) {
            Property p = new Property(kp.getName(), kp.getValue());
            element.getProperties().add(p);
        }
    }

    public void executeInstancesToPOJO(List<net.cloudml.core.ExecuteInstance> kexecuteInstances) {
        if (kexecuteInstances != null) {
            for (net.cloudml.core.ExecuteInstance kei : kexecuteInstances) {
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

    private void convertResources(net.cloudml.core.CloudMLElementWithProperties kElement, WithResources element) {
        for (net.cloudml.core.Resource kr : kElement.getResources()) {
            Resource r = new Resource(kr.getName(), kr.getInstallCommand(), kr.getDownloadCommand(), kr.getConfigureCommand(), kr.getStartCommand(), kr.getStopCommand());

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

            element.getResources().add(r);
        }
    }
}
