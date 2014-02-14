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

    public CloudMLElement toPOJO(net.cloudml.core.DeploymentModel kDeploy) {
        Map<String, VM> nodes = new HashMap<String, VM>();
        Map<String, Provider> providers = new HashMap<String, Provider>();
        Map<String, InternalComponent> artefacts = new HashMap<String, InternalComponent>();
        Map<String, RequiredPort> clientPorts = new HashMap<String, RequiredPort>();
        Map<String, ProvidedPort> serverPorts = new HashMap<String, ProvidedPort>();
        Map<String, InternalComponentInstance> artefactInstances = new HashMap<String, InternalComponentInstance>();
        Map<String, RequiredPortInstance> clientPortInstances = new HashMap<String, RequiredPortInstance>();
        Map<String, ProvidedPortInstance> serverPortInstances = new HashMap<String, ProvidedPortInstance>();
        Map<String, VMInstance> nodeInstances = new HashMap<String, VMInstance>();
        Map<String, Relationship> bindings = new HashMap<String, Relationship>();

        CloudMLModel model = new CloudMLModel(kDeploy.getName());

        for (net.cloudml.core.Provider kProvider : kDeploy.getProviders()) {
            Provider p = new Provider(kProvider.getName(), kProvider.getCredentials());
            initProperties(kProvider, p);
            model.getProviders().add(p);
            providers.put(p.getName(), p);
        }

        for (net.cloudml.core.Node kNode : kDeploy.getNodeTypes()) {
            VM vm = new VM(kNode.getName());
            initProperties(kNode, vm);

            Provider p = providers.get(kNode.getCloudProvider().getName());

            vm.setProvider(p);
            vm.setGroupName(kNode.getGroupName());
            vm.setImageId(kNode.getImageID());
            vm.setIs64os(kNode.getIs64os());
            vm.setLocation(kNode.getLocation());
            vm.setMinCores(kNode.getMinCore());
            vm.setMinDisk(kNode.getMinDisk());
            vm.setMinRam(kNode.getMinRam());
            vm.setOs(kNode.getOS());
            vm.setPrivateKey(kNode.getPrivateKey());
            vm.setSecurityGroup(kNode.getSecurityGroup());
            vm.setSshKey(kNode.getSshKey());

            nodes.put(vm.getName(), vm);

            model.getVms().put(vm.getName(), vm);
        }

        for (net.cloudml.core.Artefact ka : kDeploy.getArtefactTypes()) {//first pass on the contained elements
            InternalComponent a = new InternalComponent(ka.getName());
            initProperties(ka, a);
            artefacts.put(a.getName(), a);

            for (net.cloudml.core.ServerPort kap : ka.getProvided()) {//TODO: duplicated code to be rationalized
                ProvidedPort ap = new ProvidedPort(kap.getName(), a, kap.getIsRemote());//TODO
                initProperties(kap, ap);
                ap.setPortNumber(kap.getPortNumber());
                a.getProvidedPorts().add(ap);//!

                serverPorts.put(a.getName() + "_" + ap.getName(), ap);
            }

            for (net.cloudml.core.ClientPort kap : ka.getRequired()) {//TODO: duplicated code to be rationalized
                RequiredPort ap = new RequiredPort(kap.getName(), a, kap.getIsRemote(), kap.getIsOptional());//TODO
                initProperties(kap, ap);
                ap.setPortNumber(kap.getPortNumber());
                a.getRequiredPorts().add(ap);//!

                clientPorts.put(a.getName() + "_" + ap.getName(), ap);
            }


            Resource r = new Resource(ka.getResource().getName(), ka.getResource().getDeployingCommand(), ka.getResource().getRetrievingCommand(), ka.getResource().getConfigurationCommand(), ka.getResource().getStartCommand(), ka.getResource().getStopCommand());

            Map<String, String> up = new HashMap<String, String>();
            for (net.cloudml.core.UploadCommand kup : ka.getResource().getUploadCommand()) {
                up.put(kup.getSource(), kup.getTarget());
            }
            r.setUploadCommand(up);

            a.setResource(r);


            model.getComponents().put(a.getName(), a);
        }

        for (net.cloudml.core.Artefact ka : kDeploy.getArtefactTypes()) {//second pass on the referenced elements
            InternalComponent a = artefacts.get(ka.getName());
            if (ka.getDestination() != null) {
                a.setDestination(serverPorts.get(((net.cloudml.core.Artefact)ka.getDestination().eContainer()).getName() + "_" + ka.getDestination().getName()));
            }

        }

        for (net.cloudml.core.Binding kb : kDeploy.getBindingTypes()) {
            Relationship b = new Relationship(clientPorts.get(((net.cloudml.core.Artefact)kb.getClient().eContainer()).getName() + "_" + kb.getClient().getName()), serverPorts.get(((net.cloudml.core.Artefact)kb.getServer().eContainer()).getName() + "_" + kb.getServer().getName()));
            b.setName(kb.getName());
            if (kb.getClientResource() != null) {
                Resource cr = new Resource(kb.getClientResource().getName());
                if (kb.getClientResource().getDeployingCommand() != null) {
                    cr.setDeployingCommand(kb.getClientResource().getDeployingCommand());
                }
                if (kb.getClientResource().getRetrievingCommand() != null) {
                    cr.setRetrievingCommand(kb.getClientResource().getRetrievingCommand());
                }
                if (kb.getClientResource().getConfigurationCommand() != null) {
                    cr.setConfigurationCommand(kb.getClientResource().getConfigurationCommand());
                }
                if (kb.getClientResource().getStartCommand() != null) {
                    cr.setStartCommand(kb.getClientResource().getStartCommand());
                }
                if (kb.getClientResource().getStopCommand() != null) {
                    cr.setStopCommand(kb.getClientResource().getStopCommand());
                }
                b.setClientResource(cr);
            }
            if (kb.getServerResource() != null) {
                Resource cr = new Resource(kb.getServerResource().getName());
                if (kb.getServerResource().getDeployingCommand() != null) {
                    cr.setDeployingCommand(kb.getServerResource().getDeployingCommand());
                }
                if (kb.getServerResource().getRetrievingCommand() != null) {
                    cr.setRetrievingCommand(kb.getServerResource().getRetrievingCommand());
                }
                if (kb.getServerResource().getConfigurationCommand() != null) {
                    cr.setConfigurationCommand(kb.getServerResource().getConfigurationCommand());
                }
                if (kb.getServerResource().getStartCommand() != null) {
                    cr.setStartCommand(kb.getServerResource().getStartCommand());
                }
                if (kb.getServerResource().getStopCommand() != null) {
                    cr.setStopCommand(kb.getServerResource().getStopCommand());
                }
                b.setServerResource(cr);
            }
            model.getRelationships().put(b.getName(), b);
            bindings.put(b.getName(), b);
        }

        for (net.cloudml.core.NodeInstance kni : kDeploy.getNodeInstances()) {
            VMInstance ni = new VMInstance(kni.getName(), nodes.get(kni.getType().getName()));
            ni.setPublicAddress(kni.getPublicAddress());
            initProperties(kni, ni);

            nodeInstances.put(ni.getName(), ni);

            model.getVMInstances().add(ni);
        }


        for (net.cloudml.core.ArtefactInstance kai : kDeploy.getArtefactInstances()) {//pass1
            InternalComponentInstance ai = new InternalComponentInstance(kai.getName(), artefacts.get(kai.getType().getName()));
            initProperties(kai, ai);
            artefactInstances.put(ai.getName(), ai);

            if (kai.getDestination() != null) {
                ai.setDestination(nodeInstances.get(kai.getDestination().getName()));
            }

            for (net.cloudml.core.ServerPortInstance kapi : kai.getProvided()) {
                ProvidedPortInstance api = new ProvidedPortInstance(kapi.getName(), serverPorts.get(ai.getType().getName() + "_" + kapi.getType().getName()), ai);
                initProperties(kapi, api);
                ai.getProvidedPortInstances().add(api);
                serverPortInstances.put(api.getName(), api);
            }

            for (net.cloudml.core.ClientPortInstance kapi : kai.getRequired()) {
                RequiredPortInstance api = new RequiredPortInstance(kapi.getName(), clientPorts.get(ai.getType().getName() + "_" + kapi.getType().getName()), ai);
                initProperties(kapi, api);
                ai.getRequiredPortInstances().add(api);
                clientPortInstances.put(api.getName(), api);
            }

            model.getComponentInstances().add(ai);
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

        for (net.cloudml.core.BindingInstance kb : kDeploy.getBindingInstances()) {
            RelationshipInstance b = new RelationshipInstance(clientPortInstances.get(kb.getClient().getName()), serverPortInstances.get(kb.getServer().getName()), bindings.get(kb.getType().getName()));
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
    private void initProperties(net.cloudml.core.WithProperties kElement, CloudMLElementWithProperties element) {
        for (net.cloudml.core.Property kp : kElement.getProperties()) {
            Property p = new Property(kp.getName(), kp.getValue());
            element.getProperties().add(p);
        }
    }

    /**
     * Complements kElement with the properties (instances of
     * org.cloudml.property.Property) defined in element
     *
     * @param element
     * @param kElement
     */
    private void initProperties(CloudMLElementWithProperties element, net.cloudml.core.WithProperties kElement, net.cloudml.core.CoreFactory factory) {
        for (Property p : element.getProperties()) {
            net.cloudml.core.Property kp = factory.createProperty();
            kp.setName(p.getName());
            kp.setValue(p.getValue());
            kElement.addProperties(kp);
        }
    }

    public net.cloudml.core.DeploymentModel toKMF(CloudMLModel deploy) {
        Map<String, net.cloudml.core.Node> nodes = new HashMap<String, net.cloudml.core.Node>();
        Map<String, net.cloudml.core.Provider> providers = new HashMap<String, net.cloudml.core.Provider>();
        Map<String, net.cloudml.core.Artefact> artefacts = new HashMap<String, net.cloudml.core.Artefact>();
        Map<String, net.cloudml.core.ClientPort> clientPorts = new HashMap<String, net.cloudml.core.ClientPort>();
        Map<String, net.cloudml.core.ServerPort> serverPorts = new HashMap<String, net.cloudml.core.ServerPort>();
        Map<String, net.cloudml.core.ArtefactInstance> artefactInstances = new HashMap<String, net.cloudml.core.ArtefactInstance>();
        Map<String, net.cloudml.core.ClientPortInstance> clientPortInstances = new HashMap<String, net.cloudml.core.ClientPortInstance>();
        Map<String, net.cloudml.core.ServerPortInstance> serverPortInstances = new HashMap<String, net.cloudml.core.ServerPortInstance>();
        Map<String, net.cloudml.core.NodeInstance> nodeInstances = new HashMap<String, net.cloudml.core.NodeInstance>();
        Map<String, net.cloudml.core.Binding> bindings = new HashMap<String, net.cloudml.core.Binding>();

        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.DeploymentModel kDeploy = factory.createDeploymentModel();
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
        for (VM vm : deploy.getVms().values()) {
            net.cloudml.core.Node kNode = factory.createNode();
            initProperties(vm, kNode, factory);
            kNode.setName(vm.getName());

            kNode.setCloudProvider(providers.get(vm.getProvider().getName()));
            kNode.setGroupName(vm.getGroupName());
            kNode.setImageID(vm.getImageId());
            kNode.setIs64os(vm.getIs64os());
            kNode.setLocation(vm.getLocation());
            kNode.setMinCore(vm.getMinCores());
            kNode.setMinDisk(vm.getMinDisk());
            kNode.setMinRam(vm.getMinRam());
            kNode.setOS(vm.getOs());
            kNode.setPrivateKey(vm.getPrivateKey());
            kNode.setSecurityGroup(vm.getSecurityGroup());
            kNode.setSshKey(vm.getSshKey());

            nodes.put(kNode.getName(), kNode);

            kDeploy.addNodeTypes(kNode);
        }


        for (Component a : deploy.getComponents().values()) {//first pass on the contained elements
            net.cloudml.core.Artefact ka = factory.createArtefact();
            ka.setName(a.getName());
            initProperties(a, ka, factory);

            artefacts.put(ka.getName(), ka);

            for (ProvidedPort ap : a.getProvidedPorts()) {//TODO: duplicated code to be rationalized
                net.cloudml.core.ServerPort kap = factory.createServerPort();
                kap.setName(ap.getName());
                initProperties(ap, kap, factory);
                kap.setPortNumber(ap.getPortNumber());
                kap.setIsRemote(ap.getIsLocal());
                ka.addProvided(kap);//!

                serverPorts.put(ka.getName() + "_" + kap.getName(), kap);
            }
            if(a instanceof InternalComponent){
                for (RequiredPort ap : ((InternalComponent)a).getRequiredPorts()) {
                    net.cloudml.core.ClientPort kap = factory.createClientPort();
                    kap.setName(ap.getName());
                    initProperties(ap, kap, factory);
                    kap.setPortNumber(ap.getPortNumber());
                    kap.setIsRemote(ap.getIsLocal());
                    kap.setIsOptional(ap.getIsMandatory());
                    ka.addRequired(kap);

                    clientPorts.put(ka.getName() + "_" + kap.getName(), kap);
                }
            }

            net.cloudml.core.Resource kr = factory.createResource();
            kr.setName(a.getResource().getName());
            kr.setDeployingCommand(a.getResource().getInstallCommand());
            kr.setRetrievingCommand(a.getResource().getRetrieveCommand());
            kr.setConfigurationCommand(a.getResource().getConfigureCommand());
            kr.setStartCommand(a.getResource().getStartCommand());
            kr.setStopCommand(a.getResource().getStopCommand());

            for(Entry<String, String> up : a.getResource().getUploadCommand().entrySet()){
                net.cloudml.core.UploadCommand kup = factory.createUploadCommand();
                kup.setSource(up.getKey());
                kup.setTarget(up.getValue());
                kr.addUploadCommand(kup);
            }

            ka.setResource(kr);

            kDeploy.addArtefactTypes(ka);
        }

        for (Component a : deploy.getComponents().values()) {//second pass on the referenced elements
            net.cloudml.core.Artefact ka = artefacts.get(a.getName());
            if(a instanceof InternalComponent){
                if (((InternalComponent)a).getDestination() != null) {
                    ka.setDestination(serverPorts.get(((InternalComponent)a).getDestination().getComponent() + "_" + ((InternalComponent)a).getDestination().getName()));
                }
            }
        }

        for (Relationship b : deploy.getRelationships().values()) {
            net.cloudml.core.Binding kb = factory.createBinding();
            kb.setName(b.getName());
            kb.setClient(clientPorts.get(b.getRequiredPort().getComponent().getName() + "_" + b.getRequiredPort().getName()));
            kb.setServer(serverPorts.get(b.getProvidedPort().getComponent().getName() + "_" + b.getProvidedPort().getName()));
            
            if (b.getClientResource() != null) {
                net.cloudml.core.Resource cr = factory.createResource();
                cr.setName(b.getClientResource().getName());
                if (b.getClientResource().getInstallCommand() != null) {
                    cr.setDeployingCommand(b.getClientResource().getInstallCommand());
                }
                if (b.getClientResource().getRetrieveCommand() != null) {
                    cr.setRetrievingCommand(b.getClientResource().getRetrieveCommand());
                }
                if (b.getClientResource().getConfigureCommand() != null) {
                    cr.setConfigurationCommand(b.getClientResource().getConfigureCommand());
                }
                if (b.getClientResource().getStartCommand() != null) {
                    cr.setStartCommand(b.getClientResource().getStartCommand());
                }
                if (b.getClientResource().getStopCommand() != null) {
                    cr.setStopCommand(b.getClientResource().getStopCommand());
                }
                kb.setClientResource(cr);
            }

            if (b.getServerResource() != null) {
                net.cloudml.core.Resource cr = factory.createResource();
                cr.setName(b.getServerResource().getName());
                if (b.getServerResource().getInstallCommand() != null) {
                    cr.setDeployingCommand(b.getServerResource().getInstallCommand());
                }
                if (b.getServerResource().getRetrieveCommand() != null) {
                    cr.setRetrievingCommand(b.getServerResource().getRetrieveCommand());
                }
                if (b.getServerResource().getConfigureCommand() != null) {
                    cr.setConfigurationCommand(b.getServerResource().getConfigureCommand());
                }
                if (b.getServerResource().getStartCommand() != null) {
                    cr.setStartCommand(b.getServerResource().getStartCommand());
                }
                if (b.getServerResource().getStopCommand() != null) {
                    cr.setStopCommand(b.getServerResource().getStopCommand());
                }
                kb.setServerResource(cr);
            }


            kDeploy.addBindingTypes(kb);
            bindings.put(kb.getName(), kb);
        }

        for (VMInstance ni : deploy.getVMInstances()) {
            net.cloudml.core.NodeInstance kni = factory.createNodeInstance();
            kni.setName(ni.getName());
            kni.setPublicAddress(ni.getPublicAddress());
            kni.setType(nodes.get(ni.getType().getName()));
            initProperties(ni, kni, factory);

            nodeInstances.put(kni.getName(), kni);

            kDeploy.addNodeInstances(kni);
        }

        for (ComponentInstance ai : deploy.getComponentInstances()) {//pass 1
            net.cloudml.core.ArtefactInstance kai = factory.createArtefactInstance();
            kai.setName(ai.getName());
            kai.setType(artefacts.get(ai.getType().getName()));
            initProperties(ai, kai, factory);

            artefactInstances.put(kai.getName(), kai);

            if (ai.getDestination() != null) {
                kai.setDestination(nodeInstances.get(ai.getDestination().getName()));
            }

            if(ai instanceof InternalComponentInstance){
                InternalComponentInstance iai=(InternalComponentInstance)ai;

                for (RequiredPortInstance api : iai.getRequiredPortInstances()) {
                    net.cloudml.core.ClientPortInstance kapi = factory.createClientPortInstance();
                    kapi.setName(api.getName());
                    kapi.setType(clientPorts.get(kai.getType().getName() + "_" + api.getType().getName()));
                    //kapi.setComponent(artefactInstances.get(ai.getName()));
                    initProperties(api, kapi, factory);
                    kai.addRequired(kapi);
                    clientPortInstances.put(kapi.getName(), kapi);
                }
            }

            for (ProvidedPortInstance api : ai.getProvidedPortInstances()) {
                net.cloudml.core.ServerPortInstance kapi = factory.createServerPortInstance();
                kapi.setName(api.getName());
                kapi.setType(serverPorts.get(kai.getType().getName() + "_" + api.getType().getName()));
                //kapi.setComponent(artefactInstances.get(ai.getName()));
                initProperties(api, kapi, factory);
                kai.addProvided(kapi);
                serverPortInstances.put(kapi.getName(), kapi);
            }

            kDeploy.addArtefactInstances(kai);
        }

        for (RelationshipInstance b : deploy.getRelationshipInstances()) {
            net.cloudml.core.BindingInstance kb = factory.createBindingInstance();
            kb.setName(b.getName());
            kb.setClient(clientPortInstances.get(b.getRequiredPortInstance().getName()));
            kb.setServer(serverPortInstances.get(b.getProvidedPortInstance().getName()));
            kb.setType(bindings.get(b.getType().getName()));

            kDeploy.addBindingInstances(kb);
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
     * File(xmiCodec.getClass().getResource("/").toURI()); for (File input :
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
