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
package org.cloudml.facade.commands;

public class CommandFactory {

    private CommandHandler handler;
    
    public CommandFactory(CommandHandler handler) {
        this.handler = handler;
    }

    /**
     * Start a given artifact of the deployment model
     *
     * @param artefactId the ID of the artifact to start
     *
     * @see {@link StartComponent}
     */
    public CloudMlCommand createStartArtifact(final String artifactId) {
        return new StartComponent(handler, artifactId);
    }

    /**
     * Stop a given artifact of the deployment model
     *
     * @param artefactId the ID of the artifact to stop
     *
     * @see {@link StopComponent}
     */
    public CloudMlCommand createStopArtifact(final String artifactId) {
        return new StopComponent(handler, artifactId);
    }

    /**
     * Install a given software in a given environment
     *
     * @param environment the ID of the environment where the software shall be
     * installed
     * @param software the ID of the software which shall be installed
     *
     * @see {@link org.cloudml.facade.commands.Install}
     */
    public CloudMlCommand createInstall(final String environmentId,
            final String softwareId) {
        return new Install(handler, environmentId, softwareId);
    }

    /**
     * Remove a given software from a given environment
     *
     * @param environment the ID of the environment where the software is
     * installed
     * @param software the ID of the software that shall be removed
     *
     * @see {@link org.cloudml.facade.commands.Uninstall}
     */
    public CloudMlCommand createUninstall(final String environmentId,
            final String softwareId) {
        return new Uninstall(handler, environmentId, softwareId);
    }

    /**
     * Attach a given service provider to a service consumer
     *
     * @param providerId the end-point of the service provider
     *
     * @param requiredId the end-point of the service consumer
     * 
     * @see {@link org.cloudml.facade.commands.Attach}
     */
    public CloudMlCommand createAttach(final String providerId,
            final String consumerId) {
        return new Attach(handler, providerId, consumerId);
    }

    /**
     * Detach a given service provider from a service consumer
     *
     * @param providerId the end-point of the service provider
     *
     * @param requiredId the end-point of the service consumer
     *
     * @see {@link org.cloudml.facade.commands.Detach}
     */
    public CloudMlCommand createDetach(final String providerId,
            final String consumerId) {
        return new Detach(handler, consumerId, providerId);
    }

    /**
     * Create a new instance of a given artifact type
     *
     * @param typeName the name (i.e., the ID) or the artifact type to
     * instantiate
     * @param instanceId the name that must be given to the instance
     *
     * @see {@link org.cloudml.facade.commands.Instantiate}
     */
    public CloudMlCommand createInstantiate(final String typeId, final String instanceId) {
        return new Instantiate(handler, typeId, instanceId);
    }

    /**
     * Destroy an instance
     *
     * @param instanceId the name of the instance to be savagely destroyed
     *
     * @see {@link org.cloudml.facade.commands.Destroy}
     */
    public CloudMlCommand createDestroy(final String instanceId) {
        return new Destroy(handler, instanceId);
    }

    /**
     * Upload a local resource on one of the artifact of the model in use
     *
     * @param artifactId the ID of the artifact where the resource must be
     * uploaded
     * @param localPath the local path where the resource is located
     * @param remotePath the remote path where the resource must be stored
     *
     * @link {@link org.cloudml.facade.commands.Upload}
     */
    public CloudMlCommand createUpload(final String artifactId,
            final String localPath, final String remotePath) {
        return new Upload(handler, artifactId, localPath, remotePath);
    }

    /**
     * Store a deployment model in a given location witha given format
     *
     * @param destination the place where the model must be stored
     * @param format the format that must be used
     *
     * @see {@link org.cloudml.facade.commands.StoreDeployment}
     */
    public CloudMlCommand createStoreDeployment(final String destination) {
        return new StoreDeployment(handler, destination);
    }

    /**
     * Load an existing local deployment model
     *
     * @param pathToModel the local path to the deployment model to load
     *
     * @see {@link org.cloudml.facade.commands.LoadDeployment}
     */
    public CloudMlCommand createLoadDeployment(final String pathToModel) {
        return new LoadDeployment(handler, pathToModel);
    }

    /**
     * Deploy the current CloudMLModel
     *
     * @see {@link org.cloudml.facade.commands.Deploy}
     */
    public CloudMlCommand createDeploy() {
        return new Deploy(handler);
    }

    /**
     * Create a new instance of ListType command
     *
     * @return the related CloudMlCommand
     *
     * @see {@link ListComponents}
     */
    public CloudMlCommand createListArtefactTypes() {
        return new ListComponents(handler);
    }

    /**
     * Create a new instance of ListComponentInstances command
     *
     * @return a fresh ListComponentInstances command
     *
     * @see {@link ListComponentInstances}
     */
    public CloudMlCommand createListArtefactInstances() {
        return new ListComponentInstances(handler);
    }

    /**
     * Create a new instance of ViewComponent command
     *
     * @param id the ID of the artefact type which is needed
     * @return a command to see the detail of the artefact type with the given
     * id
     */
    public CloudMlCommand createViewArtefactType(final String id) {
        return new ViewComponent(handler, id);
    }

    /**
     * Create a new instance of ViewComponentInstance command
     *
     * @param id the ID of the artefact instance which is needed
     * @return a command to fetch the artefact instance with the given ID
     */
    public CloudMlCommand createViewArtefactInstance(final String id) {
        return new ViewComponentInstance(handler, id);
    }

    /**
     * Create a new command to store the crendentials currently in used by
     * CloudML
     *
     * @param destination the place where the credentials shall be stored
     * @return the associated command to store the credentials
     */
    public CloudMlCommand createStoreCredentials(final String destination) {
        return new StoreCredentials(handler, destination);
    }

    /**
     * Create a new command to load the credentials containing in a given file
     *
     * @param pathToCredentials the path to the file that contains the
     * credentials
     * @return the command to load the credentials contained in the file
     */
    public CloudMlCommand createLoadCredentials(final String pathToCredentials) {
        return new LoadCredentials(handler, pathToCredentials);
    }
    
    /**
     * Load an existing local deployment model
     *
     * @param pathToModel the local path to the deployment model to load
     *
     * @see {@link org.cloudml.facade.commands.LoadDeployment}
     */
    public CloudMlCommand createSnapshot(final String pathToSnapshot) {
        return new Snapshot(handler, pathToSnapshot);
    }
}
