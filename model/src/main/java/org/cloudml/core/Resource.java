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

import java.util.HashMap;
import java.util.Map;

public class Resource extends WithProperties {

    private String deployingResourceCommand = "";
    private String retrievingResourceCommand = "";
    private String configurationResourceCommand = "";
    private String startResourceCommand = "";
    private String stopResourceCommand = "";
    private Map<String, String> uploadCommand = new HashMap<String, String>();

    public Resource() {
    }

    public Resource(String name) {
        super(name);
    }

    public Resource(String name, String deployingCommand, String retrievingCommand) {
        super(name);
        this.deployingResourceCommand = deployingCommand;
        this.retrievingResourceCommand = retrievingCommand;
    }

    public Resource(String name, String deployingCommand, String retrievingCommand, String configurationCommand, String startCommand, String stopCommand) {
        super(name);
        this.deployingResourceCommand = deployingCommand;
        this.retrievingResourceCommand = retrievingCommand;
        this.configurationResourceCommand = configurationCommand;
        this.startResourceCommand = startCommand;
        this.stopResourceCommand = stopCommand;
    }

    public String getDeployingResourceCommand() {
        return deployingResourceCommand;
    }

    public String getRetrievingResourceCommand() {
        return retrievingResourceCommand;
    }

    public String getConfigurationResourceCommand() {
        return configurationResourceCommand;
    }

    public String getStartResourceCommand() {
        return startResourceCommand;
    }

    public String getStopResourceCommand() {
        return stopResourceCommand;
    }

    public Map<String, String> getUploadCommand() {
        return uploadCommand;
    }

    public void setDeployingCommand(String deployingCommand) {
        this.deployingResourceCommand = deployingCommand;
    }

    public void setRetrievingCommand(String retrievingCommand) {
        this.retrievingResourceCommand = retrievingCommand;
    }

    public void setConfigurationCommand(String configurationCommand) {
        this.configurationResourceCommand = configurationCommand;
    }

    public void setStartCommand(String startCommand) {
        this.startResourceCommand = startCommand;
    }

    public void setStopCommand(String stopCommand) {
        this.stopResourceCommand = stopCommand;
    }

    public void setUploadCommand(Map<String, String> paths) {
        this.uploadCommand = paths;
    }

    @Override
    public String toString() {
        return "Resource: " + getName();
    }
}
