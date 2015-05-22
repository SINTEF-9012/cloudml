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


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.util.StringInputStream;
import org.apache.commons.io.FileUtils;
import org.cloudml.core.VM;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.Deployment;
import org.cloudml.core.Provider;
import org.cloudml.core.credentials.FileCredentials;
import org.cloudml.core.credentials.MemoryCredentials;
import org.cloudml.facade.commands.*;
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
            wsClient=new WSClient(new URI(serverURI), this);
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
            JsonCodec jc=new JsonCodec();
            InputStream is = new StringInputStream(json);
            Deployment temp=(Deployment)jc.load(is);
            for(Provider p: temp.getProviders()){
                if(p.getCredentials() instanceof FileCredentials){
                    String login = "";
                    String password = "";
                    if(p.getCredentials().getLogin() != null)
                        login=p.getCredentials().getLogin();
                    if(p.getCredentials().getPassword() != null)
                        password=p.getCredentials().getPassword();
                    MemoryCredentials mc=new MemoryCredentials(login, password);
                    p.setCredentials(mc);
                }
            }
            for(VM v : temp.getComponents().onlyVMs()){
                File file = new File(v.getPrivateKey());
                if(file.exists() && !file.isDirectory()) {
                    String contentKey = FileUtils.readFileToString(new File(v.getPrivateKey()));
                    v.setPrivateKey(contentKey);
                }
            }
            JsonCodec jsonCodec=new JsonCodec();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            jsonCodec.save(temp,baos);
            wsClient.send("!additional json-string:" + baos.toString());
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

    @Override
    public void handle(GetDeployment command) {
        wsClient.send("!getSnapshot\n" +
                "  path : /");
    }
}
