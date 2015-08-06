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

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import com.heroku.api.*;
import com.heroku.api.connection.ConnectionFactory;

/**
 * Created by ferrynico on 06/03/15.
 */
public class HerokuConnector implements PaaSConnector {
    private static final Logger journal = Logger.getLogger(HerokuConnector.class.getName());
    private HerokuAPI api;

    public HerokuConnector(String passwd){
        api=new HerokuAPI(ConnectionFactory.get(),passwd);
    }

    @Override
    public String createEnvironmentWithWar(String applicationName, String domainName, String envName, String stackName, String warFile, String versionLabel) {
        if(api.isAppNameAvailable(applicationName)){
            App application=new App().named(applicationName);
            App a=api.createApp(application);
            return a.getWebUrl();
        }
        throw new IllegalArgumentException("Application name not available!");
    }

    @Override
    public void createDBInstance(String engine, String version, String dbInstanceIdentifier, String dbName, String username, String password, Integer allocatedSize, String dbInstanceClass, String securityGroup) {

    }

    @Override
    public String getDBEndPoint(String dbInstanceId, int timeout) {
        return null;
    }

    @Override
    public void uploadWar(String warFile, String versionLabel, String applicationName, String envName, int timeout) {

    }

    @Override
    public String createQueue(String name) {
        return null;
    }

    @Override
    public void deleteQueue(String name) {

    }

    @Override
    public List<String> listQueues() {
        return null;
    }

    @Override
    public void restoreDB(String host, String port, String dbUser, String dbPass, String dbName, String local_file) {

    }

    @Override
    public void configAppParameters(String applicationName, Map<String, String> params) {

    }

    @Override
    public void bindDbToApp(String appId, String dbId, String alias) {

    }

    @Override
    public void setEnvVar(String appName, String nameVar, String val) {

    }

    @Override
    public void deleteApp(String appName) {
        api.destroyApp(appName);
    }
}
