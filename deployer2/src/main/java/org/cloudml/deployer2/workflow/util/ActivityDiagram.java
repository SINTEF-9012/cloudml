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
package org.cloudml.deployer2.workflow.util;

import org.cloudml.connectors.*;
import org.cloudml.connectors.util.CloudMLQueryUtil;
import org.cloudml.connectors.util.MercurialConnector;
import org.cloudml.core.*;
import org.cloudml.core.InternalComponentInstance.State;
import org.cloudml.core.collections.ComponentInstanceGroup;
import org.cloudml.core.collections.ExternalComponentInstanceGroup;
import org.cloudml.core.collections.RelationshipInstanceGroup;
import org.cloudml.deployer.CloudAppDeployer;
import org.cloudml.deployer.CloudMLModelComparator;
import org.cloudml.deployer.PuppetManifestGenerator;
import org.cloudml.deployer.Scaler;
import org.cloudml.deployer2.dsl.*;
import org.cloudml.deployer2.dsl.util.ActivityBuilder;
import org.cloudml.monitoring.status.StatusMonitor;
import org.cloudml.mrt.Coordinator;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * The deployment Engine
 * author: Nicolas Ferry
 * author: Hui Song
 */
public class ActivityDiagram  {

    private HashMap<String, ArrayList<? extends Element>> collector = new HashMap<String, ArrayList<? extends Element>>();
    private Activity oldPlan; //this variable is used only as a flag for some checks during the redeployment
    // oldEdges and oldNodes are actually used to store components of the old plan
    // I couldn't use oldPlan = ActivityBuilder.getActivity() because plan is a static variable and it will change oldPlan variable when I add new nodes
    private ArrayList<ActivityEdge> oldEdges = new ArrayList<ActivityEdge>();
    private ArrayList<ActivityNode> oldNodes = new ArrayList<ActivityNode>();

    private static final Logger journal = Logger.getLogger(ActivityDiagram.class.getName());
    private static boolean DEBUG=false;

    static ComponentInstanceGroup<ComponentInstance<? extends Component>> alreadyDeployed = new ComponentInstanceGroup<ComponentInstance<? extends Component>>();
    ComponentInstanceGroup<ComponentInstance<? extends Component>> alreadyStarted = new ComponentInstanceGroup<ComponentInstance<? extends Component>>();
    private Deployment currentModel;
    private Deployment targetModel;
    private static Coordinator coordinator;
    private boolean statusMonitorActive;
    private StatusMonitor statusMonitor; //always check if active

    public ActivityDiagram() {
        System.setProperty("jsse.enableSNIExtension", "false");
    }

    public Deployment getCurrentModel(){
        return currentModel;
    }

    public StatusMonitor getStatusMonitor(){
        return this.statusMonitor;
    }

    public Coordinator getCoordinator(){
        return coordinator;
    }

    public void setCoordinator(Coordinator coordinator){
        this.coordinator=coordinator;
    }

    /**
     * Deploy from a deployment model
     *
     * @param targetModel an updated deployment model
     */
    public void deploy(Deployment targetModel) throws Exception {

        unlessNotNull("Cannot deploy null!", targetModel);
//        this.targetModel = newModel;
//        if (oldModel != null)
//            currentModel = oldModel;
        //set up the monitoring
//        StatusConfiguration.StatusMonitorProperties statusMonitorProperties = StatusConfiguration.load();
//        MonitoringPlatformConfiguration.MonitoringPlatformProperties monitoringPlatformProperties = MonitoringPlatformConfiguration.load();

        if (currentModel == null) {
            journal.log(Level.INFO, ">> First deployment plan ...");
            this.currentModel = targetModel;

//            if (statusMonitorProperties.getActivated() && statusMonitor == null) {
//                statusMonitorActive = true;
//                statusMonitor = new StatusMonitor(statusMonitorProperties.getFrequency(), false, coordinator);
//            }

            // Provisioning vms and external services
            setExternalServices(targetModel.getComponentInstances().onlyExternals());

            // Deploying on vms
            prepareComponents(targetModel.getComponentInstances(), targetModel.getRelationshipInstances());

            //Configure the components with the relationships
            configureWithRelationships(targetModel.getRelationshipInstances());

            //configuration process at SaaS level
            configureSaas(targetModel.getComponentInstances().onlyInternals(), targetModel.getRelationshipInstances());

            //Run puppet
//            configureWithPuppet(targetModel.getComponentInstances().onlyInternals());
//            generatePuppetManifestAndConfigure();

            //send the current deployment to the monitoring platform
//            if (monitoringPlatformProperties.isMonitoringPlatformGiven()) {
//                MonitoringSynch.sendCurrentDeployment(monitoringPlatformProperties.getIpAddress(), currentModel);
//            }
        } else {
            journal.log(Level.INFO, ">> Generating deployment plan to update running application...");
            CloudMLModelComparator diff = new CloudMLModelComparator(currentModel, targetModel);
            diff.compareCloudMLModel();

            updateCurrentModel(diff);

            // when we redeploy, old depoyment plan is in memory, so we save it separately before we add any new nodes
            oldPlan = ActivityBuilder.getActivity();
            oldNodes.addAll(oldPlan.getNodes());
            oldEdges.addAll(oldPlan.getEdges());

            //Added stuff
            if (!diff.getAddedECs().isEmpty())
                setExternalServices(new ExternalComponentInstanceGroup(diff.getAddedECs()).onlyExternals());
            if (!diff.getAddedComponents().isEmpty())
                prepareComponents(new ComponentInstanceGroup(diff.getAddedComponents()), targetModel.getRelationshipInstances());
            if (!diff.getAddedRelationships().isEmpty())
                configureWithRelationships(new RelationshipInstanceGroup(diff.getAddedRelationships()));
            if (!diff.getAddedComponents().isEmpty())
                configureSaas(new ComponentInstanceGroup<InternalComponentInstance>(diff.getAddedComponents()), targetModel.getRelationshipInstances());
//            configureWithPuppet(targetModel.getComponentInstances().onlyInternals());

            removeOldActivityDiagram();

            // in case we only remove components, our adaptation plan shall start here
            if (ActivityBuilder.getStartNode() == null && ActivityBuilder.getFinalNode() == null)
                ActivityBuilder.controlStart();

            //removed stuff
            /*
                To keep it more or less simple: in each method check if FinalNodeExists.
                If it does - remove final node and connect remove actions to the node before last.
                If it does not, get last node from activity and connect actions to it.
                Finally, after all remove actions are created, get last node, create Final node, and connect last to final.
             */
            unconfigureRelationships(diff.getRemovedRelationships());
            stopInternalComponents(diff.getRemovedComponents());
            terminateExternalServices(diff.getRemovedECs());

            // if final node does not exist, it means we have some remove actions, so we get last edge's Source and add Final node
            ActivityFinalNode finalNode = ActivityBuilder.getFinalNode();
            if (finalNode == null){
                int last = ActivityBuilder.getActivity().getNodes().size() - 1;
                ActivityNode source = ActivityBuilder.getActivity().getNodes().get(last);
                ActivityFinalNode stop = ActivityBuilder.controlStop();
                ActivityBuilder.connect(source, stop, true);
            }



            //send the changes to the monitoring platform
//            if (monitoringPlatformProperties.isMonitoringPlatformGiven()) {
//                MonitoringSynch.sendAddedComponents(monitoringPlatformProperties.getIpAddress(), diff.getAddedECs(), diff.getAddedComponents());
//                boolean result = MonitoringSynch.sendRemovedComponents(monitoringPlatformProperties.getIpAddress(), diff.getRemovedECs(), diff.getRemovedComponents());
//                if (!result && monitoringPlatformProperties.isMonitoringPlatformGiven()){
//                    MonitoringSynch.sendCurrentDeployment(monitoringPlatformProperties.getIpAddress(), currentModel);
//                }
//            }
        }

        //start the monitoring of VMs
//        if (statusMonitorActive) {
//            statusMonitor.start();
//        }

        //MODAClouds specific code
//        if(targetModel.getProperties().get("sla_url") != null && targetModel.getProperties().get("agreement_id") != null){
//            Boolean status=startSLA(targetModel.getProperties().get("sla_url").getValue(),targetModel.getProperties().get("agreement_id").getValue());
//            if(status){
//                journal.log(Level.INFO, ">> SLA management started");
//            }else{
//                journal.log(Level.INFO, ">> SLA management not started");
//            }
//        }
    }

    private void removeOldActivityDiagram() {
        boolean plansAreDifferent = true;

        if (ActivityBuilder.getActivity().getEdges().size() == oldEdges.size()) {
            for (int i = 0; i < ActivityBuilder.getActivity().getEdges().size(); i++) {
                if (ActivityBuilder.getActivity().getEdges().get(i) != oldEdges.get(i))
                    plansAreDifferent = false;
            }
        }

        if (ActivityBuilder.getActivity().getNodes().size() == oldNodes.size()) {
            for (int i = 0; i < ActivityBuilder.getActivity().getNodes().size(); i++) {
                if (ActivityBuilder.getActivity().getNodes().get(i) != oldNodes.get(i))
                    plansAreDifferent = false;
            }
        }

        if (plansAreDifferent) {
            // if we do redeployment, we have to clean deployment plan from old nodes and edges
            ActivityBuilder.getActivity().getEdges().removeAll(oldEdges);
            //with nodes removeAll removes both IPAddresses nodes, so here is workaround to avoid that
            for (ActivityNode node : oldNodes) {
                if (ActivityBuilder.getActivity().getNodes().contains(node))
                    ActivityBuilder.getActivity().getNodes().remove(node);
                if (node instanceof ActivityFinalNode)
                    break;
            }
        } else {
            ActivityBuilder.getActivity().getEdges().clear();
            ActivityBuilder.getActivity().getNodes().clear();
        }
    }

    // connects remove action from the diff part to the end of graph. This remove action has incoming control edge
    private void connectRemoveToPlan(ActivityNode node) throws Exception {
        ActivityFinalNode finalNode = ActivityBuilder.getFinalNode();
        if (finalNode == null){
            /*
              We need to substract as many edges from the last one, as input node has in total
             */
//            int discountedEdges = node.getIncoming().size() + node.getOutgoing().size();
            int previousIndex = ActivityBuilder.getActivity().getNodes().size() - 2; // -2 because node from the parameter is the last one
//            ActivityEdge lastEdge = ;
            ActivityNode source = ActivityBuilder.getActivity().getNodes().get(previousIndex);
            ActivityBuilder.connect(source, node, true);
//            source.removeEdge(lastEdge, ActivityNode.Direction.OUT);
//            ActivityBuilder.getActivity().removeEdge(lastEdge);
//            source.addEdge(node.getIncoming().get(0), ActivityNode.Direction.OUT);
        } else {
            ActivityEdge lastEdge = finalNode.getIncoming().get(0);
            ActivityNode previousNode = lastEdge.getSource();
            ActivityBuilder.deleteNode(finalNode);
            ActivityBuilder.connect(previousNode, node, true);
//            source.removeEdge(lastEdge, ActivityNode.Direction.OUT);
//            ActivityBuilder.getActivity().removeEdge(lastEdge);
//            ActivityBuilder.getActivity().removeNode(finalNode);
//            source.addEdge(node.getIncoming().get(0), ActivityNode.Direction.OUT);
        }
    }

    private Boolean startSLA(String url, String agreementId){
        URL slaUrl = null;
        try {
            slaUrl = new URL(url+"/modaclouds/"+agreementId+"/start");
            HttpURLConnection httpCon = (HttpURLConnection) slaUrl.openConnection();
            httpCon.setRequestMethod("PUT");
            httpCon.setRequestProperty("Content-Type", "application/json");
            httpCon.connect();
            if(httpCon.getResponseCode() == 202){
                return true;
            }
        } catch (MalformedURLException e) {
            journal.log(Level.SEVERE, e.getMessage());
        } catch (ProtocolException e) {
            journal.log(Level.SEVERE, e.getMessage());
        } catch (IOException e) {
            journal.log(Level.SEVERE, e.getMessage());
        }
        return false;
    }


    public void deploy(Deployment targetModel, CloudMLModelComparator diff) throws Exception {
        unlessNotNull("Cannot deploy null!", targetModel);
        this.targetModel = targetModel;
        //set up the monitoring
//        StatusConfiguration.StatusMonitorProperties statusMonitorProperties = StatusConfiguration.load();
//        MonitoringPlatformConfiguration.MonitoringPlatformProperties monitoringPlatformProperties = MonitoringPlatformConfiguration.load();

        journal.log(Level.INFO, ">> Updating a deployment...");

        // when we redeploy, old depoyment plan is in memory, so we save it separately before we add any new nodes
        oldPlan = ActivityBuilder.getActivity();
        oldNodes.addAll(oldPlan.getNodes());
        oldEdges.addAll(oldPlan.getEdges());

        //Added stuff
        if (!diff.getAddedECs().isEmpty())
            setExternalServices(new ExternalComponentInstanceGroup(diff.getAddedECs()).onlyExternals());
        if (!diff.getAddedComponents().isEmpty())
            prepareComponents(new ComponentInstanceGroup(diff.getAddedComponents()), targetModel.getRelationshipInstances());
        if (!diff.getAddedRelationships().isEmpty())
            configureWithRelationships(new RelationshipInstanceGroup(diff.getAddedRelationships()));
        if (!diff.getAddedComponents().isEmpty())
            configureSaas(new ComponentInstanceGroup<InternalComponentInstance>(diff.getAddedComponents()), targetModel.getRelationshipInstances());
//        configureWithPuppet(targetModel.getComponentInstances().onlyInternals());

        // if we do redeployment, we have to clean deployment plan from old nodes and edges
        removeOldActivityDiagram();

        //removed stuff
        unconfigureRelationships(diff.getRemovedRelationships());
        stopInternalComponents(diff.getRemovedComponents());
        terminateExternalServices(diff.getRemovedECs());


        //send the changes to the monitoring platform
//        if (monitoringPlatformProperties.isMonitoringPlatformGiven()) {
//            MonitoringSynch.sendAddedComponents(monitoringPlatformProperties.getIpAddress(), diff.getAddedECs(), diff.getAddedComponents());
//            boolean result = MonitoringSynch.sendRemovedComponents(monitoringPlatformProperties.getIpAddress(), diff.getRemovedECs().keySet(), diff.getRemovedComponents());
//            if (!result && monitoringPlatformProperties.isMonitoringPlatformGiven()){
//                MonitoringSynch.sendCurrentDeployment(monitoringPlatformProperties.getIpAddress(), currentModel);
//            }
//        }
    }

    private static void unlessNotNull(String message, Object... obj) {
        if (obj != null) {
            for (Object o : obj) {
                if (o == null) {
                    throw new IllegalArgumentException(message);
                }
            }
        } else {
            throw new IllegalArgumentException(message);
        }
    }


    /**
     * Update the currentModel with the targetModel and preserve all the CPSM
     * metadata
     *
     * @param diff a model comparator
     */
    public void updateCurrentModel(CloudMLModelComparator diff) {
        if (diff != null) {
            currentModel.getComponentInstances().removeAll(diff.getRemovedComponents());
            currentModel.getRelationshipInstances().removeAll(diff.getRemovedRelationships());
            currentModel.getComponentInstances().removeAll(diff.getRemovedECs().keySet());
            currentModel.getExecuteInstances().removeAll(diff.getRemovedExecutes());
            alreadyDeployed.removeAll(diff.getRemovedComponents());
            alreadyStarted.removeAll(diff.getRemovedComponents());

            currentModel.getComponentInstances().addAll(diff.getAddedComponents());
            currentModel.getRelationshipInstances().addAll(diff.getAddedRelationships());
            currentModel.getComponentInstances().addAll(diff.getAddedECs());
            currentModel.getExecuteInstances().addAll(diff.getAddedExecutes());
        } else {
            throw new IllegalArgumentException("Cannot update current model without comparator!");
        }
    }

    /**
     * Prepare the components before their start. Retrieves their resources,
     * builds their PaaS and installs them
     *
     * @param components a list of components
     * @throws java.net.MalformedURLException
     */
    public void prepareComponents(ComponentInstanceGroup<ComponentInstance<? extends Component>> components, RelationshipInstanceGroup relationships) {
        unlessNotNull("Cannot prepare for deployment null!", components);
        // get VM provisioning tasks
        ArrayList<Action> provisioned = new ArrayList<Action>();
        for (ActivityNode node: ActivityBuilder.getActivity().getNodes()){
            if(node.getName().contains("provision")){
                provisioned.add((Action) node);
            }
        }
        for (ComponentInstance<? extends Component> x : components) {
            if (x instanceof InternalComponentInstance) {
                try {
                    prepareAnInternalComponent((InternalComponentInstance) x, components, relationships, provisioned);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Prepare a component before it starts. Retrieves its resources, builds
     * its PaaS and installs it
     *
     * @param instance   an InternalComponentInstance
     * @param components a list of components
     * @param provisioned a list of provisioning actions to connect them with further actions
     * @throws java.net.MalformedURLException
     */
    private void prepareAnInternalComponent(InternalComponentInstance instance, ComponentInstanceGroup<ComponentInstance<? extends Component>> components, RelationshipInstanceGroup relationships, ArrayList<Action> provisioned) throws Exception {
        unlessNotNull("Cannot deploy null!", instance);
        if (!alreadyDeployed.contains(instance) && (instance.getRequiredExecutionPlatform() != null)) {
            ExternalComponentInstance host = instance.externalHost();
            if (host.isVM()) {
                VMInstance ownerVM = host.asVM();
                VM n = ownerVM.getType();

                Action upload = ActivityBuilder.actionNode("executeUploadCommands", ownerVM, instance, null);
                for (Action action:provisioned){
                    if (action.getInputs().get(0).equals(host)){
                        ActivityBuilder.connect(action, upload, true);
                    }
                }

                Action retrieve = ActivityBuilder.actionNode("executeRetrieveCommand", ownerVM, instance, null);
                ActivityBuilder.connect(upload, retrieve, true);

                alreadyDeployed.add(instance);

                ArrayList<Action> outgoingFromPaas = buildPaas(instance, relationships.toList(),retrieve);

                // if buildPaas  creates some actions we need to find related thread to those actions (e.g. this is supervisor thread, but build pass actions are related to nimbus)
                // and connect these action to that thread
                // also we create join node here because we have to synchronize retrieve action from here with actions produced by buildPaas after they
                // were moved to another thread
                Join joinBeforeInstall = null;
                if (outgoingFromPaas.size() > 1 || !outgoingFromPaas.get(0).equals(retrieve)){

                    Action moveToOtherThread = null;
                    if (!retrieve.getOutgoing().isEmpty())
                        moveToOtherThread = (Action) retrieve.getOutgoing().get(0).getTarget();

                    if (moveToOtherThread != null) {
                        for (Action action : provisioned) {
                            if (action.getInputs().get(0).equals(moveToOtherThread.getInputs().get(0))) {
                                ActivityBuilder.disconnect(retrieve, moveToOtherThread, true);
                                ActivityBuilder.connect(action, moveToOtherThread, true);
                            }
                        }
                    }
                    // last node represents nodes that are related to actions which were created based on relationships
                    // other nodes will be related to requiredExecutionPlatforms or buildExecutes() method to say it more precisely
                    Action lastFromPaas = outgoingFromPaas.get(outgoingFromPaas.size() - 1);

                    joinBeforeInstall = ActivityBuilder.joinNode(false);
                    ActivityBuilder.connect(retrieve, joinBeforeInstall, true);
                    ActivityBuilder.connect(lastFromPaas, joinBeforeInstall, true);

                    // now after we handled retrieve action and actions based on connections we need to add actions based on buildExecutes
                    // so we traverse through outgoingFromPaas array from first to the one before last
                    if (outgoingFromPaas.size() > 1) {
                        for (int i = 0; i < (outgoingFromPaas.size() - 1); i++) {
                            Action target = (Action) outgoingFromPaas.get(i).getOutgoing().get(0).getTarget();
                            ActivityBuilder.disconnect(outgoingFromPaas.get(i), target, true);
                            ActivityBuilder.connect(outgoingFromPaas.get(i), joinBeforeInstall, true);

                            // move actions resulted from dependencies to separate thread
                            for (Action action : provisioned) {
                                if (action.getInputs().get(0).equals(target.getInputs().get(0)))
                                    ActivityBuilder.connect(action, target, true);
                            }
                        }
                    }
                }

                Action install = ActivityBuilder.actionNode("executeInstallCommand", ownerVM, instance, null);
                if (joinBeforeInstall == null)
                    ActivityBuilder.connect(retrieve, install, true);
                else
                    ActivityBuilder.connect(joinBeforeInstall, install, true);
            }

//                coordinator.updateStatusInternalComponent(instance.getName(), State.INSTALLED.toString(), ActivityDiagram.class.getName());
//                //instance.setStatus(State.INSTALLED);
//                jc.closeConnection();
//            } else { // If the destination is a PaaS platform
//                ExternalComponent ownerType = (ExternalComponent) host.getType();
//                Provider p = ownerType.getProvider();
//                PaaSConnector connector = ConnectorFactory.createPaaSConnector(p);
//                String stack = "";
//                if(instance.getType().hasProperty("stack"))
//                    stack = instance.getType().getProperties().valueOf("stack");
//                if(instance.hasProperty("stack"))
//                    stack = instance.getProperties().valueOf("stack");
//                if(instance.getType().hasProperty("buildpack"))
//                    stack = instance.getType().getProperties().valueOf("buildpack");
//                if(instance.hasProperty("buildpack"))
//                    stack = instance.getProperties().valueOf("buildpack");
//                String url=connector.createEnvironmentWithWar(
//                        instance.getName(),
//                        instance.getName(),
//                        host.getName(),
//                        stack,
//                        instance.getType().getProperties().valueOf("warfile"),
//                        instance.getType().hasProperty("version") ? instance.getType().getProperties().valueOf("version") : "default-cloudml"
//                );
//                host.setPublicAddress(url);
//                if(instance.hasProperty("containerSize")){
//                    String size =instance.getProperties().valueOf("containerSize");
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("containerSize", size);
//                    connector.configAppParameters(instance.getName(), params);
//                }
//                for(InternalComponentInstance ici: host.hostedComponents()){
//                    coordinator.updateStatus(ici.getName(), State.RUNNING.toString(), ActivityDiagram.class.getName());
//                }
//                coordinator.updateStatusInternalComponent(host.getName(), ComponentInstance.State.RUNNING.toString(), ActivityDiagram.class.getName());
//            }
        }
    }

    /**
     * Execute a command either on Linux or on Windows depending on the name of the OS set up in the type
     * @param owner the VMInstance on which the command will be executed
     * @param jc a connector
     * @param command the command to be executed
     */
    private static void executeCommand(VMInstance owner, Connector jc, String command) {
        if(DEBUG){
            journal.log(Level.INFO, ">> Executing command: " + command);
            journal.log(Level.INFO, ">> On VM: " + owner.getName());
            return;
        }
        if (!command.equals("")) {
            if (!owner.getType().getOs().toLowerCase().contains("windows")) {
                jc.execCommand(owner.getId(), command, "ubuntu", owner.getType().getPrivateKey());
            } else {
                if (command != null && !command.isEmpty()) {
                    PowerShellConnector run = null;
                    try {
                        Thread.sleep(90000); // crappy stuff: wati for windows .... TODO
                        String cmd = "powershell  \"" + command + " " + owner.getType().getPrivateKey() + " " + owner.getPublicAddress() + "\"";
                        journal.log(Level.INFO, ">> Executing command: " + cmd);
                        run = new PowerShellConnector(cmd);
                        journal.log(Level.INFO, ">> STDOUT: " + run.getStandardOutput());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void executeInstallCommand(InternalComponentInstance x, VMInstance owner, Connector jc, boolean debugMode) {
        if (debugMode){
            journal.log(Level.INFO, ">> Installation of " + x.getType().getName() + " is done.");
        } else {
            journal.log(Level.INFO, ">> Install " + x.getType().getName());
            jc = ConnectorFactory.createIaaSConnector(owner.getType().getProvider());
            unlessNotNull("Cannot install with an argument at null", x, owner, jc);
            for (Resource r : x.getType().getResources()) {
                if (!r.getInstallCommand().equals("")) {
                    if (r.getRequireCredentials()) {
                        jc.execCommand(owner.getId(), CloudMLQueryUtil.cloudmlStringRecover(r.getInstallCommand(), r, x) + " " + owner.getType().getProvider().getCredentials().getLogin() + " " + owner.getType().getProvider().getCredentials().getPassword(), "ubuntu", owner.getType().getPrivateKey());
                    } else {
                        executeCommand(owner, jc, CloudMLQueryUtil.cloudmlStringRecover(r.getInstallCommand(), r, x));
                    }
                }
            }
            coordinator.updateStatusInternalComponent(x.getType().getName(), State.INSTALLED.toString(), ActivityDiagram.class.getName());
            jc.closeConnection();
        }
    }

    /**
     * Upload resources associated to an internal component on a specified
     * external component
     *  @param x     the internal component with upload commands
     * @param owner the external component on which the resources are about to
     *              be uploaded
     * @param jc    the connector used to upload
     * @param debugMode
     */
    public static void executeUploadCommands(InternalComponentInstance x, VMInstance owner, Connector jc, boolean debugMode) {
        if (debugMode){
            journal.log(Level.INFO, ">> Uploading of " + x.getType().getName() + " is done.");
        } else {
            journal.log(Level.INFO, ">> Upload " + x.getType().getName());
            jc = ConnectorFactory.createIaaSConnector(owner.getType().getProvider());
            unlessNotNull("Cannot upload with an argument at null", x, owner, jc);
            for (Resource r : x.getType().getResources()) {
                for (String path : r.getUploadCommand().keySet()) {
                    jc.uploadFile(path, r.getUploadCommand().get(path), owner.getId(), "ubuntu", owner.getType().getPrivateKey());
                }
            }
            jc.closeConnection();
        }
    }

    /**
     * Retrieve the resources associated to an InternalComponent
     *
     * @param x     the internalComponent we want to retrieve the resource
     * @param owner the externalComponent on which the resources will be
     *              downloaded
     * @param jc    the connector used to trigger the commands
     * @param debugMode set debug mode to true or false
     */
    public static void executeRetrieveCommand(InternalComponentInstance x, VMInstance owner, Connector jc, boolean debugMode) {
        if (debugMode){
            journal.log(Level.INFO, ">> Retrieving " + x.getType().getName() + " is done.");
        } else {
            journal.log(Level.INFO, ">> Retrieve " + x.getType().getName());
            jc = ConnectorFactory.createIaaSConnector(owner.getType().getProvider());
            unlessNotNull("Cannot retrieve resources of null!", x, owner, jc);
            for (Resource r : x.getType().getResources()) {
                if (!r.getRetrieveCommand().equals("")) {
                    if (r.getRequireCredentials())
                        jc.execCommand(owner.getId(), CloudMLQueryUtil.cloudmlStringRecover(r.getRetrieveCommand(), r, x) + " " + owner.getType().getProvider().getCredentials().getLogin() + "" + owner.getType().getProvider().getCredentials().getPassword(), "ubuntu", owner.getType().getPrivateKey());
                    else executeCommand(owner, jc, CloudMLQueryUtil.cloudmlStringRecover(r.getRetrieveCommand(), r, x));
                }
            }
            jc.closeConnection();
        }
    }

    /**
     * Retrieve the external component on which an component should be deployed
     *
     * @param component the component who want to retrieve the destination
     * @return
     */
    public ExternalComponentInstance getDestination(ComponentInstance component) {
        unlessNotNull("Cannot find destination of null!", component);
        if (component instanceof InternalComponentInstance) {
            InternalComponentInstance internalComponent = (InternalComponentInstance) component;
            return internalComponent.externalHost();
        } else {
            return (ExternalComponentInstance) component;
        }
    }


    /**
     * Generate the manifest file for each VM from the manifestEntry of each puppet resource and start puppet.
     */
    private void generatePuppetManifestAndConfigure(){
        for(VMInstance vmi : currentModel.getComponentInstances().onlyVMs()){
            PuppetManifestGenerator pmg = new PuppetManifestGenerator(vmi, currentModel);
            String path=pmg.generate();
            if(path != null){
                managePuppet(pmg.getSkeleton(), vmi, vmi.getName(), path);
            }
        }
    }


    /**
     * Install puppet, manage the repo, change the hostname and execute puppet on a VM
     * @param pr the puppet resource
     * @param n the vm instance on which puppet will be installed and executed
     * @param hostname the new hostname of the vm for puppet
     * @param path the path to the puppet manifest
     */
    private void managePuppet(PuppetResource pr, VMInstance n, String hostname, String path){
        PuppetMarionnetteConnector puppet=new PuppetMarionnetteConnector(pr.getMaster(),n);
        //check if the configuration file is in the repo and manage the repo
        MercurialConnector mc=new MercurialConnector(pr.getRepo(),pr.getRepositoryKey());
        mc.addFile(path, pr.getUsername());
        //Touch the site.pp file
        puppet.touchSiteFile();
        //call the update host command
        puppet.configureHostname(n.getType().getPrivateKey(), n.getType().getLogin(),n.getType().getPasswd(),
                n.getPublicAddress(), pr.getMaster(), hostname, pr.getConfigureHostnameCommand());
        //start the puppet run
        puppet.install(n);
    }

    /**
     * For each component, execute the puppet manifest associated
     * @param components
     */
    private void configureWithPuppet(ComponentInstanceGroup<InternalComponentInstance> components){
        unlessNotNull("Cannot configure null!", components);
        Connector jc;
        for (InternalComponentInstance ic : components) {
            if(ic.externalHost().isVM()){
                for(Resource r: ic.getType().getResources()){
                    if(r instanceof PuppetResource){
                        PuppetResource pr=(PuppetResource)r;
                        if(!pr.getConfigurationFile().isEmpty()){
                            journal.log(Level.INFO, ">> Using Puppet to configure the following component: "+ic.getName());
                            VMInstance n= ic.getHost().asExternal().asVM();
                            Provider p = n.getType().getProvider();
                            managePuppet(pr,n, pr.getName(),pr.getConfigurationFile());
                        }
                    }
                }
            }
        }
    }


//    private void startExecutes(InternalComponentInstance x){
//        VMInstance ownerVM = x.externalHost().asVM(); //need some tests but if you need to build PaaS then it means that you want to deploy on IaaS
//        VM n = ownerVM.getType();
//
//        Connector jc = ConnectorFactory.createIaaSConnector(n.getProvider());
//
//        ComponentInstance host = x.getHost();
//
//        if (!alreadyStarted.contains(host)) {
//            if (host.isInternal()) {
//                startExecutes(host.asInternal());
//                for (Resource r : host.getType().getResources()) {
//                    String startCommand = CloudMLQueryUtil.cloudmlStringRecover(r.getStartCommand(), r, x);
//                    start(jc, n, ownerVM, startCommand, true);
//                }
//                coordinator.updateStatusInternalComponent(host.getName(), State.RUNNING.toString(), ActivityDiagram.class.getName());
//
//                alreadyStarted.add(host);
//            }
//        }
//        jc.closeConnection();
//    }

    private Action buildExecutes(InternalComponentInstance x, Action incoming) throws Exception {
        Action outgoingFromBuildExecutes = incoming;
        VMInstance ownerVM = x.externalHost().asVM(); //need some tests but if you need to build PaaS then it means that you want to deploy on IaaS
        VM n = ownerVM.getType();

        ComponentInstance host = x.getHost();

        if (!alreadyDeployed.contains(host)) {
            if (host.isInternal()) {
                Action outgoingTwo = buildExecutes(host.asInternal(), outgoingFromBuildExecutes);
                Action upload = ActivityBuilder.actionNode("executeUploadCommands", ownerVM, host.asInternal(), null );
                ActivityBuilder.connect(outgoingTwo, upload, true);

                Action retrieve = ActivityBuilder.actionNode("executeRetrieveCommand", ownerVM, host.asInternal(), null );
                ActivityBuilder.connect(upload, retrieve, true);

                Action install = ActivityBuilder.actionNode("executeInstallCommand", ownerVM, host.asInternal(), null );
                ActivityBuilder.connect(retrieve, install, true);

                outgoingFromBuildExecutes = install;

                alreadyDeployed.add(host);
            }
        } else {
            for (Action action:ActivityBuilder.getActions()){
                if (action.getName().equals("executeInstallCommand") && ((InternalComponentInstance) action.getInputs().get(1)).getName().equals(host.getName())) {
                    outgoingFromBuildExecutes = action;
                    break;
                }
            }
        }
        return outgoingFromBuildExecutes;
    }


    /**
     * Build the paas of an component instance
     *
     * @param x An component instance
     * @param incoming edge that comes from previous Action
     */
    private ArrayList<Action> buildPaas(InternalComponentInstance x, List<RelationshipInstance> relationships, Action incoming) {
        ArrayList<Action> result = new ArrayList<Action>();
        Action outgoingFromPaasByRelationships = incoming;
        unlessNotNull("Cannot deploy null", x, relationships);
        VMInstance ownerVM = x.externalHost().asVM(); //need some tests but if you need to build PaaS then it means that you want to deploy on IaaS
        VM n = ownerVM.getType();

        Action outgoingFromBuildExecutes = null;
        try {
            outgoingFromBuildExecutes = buildExecutes(x, incoming);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Action toUpload = outgoingFromBuildExecutes;;
        if (!outgoingFromBuildExecutes.equals(incoming)){
            result.add(outgoingFromBuildExecutes);
        }


        for (RelationshipInstance bi : relationships) {
            if (bi.getRequiredEnd().getType().isMandatory() && x.getRequiredPorts().contains(bi.getRequiredEnd())) {
                final ComponentInstance<? extends Component> serverComponent = bi.getServerComponent();
                if( getDestination(serverComponent).isVM()){
                    VMInstance owner = (VMInstance) getDestination(serverComponent);
                    if (owner == null) {
                        owner = ownerVM;
                    }
                    if (!alreadyDeployed.contains(serverComponent)) {
                        try {
                            Action upload = null;
                            for (Resource r : serverComponent.getType().getResources()) {
                                if (r.getUploadCommand() != null && !r.getUploadCommand().isEmpty()) {
                                    upload = ActivityBuilder.actionNode("executeUploadCommands", owner, serverComponent.asInternal(), null);
                                    ActivityBuilder.connect(toUpload, upload, true);
                                }
                            }

                            Action retrieve = null;
                            for (Resource r : serverComponent.getType().getResources()) {
                                if (r.getRetrieveCommand() != null && !r.getRetrieveCommand().isEmpty()) {
                                    Action input = upload == null ? toUpload : upload;
                                    retrieve = ActivityBuilder.actionNode("executeRetrieveCommand", owner, serverComponent.asInternal(), null);
                                    ActivityBuilder.connect(input, retrieve, true);
                                }
                            }

                            Action install = null;
                            for (Resource r : serverComponent.getType().getResources()) {
                                if (r.getInstallCommand() != null && !r.getInstallCommand().isEmpty()) {
                                    Action input =
                                                retrieve == null ?
                                                        (upload == null ? toUpload : upload) :
                                                        retrieve;
                                    install = ActivityBuilder.actionNode("executeInstallCommand", owner, serverComponent.asInternal(), null);
                                    ActivityBuilder.connect(input, install, true);
                                }
                            }

                            outgoingFromPaasByRelationships =
                                    install == null ?
                                    (retrieve == null?
                                            (upload == null? toUpload : upload)
                                            : retrieve) :
                                            install;

                            alreadyDeployed.add(serverComponent);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if (result.isEmpty() || !outgoingFromPaasByRelationships.equals(incoming)) {
            result.add(outgoingFromPaasByRelationships);
        }
        return result;

    }

    /**
     * Configure and start SaaS components
     *
     * @param components a list of components
     * @param relationshipInstances
     * @throws java.net.MalformedURLException
     */
    public void configureSaas(ComponentInstanceGroup<InternalComponentInstance> components, RelationshipInstanceGroup relationshipInstances) throws Exception {
        unlessNotNull("Cannot configure null!", components);
//        Connector jc;

        ArrayList<ActivityNode> nodesToFinalJoin = new ArrayList<ActivityNode>();

        ArrayList<ActivityNode> lastFromComponents = new ArrayList<ActivityNode>();
        for (InternalComponentInstance x : components) {
            if (!alreadyStarted.contains(x)) {
                ExternalComponentInstance owner = x.externalHost();
                if (owner instanceof VMInstance) { //TODO: refactor and be more generic for external component in general
                    VMInstance ownerVM = (VMInstance) owner;

                    // find out if some components must be started before this one
                    ArrayList<String> providers = new ArrayList<String>();
                    //TODO some components may have to be started not because required port, but because require execution platform. Refactor
                    for (RequiredPort port:x.getType().getRequiredPorts().toList()){
                        if (port.isMandatory()) {
                            for (RelationshipInstance conn : relationshipInstances) {
                                if (port.equals(conn.getRequiredEnd().getType())){
                                    String provider = conn.getProvidedEnd().getOwner().get().getName();
                                    if (!providers.contains(provider))
                                        providers.add(provider);
                                }
                            }
                        }
                    }

                    ArrayList<ActivityNode> configureConnectionsNodes = new ArrayList<>();

                    // number of providers will be equal to number of forks after connection configuration
                    ArrayList<Join> joins = new ArrayList<Join>();
                    if (!providers.isEmpty()) {
                        for (String provider : providers) {

                            //find last configure:connection_'Action' actions with 'oppositeConnectionEnd' property = provider.getName() and input VMInstance = ownerVM
                            Action lastConnectionConfigure = null;
                            for (Action action:ActivityBuilder.getActions()){
                                if (action.getName().contains("configure:connection")){
                                    if (action.getInputs().get(4).equals(x.getName()) &&
                                            action.getProperties().get("oppositeConnectionEnd") != null &&
                                            action.getProperties().get("oppositeConnectionEnd").equals(provider)) {
                                        lastConnectionConfigure = action;
                                    }
                                }
                            }
                            // connection may be without any commands, in such case we have to get install command
                            if (lastConnectionConfigure == null){
                                for (Action action:ActivityBuilder.getActions()){
                                    if (action.getName().equals("executeInstallCommand") && ((InternalComponentInstance)action.getInputs().get(1)).getName().equals(x.getName())){
                                        lastConnectionConfigure = action;
                                    }
                                }
                            }

                            //TODO - check what happens when components are removed
                            // this code snippet is related to diff. Currently - works for cases when new components are added
                            boolean drawJoins = true;
                            for (ComponentInstance component:alreadyStarted){
                                if (component.getName().equals(provider)){
                                    drawJoins = false;
                                    break;
                                }
                            }

                            if (drawJoins) {
                                // create join with specific property to be able to retrieve it precisely
                                Join connectionJoin = ActivityBuilder.joinNode(false);
                                connectionJoin.getProperties().put("missingActionFrom", provider);
                                ActivityBuilder.connect(lastConnectionConfigure, connectionJoin, true);
                                joins.add(connectionJoin);
                            } else {
                                configureConnectionsNodes.add(lastConnectionConfigure);
                            }

                        }
                    }

                    // connect all joins into one
                    ActivityNode toConfigure = null;
                    if (!joins.isEmpty() && joins != null){
                        if (joins.size() > 1){
                            Join beforeConfigure = ActivityBuilder.joinNode(false);

                            for (int i = 0; i < joins.size(); i++)
                                ActivityBuilder.connect(joins.get(i), beforeConfigure, true);

                            toConfigure = beforeConfigure;
                        } else {
                            toConfigure = joins.get(0);
                        }  // this code snippet  " else if (!edgesFromConfigureConnections.isEmpty()) " is related to the diff part
                    } else if (!configureConnectionsNodes.isEmpty()) {
                        if (configureConnectionsNodes.size() > 1){
                            Join beforeConfigure = ActivityBuilder.joinNode(false);

                            for (ActivityNode act:configureConnectionsNodes)
                                ActivityBuilder.connect(act, beforeConfigure, true);

                            toConfigure = beforeConfigure;
                        } else {
                            toConfigure = configureConnectionsNodes.get(0);
                        }
                    } else {
                        // if no joins, then there was no connection configuration commands and previous action was executeInstall
                        for (Action action:ActivityBuilder.getActions()){
                            //TODO theoritically, component may not have install command, and previous could be upload or retrieve, but this is highly unlikely
                            if (action.getName().equals("executeInstallCommand") && ((InternalComponentInstance)action.getInputs().get(1)).getName().equals(x.getName()))
                                toConfigure = action;
                        }
                    }

                    VM n = ownerVM.getType();

                    Action configure = null;
                    for (Resource r : x.getType().getResources()) {
                        String configurationCommand = CloudMLQueryUtil.cloudmlStringRecover(r.getConfigureCommand(), r, x);
                        if (configurationCommand != null && !configurationCommand.isEmpty()) {
                            configure = ActivityBuilder.actionNode("configure", ownerVM, n, null, configurationCommand, x.getName(), r.getRequireCredentials());
                            ActivityBuilder.connect(toConfigure, configure, true);
                        }
                    }
//                    coordinator.updateStatusInternalComponent(x.getName(), State.CONFIGURED.toString(), ActivityDiagram.class.getName());
                    //x.setStatus(State.CONFIGURED);

                    Action start = null;
                    for (Resource r : x.getType().getResources()) {
                        String startCommand = CloudMLQueryUtil.cloudmlStringRecover(r.getStartCommand(), r, x);
                        if (startCommand != null && !startCommand.isEmpty()) {
                            ActivityNode input = configure == null ? toConfigure : configure;
                            start = ActivityBuilder.actionNode("start", ownerVM, n, null, startCommand, x.getName());
                            ActivityBuilder.connect(input, start, true);
                        }
                    }
//                    coordinator.updateStatusInternalComponent(x.getName(), State.RUNNING.toString(), ActivityDiagram.class.getName());
                    //x.setStatus(State.RUNNING);
                    ActivityNode last =
                            start == null?
                                    (configure == null ? toConfigure : configure) :
                                    start;

                    lastFromComponents.add(last);
                }
            }//TODO if not InternalComponent
        }

        // because we don't know ordering of components, we have to go through them again to do some connections
        for (InternalComponentInstance x : components) {
            if (!alreadyStarted.contains(x)) {
                ExternalComponentInstance owner = x.externalHost();
                if (owner instanceof VMInstance) {
                    VMInstance ownerVM = (VMInstance) owner;
                    int index = components.toList().indexOf(x);
                    ActivityNode last = lastFromComponents.get(index);

                    // find any Join nodes that expect edge from this component
                    ArrayList<Join> waitingJoins = new ArrayList<Join>();
                    for (ControlNode control:ActivityBuilder.getControlNodes()){
                        if (control instanceof Join && !last.equals(control)){
                            Join join = (Join) control;
                            if (join.getProperties().get("missingActionFrom") != null && join.getProperties().get("missingActionFrom").equals(x.getName())){
                                ActivityBuilder.connect(last, join, true);
                                waitingJoins.add(join);
                            }
                        }
                    }

                    if (waitingJoins.isEmpty())
                        nodesToFinalJoin.add(last);

                    alreadyStarted.add(x);
                }
            }
        }

        // if we have more than one edge, join them, otherwise  - just connect it to final node
        ActivityFinalNode finalNode = null;
        if (nodesToFinalJoin.size() > 1){
            Join finalJoin = ActivityBuilder.joinNode(false);

            for (ActivityNode node:nodesToFinalJoin)
                ActivityBuilder.connect(node, finalJoin, true);

            finalNode = ActivityBuilder.controlStop();
            ActivityBuilder.connect(finalJoin, finalNode, true);
        } else {
            ActivityNode node = nodesToFinalJoin.get(0);
            finalNode = ActivityBuilder.controlStop();
            ActivityBuilder.connect(node, finalNode, true);
        }
    }

    /**
     * Configure a component
     *
     * @param jc                   a connector
     * @param n                    A VM type
     * @param ni                   a VM instance
     * @param configurationCommand the command to configure the component,
     *                             parameters are: IP IPDest portDest
     */
    public static void configure(Connector jc, VM n, VMInstance ni, String configurationCommand, Boolean keyRequired, String componentInstanceName, boolean debugMode) {
        if (debugMode){
            journal.log(Level.INFO, "Configuration of " + ni.getType().getName() + " is done");
        } else {
            journal.log(Level.INFO, "Configure " + ni.getType().getName());
            jc = ConnectorFactory.createIaaSConnector(ni.getType().getProvider());
            if (!configurationCommand.equals("")) {
                if (keyRequired)
                    jc.execCommand(ni.getId(), configurationCommand + " " + ni.getType().getProvider().getCredentials().getLogin() + " " + ni.getType().getProvider().getCredentials().getPassword(), "ubuntu", n.getPrivateKey());
                else executeCommand(ni, jc, configurationCommand);
            }
            coordinator.updateStatusInternalComponent(componentInstanceName, State.CONFIGURED.toString(), ActivityDiagram.class.getName());
            jc.closeConnection();
        }
    }

    /**
     * start a component
     *
     * @param jc           a connector
     * @param n            A VM type
     * @param ni           a VM instance
     * @param startCommand the command to start the component
     */
    public static void start(Connector jc, VM n, VMInstance ni, String startCommand, String componentInstanceName, boolean debugMode) {
        if (debugMode){
            journal.log(Level.INFO, ni.getType().getName() + " started");
        } else {
            journal.log(Level.INFO, "Start " + ni.getType().getName());
            jc = ConnectorFactory.createIaaSConnector(ni.getType().getProvider());
            unlessNotNull("Cannot start without connector", jc, n, ni, startCommand);
            if (!startCommand.equals("")) {
                executeCommand(ni, jc, startCommand);
            }
            coordinator.updateStatusInternalComponent(componentInstanceName, State.RUNNING.toString(), ActivityDiagram.class.getName());
            jc.closeConnection();
        }
    }

    public void setExternalServices(ExternalComponentInstanceGroup ems) throws Exception{

        ActivityInitialNode controlStart = ActivityBuilder.controlStart();
        Fork controlFork = ActivityBuilder.forkNode(false);
        ActivityBuilder.connect(controlStart, controlFork, true);

        // list of actions with input data
        Action action = null;
        ArrayList<Action> provisioning = new ArrayList<Action>(ems.size());

        for (ExternalComponentInstance n : ems) {
            if (n instanceof VMInstance) {
                action = ActivityBuilder.actionNode("provisionAVM", n);
                provisioning.add(action);
            } else {
                action = ActivityBuilder.actionNode("provisionAPlatform", n);
                provisioning.add(action);
            }

            ActivityBuilder.connect(controlFork, action, true);
        }

        ObjectNode IPs = null;

        // this code snippet is related to diff
        if (oldPlan != null) {
            IPs = ActivityBuilder.getIPregistry();
            IPs.getIncoming().clear();
            IPs.getOutgoing().clear();
            IPs.getProperties().put("Status", String.valueOf(Element.Status.INACTIVE));
            ActivityBuilder.getActivity().addNode(IPs);
        }

        Join dataJoin = null;
        if (ems.size() > 1) {
            if (IPs == null)
                IPs = ActivityBuilder.createIPregistry();

            dataJoin = ActivityBuilder.joinNode(true);
            ActivityBuilder.connect(dataJoin, IPs, false);

            for (Action act:provisioning)
                ActivityBuilder.connect(act, dataJoin, false);
        } else {
            if (IPs == null)
                IPs = ActivityBuilder.createIPregistry();
            ActivityBuilder.connect(provisioning.get(0), IPs, false);
        }
    }

    /**
     * Provision a VM
     *
     * @param action action which holds VMInstance as input
     */
    public static void provisionAVM(Action action, boolean debugMode) {
        VMInstance n = (VMInstance) action.getInputs().get(0);

        if(debugMode){
            journal.log(Level.INFO, ">> Provisioning of: " + n.getName() + " is done");
            action.addOutput("debug IP");
        } else {
            Provider p = n.getType().getProvider();
            Connector jc = ConnectorFactory.createIaaSConnector(p);
            coordinator.updateStatus(n.getName(), ComponentInstance.State.PENDING.toString(), ActivityDiagram.class.getName());
            HashMap<String, String> runtimeInformation = jc.createInstance(n);
            coordinator.updateStatus(n.getName(), runtimeInformation.get("status"), ActivityDiagram.class.getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // save IP to the plan
            action.addOutput(runtimeInformation.get("publicAddress"));

            coordinator.updateIP(n.getName(), runtimeInformation.get("publicAddress"), ActivityDiagram.class.getName());
            //enable the monitoring of the new machine
//        if (statusMonitorActive) {
//            statusMonitor.attachModule(jc);
//        }
            jc.closeConnection();
        }
    }

    /**
     * Provision a platform.
     * So far (with only two examples of BeansTalk and CloudBees), the main PaaS
     * platforms are not necessary to be provisioned before deployment, so this
     * method is basically used to launch a DB
     *
     * @param action action which holds ExternalComponentInstance as input
     */
    public static void provisionAPlatform(Action action, boolean debugMode) {
        ExternalComponentInstance n = (ExternalComponentInstance) action.getInputs().get(0);

        if(debugMode){
            journal.log(Level.INFO, ">> Provisioning of: " + n.getName() + " is done");
            action.addOutput("debug IP");
        } else {
            ExternalComponentInstance<? extends ExternalComponent> eci = (ExternalComponentInstance<? extends ExternalComponent>) n;
            ExternalComponent ec = eci.getType();
            Provider p = eci.getType().getProvider();

            if (ec.getServiceType() == null)
                return;
            if (ec.getServiceType().toLowerCase().equals("database")) {//For now we use string but this will evolve to an enum
                PaaSConnector connector = (PaaSConnector) ConnectorFactory.createPaaSConnector(p);
                connector.createDBInstance(
                        ec.hasProperty("DB-Engine") ? ec.getProperties().valueOf("DB-Engine") : null,
                        ec.hasProperty("DB-Version") ? ec.getProperties().valueOf("DB-Version") : null,
                        eci.getName(),
                        ec.hasProperty("DB-Name") ? ec.getProperties().valueOf("DB-Name") : null,
                        ec.getLogin(),
                        ec.getPasswd(),
                        ec.hasProperty("allocatedSize") ? Integer.parseInt(ec.getProperties().valueOf("allocatedSize")) : 0,
                        null,
                        ec.hasProperty("securityGroup") ? ec.getProperties().valueOf("securityGroup") : "");
                String pa = connector.getDBEndPoint(eci.getName(), 600);
                eci.setPublicAddress(pa);
                action.addOutput(pa);
//            coordinator.updateIP(n.getName(),pa,ActivityDiagram.class.getName());
//            coordinator.updateStatus(n.getName(), ComponentInstance.State.RUNNING.toString(), ActivityDiagram.class.getName());
                //execute the configure command
            /*if (!n.getType().getResources().isEmpty()) {
                for (Resource r : n.getType().getResources()) {
                    if (r.getConfigureCommand() != null) {
                        connector.restoreDB(eci.getPublicAddress(), "3306", ec.getLogin(), ec.getPasswd(),
                                ec.hasProperty("DB-Name") ? ec.getProperties().valueOf("DB-Name") : null, r.getConfigureCommand());
                    }
                }
            }*/

            }
            if (ec.getServiceType().toLowerCase().equals("messagequeue")) {
                PaaSConnector connector = (PaaSConnector) ConnectorFactory.createPaaSConnector(p);
                String url = connector.createQueue(n.getName());
                eci.setPublicAddress(url);
                action.addOutput(url);
            }
        }
    }


    /**
     * Configure components according to the relationships
     *
     * @param relationships a list of relationships
     * @throws java.net.MalformedURLException
     */
    public void configureWithRelationships(RelationshipInstanceGroup relationships) throws Exception {
        ObjectNode publicAddresses = ActivityBuilder.getIPregistry();
        ArrayList<Action> connectionActions = new ArrayList<Action>();

        //Configure on the basis of the relationships
        //parameters transmitted to the configuration scripts are "ip ipDestination portDestination"
        for (RelationshipInstance bi : relationships) {
            if (bi.getProvidedEnd().getOwner().get().isExternal()) {  //For DB
                //TODO I skipped Paas part for now, get back to it later
//                for (Resource res : bi.getType().getResources()) {
//                    ConfigValet valet = ConfigValet.createValet(bi, res);
//                    if (valet != null)
//                        valet.config();
//                    else if(res.hasProperty("db-binding-alias")){
//                        coordinator.updateStatus(bi.getProvidedEnd().getOwner().get().getName(), ComponentInstance.State.PENDING.toString(), ActivityDiagram.class.getName());
//                        try{
//                            Provider p = ((ExternalComponent) bi.getProvidedEnd().getOwner().get().getType()).getProvider();
//                            PaaSConnector connector = ConnectorFactory.createPaaSConnector(p);
//                            String alias = res.getProperties().valueOf("db-binding-alias");
//                            connector.bindDbToApp(bi.getRequiredEnd().getOwner().getName(), bi.getProvidedEnd().getOwner().getName(), alias);
//                            coordinator.updateStatus(bi.getProvidedEnd().getOwner().get().getName(), ComponentInstance.State.RUNNING.toString(), ActivityDiagram.class.getName());
//                        }catch(Exception ex){
//                            ex.printStackTrace();
//                            journal.log(Level.INFO, ">> db-binding only works for PaaS databases" );
//                        }
//                    }
//                }
                ComponentInstance clienti = bi.getRequiredEnd().getOwner().get();
                Component client = clienti.getType();
                ComponentInstance pltfi = getDestination(clienti);
                if (pltfi.isExternal()) {
                    ExternalComponent pltf = (ExternalComponent) pltfi.getType();
                    if (!pltf.isVM()) {
                        //TODO get back to Paas later
//                        if(client.hasProperty("temp-warfile")) {
//                            try {
//                                PaaSConnector connector = (PaaSConnector) ConnectorFactory.createPaaSConnector(pltf.getProvider());
//                                connector.uploadWar(client.getProperties().valueOf("temp-warfile"), "db-reconfig", clienti.getName(), pltfi.getName(), 600);
//                                coordinator.updateStatusInternalComponent(clienti.getName(), State.RUNNING.toString(), CloudAppDeployer.class.getName());
//                            } catch (NullPointerException e) {
//                                journal.log(Level.INFO, ">> no temp-warfile specified, no re-deploy");
//                            }
//                        }
                    } else {
                        journal.log(Level.INFO, ">> Connection IaaS to PaaS ...");
                        RequiredPortInstance clientInternal = bi.getRequiredEnd();
                        ProvidedPortInstance server = bi.getProvidedEnd();

                        Resource clientResource = bi.getType().getClientResource();

                        Connector jcClient;
                        VMInstance ownerVMClient = (VMInstance) getDestination(clientInternal.getOwner().get());
                        VM VMClient = ownerVMClient.getType();
                        jcClient = ConnectorFactory.createIaaSConnector(VMClient.getProvider());

//                        String destinationIpAddress = getDestination(server.getOwner().get()).getPublicAddress();
                        int destinationPortNumber = server.getType().getPortNumber();
                        String destinationVM = ((VMInstance) getDestination(server.getOwner().get())).getName();

//                        String ipAddress = getDestination(clientInternal.getOwner().get()).getPublicAddress();
                        if (clientResource == null)
                            return; // ignore configuration if there is no resource at all

                        Action retrieve = null;
                        if (clientResource.getRetrieveCommand() != null && !clientResource.getRetrieveCommand().equals("")) {
                            ActivityNode last = getInstallAction(clienti.getName());
                            // :: sign inside command string will indicate that we have to retrieve real IPs during execution inside ActionExecutable.java
                            String retrieveCommand = clientResource.getRetrieveCommand() + "::" + destinationPortNumber + "::" + destinationVM;
                            //finally create retrieve action
                            retrieve = getConfigureAction(clientResource, jcClient, ownerVMClient, VMClient, last, retrieveCommand, clientInternal.getOwner().get().getName());
                            connectionActions.add(retrieve);

//                            String retrieveCommand = (clientResource.getRetrieveCommand() + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber);
//                            jcClient.execCommand(ownerVMClient.getId(), clientResource.getRetrieveCommand() + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber, "ubuntu", VMClient.getPrivateKey());
                        }

                        Action install = null;
                        if (clientResource.getInstallCommand() != null && !clientResource.getInstallCommand().equals("")) {
                            ActivityNode last = getLastEdge(clientInternal.getOwner().get().getName(), retrieve, null);
                            String installationCommand = clientResource.getInstallCommand() + "::" + destinationPortNumber + "::" + destinationVM;
                            install = getConfigureAction(clientResource, jcClient, ownerVMClient, VMClient, last, installationCommand, clientInternal.getOwner().get().getName());
                            connectionActions.add(install);
//                            String installationCommand = clientResource.getInstallCommand() + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber;
//                            configure(jcClient, VMClient, ownerVMClient, installationCommand, clientResource.getRequireCredentials(), true);
                        }

                        Action configure = null;
                        if (clientResource.getConfigureCommand() != null && !clientResource.getConfigureCommand().equals("")) {
                            ActivityNode last = getLastEdge(clientInternal.getOwner().get().getName(), install, retrieve);
                            String configurationCommand = clientResource.getConfigureCommand() + "::" + destinationPortNumber + "::" + destinationVM;
                            configure = getConfigureAction(clientResource, jcClient, ownerVMClient, VMClient, last, configurationCommand, clientInternal.getOwner().get().getName());
                            connectionActions.add(configure);
//                            String configurationCommand = clientResource.getConfigureCommand() + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber;
//                            configure(jcClient, VMClient, ownerVMClient, configurationCommand, clientResource.getRequireCredentials(), true);
                        }

//                        jcClient.closeConnection();
                    }
                }

            } else if (bi.getRequiredEnd().getType().isRemote()) {
                RequiredPortInstance client = bi.getRequiredEnd();
                ProvidedPortInstance server = bi.getProvidedEnd();

                Resource clientResource = bi.getType().getClientResource();
                Resource serverResource = bi.getType().getServerResource();
                this.bi=bi;
                retrieveIPandConfigure(serverResource,clientResource,server,client, connectionActions);
            }
//            if(isPaaS2PaaS(bi)) {
//                ComponentInstance clienti = bi.getRequiredEnd().getOwner().get();
//                ComponentInstance s=bi.getProvidedEnd().getOwner().get().asInternal();
//                ExternalComponentInstance serveri = bi.getProvidedEnd().getOwner().get().asInternal().externalHost();
//                ExternalComponent pltf = clienti.asInternal().externalHost().getType();
//                PaaSConnector connector = (PaaSConnector) ConnectorFactory.createPaaSConnector(pltf.getProvider());
//                connector.setEnvVar(clienti.getName(), s.getName(), serveri.getPublicAddress());
//            }

        }
        Fork addressesFork = ActivityBuilder.forkNode(true);
        ActivityBuilder.connect(publicAddresses, addressesFork, false);
        for (Action act:connectionActions)
            ActivityBuilder.connect(addressesFork, act, false);
    }

    // returns action with outgoing control flow
    private Action getConfigureAction(Resource resource, Connector jcConnector, VMInstance ownerVMInstance, VM VM, ActivityNode incomingControl, String retrieveCommand, String componentName) throws Exception {
        Action retrieve;
        String connectionCommand = "";
        if (retrieveCommand.split("::").length == 4){
            connectionCommand = ":" + retrieveCommand.split("::")[3];
            retrieveCommand = retrieveCommand.replaceFirst("::" + connectionCommand, "");
        }
        retrieve = ActivityBuilder.actionNode("configure" + connectionCommand, ownerVMInstance, VM, jcConnector, retrieveCommand, componentName,
                resource.getRequireCredentials());
        ActivityBuilder.connect(incomingControl, retrieve, true);
        return retrieve;
    }

    // order of configuration commands are retrieve, install, configure. When we at configure we have to check both install and retrieve if they equal null
    private ActivityNode getLastEdge(String componentName, Action previousAction, Action twoBefore) throws Exception {
        ActivityNode last = null;
        if (previousAction == null && twoBefore == null) {
            last = getInstallAction(componentName);
        } else {
            if (previousAction == null){
                last = twoBefore;
            } else {
                last = previousAction;
            }
        }
        return last;
    }

    private Action getInstallAction(String componentName) throws Exception {
        // get install command that correspond to this connection instance. For example, if require port is nimbusRequired
        // then we need to get action "executeInstallCommand" on nimbus VM
        Action install = null;
        for (Action action : ActivityBuilder.getActions()) {
            if (action.getName().equals("executeInstallCommand") && ((InternalComponentInstance) action.getInputs().get(1)).getName().equals(componentName))
                install = action;
        }
        return install;
    }

    private Boolean isPaaS2PaaS(RelationshipInstance ri){
        if(bi.getRequiredEnd().getOwner().get().isInternal()){
            if(bi.getProvidedEnd().getOwner().get().isInternal()){
                if(!bi.getRequiredEnd().getOwner().get().asInternal().externalHost().isVM()
                        && !bi.getProvidedEnd().getOwner().get().asInternal().externalHost().isVM()){
                    return true;
                }
            }
        }
        return false;
    }

    private RelationshipInstance bi = null;

    public void retrieveIPandConfigure(Resource serverResource, Resource clientResource, PortInstance<? extends Port> server, PortInstance<? extends Port> client, ArrayList<Action> connectionActions) throws Exception {
        int destinationPortNumber = server.getType().getPortNumber();
        String destinationVM = ((VMInstance) getDestination(server.getOwner().get())).getName();
        if(clientResource == null && serverResource == null)
            return; // ignore configuration if there is no resource at all
        configureWithIP(serverResource, clientResource, server, client, destinationVM, destinationPortNumber, connectionActions);
    }

    private void configureWithIP(Resource server, Resource client,
                                 PortInstance<? extends Port> pserver, PortInstance<? extends Port> pclient, String destinationVM, int destinationPortNumber, ArrayList<Action> connectionActions) throws Exception {
        VMInstance ownerVMServer = (VMInstance) getDestination(pserver.getOwner().get());//TODO:generalization for PaaS
        String serverComponentName = pserver.getOwner().get().getName();
        VM VMserver = ownerVMServer.getType();
        VMInstance ownerVMClient = (VMInstance) getDestination(pclient.getOwner().get());//TODO:generalization for PaaS
        String clientComponentName = pclient.getOwner().get().getName();
        VM VMClient = ownerVMClient.getType();

        // join from client and server before we start any configuration actions
        Join joinBeforeConnections = null;

        //TODO - check what happens when components are removed
        // this code snippet is related to diff. Currently - works for cases when new components are added
        boolean drawServerConnection = true;
        for (ComponentInstance component:alreadyStarted){
            if (component.getName().equals(serverComponentName)){
                drawServerConnection = false;
                break;
            }
        }

        Action clientInstall = getInstallAction(clientComponentName);

        if (drawServerConnection) {
            joinBeforeConnections = ActivityBuilder.joinNode(false);
            Action serverInstall = getInstallAction(serverComponentName);
            ActivityBuilder.connect(clientInstall, joinBeforeConnections, true);
            ActivityBuilder.connect(serverInstall, joinBeforeConnections, true);
        }

        // if we have resources from client and server, we create another fork for that, otherwise we use outgoing edge from previous join for connection commands
        //TODO never had connections with two resources, maybe this won't work properly, did not test
        Fork forkClientServer = null;
        if (server != null && client != null){
            forkClientServer = ActivityBuilder.forkNode(false);
            ActivityBuilder.connect(joinBeforeConnections, forkClientServer, true);
        }

        // this code snippet is related to diff: "toFirstConfigureAction". Before that joinBeforeConnections.getOutgoing().get(0) was used
        ActivityNode toFirstConfigureAction = joinBeforeConnections == null ? clientInstall : joinBeforeConnections;
        Action retrieveServer = null;
        if(server != null){
            if(server.getRetrieveCommand() != null && !server.getRetrieveCommand().equals("")){
                ActivityNode last = forkClientServer == null ? toFirstConfigureAction : forkClientServer;
                String retrieveCommand = server.getRetrieveCommand() + "::" + destinationPortNumber + "::" + destinationVM + "::connectionRetrieve";
                retrieveServer = getConfigureAction(server, null, ownerVMServer, VMserver, last, retrieveCommand, serverComponentName);
                retrieveServer.getProperties().put("oppositeConnectionEnd", clientComponentName);
                connectionActions.add(retrieveServer);
            }
        }

        Action retrieveClient = null;
        if(client !=null){
            if(client.getRetrieveCommand() != null && !client.getRetrieveCommand().equals("")) {
                ActivityNode last = forkClientServer == null ? toFirstConfigureAction : forkClientServer;
                String retrieveCommand = client.getRetrieveCommand() + "::" + destinationPortNumber + "::" + destinationVM + "::connectionRetrieve";
                retrieveClient = getConfigureAction(client, null, ownerVMClient, VMClient, last, retrieveCommand, clientComponentName);
                retrieveClient.getProperties().put("oppositeConnectionEnd", serverComponentName);
                connectionActions.add(retrieveClient);
            }
        }

        Action installServer = null;
        if(server != null){
            if(server.getInstallCommand() != null && !server.getInstallCommand().equals("")){
                ActivityNode last = getLastEdge(serverComponentName, retrieveServer, null);
                String installationCommand = server.getInstallCommand() + "::" + destinationPortNumber + "::" + destinationVM + "::connectionInstall";
                installServer = getConfigureAction(server, null, ownerVMServer, VMserver, last, installationCommand, serverComponentName);
                installServer.getProperties().put("oppositeConnectionEnd", clientComponentName);
                connectionActions.add(installServer);
            }
        }

        Action installClient = null;
        if(client != null){
            if(client.getInstallCommand() != null && !client.getInstallCommand().equals("")){
                ActivityNode last = getLastEdge(clientComponentName, retrieveClient, null);
                String installationCommand = client.getInstallCommand() + "::" + destinationPortNumber + "::" + destinationVM + "::connectionInstall";
                installClient = getConfigureAction(client, null, ownerVMClient, VMClient, last, installationCommand, clientComponentName);
                installClient.getProperties().put("oppositeConnectionEnd", serverComponentName);
                connectionActions.add(installClient);
            }
        }

        Action configureServer = null;
        if(server != null){
            if(server.getConfigureCommand() != null && !server.getConfigureCommand().equals("")){
                ActivityNode last = getLastEdge(serverComponentName, installServer, retrieveServer);
                String configurationCommand = server.getConfigureCommand() + "::" + destinationPortNumber + "::" + destinationVM + "::connectionConfigure";
                configureServer = getConfigureAction(server, null, ownerVMServer, VMserver, last, configurationCommand, serverComponentName);
                configureServer.getProperties().put("oppositeConnectionEnd", clientComponentName);
                connectionActions.add(configureServer);
            }
        }

        Action configureClient = null;
        if(client != null){
            if(client.getConfigureCommand() != null && !client.getConfigureCommand().equals("")){
                ActivityNode last = getLastEdge(clientComponentName, installClient, retrieveClient);
                String configurationCommand = client.getConfigureCommand() + "::" + destinationPortNumber + "::" + destinationVM + "::connectionConfigure";
                configureClient = getConfigureAction(client, null, ownerVMClient, VMClient, last, configurationCommand, clientComponentName);
                configureClient.getProperties().put("oppositeConnectionEnd", serverComponentName);
                connectionActions.add(configureClient);
            }
        }
    }

    /**
     * Configuration with parameters IP, IPDest, PortDest
     *
     * @param r                     resources for configuration
     * @param i                     port of the component to be CONFIGURED
     * @param destinationIpAddress  IP of the server
     * @param ipAddress             IP of the client
     * @param destinationPortNumber port of the server
     * @throws java.net.MalformedURLException
     */
//    private void configureWithIP(Resource r, PortInstance<? extends Port> i, String destinationIpAddress, String ipAddress, int destinationPortNumber) {
//        if(DEBUG){
//            journal.log(Level.INFO, ">> Configure with IP ");
//            return;
//        }
//        Connector jc;
//        if (r != null) {
//            VMInstance ownerVM = (VMInstance) getDestination(i.getOwner().get());//TODO:generalization for PaaS
//            VM n = ownerVM.getType();
//            jc = ConnectorFactory.createIaaSConnector(n.getProvider());
//            //jc=new JCloudsConnector(n.getProvider().getName(), n.getProvider().getLogin(), n.getProvider().getPasswd());
//            jc.execCommand(ownerVM.getId(), r.getRetrieveCommand(), "ubuntu", n.getPrivateKey());
//            if (r.getConfigureCommand() != null) {
//                String configurationCommand = r.getConfigureCommand() + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber;
//                configure(jc, n, ownerVM, configurationCommand, r.getRequireCredentials(), true);
//            }
//            if (r.getInstallCommand() != null) {
//                String installationCommand = r.getInstallCommand() + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber;
//                configure(jc, n, ownerVM, installationCommand, r.getRequireCredentials(), true);
//            }
//            jc.closeConnection();
//        }
//    }

    /**
     * Terminates a set of VMs
     *
     * @param vms A list of vmInstances
     * @throws java.net.MalformedURLException
     */
    private void terminateExternalServices(Map<ExternalComponentInstance<? extends ExternalComponent>,List<InternalComponentInstance>> vms) throws Exception {
        List<VMInstance> VMs = new ArrayList<VMInstance>();

        for (ExternalComponentInstance n : vms.keySet()) {
            if (n instanceof VMInstance) {
                VMs.add((VMInstance) n);
            } else{
//                PaaSConnector pc = ConnectorFactory.createPaaSConnector(n.getType().asExternal().getProvider());
//                for(InternalComponentInstance c: vms.get(n)){
//                    journal.log(Level.INFO, ">> Terminating app "+c.asInternal().getName());
//                    pc.deleteApp(c.asInternal().getName());
//                }
//                journal.log(Level.INFO, ">> Terminated!");
            }
        }

        if (VMs.size() > 1){
            Fork forkVMs = ActivityBuilder.forkNode(false);
            connectRemoveToPlan(forkVMs);

            ArrayList<Action> actions = new ArrayList<Action>();
            for (VMInstance a : VMs) {
//                int componentIndex = VMs.indexOf(a);
                Action stop = ActivityBuilder.actionNode("terminateVM", a);
                ActivityBuilder.connect(forkVMs, stop, true);
                actions.add(stop);
            }

            Join joinVMs = ActivityBuilder.joinNode(false);
            for (Action act:actions)
                ActivityBuilder.connect(act, joinVMs, true);
//            ActivityBuilder.connectActionsWithJoinNodes(actions, joinVMs, null);
        } else if (!VMs.isEmpty()) {
            Action stop = ActivityBuilder.actionNode("terminateVM", VMs.get(0));
            connectRemoveToPlan(stop);
//            ActivityEdge out = new ActivityEdge();
//            stop.addEdge(out, ActivityNode.Direction.OUT);
//            ActivityBuilder.getActivity().addEdge(out);
        }

    }

    /**
     * Terminate a VM
     *
     * @param n A VM instance to be terminated
     * @throws java.net.MalformedURLException
     */
    private void terminateVM(VMInstance n, boolean debugMode) {
        if (debugMode){
            journal.log(Level.INFO, ">> Stop vm " + n.getName());
        } else {
            Provider p = n.getType().getProvider();
            Connector jc = ConnectorFactory.createIaaSConnector(p);
            jc.destroyVM(n.getId());
            jc.closeConnection();
            coordinator.updateStatus(n.getName(), ComponentInstance.State.STOPPED.toString(), ActivityDiagram.class.getName());
            //old way without using mrt
            //n.setStatusAsStopped();
        }
    }

    /**
     * Stop a list of component
     *
     * @param components a list of ComponentInstance
     * @throws java.net.MalformedURLException
     */
    private void stopInternalComponents(List<InternalComponentInstance> components) throws Exception {//TODO: List<InternalComponentInstances>
        if (components.size() > 1) {
            Fork forkComponents = ActivityBuilder.forkNode(false);
            connectRemoveToPlan(forkComponents);

            ArrayList<Action> actions = new ArrayList<Action>();
            for (InternalComponentInstance a : components) {
                int componentIndex = components.indexOf(a);
                Action stop = ActivityBuilder.actionNode("stopInternalComponent", a);
                ActivityBuilder.connect(forkComponents, stop, true);
                actions.add(stop);
            }

            Join joinRelationships = ActivityBuilder.joinNode(false);
            for (Action act:actions)
                ActivityBuilder.connect(act, joinRelationships, true);
//            ActivityBuilder.connectActionsWithJoinNodes(actions,joinRelationships, null);
        } else if (!components.isEmpty()){
            Action stop = ActivityBuilder.actionNode("stopInternalComponent", components.get(0));
            connectRemoveToPlan(stop);
//            ActivityEdge out = new ActivityEdge();
//            stop.addEdge(out, ActivityNode.Direction.OUT);
//            ActivityBuilder.getActivity().addEdge(out);
        }
    }

    /**
     * Stop a specific component instance
     *
     * @param a An InternalComponent Instance
     * @throws java.net.MalformedURLException
     */
    public void stopInternalComponent(InternalComponentInstance a, boolean debugMode) {
        if (debugMode){
            journal.log(Level.INFO, ">> Stop component " + a.getName());
        } else {
            VMInstance ownerVM = (VMInstance) findDestinationWhenNoRequiredExecutionPlatformSpecified(a); //TODO: to be generalized
            if (ownerVM != null) {
                VM n = ownerVM.getType();
                Connector jc = ConnectorFactory.createIaaSConnector(n.getProvider());

                for (Resource r : a.getType().getResources()) {
                    String stopCommand = r.getStopCommand();
                    jc.execCommand(ownerVM.getId(), stopCommand, "ubuntu", n.getPrivateKey());
                }

                jc.closeConnection();
                coordinator.updateStatusInternalComponent(a.getName(), State.UNINSTALLED.toString(), ActivityDiagram.class.getName());
                //a.setStatus(State.CONFIGURED);
            }
        }
    }

    /**
     * After the deletion of a relationships the configuration parameters
     * specific to this relationships are removed
     *
     * @param relationships list of relationships removed
     */
    private void unconfigureRelationships(List<RelationshipInstance> relationships) throws Exception {
        ArrayList<ActivityNode> fromUnconfigure = new ArrayList<>();

        if (relationships.size() > 1) {
            Fork forkRelationships = ActivityBuilder.forkNode(false);
            connectRemoveToPlan(forkRelationships);

            for (RelationshipInstance b : relationships) {
//                int edgeIndex = relationships.indexOf(b);
                unconfigureRelationship(b, forkRelationships, fromUnconfigure);
            }

            Join joinRelationships = ActivityBuilder.joinNode(false);
//            ActivityBuilder.getActivity().getEdges().removeAll(joinRelationships.getIncoming());
//            joinRelationships.getIncoming().clear();
            for (ActivityNode node:fromUnconfigure)
                ActivityBuilder.connect(node, joinRelationships, true);
        } else if (!relationships.isEmpty()){
            //TODO this fake node is bad design, refactor later. I added it during the refactoring of ActivityBuilder to comply with unconfigureRelationship method signature
            ActivityNode fake = ActivityBuilder.actionNode("Fake node");
            unconfigureRelationship(relationships.get(0), fake, fromUnconfigure);
            if (fromUnconfigure.get(0) != fake) {
                ActivityNode singleAction = fake.getOutgoing().get(0).getTarget();
                connectRemoveToPlan(singleAction);
            }
            ActivityBuilder.deleteNode(fake);
        }

    }

    private void unconfigureRelationship(RelationshipInstance b, ActivityNode incomingNode, ArrayList<ActivityNode> outgoingNodes) throws Exception {
        if (!b.getRequiredEnd().getType().isLocal()) {
            RequiredPortInstance client = b.getRequiredEnd();
            ProvidedPortInstance server = b.getProvidedEnd();

            Resource clientResource = b.getType().getClientResource();
            Resource serverResource = b.getType().getServerResource();

            if (clientResource == null && serverResource == null) {
                outgoingNodes.add(incomingNode);
                return;
            } else if (clientResource != null && serverResource != null) {
                Fork forkResources = (Fork) ActivityBuilder.forkNode(false);
                ActivityBuilder.connect(incomingNode, forkResources, true);
//                ActivityBuilder.getActivity().removeEdge(forkResources.getIncoming().get(0));
//                forkResources.getIncoming().clear();
//                forkResources.addEdge(incomingNode, ActivityNode.Direction.IN);
//                ActivityBuilder.getActivity().addEdge(incomingNode);

                Action clientAction = ActivityBuilder.actionNode("unconfigureWithIP", clientResource, client);
//                clientAction.addInput(client);
                ActivityBuilder.connect(forkResources, clientAction, true);

                Action serverAction = ActivityBuilder.actionNode("unconfigureWithIP", serverResource, server);
//                serverAction.addInput(server);
                ActivityBuilder.connect(forkResources, serverAction, true);

                Join joinResources = ActivityBuilder.joinNode(false);
                ActivityBuilder.connect(clientAction, joinResources, true);
                ActivityBuilder.connect(serverAction, joinResources, true);
//                clientAction.addEdge(joinResources.getIncoming().get(0), ActivityNode.Direction.OUT);
//                serverAction.addEdge(joinResources.getIncoming().get(1), ActivityNode.Direction.OUT);

                outgoingNodes.add(joinResources);
            } else {
                //client resources
                if (clientResource != null) {
                    Action clientAction = ActivityBuilder.actionNode("unconfigureWithIP", clientResource, client);
                    ActivityBuilder.connect(incomingNode, clientAction, true);
                    outgoingNodes.add(clientAction);
                }

                //server resources
                if (serverResource != null) {
                    Action serverAction = ActivityBuilder.actionNode("unconfigureWithIP", serverResource, server);
                    ActivityBuilder.connect(incomingNode, serverAction, true);
                    outgoingNodes.add(serverAction);
                }
            }
        }
    }

    public void unconfigureWithIP(Resource r, PortInstance<? extends Port> i, boolean debugMode) {
        if (debugMode){
            journal.log(Level.INFO, ">> Unconfigure with IP " + i.getOwner().get().getName());
        } else {
            Connector jc;
            if (r != null) {
                VMInstance ownerVM = (VMInstance) getDestination(i.getOwner().get()); //TODO: generalize to PaaS
                VM n = ownerVM.getType();
                jc = ConnectorFactory.createIaaSConnector(n.getProvider());
                //jc=new JCloudsConnector(n.getProvider().getName(), n.getProvider().getLogin(), n.getProvider().getPasswd());
                jc.execCommand(ownerVM.getId(), r.getStopCommand(), "ubuntu", n.getPrivateKey());
                ;
                jc.closeConnection();
            }
        }
    }

    /**
     * To initialise a deployment Model as the model of the current system if
     * the system is already RUNNING
     *
     * @param current the current Deployment model
     */
    public void setCurrentModel(Deployment current) {
        this.currentModel = current;
        Connector jc;
        for (VMInstance vm : currentModel.getComponentInstances().onlyVMs()) {
            if (vm.getPublicAddress().equals("")) {
                jc = ConnectorFactory.createIaaSConnector(vm.getType().getProvider());
                jc.updateVMMetadata(vm);
            }
        }
    }

    public void reset(){
        ActivityBuilder.setActivity(null);
        alreadyDeployed = new ComponentInstanceGroup<ComponentInstance<? extends Component>>();
        alreadyStarted = new ComponentInstanceGroup<ComponentInstance<? extends Component>>();
        setCurrentModel(null);
    }

    /**
     * Find the destination of an ComponentInstance
     *
     * @param a an instance of component
     * @return a VMInstance
     */
    private ExternalComponentInstance findDestinationWhenNoRequiredExecutionPlatformSpecified(InternalComponentInstance a) {
        if (getDestination(a) != null) {
            return getDestination(a);
        } else {
            for (RelationshipInstance b : currentModel.getRelationshipInstances()) {
                if (a.getRequiredPorts().contains(b.getRequiredEnd()) && b.getRequiredEnd().getType().isLocal()) {
                    return getDestination(b.getProvidedEnd().getOwner().get());
                }
                if (a.getProvidedPorts().contains(b.getProvidedEnd()) && b.getProvidedEnd().getType().isLocal()) {
                    return getDestination(b.getRequiredEnd().getOwner().get());
                }
            }
            return null;
        }
    }

    public void scaleOut(VMInstance vmi){
        Scaler scaler=new Scaler(currentModel,coordinator,new CloudAppDeployer());
        scaler.scaleOut(vmi);
    }

    public void scaleOut(VMInstance vmi, int nb){
        Scaler scaler=new Scaler(currentModel,coordinator,new CloudAppDeployer());
        scaler.scaleOut(vmi,nb);
    }

    public void scaleOut(VMInstance vmi,Provider provider){
        Scaler scaler=new Scaler(currentModel,coordinator,new CloudAppDeployer());
        scaler.scaleOut(vmi,provider);
    }

    public Deployment scaleOut(ExternalComponentInstance eci,Provider provider){
        Scaler scaler=new Scaler(currentModel,coordinator,new CloudAppDeployer());
        return scaler.scaleOut(eci,provider);
    }

    public void activeDebug(){
        DEBUG=true;
    }

    public void stopDebug(){
        DEBUG=false;
    }

}
