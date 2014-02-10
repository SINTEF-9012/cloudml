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

import org.cloudml.core.Provider;
/*
import eu.cloud4soa.adapter.rest.response.model.Application;
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
    	// where to put this ?
    	//pi = new PaasInstance();
        //pi.setName(name);
    	//ai = new ApplicationInstance();
        //ai.setAdapterUrl(paasInfo.getAdapterUrl());
        //ai.setAppName(appName);
    }
    
    private Application[] listApplications(String paasName) throws Cloud4SoaException {
    	pi = new PaasInstance();
        pi.setName(paasName);
        return ems.listApplications("adapterUrl", credentials, pi);
    }
    
    private void deploy() throws Cloud4SoaException {
        DeployApplicationParameters parameters = new DeployApplicationParameters();
        //TO BE defined
        //File file = new File(this.getClass().getResource("/SampleApp2.war").getFile());
        //parameters.setApplicationArchive(file);
        ems.deployApplication("adapterUrl", credentials, pi, ai, parameters);
    }
    
    private void undeploy() throws Cloud4SoaException {        
        ems.undeployApplication("adapterUrl", credentials, pi, ai);
    }


    private void startStop(StartStopCommand command) throws Cloud4SoaException {
        ems.startStopApplication("adapterUrl", credentials, pi, ai, command);
    }*/
    
}
