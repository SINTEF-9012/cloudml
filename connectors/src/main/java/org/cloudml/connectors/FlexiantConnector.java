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


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.cloudml.core.ComponentInstance;
import org.cloudml.core.VM;
import org.cloudml.core.VMInstance;
import org.cloudml.core.Property;

import net.flexiant.extility.*;

public class FlexiantConnector implements Connector{

    private static final Logger journal = Logger.getLogger(FlexiantConnector.class.getName());

    private final String endpoint;
    private UserService service;
    private BindingProvider portBP;
    private HashMap<String,Object> runtimeInformation=new HashMap<String, Object>();

    @SuppressWarnings("restriction")
    public FlexiantConnector(String endPoint, String login, String secretKey) throws MalformedURLException{
        this.endpoint=endPoint;
        System.setProperty ("jsse.enableSNIExtension", "false");

        journal.log(Level.INFO, ">> Connecting to Flexiant ...");

        URL url = ClassLoader.getSystemClassLoader().getResource(
                "UserAdmin.wsdl");

        // Get the UserAPI
        UserAPI api = new UserAPI(url,
                new QName("http://extility.flexiant.net", "UserAPI"));

        // and set the service port on the service
        service = api.getUserServicePort();

        // Get the binding provider
        BindingProvider portBP = (BindingProvider) service;

        // and set the service endpoint
        portBP.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                endPoint);

        journal.log(Level.INFO, ">> Authenticating ...");
        // and the caller's authentication details and password
        portBP.getRequestContext().put(BindingProvider.USERNAME_PROPERTY,
                login);
        portBP.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY,
                secretKey);

    }


    public List<Object> getListServer(){
        try {
            // Create an FQL filter and a filter condition
            SearchFilter sf = new SearchFilter();
            FilterCondition fc = new FilterCondition();

            // set the condition type
            fc.setCondition(Condition.IS_EQUAL_TO);

            // the field to be matched
            fc.setField("status");

            // and a list of values
            fc.getValue().add(ServerStatus.RUNNING.name());
            fc.getValue().add(ServerStatus.STARTING.name());

            // Add the filter condition to the query
            sf.getFilterConditions().add(fc);

            // Set a limit to the number of results
            QueryLimit lim = new QueryLimit();
            lim.setMaxRecords(50);

            // Call the service to execute the query
            ListResult result = service.listResources(sf,lim, ResourceType.SERVER);

            return result.getList();

        } catch (Exception e) {
            journal.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    public List<Object> getListImages(){
        try {
            // Create an FQL filter and a filter condition
            SearchFilter sf = new SearchFilter();
            FilterCondition fc = new FilterCondition();

            // the field to be matched
            fc.setField("ImageType");

            // set the condition type
            fc.setCondition(Condition.IS_EQUAL_TO);

            // Add the filter condition to the query
            sf.getFilterConditions().add(fc);

            // and a list of values
            fc.getValue().add(ImageType.DISK.name());

            // Set a limit to the number of results
            QueryLimit lim = new QueryLimit();
            lim.setMaxRecords(50);

            // Call the service to execute the query
            ListResult result = service.listResources(sf,lim, ResourceType.IMAGE);
            return result.getList();
        } catch (Exception e) {
            journal.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    public HashMap<String,Object> createInstance(VMInstance a){
        ComponentInstance.State state = ComponentInstance.State.UNRECOGNIZED;
        try {
            Server template = new Server();
            if(findResourceByName(a.getName(), ResourceType.SERVER).equals("")){
                VM vm = a.getType();
                List<String> sshKeyList = new ArrayList<String> ();

                template.setResourceType(ResourceType.SERVER);

                journal.log(Level.INFO, ">> Provisioning a vm ...");

                if (vm.getMinCores() > 0 && vm.getMinRam() > 0)
                    template.setProductOfferUUID(findProduct(((double) vm.getMinRam()), vm.getMinCores()));

                if (!vm.getGroupName().equals(""))
                    template.setVdcUUID(findResourceByName(vm.getGroupName(),ResourceType.VDC));

                if(!vm.getImageId().equals(""))
                    template.setImageUUID(findResourceByName(vm.getImageId(),ResourceType.IMAGE)); //TODO: find by OS

                if(!vm.getSshKey().equals(""))
                    sshKeyList.add(findResourceByName(vm.getSshKey(),ResourceType.SSHKEY));

                template.setResourceName(a.getName());
                Nic n=new Nic();
                if(findResourceByName("Default network - Default cluster", ResourceType.NETWORK).equals(""))
                    n.setNetworkUUID(findResourceByName("Default network - KVM", ResourceType.NETWORK));
                else n.setNetworkUUID(findResourceByName("Default network - Default cluster", ResourceType.NETWORK));
                n.setNetworkName(a.getName());
                template.getNics().add(n);

                Disk d = new Disk();
                d.setSize(vm.getMinStorage());
                d.setResourceName(a.getName());
                template.getDisks().add(d);

                //TODO: Add disk
                Job job=service.createServer(template, sshKeyList, null);
                service.waitForJob(job.getResourceUUID(), false);

                a.getProperties().add(new Property("ProviderSpecificInstanceType", findProductName(((double) vm.getMinRam()), vm.getMinCores())));
                a.setProviderSpecificType(findProductName(((double) vm.getMinRam()), vm.getMinCores()));
                a.setId(findResourceByName(a.getName(), ResourceType.SERVER));

				/*Job nicJob=service.createNetworkInterface(n, null);
				service.waitForJob(nicJob.getResourceUUID(), false);

				service.attachNetworkInterface(job.getItemUUID(), nicJob.getItemUUID(), 0, null);*/
                journal.log(Level.INFO, ">> vm type: "+ findProductName(((double) vm.getMinRam()), vm.getMinCores()) +  " named " + template.getResourceName());

                Job startJob=service.changeServerStatus(a.getId(), ServerStatus.RUNNING, true, null, null);
                service.waitForJob(startJob.getResourceUUID(), false);
                Thread.sleep(100000);

                //System.out.println(((Server) findObjectResourceByName(a.getName(), ResourceType.SERVER)).getInitialPassword());
            }
            a.setId(findResourceByName(a.getName(), ResourceType.SERVER));
            Server temp=(Server)findObjectResourceByName(a.getName(), ResourceType.SERVER);
            a.setCore(temp.getCpu());
            //a.setPublicAddress(temp.getNics().get(0).getIpAddresses().get(0).getIpAddress());
            runtimeInformation.put("publicAddress", temp.getNics().get(0).getIpAddresses().get(0).getIpAddress());
            journal.log(Level.INFO, ">> Running VM: " + a.getName() + " id: " + a.getId() + " with public address: " + a.getPublicAddress());
            //a.setStatusAsRunning();
            state = ComponentInstance.State.RUNNING;
        } catch (ExtilityException e) {
            // TODO Auto-generated catch block
            journal.log(Level.SEVERE, e.getMessage());
            //a.setStatusAsError();
            state = ComponentInstance.State.ERROR;
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            journal.log(Level.SEVERE, e.getMessage());
            //a.setStatusAsError();
            state = ComponentInstance.State.ERROR;
        }
        runtimeInformation.put("status", state);
        return runtimeInformation;
    }

    public void execCommand(VMInstance n, String command, String login, String keyPath){
        SSHConnector sc=new SSHConnector(keyPath, login, n.getPublicAddress());
        sc.execCommandSsh(command);
    }


    public void execCommand(String id, String command, String login, String keyPath){

        Server temp=(Server)findObjectResourceByID(id, ResourceType.SERVER);
        String ip=temp.getNics().get(0).getIpAddresses().get(0).getIpAddress();
        SSHConnector sc=new SSHConnector(keyPath, login, ip);
        while (!sc.checkConnectivity()){
            try {
                Thread.sleep(32000);
            } catch (InterruptedException e) {
                journal.log(Level.SEVERE, e.getMessage());
            }
        }
        try {
            Thread.sleep(32000);
        } catch (InterruptedException e) {
            journal.log(Level.SEVERE, e.getMessage());
        }
        sc.execCommandSsh(command);
    }


    public String findResourceByName(String name, ResourceType t){
        try {
            // Create an FQL filter and a filter condition
            SearchFilter sf = new SearchFilter();
            FilterCondition fc = new FilterCondition();

            // the field to be matched
            fc.setField("ResourceName");

            // set the condition type
            fc.setCondition(Condition.IS_EQUAL_TO);

            // Add the filter condition to the query
            sf.getFilterConditions().add(fc);

            // and a list of values
            fc.getValue().add(name);

            // Set a limit to the number of results
            QueryLimit lim = new QueryLimit();
            lim.setMaxRecords(50);
            lim.setLoadChildren(true);

            // Call the service to execute the query
            ListResult result = service.listResources(sf,lim, t);
            if(result.getList().size() >= 0){
                Resource s= (Resource) result.getList().get(0);
                return s.getResourceUUID();
            }else{
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public Object findObjectResourceByName(String name, ResourceType t){
        try {
            // Create an FQL filter and a filter condition
            SearchFilter sf = new SearchFilter();
            FilterCondition fc = new FilterCondition();

            // the field to be matched
            fc.setField("ResourceName");

            // set the condition type
            fc.setCondition(Condition.IS_EQUAL_TO);

            // Add the filter condition to the query
            sf.getFilterConditions().add(fc);

            // and a list of values
            fc.getValue().add(name);

            // Set a limit to the number of results
            QueryLimit lim = new QueryLimit();
            lim.setMaxRecords(50);
            lim.setLoadChildren(true);

            // Call the service to execute the query
            ListResult result = service.listResources(sf,lim, t);
            return result.getList().get(0);
        } catch (Exception e) {
            journal.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    public Object findObjectResourceByID(String id, ResourceType t){
        try {
            // Create an FQL filter and a filter condition
            SearchFilter sf = new SearchFilter();
            FilterCondition fc = new FilterCondition();

            // the field to be matched
            fc.setField("ResourceUUID");

            // set the condition type
            fc.setCondition(Condition.IS_EQUAL_TO);

            // Add the filter condition to the query
            sf.getFilterConditions().add(fc);

            // and a list of values
            fc.getValue().add(id);

            // Set a limit to the number of results
            QueryLimit lim = new QueryLimit();
            lim.setMaxRecords(100);
            lim.setLoadChildren(true);

            // Call the service to execute the query
            ListResult result = service.listResources(sf,lim, t);
            return result.getList().get(0);
        } catch (Exception e) {
            journal.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    public String findProductName(double ram, int CPU){
        try {
            // Create an FQL filter and a filter condition
            SearchFilter sf = new SearchFilter();
            FilterCondition fc = new FilterCondition();

            // the field to be matched
            fc.setField("ResourceName");

            // set the condition type
            fc.setCondition(Condition.CONTAINS);

            // Add the filter condition to the query
            sf.getFilterConditions().add(fc);

            // and a list of values
            fc.getValue().add(CPU+" CPU");

            // Set a limit to the number of results
            QueryLimit lim = new QueryLimit();
            lim.setMaxRecords(50);

            // Call the service to execute the query
            ListResult result = service.listResources(sf,lim, ResourceType.PRODUCTOFFER);

            if(result.getList().size() > 0){
                ProductOffer p=(ProductOffer)result.getList().get(0);
                Double min=5000.0;
                for(Object o : result.getList()) {
                    ProductOffer s = ((ProductOffer)o);
                    for(ProductComponent t : s.getComponentConfig()){
                        for(Value v: t.getProductConfiguredValues()){
                            if(v.getKey().equals("ram")){
                                double productRam=Double.parseDouble(v.getValue()); //RAM value
                                if(productRam >= ram && productRam <= min){
                                    min=productRam;
                                    p=s;
                                }
                            }
                        }
                    }
                }
                return p.getResourceName();
            }
            return "";
        } catch (Exception e) {
            journal.log(Level.SEVERE, e.getMessage());
            return "";
        }
    }


    public String findProduct(double ram, int CPU){
        try {
            // Create an FQL filter and a filter condition
            SearchFilter sf = new SearchFilter();
            FilterCondition fc = new FilterCondition();

            // the field to be matched
            fc.setField("ResourceName");

            // set the condition type
            fc.setCondition(Condition.CONTAINS);

            // Add the filter condition to the query
            sf.getFilterConditions().add(fc);

            // and a list of values
            fc.getValue().add(CPU+" CPU");

            // Set a limit to the number of results
            QueryLimit lim = new QueryLimit();
            lim.setMaxRecords(50);

            // Call the service to execute the query
            ListResult result = service.listResources(sf,lim, ResourceType.PRODUCTOFFER);

            if(result.getList().size() > 0){
                ProductOffer p=(ProductOffer)result.getList().get(0);
                Double min=5000.0;
                for(Object o : result.getList()) {
                    ProductOffer s = ((ProductOffer)o);
                    for(ProductComponent t : s.getComponentConfig()){
                        for(Value v: t.getProductConfiguredValues()){
                            if(v.getKey().equals("ram")){
                                double productRam=Double.parseDouble(v.getValue()); //RAM value
                                if(productRam >= ram && productRam <= min){
                                    min=productRam;
                                    p=s;
                                }
                            }
                        }
                    }
                }
                return p.getResourceUUID();
            }
            return "";
        } catch (Exception e) {
            journal.log(Level.SEVERE, e.getMessage());
            return "";
        }
    }

    public String getEndpoint(){
        return this.endpoint;
    }

    public void destroyVM(String id) {
        // TODO Auto-generated method stub
        journal.log(Level.INFO, ">> Not yet implemented ");
    }


    /**
     * create the snapshot of a disk (only feature supported now on the MODAClouds platform)
     * @param vmi a VM instance
     * @return id of the snapshot
     */
    public String createSnapshot(VMInstance vmi){
        journal.log(Level.INFO, ">> Creating a snapshot of VM id: "+vmi.getId());
        Snapshot snapshot=new Snapshot();
        Server temp=(Server)findObjectResourceByID(vmi.getId(), ResourceType.SERVER);
        snapshot.setParentUUID(temp.getDisks().get(0).getResourceUUID());
        snapshot.setResourceName(vmi.getName()+"-snapshot");
        snapshot.setType(SnapshotType.DISK);
        snapshot.setResourceType(ResourceType.SNAPSHOT);
        snapshot.setClusterUUID(temp.getClusterUUID());

        try {
            Job job=service.createSnapshot(snapshot,null);
            service.waitForJob(job.getResourceUUID(), false);
        } catch (ExtilityException e) {
            e.printStackTrace();
        }
        journal.log(Level.INFO, ">> Snapshot created with id: "+vmi.getName()+"-snapshot");
        return vmi.getName()+"-snapshot";
    }

    /**
     * Create an image of the specified VM
     * @param vmi a VM instance
     * @return id of the image
     */
    public String createImage(VMInstance vmi){
        journal.log(Level.INFO, ">> Creating an image of VM id: "+vmi.getId());
        Image image=new Image();
        Server temp=(Server)findObjectResourceByID(vmi.getId(), ResourceType.SERVER);
        image.setBaseUUID(temp.getDisks().get(0).getResourceUUID());
        image.setClusterUUID(temp.getClusterUUID());
        image.setResourceName(vmi.getName()+"-image");
        image.setResourceType(ResourceType.IMAGE);
        image.setVdcUUID(temp.getVdcUUID());

        try {
            Job job1=service.changeServerStatus(temp.getResourceUUID(),ServerStatus.STOPPED,true,null,null);
            service.waitForJob(job1.getResourceUUID(), false);

            Job job2=service.createImage(image, null);
            service.waitForJob(job2.getResourceUUID(), false);

            Job job3=service.changeServerStatus(temp.getResourceUUID(),ServerStatus.RUNNING,true,null,null);
            service.waitForJob(job3.getResourceUUID(), false);
        } catch (ExtilityException e) {
            journal.log(Level.SEVERE, e.getMessage());
        }

        journal.log(Level.INFO, ">> Image created with id: "+vmi.getName()+"-image");
        return vmi.getName()+"-image";
    }

    @Override
    public void startVM(VMInstance a) {
        try {
            journal.log(Level.INFO, ">> Starting VM: "+a.getName());
            Job startJob=service.changeServerStatus(a.getId(), ServerStatus.RUNNING, true, null, null);
            service.waitForJob(startJob.getResourceUUID(), false);
        } catch (ExtilityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopVM(VMInstance a) {
        try {
            journal.log(Level.INFO, ">> Stopping VM: "+a.getName());
            Job startJob=service.changeServerStatus(a.getId(), ServerStatus.STOPPED, true, null, null);
            service.waitForJob(startJob.getResourceUUID(), false);
        } catch (ExtilityException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {}

    public void updateVMMetadata(VMInstance a) {
        a.setId(findResourceByName(a.getName(), ResourceType.SERVER));
        Server temp=(Server)findObjectResourceByName(a.getName(), ResourceType.SERVER);
        a.setPublicAddress(temp.getNics().get(0).getIpAddresses().get(0).getIpAddress());
    }

    public void uploadFile(String sourcePath, String destinationPath,
                           String VMId, String login, String key) {
        Server temp=(Server)findObjectResourceByID(VMId, ResourceType.SERVER);
        String ip=temp.getNics().get(0).getIpAddresses().get(0).getIpAddress();
        SSHConnector sc=new SSHConnector(key, login, ip);
        sc.upload(sourcePath,destinationPath);
    }

}

