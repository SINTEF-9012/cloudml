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

import java.io.File;

import org.cloudml.core.Provider;

/*import eu.cloud4soa.adapter.rest.response.model.Application;
import eu.cloud4soa.adapter.rest.response.model.Database;
import eu.cloud4soa.api.datamodel.governance.ApplicationInstance;
import eu.cloud4soa.api.datamodel.governance.Credentials;
import eu.cloud4soa.api.datamodel.governance.DeployApplicationParameters;
import eu.cloud4soa.api.datamodel.governance.PaasInstance;
import eu.cloud4soa.api.datamodel.governance.StartStopCommand;
import eu.cloud4soa.api.util.exception.adapter.Cloud4SoaException;
import eu.cloud4soa.governance.ems.ExecutionManagementServiceModule;
import eu.cloud4soa.governance.ems.ExecutionManagementServiceModule.Paas;
import eu.cloud4soa.governance.ems.IExecutionManagementService;
*/

public class Cloud4soaConnector {
	
	/*private ApplicationInstance ai;
    private PaasInstance pi;
    private Provider provider;
    private Credentials credentials;
    private IExecutionManagementService ems; 
	
    public Cloud4soaConnector(Provider provider){
    	ems = new ExecutionManagementServiceModule();
    	this.provider=provider;
    	this.credentials=new Credentials(provider.getLogin(),provider.getPasswd());
    	pi = new PaasInstance();
        pi.setName(provider.getName());
    }
    
    private Application[] listApplications() throws Cloud4SoaException {
    	String adapterUrl=provider.getProperty("enPoint");
        return ems.listApplications("adapterUrl", credentials, pi);
    }
    
    private void deploy(String name, File warFile) throws Cloud4SoaException {
        DeployApplicationParameters parameters = new DeployApplicationParameters();
        String adapterUrl=provider.getProperty("enPoint");
        ai = new ApplicationInstance();
        ai.setAppName(name);
        ai.setAdapterUrl("adapterURL");
        parameters.setApplicationArchive(warFile);
        ems.deployApplication(adapterUrl, credentials, pi, ai, parameters);
    }
    
    private void undeploy(String name) throws Cloud4SoaException {
    	String adapterUrl=provider.getProperty("enPoint");
    	ai = new ApplicationInstance();
        ai.setAppName(name);
        ai.setAdapterUrl("adapterURL");
        ems.undeployApplication(adapterUrl, credentials, pi, ai);
    }


    private void startStop(StartStopCommand command) throws Cloud4SoaException {
        ems.startStopApplication("adapterUrl", credentials, pi, ai, command);
    }
    
    private Database getDatabaseJob() throws Cloud4SoaException {
    	return null;
//        return ems.getDatabase(ai, PUBLIC_KEY, PRIVATE_KEY, ACCOUNT_KEY, dbName);
    }
    
    private Database[] listDatabasesJob() throws Cloud4SoaException {
    	return null;
        
//        return ems.listDatabases(ai, PUBLIC_KEY, PRIVATE_KEY, ACCOUNT_KEY);
    }
    
    private void createDatabaseJob() throws Cloud4SoaException {
//        ems.createDatabase(ai, pi, PUBLIC_KEY, PRIVATE_KEY, ACCOUNT_KEY, dbName, dbuser,
//                dbpassword, dbtype);
    }

    private void deleteDatabaseJob() throws Cloud4SoaException {
//        ems.deleteDatabase(ai, pi, PUBLIC_KEY, PRIVATE_KEY, ACCOUNT_KEY, dbName, dbuser,
//                dbpassword, dbtype);
    }*/
    
}
