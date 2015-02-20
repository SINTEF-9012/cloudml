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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.logging.Logger;

import org.cloudml.facade.commands.Deploy;
import org.cloudml.facade.commands.LoadDeployment;
import org.cloudml.facade.util.WSClient;

/**
 * Created by nicolasf on 17.02.15.
 */
public class RemoteFacade extends Facade{

    private static final Logger journal = Logger.getLogger(RemoteFacade.class.getName());
    private WSClient wsClient;
    private Thread t;

    public RemoteFacade(String serverURI){
        super();
        try {
            wsClient=new WSClient(new URI(serverURI));
            t =new Thread(wsClient);
            t.start();
            while(!wsClient.getConnected()){
                Thread.sleep(2000);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void stop(){
        wsClient.close();
    }

    @Override
    public void handle(LoadDeployment command) {
        wsClient.send("!extended { name : LoadDeployment }");
        final File f = new File(command.getPathToModel());
        try {
            String json=new String(Files.readAllBytes(f.toPath()));
            wsClient.send("!additional json-string:" + json);
            Thread.sleep(3000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(Deploy command) {
        wsClient.send("!listenToAny");
        wsClient.send("!extended { name : Deploy }");
    }
}
