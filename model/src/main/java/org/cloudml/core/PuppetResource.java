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
package org.cloudml.core;

/**
 * Created by nicolasf on 03.09.14.
 */
public class PuppetResource extends Resource {


    private static String masterEndpoint = "";
    private String repositoryEndpoint="";
    private String configureHostnameCommand="";
    private String configurationFile="";
    private String repositoryKey="";
    private String username="";

    public PuppetResource(){
        super();
    }

    public PuppetResource(String name){
        super(name);
    }

    public PuppetResource(String name, String deployingCommand, String retrievingCommand, String configurationCommand, String startCommand, String stopCommand) {
        super(name,deployingCommand,retrievingCommand,configurationCommand,startCommand,stopCommand);
    }

    public PuppetResource(String name, String master){
        super(name);
        this.masterEndpoint =master;
    }

    public PuppetResource(String name, String master, String repo, String configurationFile){
        super(name);
        this.masterEndpoint =master;
        this.repositoryEndpoint=repo;
        this.configurationFile=configurationFile;
    }

    public void setRepo(String repo){
        this.repositoryEndpoint=repo;
    }

    public String getRepo(){
        return this.repositoryEndpoint;
    }

    public void setConfigureHostnameCommand(String configureHostnameCommand){
        this.configureHostnameCommand=configureHostnameCommand;
    }

    public String getConfigureHostnameCommand(){
        return this.configureHostnameCommand;
    }

    public void setMaster(String master){
        this.masterEndpoint =master;
    }

    public String getMaster(){
        return masterEndpoint;
    }


    public String getConfigurationFile() {
        return configurationFile;
    }

    public void setConfigurationFile(String configurationFile) {
        this.configurationFile = configurationFile;
    }


    public String getRepositoryKey() {
        return repositoryKey;
    }

    public void setRepositoryKey(String repositoryKey) {
        this.repositoryKey = repositoryKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Resource: " + getName() + ", Master endpoint:";
    }

}
