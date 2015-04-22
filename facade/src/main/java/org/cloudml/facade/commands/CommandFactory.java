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

import java.util.List;

/**
 * Factory methods to simplify the creation of CloudML commands
 */
public class CommandFactory {

    /**
     * Start a given artifact of the deployment model
     *
     * @param artifactId the ID of the artifact to start
     *
     * @return the associated command object
     */
    public CloudMlCommand startComponent(final List<String> artifactId) {
        return new StartComponent(artifactId);
    }

    /**
     * Stop a given artifact of the deployment model
     *
     * @param artifactId the ID of the artifact to stop
     * @return the associated command object
     */
    public CloudMlCommand stopComponent(final String artifactId) {
        return new StopComponent(artifactId);
    }

    /**
     * Install a given software in a given environment
     *
     * @param environmentId the ID of the environment where the software shall
     * be installed
     * @param softwareId the ID of the software which shall be installed
     * @return the associated command object
     */
    public CloudMlCommand install(final String environmentId,
                                  final String softwareId) {
        return new Install(environmentId, softwareId);
    }

    /**
     * Remove a given software from a given environment
     *
     * @param environmentId the ID of the environment where the software is
     * installed
     * @param softwareId the ID of the software that shall be removed
     * @return the associated command object
     */
    public CloudMlCommand uninstall(final String environmentId,
                                    final String softwareId) {
        return new Uninstall(environmentId, softwareId);
    }

    /**
     * Attach a given service provider to a service consumer
     *
     * @param providerId the end-point of the service provider
     *
     * @param requiredId the end-point of the service consumer
     * @return the associated command object
     */
    public CloudMlCommand attach(final String providerId,
                                 final String requiredId) {
        return new Attach(providerId, requiredId);
    }

    /**
     * Detach a given service provider from a service consumer
     *
     * @param providerId the end-point of the service provider
     *
     * @param requiredId the end-point of the service consumer
     * @return the associated command object
     */
    public CloudMlCommand detach(final String providerId,
                                 final String requiredId) {
        return new Detach(requiredId, providerId);
    }

    /**
     * Create a new instance of a given artifact type
     *
     * @param typeId the name (i.e., the ID) or the artifact type to instantiate
     * @param instanceId the name that must be given to the instance
     * @return the associated command object
     */
    public CloudMlCommand instantiate(final String typeId, final String instanceId) {
        return new Instantiate(typeId, instanceId);
    }

    /**
     * Destroy an instance
     *
     * @param instanceId the name of the instance to be savagely destroyed
     * @return the associated command object
     */
    public CloudMlCommand destroy(final String instanceId) {
        return new Destroy(instanceId);
    }

    /**
     * Upload a local resources on one of the artifact of the model in use
     *
     * @param artifactId the ID of the artifact where the resources must be
     * uploaded
     * @param localPath the local path where the resources is located
     * @param remotePath the remote path where the resources must be stored
     * @return the associated command object
     *
     */
    public CloudMlCommand upload(final String artifactId,
                                 final String localPath, final String remotePath) {
        return new Upload(artifactId, localPath, remotePath);
    }

    /**
     * Store a deployment model in a given location with a given format
     *
     * @param destination the place where the model must be stored
     * @return the associated command object
     */
    public CloudMlCommand storeDeployment(final String destination) {
        return new StoreDeployment(destination);
    }

    /**
     * Load an existing local deployment model
     *
     * @param pathToModel the local path to the deployment model to load
     * @return the associated command object
     */
    public CloudMlCommand loadDeployment(final String pathToModel) {
        return new LoadDeployment(pathToModel);
    }

    /**
     * Deploy the current CloudMLModel
     *
     * @return the associated command object
     */
    public CloudMlCommand deploy() {
        return new Deploy();
    }

    /**
     * Create a new instance of ListType command
     *
     * @return the related CloudMlCommand
     */
    public CloudMlCommand listTypes() {
        return new ListComponents();
    }

    /**
     * Create a new instance of ListComponentInstances command
     *
     * @return a fresh ListComponentInstances command
     *
     */
    public CloudMlCommand listInstances() {
        return new ListComponentInstances();
    }

    /**
     * Create a new instance of ViewComponent command
     *
     * @param id the ID of the artefact type which is needed
     * @return a command to see the detail of the artefact type with the given
     * id
     */
    public CloudMlCommand viewType(final String id) {
        return new ViewComponent(id);
    }

    /**
     * Create a new instance of ViewComponentInstance command
     *
     * @param id the ID of the artefact instance which is needed
     * @return a command to fetch the artefact instance with the given ID
     */
    public CloudMlCommand viewInstance(final String id) {
        return new ViewComponentInstance(id);
    }

    /**
     * Create a new command to store the credentials currently in used by
     * CloudML
     *
     * @param destination the place where the credentials shall be stored
     * @return the associated command to store the credentials
     */
    public CloudMlCommand storeCredentials(final String destination) {
        return new StoreCredentials(destination);
    }

    /**
     * Create a new command to load the credentials containing in a given file
     *
     * @param pathToCredentials the path to the file that contains the
     * credentials
     *
     * @return the command to load the credentials contained in the file
     */
    public CloudMlCommand loadCredentials(final String pathToCredentials) {
        return new LoadCredentials(pathToCredentials);
    }

    /**
     * Create a snapshot of a VM
     *
     * @param id the id of the VM instance
     *
     * @return the associated command object
     */
    public CloudMlCommand snapshot(final String id) {
        return new Snapshot(id);
    }

    /**
     * Create an image of a VM
     *
     * @param id the id of the VM instance
     *
     * @return the associated command object
     */
    public CloudMlCommand image(final String id) {
        return new Image(id);
    }

    /**
     * Reset the deployment engine
     *
     * @return the associated command object
     */
    public CloudMlCommand reset() {
        return new Reset();
    }

    /**
     * Scale out a VM
     *
     * @param id of the VM
     *
     * @return the associated command object
     */
    public CloudMlCommand scaleOut(final String id) {
        return new ScaleOut(id);
    }
    
    public CloudMlCommand scaleOut(final String id, int nb) {
        return new ScaleOut(id, nb);
    }
    
    public CloudMlCommand burst(final String vmID, final String providerID){
        return new Burst(vmID, providerID);
    }
}
