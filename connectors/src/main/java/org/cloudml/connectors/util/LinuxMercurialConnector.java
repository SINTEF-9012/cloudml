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
package org.cloudml.connectors.util;

import org.cloudml.connectors.FlexiantConnector;
import org.cloudml.connectors.PowerShellConnector;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nicolasf on 03.09.14.
 */
public class LinuxMercurialConnector implements MercurialConnector{

    private static final Logger journal = Logger.getLogger(FlexiantConnector.class.getName());

    public static String endPoint;
    public static String sshKey;

    public LinuxMercurialConnector(String endPoint, String sshKey){
        this.endPoint=endPoint;
        this.sshKey=sshKey;
    }

    public void clone(String pathDest){
        String cmd="hg clone \"ssh -i "+sshKey+"\"  "+endPoint+" "+pathDest;
        execute(cmd);
    }

    private void execute(String cmd){
        try {
            journal.log(Level.INFO, ">> "+ cmd);
            PowerShellConnector psc2=new PowerShellConnector(cmd);
            journal.log(Level.INFO, ">>  "+ psc2.getStandardOutput());
            journal.log(Level.INFO, ">>  "+ psc2.getStandardError());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void add(String files){
        String cmd="hg add "+files;
        execute(cmd);
    }

    public void commit(String username, String message){
        String cmd="hg ci -u "+username+" -m"+message;
        execute(cmd);
    }

    public void push(String username){
        String cmd="hg push -e \"ssh -i "+sshKey+"\"";
        execute(cmd);
    }

    @Override
    public void addFile(String files, String username) {
        clone("./nodes");
        commit(username, "updated by CloudML");
        push(username);
    }

}
