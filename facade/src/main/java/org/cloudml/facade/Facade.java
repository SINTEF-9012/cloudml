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
package org.cloudml.facade;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.cloudml.codecs.DrawnIconVertexDemo;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.codecs.Vertex;
import org.cloudml.codecs.XmiCodec;
import org.cloudml.codecs.commons.Codec;
import org.cloudml.connectors.Connector;
import org.cloudml.connectors.ConnectorFactory;
import org.cloudml.core.*;
import org.cloudml.deployer.CloudAppDeployer;
import org.cloudml.connectors.JCloudsConnector;
import org.cloudml.core.credentials.Credentials;
import org.cloudml.core.samples.SensApp;
import org.cloudml.core.validation.DeploymentValidator;
import org.cloudml.core.validation.Report;
import org.cloudml.deployer.CloudMLModelComparator;
import org.cloudml.facade.commands.*;
import org.cloudml.facade.events.*;
import org.cloudml.facade.events.Message.Category;
import org.cloudml.indicators.Robustness;
import org.cloudml.mrt.Coordinator;
import org.cloudml.mrt.SimpleModelRepo;
import org.jclouds.compute.domain.ComputeMetadata;

/**
 * This class implements an easier access to typical CloudML features, such as
 * deploying an application described in a JSON file for instance.
 *
 * It holds a list of clients and may send events to them. The facade
 * communicate with its clients using events.
 */
class Facade implements CloudML, CommandHandler {

    private static final Logger journal = Logger.getLogger(Facade.class.getName());

    private final List<EventHandler> handlers = Collections.synchronizedList(new ArrayList<EventHandler>());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Deployment deploy;
    private boolean stopOnTimeout = false;
    private final CloudAppDeployer deployer;
    private CloudMLModelComparator diff = null;
    private Coordinator coordinator;

    private static final String JSON_STRING_PREFIX = "json-string:";

    public Facade() {
        XmiCodec.init();
        JsonCodec.init();
        this.deployer = new CloudAppDeployer();
    }

    @Override
    public void terminate() {
        executor.shutdown();
        try {
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(Facade.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            executor.shutdownNow();
        }
    }

    @Override
    public Execution fireAndForget(CloudMlCommand command) {
        final Execution execution = new Execution(command, this);
        execution.setCoordinator(coordinator);
        executor.submit(execution);
        return execution;
    }

    @Override
    public Execution fireAndWait(CloudMlCommand command) {
        final Execution execution = new Execution(command, this);
        execution.setCoordinator(coordinator);
        final Future<Boolean> done = executor.submit(execution, true);
        try {
            if (execution.getTimeout() > -1) {
                dispatch(new Message(command, Category.INFORMATION, "Will wait (max) for " + execution.getTimeout() + " ms..."));
                done.get(execution.getTimeout(), TimeUnit.MILLISECONDS);
                dispatch(new Message(command, Category.INFORMATION, "OK!"));

            } else {
                dispatch(new Message(command, Category.INFORMATION, "Will wait until completion"));
                done.get();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Facade.class.getName()).log(Level.SEVERE, null, ex);

        } catch (ExecutionException ex) {
            Logger.getLogger(Facade.class.getName()).log(Level.SEVERE, null, ex);

        } catch (TimeoutException ex) {
            //Logger.getLogger(Facade.class.getName()).log(Level.SEVERE, null, ex);
            if (stopOnTimeout) {
                terminate();
                final String text = "Sequence interrupted because of timeout on last command: " + command;
                final Message failure = new Message(command, Category.ERROR, text);
                dispatch(failure);
            } else {
                final String text = "Timeout on command: " + command;
                final Message message = new Message(command, Category.INFORMATION, text);
                dispatch(message);
            }
        }
        return execution;
    }

    @Override
    public void register(EventHandler handler) {
        this.handlers.add(handler);
    }

    /**
     * Dispatch the given event to those which have registered a handler for
     * this type of event
     *
     * @param event the event to dispatch
     */
    private void dispatch(Event event) {
        for (EventHandler handler: handlers) {
            event.accept(handler);
        }
    }

    ////////LEGACY CODE TO BE MIGRATED INTO NEW COMMANDS/////////////////////    
    /**
     * Create the VM described by the given VMInstance object
     *
     * @param a
     */
    public void createVM(VMInstance a) {
        Provider provider = a.getType().getProvider();
        JCloudsConnector jc = new JCloudsConnector(provider.getName(), provider.getCredentials().getLogin(),
                provider.getCredentials().getPassword());
        jc.createInstance(a);
        jc.closeConnection();
    }

    /**
     * Execute a given command on an component
     *
     * @param a the component on which the command will be executed
     * @param command the related shell command as a String
     * @param user the user associated
     */
    public void executeOnVM(ComponentInstance a, String command, String user) {//TODO: use the connector factory
        VM ownerVM = (VM) deployer.getDestination(a).getType();//TODO: generics
        Provider provider = ownerVM.getProvider();
        final Credentials credentials = provider.getCredentials();
        JCloudsConnector jc = new JCloudsConnector(provider.getName(), credentials.getLogin(),
                credentials.getPassword());
        jc.execCommand(ownerVM.getGroupName(), command, user,
                ownerVM.getPrivateKey());

        jc.closeConnection();
    }

    public ComputeMetadata findVMByName(String name, Provider p) {//TODO: use the connector factory
        JCloudsConnector jc = new JCloudsConnector(p.getName(), p.getCredentials().getLogin(),
                p.getCredentials().getPassword());
        ComputeMetadata cm = jc.getVMByName(name);
        jc.closeConnection();
        return cm;
    }

    public Set<? extends ComputeMetadata> listOfVMs(Provider p) {//TODO: use the connector factory
        JCloudsConnector jc = new JCloudsConnector(p.getName(), p.getCredentials().getLogin(),
                p.getCredentials().getPassword());
        Set<? extends ComputeMetadata> list = jc.listOfVMs();
        jc.closeConnection();
        return list;
    }
    ////////END OF LEGACY CODE TO BE MIGRATED INTO NEW COMMANDS/////////////////////

    @Override
    public Deployment getDeploymentModel() {
        return deploy;
    }

    /*
     * COMMAND HANDLERS
     */
    @Override
    public void handle(AnalyseRobustness command) {
        if (isDeploymentLoaded()) {
            robustness(command);
            robustnessWithSelfRepair(command);

        } else {
            reportNoDeploymentLoaded(command);

        }
    }

    private void robustnessWithSelfRepair(AnalyseRobustness command) {
        final Robustness robustness = Robustness.ofSelfRepairing(deploy, command.getToObserve(), command.getToControl());
        final String content = String.format("Robustness (with self-repair): %.2f percent", robustness.value());
        dispatch(new Message(command, Category.INFORMATION, content));
    }

    private void robustness(AnalyseRobustness command) {
        final Robustness robustness = Robustness.of(deploy, command.getToObserve(), command.getToControl());
        final String content = String.format("Robustness: %.2f percent", robustness.value());
        dispatch(new Message(command, Category.INFORMATION, content));
    }

    @Override
    public void handle(StartComponent command) {
        if (isDeploymentLoaded()) {
            dispatch(new Message(command, Category.INFORMATION, "Starting VM: " + command.getComponentId()));
            VMInstance vmi = deploy.getComponentInstances().onlyVMs().withID(command.getComponentId());
            if(vmi != null){
                Provider provider = vmi.getType().getProvider();
                Connector c=ConnectorFactory.createIaaSConnector(provider);
                c.startVM(vmi);
            }
        }
    }

    /**
     * Report to the user that the given command is not yet supported.
     *
     * @param command the command which is not yet supported!
     */
    private void reportCommandNotYetSupported(CloudMlCommand command) {
        dispatch(new Message(command, Category.ERROR, "Not yet implemented"));
    }

    /**
     * Report to the client that the given command cannot be performed because
     * there is not yet any deployment model loaded.
     *
     * @param command the command that triggered this message
     */
    private void reportNoDeploymentLoaded(CloudMlCommand command) {
        final String text = "No deployment model. Please first load a deployment model";
        final Message message = new Message(command, Category.ERROR, text);
        dispatch(message);
    }

    @Override
    public void handle(StopComponent command) {
        if (isDeploymentLoaded()) {
            dispatch(new Message(command, Category.INFORMATION, "Stopping VM: " + command.getComponentId()));
            VMInstance vmi = deploy.getComponentInstances().onlyVMs().withID(command.getComponentId());
            if(vmi != null){
                Provider provider = vmi.getType().getProvider();
                Connector c=ConnectorFactory.createIaaSConnector(provider);
                c.stopVM(vmi);
            }
        }
    }

    @Override
    public void handle(Attach command) {
        if (isDeploymentLoaded()) {
            dispatch(new Message(command, Category.INFORMATION, "Relationship created!"));

        } else {
            reportNoDeploymentLoaded(command);
        }
    }

    @Override
    public void handle(Detach command) {
        reportCommandNotYetSupported(command);
    }

    @Override
    public void handle(Install command) {
        reportCommandNotYetSupported(command);
    }

    @Override
    public void handle(Uninstall command) {
        reportCommandNotYetSupported(command);
    }

    @Override
    public void handle(Instantiate command) {
        reportCommandNotYetSupported(command);
    }

    @Override
    public void handle(Destroy command) {
        if (isDeploymentLoaded()) {
            final VMInstance instance = deploy.getComponentInstances().onlyVMs().firstNamed(command.getInstanceId());
            if (instance == null) {
                final String text = String.format("No VM with ID=\"%s\"", command.getInstanceId());
                final Message message = new Message(command, Category.ERROR, text);
                dispatch(message);
            } else {
                Provider p = instance.getType().getProvider();
                JCloudsConnector jc = new JCloudsConnector(p.getName(), p.getCredentials().getLogin(), p.getCredentials().getPassword());
                jc.destroyVM(instance.getId());
                dispatch(new Message(command, Category.INFORMATION, "VM instance terminated"));
            }

        } else {
            reportNoDeploymentLoaded(command);

        }
    }

    @Override
    public void handle(Upload command) {
        if (isDeploymentLoaded()) {
            ExternalComponentInstance ownerVM = null;
            for (ExternalComponentInstance ni: deploy.getComponentInstances().onlyExternals()) {
                if (ni.getName().equals(command.getArtifactId())) {
                    ownerVM = ni;
                }
            }
            if (ownerVM != null && ownerVM instanceof VMInstance) {
                Provider p = ((VMInstance) ownerVM).getType().getProvider();
                JCloudsConnector jc = new JCloudsConnector(p.getName(), p.getCredentials().getLogin(), p.getCredentials().getPassword());
                ComputeMetadata c = jc.getVMByName(command.getArtifactId());
                jc.uploadFile(command.getLocalPath(), command.getRemotePath(), c.getId(), "ubuntu",
                        ((VM) ownerVM.getType()).getPrivateKey());
            } else {
                final String text = "There is no VM with this ID!";
                final Message message = new Message(command, Category.ERROR, text);
                dispatch(message);
            }

        } else {
            reportNoDeploymentLoaded(command);

        }
    }

    private void saveMetadata(Deployment deploy2) {
        if (isDeploymentLoaded()) {
            diff = new CloudMLModelComparator(deploy, deploy2);
            diff.compareCloudMLModel();

            deploy.getComponents().addAll(deploy2.getComponents());
            deploy.getRelationships().addAll(deploy2.getRelationships());

            deploy.getRelationshipInstances().removeAll(diff.getRemovedRelationships());
            deploy.getExecuteInstances().removeAll(diff.getRemovedExecutes());
            deploy.getComponentInstances().removeAll(diff.getRemovedECs());
            deploy.getComponentInstances().removeAll(diff.getRemovedComponents());

            deploy.getRelationshipInstances().replaceAll(diff.getAddedRelationships());
            deploy.getComponentInstances().replaceAll(diff.getAddedECs());
            deploy.getExecuteInstances().replaceAll(diff.getAddedExecutes());
            deploy.getComponentInstances().replaceAll(diff.getAddedComponents());

        } else {
            deploy = deploy2;
        }
    }

    @Override
    public void handle(LoadDeployment command) {
        String path = command.getPathToModel();
        if ("sample://sensapp".equals(path.toLowerCase())) {
            deploy = SensApp.completeSensApp().build();
            final Message message = new Message(command, Category.INFORMATION, "Loading Complete.");
            dispatch(message);

        }else if (path.trim().startsWith(JSON_STRING_PREFIX)) {
            String content = path.trim().substring(JSON_STRING_PREFIX.length()).trim();
            InputStream instream = new ByteArrayInputStream(content.getBytes());
            Deployment deploy2 = (Deployment) new JsonCodec().load(instream);
            saveMetadata(deploy2);
            final Message message = new Message(command, Category.INFORMATION, "Loading Complete.");
            dispatch(message);
        } else {

            final String extension = canHandle(command.getPathToModel());

            if (extension != null) {

                final File f = new File(command.getPathToModel());
                try {
                    deploy = (Deployment) new JsonCodec().load(new FileInputStream(f));
                    final Message message = new Message(command, Category.INFORMATION, "Loading Complete.");
                    dispatch(message);
                } catch (FileNotFoundException ex) {
                    final Message message = new Message(command, Category.ERROR, "Unable to find file: " + command.getPathToModel());
                    dispatch(message);
                } catch (Exception e) {
                    final Message message = new Message(command, Category.ERROR, "Error while loading model: " + e.getLocalizedMessage());
                    dispatch(message);
                }
            } else {
                wrongFileFormat(command.getPathToModel(), command);
            }
        }
        initCoordinator();
    }

    private void initCoordinator(){
        if (coordinator == null) {
            if(Coordinator.SINGLE_INSTANCE == null){
                //only if there is no WebSocket server running.
                coordinator = new Coordinator();
                SimpleModelRepo modelRepo = new SimpleModelRepo(deploy);
                coordinator.setModelRepo(modelRepo);
                coordinator.start();
            }
            else
                coordinator = Coordinator.SINGLE_INSTANCE;
        }
        deployer.setCoordinator(coordinator);
    }

    @Override
    public void handle(StoreDeployment command) {
        final String extension = canHandle(command.getDestination());
        if (extension != null) {
            final File f = new File(command.getDestination());
            try {
                getCodec(extension).save(deploy, new FileOutputStream(f));
                final Message message = new Message(command, Category.INFORMATION, "Serialisation Complete.");
                dispatch(message);

            } catch (FileNotFoundException ex) {
                final Message message = new Message(command, Category.ERROR, "Cannot save model to specified destination.");
                dispatch(message);
            } catch (Exception e) {
                final Message message = new Message(command, Category.ERROR, "Error while saving model: " + e.getLocalizedMessage());
                dispatch(message);
            }
        } else {
            wrongFileFormat(command.getDestination(), command);
        }
    }

    @Override
    public void handle(Deploy command) {
        if (isDeploymentLoaded()) {
            if (diff != null) {
                deployer.deploy(deploy, diff);
            } else {
                deployer.deploy(deploy);
            }
            final Message success = new Message(command, Category.INFORMATION, "Deployment Complete.");
            dispatch(success);

        } else {
            reportNoDeploymentLoaded(command);
        }
    }

    public void handle(DebugMode command){
        final Message success;
        if(command.getDebug()) {
            deployer.activeDebug();
            success = new Message(command, Category.INFORMATION, "Debug mode activated.");
        }else{
            deployer.stopDebug();
            success = new Message(command, Category.INFORMATION, "Debug mode stopped.");
        }
        dispatch(success);
    }

    @Override
    public void handle(Burst command){
        dispatch(new Message(command, Category.INFORMATION, "Bursting out VM: " + command.getVmId()+" to "+ command.getProviderID()));
        VMInstance vmi = deploy.getComponentInstances().onlyVMs().withID(command.getVmId());
        Provider p=deploy.getProviders().firstNamed(command.getProviderID());
        if (vmi == null) {
            dispatch(new Message(command, Category.ERROR, "Cannot find a VM with this ID!"));
        } else {
            if(p == null){
                dispatch(new Message(command, Category.ERROR, "Cannot find a Provider with this ID!"));
            }else{
                deployer.scaleOut(vmi,p);
            }
        }
    }


    @Override
    public void handle(ListComponents command) {
        if (isDeploymentLoaded()) {
            final ComponentList data = new ComponentList(command, deploy.getComponents());
            dispatch(data);

        } else {
            reportNoDeploymentLoaded(command);
        }
    }

    @Override
    public void handle(ListComponentInstances command) {
        if (isDeploymentLoaded()) {
            final Collection<ComponentInstance<? extends Component>> instances = deploy.getComponentInstances().toList();
            final ComponentInstanceList data = new ComponentInstanceList(command, instances);
            dispatch(data);

        } else {
            reportNoDeploymentLoaded(command);

        }
    }

    @Override
    public void handle(ViewComponent command) {
        if (isDeploymentLoaded()) {
            final Component type = deploy.getComponents().firstNamed(command.getComponentId());
            if (type == null) {
                final String text = String.format("No artefact type with ID \"%s\"", command.getComponentId());
                final Message message = new Message(command, Category.ERROR, text);
                dispatch(message);

            } else {
                final ComponentData data = new ComponentData(command, type);
                dispatch(data);

            }

        } else {
            reportNoDeploymentLoaded(command);
        }
    }

    @Override
    public void handle(ViewComponentInstance command) {
        if (isDeploymentLoaded()) {
            final ComponentInstance instance = deploy.getComponentInstances().onlyInternals().firstNamed(command.getComponentId());
            if (instance == null) {
                final String text = String.format("No artefact instance with ID \"%s\"", command.getComponentId());
                final Message message = new Message(command, Category.ERROR, text);
                dispatch(message);

            } else {
                final ComponentInstanceData data = new ComponentInstanceData(command, instance);
                dispatch(data);

            }

        } else {
            reportNoDeploymentLoaded(command);
        }
    }

    @Override
    public void handle(LoadCredentials command) {
        reportCommandNotYetSupported(command);
    }

    @Override
    public void handle(StoreCredentials command) {
        reportCommandNotYetSupported(command);
    }

    @Override
    public void handle(ShotImage command) {
        if (isDeploymentLoaded()) {
            dispatch(new Message(command, Category.INFORMATION, "Generating picture ..."));
            DrawnIconVertexDemo g = new DrawnIconVertexDemo(deploy);
            ArrayList<Vertex> v = g.drawFromDeploymentModel();
            File f = new File(command.getPathToSnapshot());
            g.writeServerJPEGImage(f);

        } else {
            reportNoDeploymentLoaded(command);
        }
    }

    @Override
    public void handle(Snapshot command) {
        if (isDeploymentLoaded()) {
            dispatch(new Message(command, Category.INFORMATION, "Generating snapshot ..."));
            VMInstance vmi = deploy.getComponentInstances().onlyVMs().withID(command.getVmId());
            Connector c = ConnectorFactory.createIaaSConnector(vmi.getType().getProvider());
            c.createSnapshot(vmi);

        } else {
            reportNoDeploymentLoaded(command);

        }
    }

    @Override
    public void handle(Image command) {
        if (isDeploymentLoaded()) {
            dispatch(new Message(command, Category.INFORMATION, "Generating an image ..."));
            VMInstance vmi = deploy.getComponentInstances().onlyVMs().withID(command.getVmId());
            Connector c = ConnectorFactory.createIaaSConnector(vmi.getType().getProvider());
            c.createImage(vmi);

        } else {
            reportNoDeploymentLoaded(command);

        }
    }


    @Override
    public void handle(Reset command) {
        dispatch(new Message(command, Category.INFORMATION, "The deployment engine has been reset ..."));
        this.deploy = null;
        this.deployer.setCurrentModel(null);
    }

    @Override
    public void handle(ValidateCommand command) {
        assert command != null: "Unable to handle a 'null' validate command!";

        if (isDeploymentLoaded()) {

            final Report validation = new DeploymentValidator().validate(deploy);
            for (org.cloudml.core.validation.Message eachError: validation.getErrors()) {
                final Message error = new Message(command, Category.ERROR, eachError.toString());
                dispatch(error);
            }

            if (command.mustReportWarnings()) {
                for (org.cloudml.core.validation.Message eachWarning: validation.getWarnings()) {
                    final Message warning = new Message(command, Category.WARNING, eachWarning.toString());
                    dispatch(warning);
                }
            }

            final String  summaryText =
                    String.format("%d error(s) ; %d warning(s).",
                            validation.getErrors().size(),
                            validation.getWarnings().size());

            final Message summary = new Message(command, Category.INFORMATION, summaryText);
            dispatch(summary);


        } else {
            reportNoDeploymentLoaded(command);

        }

    }

    /**
     * @return if a deployment model has been loaded, and is available for
     * further commands, false otherwise.
     */
    private boolean isDeploymentLoaded() {
        return deploy != null;
    }

    @Override
    public void handle(ScaleOut command) {
        dispatch(new Message(command, Category.INFORMATION, "Scaling out VM: " + command.getVmId()));
        VMInstance vmi = deploy.getComponentInstances().onlyVMs().withID(command.getVmId());
        if (vmi == null) {
            dispatch(new Message(command, Category.ERROR, "Cannot find a VM with this ID!"));
        } else {
            deployer.scaleOut(vmi);
        }
    }

    /**
     *
     * @param pathName
     * @return null if we cannot manage this type of files. Returns the
     * extension if we can.
     */
    private String canHandle(String pathName) {
        final String extension = FilenameUtils.getExtension(pathName);
        if (Codec.extensions.keySet().contains(extension)) {
            return extension;
        } else {
            return null;
        }
    }

    /**
     * Formats and emits a proper error message if the model located at
     * 'pathName' cannot be handled by 'command' (i.e. if canHandle(pathName) ==
     * null)
     *
     * @param pathName
     * @param command
     */
    private void wrongFileFormat(String pathName, CloudMlCommand command) {
        final StringBuilder ext = new StringBuilder();
        int i = 0;
        for (String e: Codec.extensions.keySet()) {
            if (i > 0) {
                ext.append(", ");
            }
            ext.append(e);
            i++;
        }
        final String text = "Cannot handle this type of file (" + FilenameUtils.getExtension(pathName) + ").\nPlease provide file with an extension CloudML codecs can manage (" + ext.toString() + ")";
        final Message message = new Message(command, Category.ERROR, text);
        dispatch(message);
    }

    /**
     *
     * @param extension
     * @return the codec able to manage the type of files specified by extension
     */
    private Codec getCodec(String extension) {
        return Codec.extensions.get(extension);
    }

}
