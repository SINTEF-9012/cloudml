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
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nicolasf on 05.12.14.
 */
public class DockerConnector {

    private static final Logger journal = Logger.getLogger(DockerConnector.class.getName());
    private DockerClient dockerClient;

    public DockerConnector(String endPoint, String version){
        DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
                .withVersion(version)
                .withUri(endPoint)
                .build();
        dockerClient = DockerClientBuilder.getInstance(config).build();
    }

    public void startDockerDaemon(String keyPath, String user, String host, String dockerPort){
        SSHConnector connector = new SSHConnector(keyPath,user,host);
        connector.execCommandSsh("sudo docker -d -H 0.0.0.0:"+dockerPort);
    }

    public void commit(String msg){
        dockerClient.commitCmd(msg).exec();
    }

    public Info getInfo(){
        Info info = dockerClient.infoCmd().exec();
        return info;
    }

    public void pullImage(String image){
        dockerClient.pullImageCmd(image).exec();
    }

    public void getImageList(){
        dockerClient.listImagesCmd().exec();
    }

    public String createContainerWithPorts(String image, int[] ports, String command){
        String[] words = command.split("\\s+");
        return createContainerWithPorts(image, ports, words);
    }

    public String createContainerWithPorts(String image, int[] ports, String... command){
        ExposedPort[] exposedPorts=new ExposedPort[ports.length];
        for(int i=0;i < ports.length;i++){
            exposedPorts[i]=ExposedPort.tcp(ports[i]);
        }

        CreateContainerResponse container = dockerClient.createContainerCmd(image)
                .withCmd(command)
                .withExposedPorts(exposedPorts)
                .exec();
        return container.getId();
    }

    public void BuildImageFromDockerFile(String path, String tag){
        File baseDir = new File(path);

        InputStream response = dockerClient.buildImageCmd(baseDir)
                .withNoCache()
                .withTag(tag)
                .exec();

        StringWriter logwriter = new StringWriter();

        try {
            LineIterator itr = IOUtils.lineIterator(response, "UTF-8");
            while (itr.hasNext()) {
                String line = itr.next();
                logwriter.write(line);
                journal.log(Level.INFO,line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(response);
        }
    }

    public String createContainer(String image, String command){
        String[] words = command.split("\\s+");
        return createContainer(image,words);
    }

    public String createContainer(String image, String... command){
        CreateContainerResponse container = dockerClient
                .createContainerCmd(image)
                .withCmd(command)
                .exec();
        return container.getId();
    }

    public void exec(String id, String... cmd){
        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(id)
                .withAttachStdout(true)
                .withCmd(cmd).exec();
    }

    public void startContainer(String id){
        dockerClient.startContainerCmd(id).exec();
    }

    public void stopContainer(String id){
        dockerClient.stopContainerCmd(id).exec();
    }




}
