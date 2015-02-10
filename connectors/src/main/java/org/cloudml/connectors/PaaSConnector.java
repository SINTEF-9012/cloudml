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

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface PaaSConnector{
    
    void createEnvironmentWithWar(String applicationName, String domainName, String envName, String stackName, String warFile, String versionLabel);
    
    void createDBInstance(String engine, String version, String dbInstanceIdentifier, String dbName, String username, String password,
            Integer allocatedSize, String dbInstanceClass, String securityGroup);
    
    String getDBEndPoint(String dbInstanceId, int timeout);
    
    public void uploadWar(String warFile, String versionLabel, String applicationName, String envName, int timeout);

    public String createQueue(String name);

    public void deleteQueue(String name);

    public List<String> listQueues();

    public void restoreDB(String host, String port, String dbUser,String dbPass, String dbName, String local_file);
    
    public void configAppParameters(String applicationName, Map<String,String> params);

    public void bindDbToApp(String appId, String dbId, String alias);

//Should be part of the interface at some point
	//public void createApplication();
	
	//public void deployApplication();
	
	//public void undeployApplication();
	
	//public void terminateApplication();
	
	//public void startApplication();
	
	//public void stopApplication();
	
	//public void updateApplication();
	
	//public void createEnvironment();
	
	//public void terminateEnvironment();
	
	//public void listApplication();
	
}
