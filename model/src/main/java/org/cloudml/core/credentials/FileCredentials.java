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
/*
 */
package org.cloudml.core.credentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudml.core.Provider;

public class FileCredentials implements Credentials {

    public static final String DEFAULT_PATH = "credentials.properties";
    
    private static final String NONE = null;
    private String path;

    private String login;
    private String password;
    
    public FileCredentials(String login, String password){
        this.login=login;
        this.password=password;
    }

    public FileCredentials() {
        this(DEFAULT_PATH);
    }

    public FileCredentials(String pathToCredentials) {
        path = pathToCredentials;
    }
    
    public String getPathToCredentials() {
        return path;
    }

    @Override
    public String getLogin() {
        initializeIfNeeded();
        return login;
    }

    @Override
    public String getPassword() {
        initializeIfNeeded();
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setLogin(String login) {
        this.login = login;
    }

    private boolean notYetInitialized() {
        return (login == NONE || password == NONE);
    }

    private void initializeCredentials() {
        FileInputStream in = null;
        try {
            Properties props = new Properties();
            in = new FileInputStream(this.path);
            props.load(in);

            login = props.getProperty("login");
            password = props.getProperty("passwd");

        } catch (IOException ex) {
            Logger.getLogger(Provider.class.getName()).log(Level.SEVERE, "Missing credentials", new Object[]{});
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Provider.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void initializeIfNeeded() {
        if (notYetInitialized()) {
            initializeCredentials();
        }
    }
}
