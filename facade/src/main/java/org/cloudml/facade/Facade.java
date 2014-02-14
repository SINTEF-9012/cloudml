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
import org.cloudml.core.*;
import org.cloudml.deployer.CloudAppDeployer;
import org.cloudml.connectors.JCloudsConnector;
import org.cloudml.facade.commands.*;
import org.cloudml.facade.events.*;
import org.cloudml.facade.events.Message.Category;
import org.jclouds.compute.domain.ComputeMetadata;

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

	private final List<EventHandler> handlers = Collections.synchronizedList(new ArrayList<EventHandler>());
	private final ExecutorService executor = Executors.newSingleThreadExecutor();
	private CloudMLModel deploy;
	private boolean stopOnTimeout = false;
	private final CloudAppDeployer deployer;

	/**
	 * Default constructor
	 */
	public Facade() {
		XmiCodec.init();
		JsonCodec.init();
		this.deployer = new CloudAppDeployer();
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
				final Message message = new Message(command, Category.INFORMATION, text);
				dispatch(message);
			}
		}
	}

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
		for (EventHandler handler : handlers) {
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
		Provider p = a.getType().getProvider();
		JCloudsConnector jc = new JCloudsConnector(p.getName(), p.getLogin(),
				p.getPasswd());
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
		VM ownerVM = a.getDestination().getType();
		Provider p = ownerVM.getProvider();
		JCloudsConnector jc = new JCloudsConnector(p.getName(), p.getLogin(),
				p.getPasswd());
		jc.execCommand(ownerVM.getGroupName(), command, user,
				ownerVM.getPrivateKey());

		jc.closeConnection();
	}

	public ComputeMetadata findVMByName(String name, Provider p) {//TODO: use the connector factory
		JCloudsConnector jc = new JCloudsConnector(p.getName(), p.getLogin(),
				p.getPasswd());
		ComputeMetadata cm = jc.getVMByName(name);
		jc.closeConnection();
		return cm;
	}

	public Set<? extends ComputeMetadata> listOfVMs(Provider p) {//TODO: use the connector factory
		JCloudsConnector jc = new JCloudsConnector(p.getName(), p.getLogin(),
				p.getPasswd());
		Set<? extends ComputeMetadata> list = jc.listOfVMs();
		jc.closeConnection();
		return list;
	}
	////////END OF LEGACY CODE TO BE MIGRATED INTO NEW COMMANDS/////////////////////
        
        public CloudMLModel getDeploymentModel(){
            return deploy;
        }
        
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
			dispatch(new Message(command, Category.INFORMATION, "Relationship created!"));
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
			final VMInstance instance = findVMInstanceById(command.getInstanceId());
			if (instance == null) {
				final String text = String.format("No VM with ID=\"%s\"", command.getInstanceId());
				final Message message = new Message(command, Category.ERROR, text);
				dispatch(message);
			} else {
				Provider p = instance.getType().getProvider();
				JCloudsConnector jc = new JCloudsConnector(p.getName(), p.getLogin(), p.getPasswd());
				jc.destroyVM(instance.getId());
				dispatch(new Message(command, Category.INFORMATION, "VM instance terminated"));
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
			VMInstance ownerVM=null;
			for(VMInstance ni : deploy.getVMInstances()){
				if(ni.getName().equals(command.getArtifactId()))
					ownerVM=ni;
			}
			if(ownerVM != null){
				Provider p = ownerVM.getType().getProvider();
				JCloudsConnector jc = new JCloudsConnector(p.getName(), p.getLogin(), p.getPasswd());
				ComputeMetadata c = jc.getVMByName(command.getArtifactId());
				jc.uploadFile(command.getLocalPath(), command.getRemotePath(), c.getId(), "ubuntu",
						ownerVM.getType().getPrivateKey());
			}else{
				final String text = "There is no VM with this ID!";
				final Message message = new Message(command, Category.ERROR, text);
				dispatch(message);
			}
		}
		//command.markAsCompleted();
	}

	public void handle(LoadDeployment command) {
		final String extension = canHandle(command.getPathToModel());
		if (extension != null) {
			final File f = new File(command.getPathToModel());
			try {
				deploy = (CloudMLModel) getCodec(extension).load(new FileInputStream(f));
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
				final Message message = new Message(command, Category.ERROR, "Cannot save model to specified destination.");
				dispatch(message);
			} catch (Exception e) {
				final Message message = new Message(command, Category.ERROR, "Error while saving model: " + e.getLocalizedMessage());
				dispatch(message);
			}
		} else {
			wrongFileFormat(command.getDestination(), command);
		}
		//command.markAsCompleted();
	}

	public void handle(Deploy command) {
		if (deploy == null) {
			final String text = "No deployment model. Please first load a deployment model";
			final Message message = new Message(command, Category.ERROR, text);
			dispatch(message);

		} else {
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
			final ComponentList data = new ComponentList(command, deploy.getComponents().values());
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
			final Collection<ComponentInstance> instances = deploy.getComponentInstances();
			final ComponentInstanceList data = new ComponentInstanceList(command, instances);
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
			Component type = findComponentById(command.getArtefactTypeId());
			if (type == null) {
				final String text = String.format("No artefact type with ID \"%s\"", command.getArtefactTypeId());
				final Message message = new Message(command, Category.ERROR, text);
				dispatch(message);

			} else {
				final ComponentData data = new ComponentData(command, type);
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
			ComponentInstance instance = findComponentInstanceById(command.getArtefactId());
			if (instance == null) {
				final String text = String.format("No artefact instance with ID \"%s\"", command.getArtefactId());
				final Message message = new Message(command, Category.ERROR, text);
				dispatch(message);

			} else {
				final ComponentInstanceData data = new ComponentInstanceData(command, instance);
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

	
	public void handle(Snapshot command) {
		dispatch(new Message(command, Category.INFORMATION, "Generating snapshot ..."));
		DrawnIconVertexDemo g = new DrawnIconVertexDemo(deploy);
		ArrayList<Vertex> v =g.drawFromDeploymentModel();
		File f=new File(command.getPathToSnapshot());
		g.writeServerJPEGImage(f);
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
		for (String e : Codec.extensions.keySet()) {
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

	/**
	 * Search for an component instance whose name matches the given id. If no
	 * such instance matches, it returns null.
	 *
	 * @param id the id of the needed instance
	 * @return the instance associated with the given id or null by default.
	 */
	private ComponentInstance findComponentInstanceById(final String id) {
		ComponentInstance result = null;
		final Iterator<ComponentInstance> instances = deploy.getComponentInstances().iterator();
		while (result == null && instances.hasNext()) {
			final ComponentInstance instance = instances.next();
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
	private Component findComponentById(final String id) {
		return deploy.getComponents().get(id);
	}

	/**
	 * Search for a VM instance whose name matches a given id
	 *
	 * @param id of the VM instance
	 * @return the VM instance with the given id or null
	 */
	private VMInstance findVMInstanceById(final String id) {
		for (VMInstance ni : deploy.getVMInstances()) {
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
	private Relationship findRelationshipById(final String id) {
		for (Relationship t : deploy.getRelationships().values()) {
			if (t.getName().equals(id)) {
				return t;
			}
		}
		return null;
	}
}