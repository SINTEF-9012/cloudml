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
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cloudml.connectors;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rds.AmazonRDSClient;
import com.amazonaws.services.rds.model.AuthorizeDBSecurityGroupIngressRequest;
import com.amazonaws.services.rds.model.CreateDBInstanceRequest;
import com.amazonaws.services.rds.model.CreateDBSecurityGroupRequest;
import com.amazonaws.services.rds.model.DBInstance;
import com.amazonaws.services.rds.model.DeleteDBInstanceRequest;
import com.amazonaws.services.rds.model.ModifyDBInstanceRequest;
import com.amazonaws.services.rds.model.RevokeDBSecurityGroupIngressRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author huis
 */
public class AwsRDSConnector implements PaaSConnector{
    
    private static final Logger journal = Logger.getLogger(AwsRDSConnector.class.getName());
    
    private AWSCredentials awsCredentials;
    private AmazonRDSClient rdsClient;
    Map<String, CreateDBInstanceRequest> previousRequests = new HashMap<String, CreateDBInstanceRequest>();
    Map<String, DBInstance> createdInstances = new HashMap<String, DBInstance>();
    
    public AwsRDSConnector(String accessKey, String secretKey, String endpoint){
        awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        rdsClient = new AmazonRDSClient(awsCredentials);
        rdsClient.setEndpoint(endpoint);
    }
    
    public void createDBInstance(String engine, String version, String dbInstanceIdentifier, String dbName, String username, String password, 
            Integer allocatedSize, String dbInstanceClass){
        
        CreateDBInstanceRequest request = new CreateDBInstanceRequest()
                .withDBName(null)
                .withAllocatedStorage(allocatedSize)
                .withDBInstanceIdentifier(dbInstanceIdentifier)
                .withDBName(dbName)
                .withMasterUsername(username)
                .withMasterUserPassword(password)
                .withEngine(engine)
                .withPubliclyAccessible(true)
                .withEngineVersion(version);
                
        previousRequests.put(dbInstanceIdentifier, request);
        if(dbInstanceClass==null || dbInstanceClass.length()==0)
            request.setDBInstanceClass("db.t1.micro");
        else
            request.setDBInstanceClass(dbInstanceClass);
        DBInstance instance = rdsClient.createDBInstance(request);
        journal.log(Level.INFO, ">> RDS instance created: "+instance.toString());
        createdInstances.put(dbInstanceIdentifier, instance);
       
    }
    
    /**
     * Not used yet!
     * @param dbInstanceIdentifier
     * @param group
     * @param owner 
     */
    public void setSecuretGroup(String dbInstanceIdentifier, String group, String owner){
        
        String groupName = dbInstanceIdentifier+"-security-group";
        CreateDBSecurityGroupRequest csg = new CreateDBSecurityGroupRequest();
        csg.setDBSecurityGroupName(groupName);
        csg.setDBSecurityGroupDescription(groupName);
        rdsClient.createDBSecurityGroup(csg);
        
        RevokeDBSecurityGroupIngressRequest rsgi = new RevokeDBSecurityGroupIngressRequest();
        rsgi.setDBSecurityGroupName(groupName);
        rsgi.setEC2SecurityGroupId(group);
        rsgi.setEC2SecurityGroupOwnerId(owner );
        rsgi.setRequestCredentials(awsCredentials);
        //rsgi.set
        rdsClient.revokeDBSecurityGroupIngress(rsgi);
        
        
        ModifyDBInstanceRequest request = new ModifyDBInstanceRequest();
        Collection<String> groups = new ArrayList();
        groups.add(groupName);
        request.setDBSecurityGroups(groups);
        request.setDBInstanceIdentifier(dbInstanceIdentifier);
        rdsClient.modifyDBInstance(request);
        
    }
    
    public void createAndSetSecuretGroup(String dbInstanceIdentifier, Collection<String> ips){
        String groupName = dbInstanceIdentifier+"-security-group";
        CreateDBSecurityGroupRequest csg = new CreateDBSecurityGroupRequest()
                .withDBSecurityGroupName(groupName)
                .withDBSecurityGroupDescription(groupName);
        try{
            rdsClient.createDBSecurityGroup(csg);
        }catch(Exception e){
            journal.log(Level.INFO, ">> Security Group " + groupName +" already exists.");
        }
        for(String ip : ips){
            AuthorizeDBSecurityGroupIngressRequest audbgi = new AuthorizeDBSecurityGroupIngressRequest()
                    .withCIDRIP(ip + "/32")
                    .withDBSecurityGroupName(groupName);
            audbgi.setRequestCredentials(awsCredentials);
            rdsClient.authorizeDBSecurityGroupIngress(audbgi);
            
        }
        
        ModifyDBInstanceRequest request = new ModifyDBInstanceRequest();
        Collection<String> groups = new ArrayList();
        groups.add(groupName);
        request.setDBSecurityGroups(groups);
        request.setDBInstanceIdentifier(dbInstanceIdentifier);
        rdsClient.modifyDBInstance(request);
        
    }
    
    public void deleteDBInstance(String dbInstanceIdentifier){
        DeleteDBInstanceRequest request = new DeleteDBInstanceRequest();
        request.setDBInstanceIdentifier(dbInstanceIdentifier);
        rdsClient.deleteDBInstance(request);
    }
    
}
