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

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;
import org.apache.commons.io.FileUtils;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.VM;
import org.cloudml.core.VMInstance;
import org.jclouds.ContextBuilder;
import org.jclouds.cloudsigma2.domain.*;
import org.jclouds.cloudsigma2.CloudSigma2Api;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.*;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.logging.config.NullLoggingModule;
import org.jclouds.sshj.config.SshjSshClientModule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by nicolasf on 02.02.15.
 */
public class CloudSigmaConnector implements Connector {

    private static final Logger journal = Logger.getLogger(CloudSigmaConnector.class.getName());
    private ComputeServiceContext computeContext;
    private ComputeService compute;
    private String provider;
    private HashMap<String,String> runtimeInformation;
    private CloudSigma2Api cloudSigmaApi;


    public CloudSigmaConnector(String provider,String login,String secretKey){
        journal.log(Level.INFO, ">> Connecting to "+provider+" ...");
        Iterable<Module> modules = ImmutableSet.<Module> of(
                new SshjSshClientModule(),
                new NullLoggingModule());
        ContextBuilder builder = ContextBuilder.newBuilder(provider)
                .credentials(login, secretKey)
                .modules(modules);
        journal.log(Level.INFO, ">> Authenticating ...");
        this.provider = provider;
        cloudSigmaApi=builder.buildApi(CloudSigma2Api.class);
    }

    public FluentIterable<ServerInfo> listOfServers(){
        return cloudSigmaApi.listServersInfo().concat();
    }

    @Override
    public void execCommand(String id, String command, String login, String key) {
        for(IP ip: cloudSigmaApi.listIPs().concat()){
            if(ip.getServer().getUuid().equals(id)){
                SSHConnector sc=new SSHConnector(key, login, ip.getUuid());
                sc.execCommandSsh(command);
                return;
            }
        }
    }

    private String findDriveByName(String name){
        FluentIterable<LibraryDrive> list= cloudSigmaApi.listLibraryDrives().concat();
        for(DriveInfo d: list){
            if(d.getName().contains(name)){
                return d.getUuid();
            }
        }
        return null;
    }

    private String readSshKey(String keyPath){
        BufferedReader br;
        String key="";
        try {
            br=new BufferedReader(new FileReader(keyPath));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            key = sb.toString();
        } catch (IOException e) {
            journal.log(Level.SEVERE, e.getMessage());
        }
        return key;
    }


    @Override
    public HashMap<String, String> createInstance(VMInstance a) {
        
        VM vm = a.getType();
        runtimeInformation=new HashMap<String, String>();
        ComponentInstance.State state = ComponentInstance.State.UNRECOGNIZED;
        
        // First try to find the desired template drive in MyDrives
        DriveInfo driveToClone =null;
        FluentIterable<DriveInfo> drives = cloudSigmaApi.listDrivesInfo().concat();
        for(DriveInfo d : drives){
            if(d.getName().contains(vm.getImageId()) && 
               d.getStatus() == DriveStatus.UNMOUNTED){
                driveToClone =d;
                break;
            }
        }
        
        if(null == driveToClone) {
            // We had no luck with MyDrives, so try the market place / library
            FluentIterable<LibraryDrive> libraryDrives = cloudSigmaApi.listLibraryDrives().concat();
            for(DriveInfo d : libraryDrives){
                if(d.getName().contains(vm.getImageId())){
                    driveToClone =d;
                    break;
                }
            }
        }

        // Next step is to clone the drive and identify it in our drive list
        LibraryDrive driveProperties = new LibraryDrive.Builder()
                .name(a.getName() + "-root")
                .description("root drive for " + vm.getImageId())
                .media(MediaType.DISK)
                .build();
        cloudSigmaApi.cloneLibraryDrive(driveToClone.getUuid(), driveProperties);

        try {//TODO: to be removed
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            journal.log(Level.SEVERE, e.getMessage());
        }

        FluentIterable<DriveInfo> concat = cloudSigmaApi.listDrivesInfo().concat();
        DriveInfo cloned=null;

        for(DriveInfo d : concat){
            if(d.getName().equals(a.getName() + "-root") &&
               d.getStatus() == DriveStatus.UNMOUNTED){
                cloned = d;
                break;
            }
        }

        FirewallPolicy p = null;
        for(FirewallPolicy fp :cloudSigmaApi.listFirewallPolicies().concat()){
            if(fp.getName().toLowerCase().equals(vm.getSecurityGroup().toLowerCase())){
                p=fp;
            }
        }

        ServerDrive drive=cloned.toServerDrive(1,"0:1", DeviceEmulationType.VIRTIO);
        NIC nic=new NIC.Builder()
                .firewallPolicy(p)
                .ipV4Configuration(new IPConfiguration(IPConfigurationType.DHCP, null))
                .build();


        Map<String,String> m=new HashMap<String, String>();
        m.put("ssh_public_key",readSshKey(vm.getSshKey()));




        org.jclouds.cloudsigma2.domain.ServerInfo serverToCreate = new org.jclouds.cloudsigma2.domain.ServerInfo.Builder()
                .name(a.getName())
                .memory(BigInteger.valueOf(vm.getMinRam() * 1000000))
                .vncPassword("cloudml")
                .cpu(vm.getMinCores())
                .nics(ImmutableList.of(nic))
                .meta(m)
                .drives(ImmutableList.of(drive))
                .build();

        org.jclouds.cloudsigma2.domain.ServerInfo createdServer = cloudSigmaApi.createServer(serverToCreate);
        cloudSigmaApi.startServer(createdServer.getUuid());

        if(createdServer.getStatus().value().equals("Running"))
            state=ComponentInstance.State.RUNNING;

        a.setId(createdServer.getUuid());
        for(IP ip: cloudSigmaApi.listIPs().concat()){
            if(ip.getServer().getUuid().equals(a.getId())){
                runtimeInformation.put("publicAddress", ip.getUuid());

                //wait for the VM to be accessible
                if (vm.getOs().toLowerCase().contains("windows")) {
                    while(!PowerShellConnector.checkConnectivity(ip.getUuid())){
                        try {
                            Thread.sleep(15000);
                        } catch (InterruptedException e) {
                            journal.log(Level.SEVERE, e.getMessage());
                        }
                    }
                } else {
                    SSHConnector sc=new SSHConnector(vm.getPrivateKey(), "ubuntu", ip.getUuid());
                    while(!sc.checkConnectivity()){
                        try {
                            Thread.sleep(15000);
                        } catch (InterruptedException e) {
                            journal.log(Level.SEVERE, e.getMessage());
                        }
                    }
                }
            }
        }


        runtimeInformation.put("status", state.toString());
        return runtimeInformation;
    }

    @Override
    public void destroyVM(String id) {
        compute.destroyNode(id);
    }

    @Override
    public void closeConnection() {
        try {
            cloudSigmaApi.close();
        } catch (IOException e) {
            journal.log(Level.SEVERE, e.getMessage());
        }
        journal.log(Level.INFO, ">> Closing connection ...");
    }

    public ComputeMetadata getVMByName(String name){
        for(ComputeMetadata n : compute.listNodes()){
            if(n.getName() != null &&  n.getName().equals(name))
                return n;
        }
        return null;
    }

    @Override
    public void updateVMMetadata(VMInstance a) {
        ComputeMetadata cm= getVMByName(a.getName());
        if(cm != null){
            a.setPublicAddress(getVMById(cm.getId()).getPublicAddresses().iterator().next());
            a.setId(cm.getId());
        }
    }

    public NodeMetadata getVMById(String id){
        return compute.getNodeMetadata(id);
    }

    private org.jclouds.domain.LoginCredentials.Builder initCredentials(String login, String key){
        String contentKey;
        org.jclouds.domain.LoginCredentials.Builder b= LoginCredentials.builder();
        File f = new File(key);
        if(f.exists() && !f.isDirectory()) {
            try {
                contentKey = FileUtils.readFileToString(new File(key));
                b.user(login);
                b.noPassword();
                b.privateKey(contentKey);
            } catch (IOException e) {
                journal.log(Level.SEVERE, e.getMessage());
            }
        }else{
            b.user(login);
            b.noPassword();
            b.privateKey(key);
        }
        return b;
    }

    @Override
    public void uploadFile(String sourcePath, String destinationPath, String nodeId, String login, String key) {
        for(IP ip: cloudSigmaApi.listIPs().concat()){
            if(ip.getServer().getUuid().equals(nodeId)){
                SSHConnector sc=new SSHConnector(key, login, ip.getUuid());
                sc.upload(sourcePath,destinationPath);
                return;
            }
        }
    }

    @Override
    public String createSnapshot(VMInstance a) {
        return null;
    }

    @Override
    public String createImage(VMInstance a) {
        return null;
    }

    @Override
    public void startVM(VMInstance a) {
        journal.log(Level.INFO, ">> Starting VM: "+a.getName());
        cloudSigmaApi.startServer(a.getId());
    }

    @Override
    public void stopVM(VMInstance a) {
        journal.log(Level.INFO, ">> Stopping VM: "+a.getName());
        cloudSigmaApi.stopServer(a.getId());
    }
}
