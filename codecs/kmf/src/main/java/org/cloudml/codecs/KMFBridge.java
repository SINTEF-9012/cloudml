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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.cloudml.core.*;

/*
 * A bridge to translate CloudML POJOs into a KMF representation (which then
 * offers XMI and JSON serialization for free, with no deps) This stupid code
 * could go away if we decide to base the metamodel on KMF generated classes...
 */
public class KMFBridge {

    //TODO: RelationshipInstance
    public KMFBridge() {
    }

    public CloudMLElement toPOJO(net.cloudml.core.CloudMLModel kDeploy) {
        Map<String, VM> nodes = new HashMap<String, VM>();
        Map<String, Provider> providers = new HashMap<String, Provider>();
        Map<String, InternalComponent> internalComponents = new HashMap<String, InternalComponent>();
        Map<String, RequiredPort> requiredPorts = new HashMap<String, RequiredPort>();
        Map<String, ProvidedPort> providedPorts = new HashMap<String, ProvidedPort>();
        Map<String, InternalComponentInstance> internalComponentInstances = new HashMap<String, InternalComponentInstance>();
        Map<String, RequiredPortInstance> requiredPortInstances = new HashMap<String, RequiredPortInstance>();
        Map<String, ProvidedPortInstance> providedPortInstances = new HashMap<String, ProvidedPortInstance>();
        Map<String, VMInstance> vmInstances = new HashMap<String, VMInstance>();
        Map<String, Relationship> relationships = new HashMap<String, Relationship>();

        CloudMLModel model = new CloudMLModel(kDeploy.getName());

        for (net.cloudml.core.Provider kProvider : kDeploy.getProviders()) {
            Provider p = new Provider(kProvider.getName(), kProvider.getCredentials());
            initProperties(kProvider, p);
            model.getProviders().add(p);
            providers.put(p.getName(), p);
        }

        for (net.cloudml.core.VM kNode : kDeploy.getVms()) {
            VM vm = new VM(kNode.getName());
            initProperties(kNode, vm);
            initResources(kNode,vm);

            Provider p = providers.get(kNode.getProvider().getName());

            vm.setProvider(p);
            vm.setGroupName(kNode.getGroupName());
            vm.setImageId(kNode.getImageId());
            vm.setIs64os(kNode.getIs64os());
            vm.setLocation(kNode.getLocation());
            vm.setMinCores(kNode.getMinCores());
            vm.setMinStorage(kNode.getMinStorage());
            vm.setMinRam(kNode.getMinRam());
            vm.setOs(kNode.getOs());
            vm.setPrivateKey(kNode.getPrivateKey());
            vm.setSecurityGroup(kNode.getSecurityGroup());
            vm.setSshKey(kNode.getSshKey());

            nodes.put(vm.getName(), vm);

            model.getExternalComponents().put(vm.getName(), vm);
        }

        for (net.cloudml.core.Component ba : kDeploy.getComponents()) {//first pass on the contained elements
            if(ba instanceof net.cloudml.core.InternalComponent){
                net.cloudml.core.InternalComponent ka=(net.cloudml.core.InternalComponent)ba;
                InternalComponent a = new InternalComponent(ka.getName());
                initProperties(ka, a);
                initResources(ka,a);
                internalComponents.put(a.getName(), a);

                for (net.cloudml.core.ProvidedPort kap : ka.getProvidedPorts()) {//TODO: duplicated code to be rationalized
                    ProvidedPort ap = new ProvidedPort(kap.getName(), a, kap.getIsLocal());//TODO
                    initProperties(kap, ap);
                    ap.setPortNumber(kap.getPortNumber());
                    a.getProvidedPorts().add(ap);//!

                    providedPorts.put(a.getName() + "_" + ap.getName(), ap);
                }

                for (net.cloudml.core.RequiredPort kap : ka.getRequiredPorts()) {//TODO: duplicated code to be rationalized
                    RequiredPort ap = new RequiredPort(kap.getName(), a, kap.getIsLocal(), kap.getIsMandatory());//TODO
                    initProperties(kap, ap);
                    ap.setPortNumber(kap.getPortNumber());
                    a.getRequiredPorts().add(ap);//!

                    requiredPorts.put(a.getName() + "_" + ap.getName(), ap);
                }


                //Resource r = new Resource(ka.getResources().getName(), ka.getResources().getInstallCommand(), ka.getResources().getRetrieveCommand(), ka.getResources().getConfigureCommand(), ka.getResources().getStartCommand(), ka.getResources().getStopCommand());


                model.getComponents().put(a.getName(), a);
            }
        }

        /*for (net.cloudml.core.Component kic : kDeploy.getComponents()) {//second pass on the referenced elements
            net.cloudml.core.InternalComponent ka=(net.cloudml.core.InternalComponent)kic;
            InternalComponent a = internalComponents.get(ka.getName());
            if (ka.getDestination() != null) {
                a.setDestination(serverPorts.get(((net.cloudml.core.Artefact)ka.getDestination().eContainer()).getName() + "_" + ka.getDestination().getName()));
            }

        }*/

        for (net.cloudml.core.Relationship kb : kDeploy.getRelationships()) {
            Relationship b = new Relationship(requiredPorts.get(((net.cloudml.core.InternalComponent)kb.getRequiredPort().eContainer()).getName() + "_" + kb.getRequiredPort().getName()), providedPorts.get(((net.cloudml.core.Component)kb.getProvidedPort().eContainer()).getName() + "_" + kb.getProvidedPort().getName()));
            b.setName(kb.getName());
            if (kb.getRequiredPortResource() != null) {
                Resource cr = new Resource(kb.getRequiredPortResource().getName());
                if (kb.getRequiredPortResource().getInstallCommand() != null) {
                    cr.setInstallCommand(kb.getRequiredPortResource().getInstallCommand());
                }
                if (kb.getRequiredPortResource().getDownloadCommand() != null) {
                    cr.setRetrieveCommand(kb.getRequiredPortResource().getDownloadCommand());
                }
                if (kb.getRequiredPortResource().getConfigureCommand() != null) {
                    cr.setConfigureCommand(kb.getRequiredPortResource().getConfigureCommand());
                }
                if (kb.getRequiredPortResource().getStartCommand() != null) {
                    cr.setStartCommand(kb.getRequiredPortResource().getStartCommand());
                }
                if (kb.getRequiredPortResource().getStopCommand() != null) {
                    cr.setStopCommand(kb.getRequiredPortResource().getStopCommand());
                }
                b.setClientResource(cr);
            }
            if (kb.getProvidedPortResource() != null) {
                Resource cr = new Resource(kb.getProvidedPortResource().getName());
                if (kb.getProvidedPortResource().getInstallCommand() != null) {
                    cr.setInstallCommand(kb.getProvidedPortResource().getInstallCommand());
                }
                if (kb.getProvidedPortResource().getDownloadCommand() != null) {
                    cr.setRetrieveCommand(kb.getProvidedPortResource().getDownloadCommand());
                }
                if (kb.getProvidedPortResource().getConfigureCommand() != null) {
                    cr.setConfigureCommand(kb.getProvidedPortResource().getConfigureCommand());
                }
                if (kb.getProvidedPortResource().getStartCommand() != null) {
                    cr.setStartCommand(kb.getProvidedPortResource().getStartCommand());
                }
                if (kb.getProvidedPortResource().getStopCommand() != null) {
                    cr.setStopCommand(kb.getProvidedPortResource().getStopCommand());
                }
                b.setServerResource(cr);
            }
            model.getRelationships().put(b.getName(), b);
            relationships.put(b.getName(), b);
        }

        for (net.cloudml.core.VMInstance kni : kDeploy.getVmInstances()) {
            VMInstance ni = new VMInstance(kni.getName(), nodes.get(kni.getType().getName()));
            ni.setPublicAddress(kni.getPublicAddress());
            initProperties(kni, ni);

            vmInstances.put(ni.getName(), ni);

            model.getExternalComponentInstances().add(ni);
        }


        for (net.cloudml.core.ComponentInstance bai : kDeploy.getComponentInstances()) {//pass1
            if(bai instanceof net.cloudml.core.InternalComponentInstance){
                net.cloudml.core.InternalComponentInstance kai=(net.cloudml.core.InternalComponentInstance)bai;
                InternalComponentInstance ai = new InternalComponentInstance(kai.getName(), internalComponents.get(kai.getType().getName()));
                initProperties(kai, ai);
                internalComponentInstances.put(ai.getName(), ai);

                if (kai.getDestination() != null) {
                    ai.setDestination(vmInstances.get(kai.getDestination().getName()));
                }

                for (net.cloudml.core.ProvidedPortInstance kapi : kai.getProvidedPortInstances()) {
                    ProvidedPortInstance api = new ProvidedPortInstance(kapi.getName(), providedPorts.get(ai.getType().getName() + "_" + kapi.getType().getName()), ai);
                    initProperties(kapi, api);
                    ai.getProvidedPortInstances().add(api);
                    providedPortInstances.put(api.getName(), api);
                }

                for (net.cloudml.core.RequiredPortInstance kapi : kai.getRequiredPortInstances()) {
                    RequiredPortInstance api = new RequiredPortInstance(kapi.getName(), requiredPorts.get(ai.getType().getName() + "_" + kapi.getType().getName()), ai);
                    initProperties(kapi, api);
                    ai.getRequiredPortInstances().add(api);
                    requiredPortInstances.put(api.getName(), api);
                }

                model.getComponentInstances().add(ai);
            }
        }

        /*for (net.cloudml.core.ArtefactInstance kai : kDeploy.getArtefactInstances()) {//pass 2
            ArtefactInstance ai = artefactInstances.get(kai.getName());

            for (net.cloudml.core.PortInstance kapi : kai.getRequiredPorts()) {
                ai.getRequiredPorts().add(clientPortInstances.get(kapi.getName()));
            }

            for (net.cloudml.core.PortInstance kapi : kai.getProvidedPortInstances()) {
                ai.getProvidedPortInstances().add(serverPortInstances.get(kapi.getName()));
            }
        }*/

        for (net.cloudml.core.RelationshipInstance kb : kDeploy.getRelationshipInstances()) {
            RelationshipInstance b = new RelationshipInstance(requiredPortInstances.get(kb.getRequiredPortInstance().getName()), providedPortInstances.get(kb.getProvidedPortInstance().getName()), relationships.get(kb.getType().getName()));
            b.setName(kb.getName());
            model.getRelationshipInstances().add(b);
        }

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
            for(Entry<String, String> up : r.getUploadCommand().entrySet()){
                kup+=up.getKey()+" "+up.getValue()+";";
            }
            kr.setUploadCommand(kup);
        }
    }

    public net.cloudml.core.CloudMLModel toKMF(CloudMLModel deploy) {
        Map<String, net.cloudml.core.VM> vms = new HashMap<String, net.cloudml.core.VM>();
        Map<String, net.cloudml.core.Provider> providers = new HashMap<String, net.cloudml.core.Provider>();
        Map<String, net.cloudml.core.InternalComponent> internalComponents = new HashMap<String, net.cloudml.core.InternalComponent>();
        Map<String, net.cloudml.core.RequiredPort> requiredPorts = new HashMap<String, net.cloudml.core.RequiredPort>();
        Map<String, net.cloudml.core.ProvidedPort> providedPorts = new HashMap<String, net.cloudml.core.ProvidedPort>();
        Map<String, net.cloudml.core.InternalComponentInstance> internalComponentInstances = new HashMap<String, net.cloudml.core.InternalComponentInstance>();
        Map<String, net.cloudml.core.RequiredPortInstance> requiredPortInstances = new HashMap<String, net.cloudml.core.RequiredPortInstance>();
        Map<String, net.cloudml.core.ProvidedPortInstance> providedPortInstances = new HashMap<String, net.cloudml.core.ProvidedPortInstance>();
        Map<String, net.cloudml.core.VMInstance> VMInstances = new HashMap<String, net.cloudml.core.VMInstance>();
        Map<String, net.cloudml.core.Relationship> relationships = new HashMap<String, net.cloudml.core.Relationship>();

        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.CloudMLModel kDeploy = factory.createCloudMLModel();
        kDeploy.setName(deploy.getName());

        for (Provider p : deploy.getProviders()) {
            net.cloudml.core.Provider kProvider = factory.createProvider();
            initProperties(p, kProvider, factory);
            kProvider.setName(p.getName());
            kProvider.setCredentials(p.getCredentials());
            kDeploy.addProviders(kProvider);

            providers.put(kProvider.getName(), kProvider);
        }

        //TODO: continue cloning and conversion...
        for (ExternalComponent ec : deploy.getExternalComponents().values()) {
            if(ec instanceof VM){
                VM vm=(VM)ec;
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

                kDeploy.addVms(kNode);
            }
        }

        for (Component ca : deploy.getComponents().values()) {//first pass on the contained elements
            if(ca instanceof InternalComponent){
                InternalComponent a=(InternalComponent)ca;
                net.cloudml.core.InternalComponent ka = factory.createInternalComponent();
                ka.setName(a.getName());
                initProperties(a, ka, factory);
                initResources(a,ka,factory);

                internalComponents.put(ka.getName(), ka);

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

        /*for (Component a : deploy.getComponents().values()) {//second pass on the referenced elements
            net.cloudml.core.Component ka = internalComponents.get(a.getName());
            if(a instanceof InternalComponent){
                if (((InternalComponent)a).getDestination() != null) {
                    ka.setDestination(providedPorts.get(((InternalComponent) a).getDestination().getComponent() + "_" + ((InternalComponent) a).getDestination().getName()));
                }
            }
        }*/

        for (Relationship b : deploy.getRelationships().values()) {
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
            relationships.put(kb.getName(), kb);
        }

        for (ExternalComponentInstance eni : deploy.getExternalComponentInstances()) {
            if(eni instanceof ExternalComponentInstance){
                VMInstance ni=(VMInstance)eni;
                net.cloudml.core.VMInstance kni = factory.createVMInstance();
                kni.setName(ni.getName());
                kni.setPublicAddress(ni.getPublicAddress());
                kni.setType(vms.get(ni.getType().getName()));
                initProperties(ni, kni, factory);

                VMInstances.put(kni.getName(), kni);

                kDeploy.addVmInstances(kni);
            }
        }

        for (ComponentInstance bai : deploy.getComponentInstances()) {//pass 1
            if(bai instanceof InternalComponentInstance){
                InternalComponentInstance ai=(InternalComponentInstance)bai;
                net.cloudml.core.InternalComponentInstance kai = factory.createInternalComponentInstance();
                kai.setName(ai.getName());
                kai.setType(internalComponents.get(ai.getType().getName()));
                initProperties(ai, kai, factory);

                internalComponentInstances.put(kai.getName(), kai);

                if (ai.getDestination() != null) {
                    kai.setDestination(VMInstances.get(ai.getDestination().getName()));
                }


                for (RequiredPortInstance api : ai.getRequiredPortInstances()) {
                    net.cloudml.core.RequiredPortInstance kapi = factory.createRequiredPortInstance();
                    kapi.setName(api.getName());
                    kapi.setType(requiredPorts.get(kai.getType().getName() + "_" + api.getType().getName()));
                    //kapi.setComponent(artefactInstances.get(ai.getName()));
                    initProperties(api, kapi, factory);
                    kai.addRequiredPortInstances(kapi);
                    requiredPortInstances.put(kapi.getName(), kapi);
                }

                for (ProvidedPortInstance api : ai.getProvidedPortInstances()) {
                    net.cloudml.core.ProvidedPortInstance kapi = factory.createProvidedPortInstance();
                    kapi.setName(api.getName());
                    kapi.setType(providedPorts.get(kai.getType().getName() + "_" + api.getType().getName()));
                    //kapi.setComponent(artefactInstances.get(ai.getName()));
                    initProperties(api, kapi, factory);
                    kai.addProvidedPortInstances(kapi);
                    providedPortInstances.put(kapi.getName(), kapi);
                }

                kDeploy.addComponentInstances(kai);
            }
        }

        for (RelationshipInstance b : deploy.getRelationshipInstances()) {
            net.cloudml.core.RelationshipInstance kb = factory.createRelationshipInstance();
            kb.setName(b.getName());
            kb.setRequiredPortInstance(requiredPortInstances.get(b.getRequiredPortInstance().getName()));
            kb.setProvidedPortInstance(providedPortInstances.get(b.getProvidedPortInstance().getName()));
            kb.setType(relationships.get(b.getType().getName()));

            kDeploy.addRelationshipInstances(kb);
        }

        return kDeploy;
        //TODO: remove the following (here just for "testing" purpose)
        /*
         * try { ModelJSONSerializer jsonSerializer = new ModelJSONSerializer();
         * File result = File.createTempFile(kDeploy.getName(), ".kmf.json");
         * OutputStream streamResult = new FileOutputStream(result);
         * jsonSerializer.serialize(kDeploy, streamResult); } catch (Exception
         * e) {}
         */

    }
    /**
     * Convenience procedure to convert JSON into XMI. Should be removed (or
     * moved) not to introduce a dependency to JSON here
     *
     * @param args
     */
    /*
     * public static void main(String[] args) { KMFBridge xmiCodec = new
     * KMFBridge(); JsonCodec jsonCodec = new JsonCodec();
     *
     * try { FilenameFilter filter = new FilenameFilter() {
     *
     * public boolean accept(File dir, String name) { return
     * name.endsWith(".json"); } }; File inputDirectory = new
     * File(xmiCodec.getClass().getResources("/").toURI()); for (File input :
     * inputDirectory.listFiles(filter)) { System.out.println("loading " +
     * input.getAbsolutePath() + ", " + jsonCodec); CloudMLModel model =
     * (CloudMLModel) jsonCodec.load(new FileInputStream(input));
     * xmiCodec.save(model, new FileOutputStream(new File(input.getParentFile(),
     * input.getName() + ".xmi"))); } } catch (FileNotFoundException ex) {
     * Logger.getLogger(KMFBridge.class.getName()).log(Level.SEVERE, null, ex);
     * } catch (URISyntaxException ex) {
     * Logger.getLogger(KMFBridge.class.getName()).log(Level.SEVERE, null, ex);
     * }
     *
     * }
     */
}
