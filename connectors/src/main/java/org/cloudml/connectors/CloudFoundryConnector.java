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
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.Staging;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by ferrynico on 03/12/2014.
 */
public class CloudFoundryConnector implements PaaSConnector {

    private CloudFoundryClient connectedClient;
    private static final int DEFAULT_MEMORY = 512; // MB

    public CloudFoundryConnector(String APIEndPoint, String login, String passwd){
        try {
            URL cloudControllerUrl = new URL(APIEndPoint);
            connectedClient = new CloudFoundryClient(new CloudCredentials(login, passwd),
                    cloudControllerUrl);
            connectedClient.login();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }


    private String computeAppUrl(String appName, String domainName) {
        return appName + "." + domainName;
    }

    @Override
    public void createEnvironmentWithWar(String applicationName, String domainName, String envName, String stackName, String warFile, String versionLabel) {
        List<String> uris = new ArrayList<String>();
        uris.add(computeAppUrl(applicationName, domainName));
        connectedClient.createApplication(applicationName, new Staging(""), DEFAULT_MEMORY, uris, null);
        CloudApplication app = connectedClient.getApplication(applicationName);
        try {
            connectedClient.uploadApplication(applicationName,new File(warFile).getCanonicalPath());
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
