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
package org.cloudml.connectors;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

/**
 * Created by nicolasf on 05.12.14.
 */
public class DockerConnector {

    private DockerClient dockerClient;

    public DockerConnector(String endPoint){
        DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
                .withVersion("1.10")
                .withUri(endPoint)
                .build();
        dockerClient = DockerClientBuilder.getInstance(config).build();
    }

    public void startDockerDaemon(String keyPath, String user, String host, String dockerPort){
        SSHConnector connector = new SSHConnector(keyPath,user,host);
        connector.execCommandSsh("sudo docker -H 0.0.0.0 "+dockerPort+" -d");
    }

    public void pullImage(String image){
        dockerClient.pullImageCmd(image).exec();
    }

    public String createContainer(String command){
        CreateContainerResponse container = dockerClient
                .createContainerCmd("ubuntu").withCmd(command).exec();
        return container.getId();
    }

    public void startContainer(String id){
        dockerClient.startContainerCmd(id);
    }

    public void stopContainer(String id){
        dockerClient.stopContainerCmd(id);
    }


}
