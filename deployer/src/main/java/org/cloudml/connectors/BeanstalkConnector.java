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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.elasticbeanstalk.*;
import com.amazonaws.services.elasticbeanstalk.model.CheckDNSAvailabilityRequest;
import com.amazonaws.services.elasticbeanstalk.model.CheckDNSAvailabilityResult;
import com.amazonaws.services.elasticbeanstalk.model.CreateApplicationRequest;
import com.amazonaws.services.elasticbeanstalk.model.CreateApplicationResult;
import com.amazonaws.services.elasticbeanstalk.model.CreateApplicationVersionRequest;
import com.amazonaws.services.elasticbeanstalk.model.CreateEnvironmentRequest;
import com.amazonaws.services.elasticbeanstalk.model.CreateEnvironmentResult;
import com.amazonaws.services.elasticbeanstalk.model.DeleteApplicationRequest;
import com.amazonaws.services.elasticbeanstalk.model.ListAvailableSolutionStacksResult;
import com.amazonaws.services.elasticbeanstalk.model.S3Location;
import com.amazonaws.services.elasticbeanstalk.model.TerminateEnvironmentRequest;
import com.amazonaws.services.elasticbeanstalk.model.UpdateEnvironmentRequest;
import com.amazonaws.services.elasticbeanstalk.model.UpdateEnvironmentResult;
import com.amazonaws.services.identitymanagement.model.CreateAccessKeyResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.*;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstanceAttributeRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceAttributeResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.elasticbeanstalk.model.DescribeEnvironmentResourcesRequest;
import com.amazonaws.services.elasticbeanstalk.model.DescribeEnvironmentResourcesResult;
import com.amazonaws.services.elasticbeanstalk.model.Instance;
import com.amazonaws.services.rds.AmazonRDSClient;
import com.amazonaws.services.rds.model.AuthorizeDBSecurityGroupIngressRequest;
import com.amazonaws.services.rds.model.CreateDBInstanceRequest;
import com.amazonaws.services.rds.model.CreateDBSecurityGroupRequest;
import com.amazonaws.services.rds.model.DBInstance;
import com.amazonaws.services.rds.model.DeleteDBInstanceRequest;
import com.amazonaws.services.rds.model.DescribeDBInstancesRequest;
import com.amazonaws.services.rds.model.DescribeDBInstancesResult;
import com.amazonaws.services.rds.model.Endpoint;
import com.amazonaws.services.rds.model.InvalidDBInstanceStateException;
import com.amazonaws.services.rds.model.ModifyDBInstanceRequest;
import com.amazonaws.services.rds.model.RevokeDBSecurityGroupIngressRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * PaasConnector for BeansTalk and RDS
 *
 * @author ? for beanstalk
 * @author Hui Song for rds
 */
public class BeanstalkConnector implements PaaSConnector {
    /*
     Classical process:
     1- create application (name, description) 
     2- create environment (tier=worker|webserver, platform=tomcat|nodejs..., type=autoscaling|single)
     3- create version (upload your app)
     4- check dns availability
     5- refine env info (name, url, description)
     6- select key and type of instance
     7- create
     */

    private static final Logger journal = Logger.getLogger(BeanstalkConnector.class.getName());

    //private String endpoint="elasticbeanstalk.eu-west-1.amazonaws.com";
    private AWSElasticBeanstalkClient beanstalkClient;
    private AWSCredentials awsCredentials;
    private String beanstalkEndpoint;
    private String rdsEndpoint;
    private AmazonRDSClient rdsClient;
    


    Map<String, CreateDBInstanceRequest> previousRequests = new HashMap<String, CreateDBInstanceRequest>();
    Map<String, DBInstance> createdInstances = new HashMap<String, DBInstance>();

    public BeanstalkConnector(String login, String pass, String region) {
        awsCredentials = new BasicAWSCredentials(login, pass);
        beanstalkClient = new AWSElasticBeanstalkClient(awsCredentials);
        this.beanstalkEndpoint = String.format("elasticbeanstalk.%s.amazonaws.com", region);
        beanstalkClient.setEndpoint(beanstalkEndpoint);

        this.rdsEndpoint = String.format("rds.%s.amazonaws.com", region);
        rdsClient = new AmazonRDSClient(awsCredentials);
        rdsClient.setEndpoint(rdsEndpoint);
    }

    public void createApplication(String name) {
        CreateApplicationRequest cr = new CreateApplicationRequest();
        cr.setApplicationName(name);
        cr.setDescription("Generated by CloudML");
        CreateApplicationResult res = beanstalkClient.createApplication(cr);
        journal.log(Level.INFO, ">> Status of the application creation: " + res.toString());
    }

    public void terminateApplication(String name) {
        DeleteApplicationRequest dr = new DeleteApplicationRequest(name);
        beanstalkClient.deleteApplication(dr);
    }

    public CheckDNSAvailabilityResult checkDNS(String domainName) {
        CheckDNSAvailabilityRequest cr = new CheckDNSAvailabilityRequest(domainName);
        CheckDNSAvailabilityResult res = beanstalkClient.checkDNSAvailability(cr);
        journal.log(Level.INFO, ">> Domain Name availability: " + res.toString());
        return res;
    }

    public void createEnvironment(String applicationName, String domainName, String envName, String stackName) {
        CreateEnvironmentRequest cr = new CreateEnvironmentRequest();
        cr.setApplicationName(applicationName);
        cr.setEnvironmentName(envName);
        String stack = findSolutionStack(stackName);
        if (!stack.equals("")) {
            cr.setSolutionStackName(stack);
            CheckDNSAvailabilityResult r = checkDNS(domainName);
            if (r.isAvailable()) {
                cr.setCNAMEPrefix(domainName);
                CreateEnvironmentResult res = beanstalkClient.createEnvironment(cr);
                journal.log(Level.INFO, ">> Status of the environment creation: " + res.toString());
            } else {
                journal.log(Level.INFO, ">> Status of the environment creation: Domain Name already existing");
            }
        } else {
            journal.log(Level.INFO, ">> Status of the environment creation: This type of stack does not exist!");
        }
    }


    public void createEnvironmentWithWar(String applicationName, String domainName, String envName, String stackName, String warFile, String versionLabel) {
        prepareWar(new File(warFile), versionLabel, applicationName);
        CreateEnvironmentRequest cr = new CreateEnvironmentRequest();

        cr.setApplicationName(applicationName);
        cr.setEnvironmentName(envName);
        cr.setVersionLabel(versionLabel);
        String stack = findSolutionStack(stackName);
        if (!stack.equals("")) {
            cr.setSolutionStackName(stack);
            CheckDNSAvailabilityResult r = checkDNS(domainName);
            if (r.isAvailable()) {
                cr.setCNAMEPrefix(domainName);
                CreateEnvironmentResult res = beanstalkClient.createEnvironment(cr);
                journal.log(Level.INFO, ">> Status of the environment creation: " + res.toString());
            } else {
                journal.log(Level.INFO, ">> Status of the environment creation: Domain Name already existing");
            }
        } else {
            journal.log(Level.INFO, ">> Status of the environment creation: This type of stack does not exist!");
        }
    }

    public void uploadWar(String warFile, String versionLabel, String applicationName, String envName, int timeout) {
        
        prepareWar(new File(warFile), versionLabel, applicationName);
        System.out.print("uploadWar:");
        while(timeout-- > 0){
            System.out.print("-");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BeanstalkConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
            try{
                
                UpdateEnvironmentResult updateEnvironment
                        = beanstalkClient.updateEnvironment(new UpdateEnvironmentRequest()
                                .withEnvironmentName(envName)
                                .withVersionLabel(versionLabel));
                break;
            }
            catch(com.amazonaws.AmazonServiceException e){
                
            }
            
        }

    }

    public void prepareWar(File warFile, String versionLabel, String applicationName) {
        AmazonS3 s3 = new AmazonS3Client(awsCredentials);
        String bucketName = beanstalkClient.createStorageLocation().getS3Bucket();
        String key;
        try {
            key = URLEncoder.encode(warFile.getName() + "-" + versionLabel, "UTF-8");
            s3.putObject(bucketName, key, warFile);
            beanstalkClient.createApplicationVersion(new CreateApplicationVersionRequest()
                    .withApplicationName(applicationName).withAutoCreateApplication(true)
                    .withVersionLabel(versionLabel)
                    .withSourceBundle(new S3Location(bucketName, key)));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void terminateEnvironment(String envName) {
        TerminateEnvironmentRequest tr = new TerminateEnvironmentRequest();
        tr.setEnvironmentName(envName);
        beanstalkClient.terminateEnvironment(tr);
    }

    //to be improved with the detailed version
    public String findSolutionStack(String name) {
        ListAvailableSolutionStacksResult list = beanstalkClient.listAvailableSolutionStacks();
        for (String s : list.getSolutionStacks()) {
            if (s.contains(name)) {
                return s;
            }
        }
        return "";
    }

    public Collection<String> getEnvIPs(String envName, int timeout) {
        DescribeEnvironmentResourcesRequest request = new DescribeEnvironmentResourcesRequest()
                .withEnvironmentName(envName);
        List<Instance> instances = null;
        System.out.print("Waiting for environment ips");
        while(timeout-->0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BeanstalkConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.print("-");
            DescribeEnvironmentResourcesResult res = beanstalkClient.describeEnvironmentResources(request);
            instances = res.getEnvironmentResources().getInstances();
            if(instances.size()==0)
                continue;
            AmazonEC2Client ec2 = new AmazonEC2Client(awsCredentials);
            ec2.setEndpoint(beanstalkEndpoint.replace("elasticbeanstalk", "ec2"));
            List<String> instanceIds = new ArrayList<String>();
            for (Instance instance : instances) {
                instanceIds.add(instance.getId());
            }
            List<String> ips = new ArrayList<String>();
            DescribeInstancesRequest desins = new DescribeInstancesRequest().withInstanceIds(instanceIds);
            DescribeInstancesResult desinres = ec2.describeInstances(desins);
            for (Reservation reservation : desinres.getReservations()) {
                for (com.amazonaws.services.ec2.model.Instance ins : reservation.getInstances()) {
                    String ip = ins.getPublicIpAddress();
                    if(ip!=null && ip.length()>0)
                        ips.add(ip);
                }
            }
            if(ips.size()>0)
                return ips;
            
        }
        return Collections.EMPTY_LIST;
    }

    public void createDBInstance(String engine, String version, String dbInstanceIdentifier, String dbName, String username, String password,
            Integer allocatedSize, String dbInstanceClass) {

        String groupName = dbInstanceIdentifier + "-security-group";
        CreateDBSecurityGroupRequest csg = new CreateDBSecurityGroupRequest()
                .withDBSecurityGroupName(groupName)
                .withDBSecurityGroupDescription(groupName);
        try {
            rdsClient.createDBSecurityGroup(csg);
        } catch (Exception e) {
            journal.log(Level.INFO, ">> Security Group " + groupName + " already exists.");
        }
        
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
        request.getDBSecurityGroups().add(groupName);
        
        previousRequests.put(dbInstanceIdentifier, request);
        if (dbInstanceClass == null || dbInstanceClass.length() == 0) {
            request.setDBInstanceClass("db.t1.micro");
        } else {
            request.setDBInstanceClass(dbInstanceClass);
        }
        DBInstance instance = rdsClient.createDBInstance(request);
        journal.log(Level.INFO, String.format(">> RDS instance created: %s, at %s", instance.toString(),instance.getEndpoint()));
        
        createdInstances.put(dbInstanceIdentifier, instance);

    }

    /**
     * Not used yet!
     *
     * @param dbInstanceIdentifier
     * @param group
     * @param owner
     */
    public void setSecuretGroup(String dbInstanceIdentifier, String group, String owner) {

        String groupName = dbInstanceIdentifier + "-security-group";
        CreateDBSecurityGroupRequest csg = new CreateDBSecurityGroupRequest();
        csg.setDBSecurityGroupName(groupName);
        csg.setDBSecurityGroupDescription(groupName);
        rdsClient.createDBSecurityGroup(csg);

        RevokeDBSecurityGroupIngressRequest rsgi = new RevokeDBSecurityGroupIngressRequest();
        rsgi.setDBSecurityGroupName(groupName);
        rsgi.setEC2SecurityGroupId(group);
        rsgi.setEC2SecurityGroupOwnerId(owner);
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

    public void openDBForIps(String dbInstanceIdentifier, Collection<String> ips, int timeout) {
        String groupName = dbInstanceIdentifier + "-security-group";
        for (String ip : ips) {
            AuthorizeDBSecurityGroupIngressRequest audbgi = new AuthorizeDBSecurityGroupIngressRequest()
                    .withCIDRIP(ip + "/32")
                    .withDBSecurityGroupName(groupName);
            audbgi.setRequestCredentials(awsCredentials);
            rdsClient.authorizeDBSecurityGroupIngress(audbgi);
            
        }
        rdsClient.authorizeDBSecurityGroupIngress(new AuthorizeDBSecurityGroupIngressRequest()
                .withCIDRIP("0.0.0.0/0")
                .withDBSecurityGroupName(groupName)
        );
        ModifyDBInstanceRequest request = new ModifyDBInstanceRequest();
        Collection<String> groups = new ArrayList();
        groups.add(groupName);
        request.setDBSecurityGroups(groups);
        request.setDBInstanceIdentifier(dbInstanceIdentifier);
        System.out.print("Modifying security group");
        while(timeout-->0){
            System.out.print("-");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BeanstalkConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try{
                rdsClient.modifyDBInstance(request);
                break;
            }catch(Exception e){
                continue;
            }
            
            
        }

    }
    
    public String getDBEndPoint(String dbInstanceId, int timeout){
        DescribeDBInstancesRequest ddbir = new DescribeDBInstancesRequest()
                .withDBInstanceIdentifier(dbInstanceId);
        System.out.print("Wainting for DB endpoints");
        while(timeout -- > 0){
            System.out.print("-");
            DescribeDBInstancesResult ddbi = rdsClient.describeDBInstances(ddbir);
            Endpoint endpoint = ddbi.getDBInstances().get(0).getEndpoint();
            if(endpoint != null && endpoint.toString().length()!=0)
                return endpoint.getAddress()+":"+endpoint.getPort();
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BeanstalkConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "";
    }
    
    public String getDBStatus(String dbInstanceId){
         DescribeDBInstancesRequest ddbir = new DescribeDBInstancesRequest()
                .withDBInstanceIdentifier(dbInstanceId);
         DescribeDBInstancesResult ddbi = rdsClient.describeDBInstances(ddbir);
         try{
             return ddbi.getDBInstances().get(0).getStatusInfos().toString();
         }catch(Exception e){
             throw new RuntimeException("DBInstance not found");
         }
    }

    public void deleteDBInstance(String dbInstanceIdentifier) {
        DeleteDBInstanceRequest request = new DeleteDBInstanceRequest();
        request.setDBInstanceIdentifier(dbInstanceIdentifier);
        rdsClient.deleteDBInstance(request);

    }

}
