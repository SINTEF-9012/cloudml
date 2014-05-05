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

import cloudadapter.Adapter;
import cloudadapter.DatabaseObject;
import com.cloudbees.api.BeesClient;
//import com.cloudbees.api.BeesClient;
import java.io.File;

import org.cloudml.core.Provider;

import eu.cloud4soa.adapter.rest.response.model.Application;
import eu.cloud4soa.adapter.rest.response.model.Database;
import eu.cloud4soa.api.datamodel.governance.ApplicationInstance;
import eu.cloud4soa.api.datamodel.governance.Credentials;
import eu.cloud4soa.api.datamodel.governance.DatabaseInfo;
import eu.cloud4soa.api.datamodel.governance.DeployApplicationParameters;
import eu.cloud4soa.api.datamodel.governance.PaasInstance;
import eu.cloud4soa.api.datamodel.governance.StartStopCommand;
import eu.cloud4soa.api.util.exception.adapter.Cloud4SoaException;
import eu.cloud4soa.governance.ems.ExecutionManagementServiceModule;
import eu.cloud4soa.governance.ems.ExecutionManagementServiceModule.Paas;
import eu.cloud4soa.governance.ems.IExecutionManagementService;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cloud4soaConnector implements PaaSConnector {

    private ApplicationInstance ai;
    private PaasInstance pi;
    private Provider provider;
    private Credentials credentials;
    private IExecutionManagementService ems;
    private String platform;
    private static final Logger journal = Logger.getLogger(Cloud4soaConnector.class.getName());

    public Cloud4soaConnector(Provider provider) {
        ems = new ExecutionManagementServiceModule();
        this.provider = provider;
        this.credentials = new Credentials(provider.getCredentials().getLogin(), provider.getCredentials().getPassword());
        pi = new PaasInstance();
        pi.setName(provider.getName());
    }

    public Cloud4soaConnector(String apiKey, String securityKey, String account, String platform) {
        credentials = new Credentials(apiKey, securityKey, account);
        this.platform = platform;
    }

    public void createEnvironmentWithWar(String applicationName, String domainName, String envName, String stackName, String warFile, String versionLabel) {
        try {
            String tmp2 = Adapter.uploadAndDeployToEnv(platform, warFile,
                                                       credentials.getPublicKey(), credentials.getPrivateKey(), credentials.getAccountName(),
                                                       applicationName, versionLabel, "", "", "", "", "", "deployed by cloudml");
            journal.log(Level.INFO, ">> Created application:" + tmp2);

        } catch (Cloud4SoaException ex) {
            Logger.getLogger(Cloud4soaConnector.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

//    private Application[] listApplications() throws Cloud4SoaException {
//    	String adapterUrl=provider.findProperty("enPoint");
//        return ems.listApplications("adapterUrl", credentials, pi);
//    }
    private void deploy(String name, File warFile) throws Cloud4SoaException {
        DeployApplicationParameters parameters = new DeployApplicationParameters();
        String adapterUrl = provider.getProperties().valueOf("enPoint");
        ai = new ApplicationInstance();
        ai.setAppName(name);
        ai.setAdapterUrl("adapterURL");
        parameters.setApplicationArchive(warFile);
        ems.deployApplication(adapterUrl, credentials, pi, ai, parameters);
    }

    private void undeploy(String name) throws Cloud4SoaException {
        String adapterUrl = provider.getProperties().valueOf("enPoint");
        ai = new ApplicationInstance();
        ai.setAppName(name);
        ai.setAdapterUrl("adapterURL");
        ems.undeployApplication(adapterUrl, credentials, pi, ai);
    }

    public void uploadWar(String warFile, String versionLabel, String applicationName, String envName, int timeout) {

        System.out.print("uploadWar:");
        while (timeout-- > 0) {
            System.out.print("-");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BeanstalkConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                String tmp2 = Adapter.uploadAndDeployToEnv(platform, warFile,
                                                           credentials.getPublicKey(), credentials.getPrivateKey(), credentials.getAccountName(),
                                                           envName, versionLabel, "", "", "", "", "", "deployed by cloudml after db injection");
                journal.log(Level.INFO, ">> Created application:" + tmp2);
                break;
            } catch (Exception e) {
            }

        }

    }

//    private void startStop(StartStopCommand command) throws Cloud4SoaException {
//        ems.startStopApplication("adapterUrl", credentials, pi, ai, command);
//    }
//    
//    private Database getDatabaseJob() throws Cloud4SoaException {
//    	return null;
////        return ems.getDatabase(ai, PUBLIC_KEY, PRIVATE_KEY, ACCOUNT_KEY, dbName);
//    }
//    
//    private Database[] listDatabasesJob() throws Cloud4SoaException {
//    	return null;
//        
////        return ems.listDatabases(ai, PUBLIC_KEY, PRIVATE_KEY, ACCOUNT_KEY);
//    }
//    private void createDatabaseJob() throws Cloud4SoaException {
//        ems.createDatabase(ai, pi, PUBLIC_KEY, PRIVATE_KEY, ACCOUNT_KEY, dbName, dbuser,
//                dbpassword, dbtype);
//    }
//    private void deleteDatabaseJob() throws Cloud4SoaException {
//        ems.deleteDatabase(ai, pi, PUBLIC_KEY, PRIVATE_KEY, ACCOUNT_KEY, dbName, dbuser,
//                dbpassword, dbtype);
    //   }
    @Override
    public void createDBInstance(String engine, String version, String dbInstanceIdentifier, String dbName, String username, String password,
                                 Integer allocatedSize, String dbInstanceClass) {
        try {
            DatabaseInfo dbinfo = Adapter.createDB(platform, credentials.getPublicKey(), credentials.getPrivateKey(), credentials.getAccountName(),
                                                   dbInstanceIdentifier, engine, version, "created by cloudml",
                                                   dbName, username, password);

            journal.log(Level.INFO, ">>DB created: " + dbinfo.toString() + ": " + dbinfo.getDatabaseUrl() + dbinfo.getHost());

        } catch (Cloud4SoaException ex) {
            Logger.getLogger(Cloud4soaConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Adapter.
    }

    /**
     * Cloud4Soa does not work for Cloudbees
     *
     * @param dbInstanceId
     * @param timeout
     * @return
     */
    public String getDBEndPoint(String dbInstanceId, int timeout) {

        //return "ec2-23-21-211-172.compute-1.amazonaws.com:3306/dieaslefehik";
//        String bees_server = "https://api.cloudbees.com/api";
//        String type = "json";
//        String apiversion = "1.0";
//        BeesClient bees = new BeesClient(bees_server, credentials.getPublicKey(), credentials.getPrivateKey(), type, apiversion);
//        try {
//            Object obj = bees.databaseInfo(dbInstanceId, false);
//            if(obj)
//            System.out.println(obj);
//        } catch (Exception ex) {
//            Logger.getLogger(Cloud4soaConnector.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return "";
        System.out.print("Retriving DB endpoint:");
        while (timeout-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Cloud4soaConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                DatabaseObject dbobject = Adapter.getDBInfo(platform,
                                                            credentials.getPublicKey(),
                                                            credentials.getPrivateKey(),
                                                            credentials.getAccountName(),
                                                            "mysql", "", "1.5.7", null, dbInstanceId, null, null);
                if (dbobject.getDbhost() != null && dbobject.getDbhost().length() > 0) {
                    System.out.println(dbobject.getDbhost() + ":" + dbobject.getPort());
                    return dbobject.getDbhost() + ":" + dbobject.getPort();
                }
            } catch (Cloud4SoaException ex) {
                Logger.getLogger(Cloud4soaConnector.class.getName()).log(Level.SEVERE, null, ex);

            }

        }
        return "";
    }
}
