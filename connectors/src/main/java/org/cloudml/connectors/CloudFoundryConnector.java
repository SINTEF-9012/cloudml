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

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.HttpProxyConfiguration;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudOrganization;
import org.cloudfoundry.client.lib.domain.CloudSpace;
import org.cloudfoundry.client.lib.domain.Staging;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * Created by ferrynico on 03/12/2014.
 */
public class CloudFoundryConnector implements PaaSConnector {

    private CloudFoundryClient connectedClient;
    private static final int DEFAULT_MEMORY = 512; // MB
    private static final Logger journal = Logger.getLogger(CloudFoundryConnector.class.getName());
    private String defaultDomainName;

    public CloudFoundryConnector(String APIEndPoint, String login, String passwd, String organization, String space){
        try {
            URL cloudControllerUrl = URI.create(APIEndPoint).toURL();
            journal.log(Level.INFO, ">> Connecting to CloudFoundry ...");
            connectedClient = new CloudFoundryClient(new CloudCredentials(login,passwd),cloudControllerUrl,organization,space);
            connectedClient.login();
            defaultDomainName = connectedClient.getDefaultDomain().getName();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void logOut(){
        connectedClient.logout();
    }

    public CloudFoundryClient getConnectedClient(){
        return connectedClient;
    }

    private String computeAppUrl(String appName, String domainName) {
        return appName + "." + (domainName.equals("")? defaultDomainName : domainName);
    }

    @Override
    public void createEnvironmentWithWar(String applicationName, String domainName, String envName, String stackName, String warFile, String versionLabel) {
        journal.log(Level.INFO, ">> Creating application ... ");
        List<String> uris = new ArrayList<String>();
        uris.add(computeAppUrl(applicationName, domainName));
        Staging staging = new Staging();
        List<String> serviceNames = new ArrayList<String>();
        connectedClient.createApplication(applicationName, staging, DEFAULT_MEMORY, uris, serviceNames);
        CloudApplication app = connectedClient.getApplication(applicationName);
        journal.log(Level.INFO, ">> Application details: "+ app.getName() + ", URI: " + app.getUris().get(0)+ ", Memory: " +app.getMemory());
        try {
            journal.log(Level.INFO, ">> Uploading application ... ");
            connectedClient.uploadApplication(applicationName,new File(warFile).getCanonicalPath());
            journal.log(Level.INFO, ">> Starting application ... ");
            connectedClient.startApplication(app.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
