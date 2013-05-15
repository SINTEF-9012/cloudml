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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.codecs.XmiCodec;
import org.cloudml.codecs.commons.Codec;
import org.cloudml.core.*;
import org.cloudml.deployer.CloudAppDeployer;
import org.cloudml.deployer.JCloudsConnector;
import org.cloudml.facade.commands.*;
import org.cloudml.facade.events.*;
import org.cloudml.facade.events.Message.Category;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.NodeMetadata;

/**
 * This class implements an easier access to typical CloudML features, such as
 * deploying an application described in a JSON file for instance.
 *
 * It holds a list of clients and may send events to them. The facade
 * communicate with its clients using events.
 *
 * @author Nicolas Ferry - SINTEF ICT
 * @author Franck Chauvel - SINTEF ICT
 * @author Brice Morin - SINTEF ICT
 *
 * @since 1.0
 */
class Facade implements CloudML, CommandHandler {

    private static final Logger journal = Logger.getLogger(Facade.class.getName());
    private final ArrayList<EventHandler> handlers;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private DeploymentModel deploy;
    private boolean stopOnTimeout = false;

    /**
     * Default constructor
     */
    public Facade() {
        this.handlers = new ArrayList<EventHandler>();
        XmiCodec.init();
        JsonCodec.init();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.cloudml.facade.CloudML#terminate()
     */
    public void terminate() {
        //processor.stop();
        executor.shutdown();
        try {
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(Facade.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            executor.shutdownNow();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.cloudml.facade.CloudML#perform(org.cloudml.facade.commands.Command)
     */
    public void fireAndForget(CloudMlCommand command) {
        executor.submit(command);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.cloudml.facade.CloudML#perform(org.cloudml.facade.commands.Command)
     */
    public void fireAndWait(CloudMlCommand command) {
        Future<Boolean> done = executor.submit(command, true);
        try {
            if (command.getTimeout() > -1) {
                dispatch(new Message(command, Category.INFORMATION, "Will wait (max) for " + command.getTimeout() + " ms..."));
                done.get(command.getTimeout(), TimeUnit.MILLISECONDS);
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
                Message message = new Message(command, Category.INFORMATION, text);
                dispatch(message);
            }
        }
    }

    public void register(EventHandler handler) {
        synchronized (handlers) {
            this.handlers.add(handler);
        }
    }

    /**
     * Dispatch the given event to those which have registered a handler for
     * this type of event
     *
     * @param event the event to dispatch
     */
    private void dispatch(Event event) {
        synchronized (handlers) {
            for (EventHandler handler : handlers) {
                event.accept(handler);
            }
        }
    }

    ////////LEGACY CODE TO BE MIGRATED INTO NEW COMMANDS/////////////////////    
    /**
     * Create the node described by the given NodeInstance object
     *
     * @param a
     */
    public NodeMetadata createNode(NodeInstance a) {
        Provider p = a.getType().getProvider();
        JCloudsConnector jc = new JCloudsConnector(p.getName(), p.getLogin(),
                p.getPasswd());
        NodeMetadata node = jc.createInstance(a);
        jc.closeConnection();
        return node;
    }

    /**
     * Execute a given command on an artefact
     *
     * @param a the artefact on which the command will be executed
     * @param command the related shell command as a String
     * @param user the user associated
     */
    public void executeOnNode(ArtefactInstance a, String command, String user) {
        Node ownerNode = a.getDestination().getOwner().getType();
        Provider p = ownerNode.getProvider();
        JCloudsConnector jc = new JCloudsConnector(p.getName(), p.getLogin(),
                p.getPasswd());
        jc.execCommand(ownerNode.getGroupName(), command, user,
                ownerNode.getPrivateKey());

        jc.closeConnection();
    }

    public ComputeMetadata findNodeByName(String name, Provider p) {
        JCloudsConnector jc = new JCloudsConnector(p.getName(), p.getLogin(),
                p.getPasswd());
        ComputeMetadata cm = jc.getNodeByName(name);
        jc.closeConnection();
        return cm;
    }

    public Set<? extends ComputeMetadata> listOfNodes(Provider p) {
        JCloudsConnector jc = new JCloudsConnector(p.getName(), p.getLogin(),
                p.getPasswd());
        Set<? extends ComputeMetadata> list = jc.listOfNodes();
        jc.closeConnection();
        return list;
    }
    ////////END OF LEGACY CODE TO BE MIGRATED INTO NEW COMMANDS/////////////////////

    /*
     * COMMAND HANDLERS
     */
    public void handle(StartArtifact command) {
        // TODO Auto-generated method stub
        dispatch(new Message(command, Category.ERROR, "Not yet implemented"));
        //command.markAsCompleted();
    }

    public void handle(StopArtifact command) {
        // TODO Auto-generated method stub
        dispatch(new Message(command, Category.ERROR, "Not yet implemented"));
        //command.markAsCompleted();
    }

    public void handle(Attach command) {
        if (deploy == null) {
            final String text = "No deployment model. Please first load a deployment model";
            final Message message = new Message(command, Category.ERROR, text);
            dispatch(message);
        } else {
            //TODO
            dispatch(new Message(command, Category.INFORMATION, "Binding created!"));
        }
        //command.markAsCompleted();
    }

    public void handle(Detach command) {
        // TODO Auto-generated method stub
        dispatch(new Message(command, Category.ERROR, "Not yet implemented"));
        //command.markAsCompleted();
    }

    public void handle(Install command) {
        // TODO Auto-generated method stub
        dispatch(new Message(command, Category.ERROR, "Not yet implemented"));
        //command.markAsCompleted();
    }

    public void handle(Uninstall command) {
        // TODO Auto-generated method stub
        dispatch(new Message(command, Category.ERROR, "Not yet implemented"));
        //command.markAsCompleted();
    }

    public void handle(Instantiate command) {
        // TODO Auto-generated method stub
        dispatch(new Message(command, Category.ERROR, "Not yet implemented"));
        //command.markAsCompleted();
    }

    public void handle(Destroy command) {
        if (deploy == null) {
            final String text = "No deployment model. Please first load a deployment model";
            final Message message = new Message(command, Category.ERROR, text);
            dispatch(message);
        } else {
            final NodeInstance instance = findNodeInstanceById(command.getInstanceId());
            if (instance == null) {
                final String text = String.format("No node with ID=\"%s\"", command.getInstanceId());
                final Message message = new Message(command, Category.ERROR, text);
                dispatch(message);
            } else {
                Provider p = instance.getType().getProvider();
                JCloudsConnector jc = new JCloudsConnector(p.getName(), p.getLogin(), p.getPasswd());
                jc.destroyNode(instance.getId());
                dispatch(new Message(command, Category.INFORMATION, "Node instance terminated"));
            }
        }
        //command.markAsCompleted();
    }

    public void handle(Upload command) {
        if (deploy == null) {
            final String text = "No deployment model. Please first load a deployment model";
            final Message message = new Message(command, Category.ERROR, text);
            dispatch(message);

        } else {
            final String user = "ubuntu"; // TODO calculate the value needed.
            final ArtefactInstance instance = findArtefactInstanceById(command.getArtifactId());
            if (instance == null) {
                final String text = String.format("No artefact with ID=\"%s\"", command.getArtifactId());
                final Message message = new Message(command, Category.ERROR, text);
                dispatch(message);

            } else {
                Node ownerNode = instance.getDestination().getOwner().getType();
                Provider p = ownerNode.getProvider();
                JCloudsConnector jc = new JCloudsConnector(p.getName(), p.getLogin(), p.getPasswd());
                ComputeMetadata c = jc.getNodeByName(instance.getName());
                jc.uploadFile(command.getLocalPath(), command.getRemotePath(), c.getId(), user,
                        ownerNode.getPrivateKey());
            }
        }
        //command.markAsCompleted();
    }

    public void handle(LoadDeployment command) {
        final String extension = canHandle(command.getPathToModel());
        if (extension != null) {
            final File f = new File(command.getPathToModel());
            try {
                deploy = (DeploymentModel) getCodec(extension).load(new FileInputStream(f));
                final Message message = new Message(command, Category.INFORMATION, "Loading Complete.");
                dispatch(message);

            } catch (FileNotFoundException ex) {
                final Message message = new Message(command, Category.ERROR, "Unable to find file: " + command.getPathToModel());
                dispatch(message);
                journal.log(Level.INFO, "Unable to access the requested file", ex);
            }

        } else {
            final String text = "Cannot handle this type of file. Please provide file with an extension CloudML codecs can manage";
            final Message message = new Message(command, Category.ERROR, text);
            dispatch(message);
        }
        //command.markAsCompleted();
    }

    public void handle(StoreDeployment command) {
        final String extension = canHandle(command.getDestination());
        if (extension != null) {
            final File f = new File(command.getDestination());
            try {
                getCodec(extension).save(deploy, new FileOutputStream(f));
                final Message message = new Message(command, Category.INFORMATION, "Serialisation Complete.");
                dispatch(message);

            } catch (FileNotFoundException ex) {
                final Message message = new Message(command, Category.ERROR, "Error when saving model to serialized form.");
                dispatch(message);
                journal.log(Level.INFO, "Error during serialization", ex);

            }
        } else {
            final String text = "Cannot handle this type of file. Please provide file with an extension CloudML codecs can manage";
            final Message message = new Message(command, Category.ERROR, text);
            dispatch(message);

        }
        //command.markAsCompleted();
    }

    public void handle(Deploy command) {
        if (deploy == null) {
            final String text = "No deployment model. Please first load a deployment model";
            final Message message = new Message(command, Category.ERROR, text);
            dispatch(message);

        } else {
            CloudAppDeployer deployer = new CloudAppDeployer();
            deployer.deploy(deploy);
            dispatch(new Message(command, Category.INFORMATION, "Deployment Complete."));
        }
        //command.markAsCompleted();
    }

    public void handle(ListArtefactTypes command) {
        if (deploy == null) {
            final String text = "No deployment model. Please first load a deployment model";
            final Message message = new Message(command, Category.ERROR, text);
            dispatch(message);

        } else {
            final ArtefactTypeList data = new ArtefactTypeList(command, deploy.getArtefactTypes().values());
            dispatch(data);

        }
        //command.markAsCompleted();
    }

    public void handle(ListArtefactInstances command) {
        if (deploy == null) {
            final String text = "No deployment model. Please first load a deployment model";
            final Message message = new Message(command, Category.ERROR, text);
            dispatch(message);

        } else {
            final Collection<ArtefactInstance> instances = deploy.getArtefactInstances();
            final ArtefactInstanceList data = new ArtefactInstanceList(command, instances);
            dispatch(data);

        }
        //command.markAsCompleted();
    }

    public void handle(ViewArtefactType command) {
        if (deploy == null) {
            final String text = "No deployment model. Please first load a deployment model";
            final Message message = new Message(command, Category.ERROR, text);
            dispatch(message);

        } else {
            Artefact type = findArtefactTypeById(command.getArtefactTypeId());
            if (type == null) {
                final String text = String.format("No artefact type with ID \"%s\"", command.getArtefactTypeId());
                final Message message = new Message(command, Category.ERROR, text);
                dispatch(message);

            } else {
                final ArtefactTypeData data = new ArtefactTypeData(command, type);
                dispatch(data);

            }
        }
        //command.markAsCompleted();
    }

    public void handle(ViewArtefactInstance command) {
        if (deploy == null) {
            final String text = "No deployment model. Please first load a deployment model";
            final Message message = new Message(command, Category.ERROR, text);
            dispatch(message);

        } else {
            ArtefactInstance instance = findArtefactInstanceById(command.getArtefactId());
            if (instance == null) {
                final String text = String.format("No artefact instance with ID \"%s\"", command.getArtefactId());
                final Message message = new Message(command, Category.ERROR, text);
                dispatch(message);

            } else {
                final ArtefactInstanceData data = new ArtefactInstanceData(command, instance);
                dispatch(data);

            }
        }
        //command.markAsCompleted();
    }

    public void handle(LoadCredentials command) {
        // TODO Auto-generated method stub
        dispatch(new Message(command, Category.ERROR, "Not yet implemented"));
        //command.markAsCompleted();
    }

    public void handle(StoreCredentials command) {
        // TODO Auto-generated method stub
        dispatch(new Message(command, Category.ERROR, "Not yet implemented"));
        //command.markAsCompleted();
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
     *
     * @param extension
     * @return the codec able to manage the type of files specified by extension
     */
    private Codec getCodec(String extension) {
        return Codec.extensions.get(extension);
    }

    /**
     * Search for an artefact instance whose name matches the given id. If no
     * such instance matches, it returns null.
     *
     * @param id the id of the needed instance
     * @return the instance associated with the given id or null by default.
     */
    private ArtefactInstance findArtefactInstanceById(final String id) {
        ArtefactInstance result = null;
        final Iterator<ArtefactInstance> instances = deploy.getArtefactInstances().iterator();
        while (result == null && instances.hasNext()) {
            final ArtefactInstance instance = instances.next();
            if (instance.getName().equals(id)) {
                result = instance;
            }
        }
        return result;
    }

    /**
     * Search for an artefact instance whose name matches the given id. If no
     * such instance matches, it returns null.
     *
     * @param id the id of the needed instance
     * @return the instance associated with the given id or null by default.
     */
    private Artefact findArtefactTypeById(final String id) {
        return deploy.getArtefactTypes().get(id);
    }

    /**
     * Search for a node instance whose name matches a given id
     *
     * @param id of the Node instance
     * @return the node instance with the given id or null
     */
    private NodeInstance findNodeInstanceById(final String id) {
        for (NodeInstance ni : deploy.getNodeInstances()) {
            if (ni.getName().equals(id)) {
                return ni;
            }
        }
        return null;
    }

    /**
     * Search for a binding type whose name matches a given id
     *
     * @param id of a binding type
     * @return the binding type with the given id or null
     */
    private Binding findBindingTypeById(final String id) {
        for (Binding t : deploy.getBindingTypes().values()) {
            if (t.getName().equals(id)) {
                return t;
            }
        }
        return null;
    }
}