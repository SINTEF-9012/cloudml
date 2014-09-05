/**
 * This file is part of CloudML [ http://cloudml.org ]
 *
 * Copyright (C) 2012 - SINTEF ICT Contact: Franck Chauvel
 * <franck.chauvel@sintef.no>
 *
 * Module: root
 *
 * CloudML is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * CloudML is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CloudML. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.cloudml.ui.shell.configuration;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

    private String closingMessage;

    private String prompt;

    private String logo;

    private String version;

    private String copyright;

    private String disclaimer;

    private ArrayList<Command> commands;

    public Configuration() {
        this.commands = new ArrayList<Command>();
    }

    /**
     * @return the commands
     */
    public List<Command> getCommands() {
        return commands;
    }

    /**
     * @param commands the commands to set
     */
    public void setCommands(List<Command> commands) {
        this.commands = new ArrayList<Command>(commands);
    }

    /**
     * @return the logo
     */
    public String getLogo() {
        return logo;
    }

    /**
     * @param logo the logo to set
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     * @return the prompt
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * @param prompt the prompt to set
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the copyright
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * @param copyright the copyright to set
     */
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    /**
     * @return the disclaimer
     */
    public String getDisclaimer() {
        return disclaimer;
    }

    /**
     * @param disclaimer the disclaimer to set
     */
    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    /**
     * @return the closingMessage
     */
    public String getClosingMessage() {
        return closingMessage;
    }

    /**
     * @param closingMessage the closingMessage to set
     */
    public void setClosingMessage(String closingMessage) {
        this.closingMessage = closingMessage;
    }

}
