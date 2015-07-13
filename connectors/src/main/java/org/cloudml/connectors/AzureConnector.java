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

import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.core.OperationResponse;
import com.microsoft.windowsazure.core.OperationStatus;
import com.microsoft.windowsazure.core.OperationStatusResponse;
import com.microsoft.windowsazure.core.utils.Constants;
import com.microsoft.windowsazure.core.utils.KeyStoreType;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.management.compute.ComputeManagementClient;
import com.microsoft.windowsazure.management.compute.ComputeManagementService;
import com.microsoft.windowsazure.management.compute.HostedServiceOperations;
import com.microsoft.windowsazure.management.compute.models.*;
import com.microsoft.windowsazure.management.configuration.ManagementConfiguration;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.VM;
import org.cloudml.core.VMInstance;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ferrynico on 12/07/15.
 */
public class AzureConnector implements Connector{

    private static final Logger journal = Logger.getLogger(JCloudsConnector.class.getName());

    private ComputeManagementClient computeManagementClient;
    private HostedServiceOperations hostedServicesOperations;
    private HashMap<String,String> runtimeInformation;

    public AzureConnector(String endpoint, String provider,String login,String secretKey){
        journal.log(Level.INFO, ">> Connecting to "+provider+" ...");
        Configuration config = null;
        try {
            config = createConfiguration(endpoint,provider,login,secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        journal.log(Level.INFO, ">> Authenticating ...");
        computeManagementClient = ComputeManagementService.create(config);
        hostedServicesOperations = computeManagementClient.getHostedServicesOperations();
    }

    @Override
    public void execCommand(String id, String command, String login, String key) {
        DeploymentGetResponse response = null;
        try {
            response = computeManagementClient.getDeploymentsOperations().getByName(id,id);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        SSHConnector sc=new SSHConnector(key, login, response.getVirtualIPAddresses().get(0).getAddress().getHostAddress());
        sc.execCommandSsh(command);
    }

    @Override
    public HashMap<String, String> createInstance(VMInstance a) {
        runtimeInformation=new HashMap<String, String>();
        VM vm = a.getType();
        ComponentInstance.State state = ComponentInstance.State.UNRECOGNIZED;

        journal.log(Level.INFO, ">> Provisioning a vm ...");

        try {

            createHostedService(a.getName(),vm.getLocation(),vm.getSshKey());
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        ArrayList<Role> rolelist = null;
        try {
            rolelist = createRoleList(a, vm, vm.getGroupName(),vm.getGroupName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        VirtualMachineCreateDeploymentParameters deploymentParameters = new VirtualMachineCreateDeploymentParameters();
        deploymentParameters.setDeploymentSlot(DeploymentSlot.Staging);
        deploymentParameters.setName(a.getName());
        deploymentParameters.setLabel(a.getName());
        deploymentParameters.setRoles(rolelist);
        deploymentParameters.setDeploymentSlot(DeploymentSlot.Production);


        VirtualMachineCreateParameters createParameters=new VirtualMachineCreateParameters();
        ArrayList<ConfigurationSet> configurationSets = new ArrayList<ConfigurationSet>();
        createParameters.setConfigurationSets(configurationSets);
        createParameters.setOSVirtualHardDisk(createOSVHD(vm, vm.getGroupName(),vm.getGroupName()));
        createParameters.setProvisionGuestAgent(true);
        createParameters.setRoleSize(VirtualMachineRoleSize.SMALL);
        createParameters.setRoleName(a.getName());
        configurationSets.add(createConfigOS(a.getName(),vm));
        configurationSets.add(createConfigNetwork());


        // Act
        try {
            OperationStatusResponse r;
            if(!isDeploymentPresent(computeManagementClient,a.getName(),a.getName()))
                r=computeManagementClient.getVirtualMachinesOperations().createDeployment(a.getName(), deploymentParameters);
            else r=computeManagementClient.getVirtualMachinesOperations().create(a.getName(), a.getName(), createParameters);
            waitOperationToComplete(r.getId(),60,100);

            DeploymentGetResponse response = computeManagementClient.getDeploymentsOperations().getByName(a.getName(),a.getName());
            String ip=response.getVirtualIPAddresses().get(0).getAddress().getHostAddress();
            runtimeInformation.put("publicAddress", ip);
            a.setId(a.getName());
            //wait for the VM to be accessible
            String login="ubuntu";
            if(!vm.getLogin().equals(""))
                login=vm.getLogin();

            SSHConnector sc=new SSHConnector(vm.getPrivateKey(), login, ip);
            while(!sc.checkConnectivity()){
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    journal.log(Level.SEVERE, e.getMessage());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        state = ComponentInstance.State.RUNNING;
        runtimeInformation.put("status", state.toString());
        return runtimeInformation;
    }

    @Override
    public void destroyVM(String id) {

    }

    @Override
    public void closeConnection() {

    }

    @Override
    public void updateVMMetadata(VMInstance a) {

    }

    @Override
    public void uploadFile(String sourcePath, String destinationPath, String nodeId, String login, String key) {

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
        OperationStatusResponse r= null;
        try {
            r = computeManagementClient.getVirtualMachinesOperations().start(a.getName(), a.getName(), a.getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        waitOperationToComplete(r.getId(),60, 100);
    }

    @Override
    public void stopVM(VMInstance a) {
        journal.log(Level.INFO, ">> Starting VM: "+a.getName());
        OperationStatusResponse r= null;
        try {
            VirtualMachineShutdownParameters params = new VirtualMachineShutdownParameters();
            params.setPostShutdownAction(PostShutdownAction.StoppedDeallocated);
            r = computeManagementClient.getVirtualMachinesOperations().shutdown(a.getName(), a.getName(), a.getName(),params);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        waitOperationToComplete(r.getId(),60, 100);
    }

    private void uploadCerts(ComputeManagementClient client, String cloudServiceName, byte[] certData){
        try {
            // Try to upload to hosted service
            ServiceCertificateCreateParameters params = new ServiceCertificateCreateParameters();
            params.setCertificateFormat(CertificateFormat.Pfx);
            params.setData(certData);
            client.getServiceCertificatesOperations().create(cloudServiceName, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createHostedService(String hostedServiceName, String loc, String certPath) throws SAXException, InterruptedException, ExecutionException, TransformerException, ServiceException, URISyntaxException, ParserConfigurationException, IOException {
        //hosted service required for vm deployment
        HostedServiceCreateParameters createParameters = new HostedServiceCreateParameters();
        //required
        createParameters.setLabel(hostedServiceName);
        //required
        createParameters.setServiceName(hostedServiceName);
        createParameters.setDescription(hostedServiceName);

        //required
        String location="North Europe";
        if(!loc.equals(""))
            location=loc;

        createParameters.setLocation(location);
        OperationResponse hostedServiceOperationResponse = hostedServicesOperations.create(createParameters);

        uploadCerts(computeManagementClient,hostedServiceName,getCertData(certPath));
    }

    private static byte[] getCertData(String path){
        FileInputStream fileInputStream=null;

        File file = new File(path);

        byte[] bFile = new byte[(int) file.length()];

        try {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();

        }catch(Exception e){
            e.printStackTrace();
        }
        return bFile;
    }

    private Configuration createConfiguration(String endpoint, String provider,String login,String secretKey) throws Exception {
        return ManagementConfiguration.configure(
                new URI("https://management.core.windows.net"),
                endpoint,
                login,
                secretKey,
                KeyStoreType.pkcs12
        );
    }

    private static boolean isDeploymentPresent(ComputeManagementClient computeClient, String hostedServiceName, String deploymentName) {
        boolean deploymentPresent = false;
        try {
            computeClient.getDeploymentsOperations().getByName(hostedServiceName, deploymentName);
            deploymentPresent = true;
        } catch (ServiceException e) {
            if (e.getMessage().contains("ResourceNotFound")) {
                deploymentPresent = false;
            } else {
                throw new IllegalArgumentException("Unable to get VM deployment state", e);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Unable to get VM deployment state", e);
        }
        return deploymentPresent;
    }

    private OSVirtualHardDisk createOSVHD(VM vm,String storageAccountName, String storageContainer){
        int random = (int)(Math.random()* 100);
        URI mediaLinkUriValue = null;
        try {
            mediaLinkUriValue = new URI("http://"+ storageAccountName + ".blob.core.windows.net/"+storageContainer+ "/ubuntu"  + random +".vhd");

            String osVHarddiskName ="ubuntuoshdname"+ random;
            String operatingSystemName ="Linux";
            if(!vm.getOs().equals(""))
                if(vm.getOs().toLowerCase().contains("win"))
                    operatingSystemName="Windows";

            String sourceImageName = "Ubuntu";
            if(!vm.getImageId().equals(""))
                sourceImageName=vm.getImageId();
            else if(!vm.getOs().equals(""))
                sourceImageName = getOSSourceImage(vm.getOs());

            OSVirtualHardDisk oSVirtualHardDisk = new OSVirtualHardDisk();
            //required
            oSVirtualHardDisk.setName(osVHarddiskName);
            oSVirtualHardDisk.setHostCaching(VirtualHardDiskHostCaching.READWRITE);
            oSVirtualHardDisk.setOperatingSystem(operatingSystemName);
            //required
            oSVirtualHardDisk.setMediaLink(mediaLinkUriValue);
            //required
            oSVirtualHardDisk.setSourceImageName(sourceImageName);

            return oSVirtualHardDisk;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private ConfigurationSet createConfigNetwork(){

        ConfigurationSet networkConfigset = new ConfigurationSet();
        networkConfigset.setConfigurationSetType(ConfigurationSetTypes.NETWORKCONFIGURATION);
        // Define endpoints
        ArrayList<InputEndpoint> enpoints = new ArrayList<InputEndpoint>();
        networkConfigset.setInputEndpoints(enpoints);
        InputEndpoint sshPort = new InputEndpoint();
        enpoints.add(sshPort);
        sshPort.setPort(22);
        sshPort.setLocalPort(22);
        sshPort.setName("ssh");
        sshPort.setProtocol(InputEndpointTransportProtocol.TCP);

        return networkConfigset;
    }

    private ConfigurationSet createConfigOS(String vmName, VM vm){
        String adminUserPassword= "ubuntu!12";
        String adminUserName = "ubuntu";
        if(!vm.getLogin().equals(""))
            adminUserName = vm.getLogin();
        if(!vm.getPasswd().equals(""))
            adminUserPassword=vm.getPasswd();

        //required
        ArrayList<ConfigurationSet> configurationSetList = new ArrayList<ConfigurationSet>();
        ConfigurationSet configurationSet = new ConfigurationSet();
        String type=ConfigurationSetTypes.LINUXPROVISIONINGCONFIGURATION;
        if(!vm.getOs().equals(""))
            if(vm.getOs().toLowerCase().contains("win"))
                type=ConfigurationSetTypes.WINDOWSPROVISIONINGCONFIGURATION;
        configurationSet.setConfigurationSetType(type);
        configurationSet.setDisableSshPasswordAuthentication(true);
        configurationSet.setComputerName(vmName);
        configurationSet.setAdminPassword(adminUserPassword);
        configurationSet.setUserName(adminUserName);
        configurationSet.setUserPassword(adminUserPassword);
        configurationSet.setAdminUserName(adminUserName);
        configurationSet.setEnableAutomaticUpdates(true);
        configurationSet.setHostName(vmName+".cloudapp.net");
        configurationSetList.add(configurationSet);


        ArrayList<SshSettingPublicKey> pks= new ArrayList<SshSettingPublicKey>();
        SshSettings ssh=new SshSettings();
        ssh.setPublicKeys(pks);
        SshSettingPublicKey sspk = new SshSettingPublicKey();
        sspk.setFingerprint(getFingerprint(vmName));
        sspk.setPath("/home/" + adminUserName + "/.ssh/authorized_keys"); //TODO: update for windows
        pks.add(sspk);

        ArrayList<SshSettingKeyPair> keyPairs= new ArrayList<SshSettingKeyPair>();
        ssh.setKeyPairs(keyPairs);
        SshSettingKeyPair keyPair = new SshSettingKeyPair();
        keyPairs.add(keyPair);
        keyPair.setFingerprint(getFingerprint(vmName));
        keyPair.setPath("/home/"+adminUserName+"/.ssh/authorized_keys"); //TODO: update for windows
        configurationSet.setSshSettings(ssh);


        return configurationSet;
    }

    private String getFingerprint(String serviceName){
        String print="";
        try {
            print=computeManagementClient.getServiceCertificatesOperations().list(serviceName).getCertificates().get(0).getThumbprint();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return print;
    }


    private ArrayList<Role> createRoleList(VMInstance a, VM vm,String storageAccountName, String storageContainer) throws Exception {
        ArrayList<Role> roleList = new ArrayList<Role>();
        Role role = new Role();
        String roleName = a.getName();

        //required
        role.setRoleName(roleName);
        role.setRoleType(VirtualMachineRoleType.PersistentVMRole.toString());
        String size=VirtualMachineRoleSize.SMALL;
        if(!vm.getProviderSpecificTypeName().equals(""))
            size=vm.getProviderSpecificTypeName();
        role.setRoleSize(size);//TODO: size with mincore
        role.setProvisionGuestAgent(true);
        ArrayList<ConfigurationSet> configurationSets = new ArrayList<ConfigurationSet>();
        role.setConfigurationSets(configurationSets);
        role.setOSVirtualHardDisk(createOSVHD(vm,storageAccountName,storageContainer));
        roleList.add(role);
        configurationSets.add(createConfigOS(a.getName(),vm));
        configurationSets.add(createConfigNetwork());
        return roleList;
    }

    private String getOSSourceImage(String args) throws Exception {
        String sourceImageName = null;
        VirtualMachineOSImageListResponse virtualMachineImageListResponse = computeManagementClient.getVirtualMachineOSImagesOperations().list();
        ArrayList<VirtualMachineOSImageListResponse.VirtualMachineOSImage> virtualMachineOSImagelist = virtualMachineImageListResponse.getImages();

        for (VirtualMachineOSImageListResponse.VirtualMachineOSImage virtualMachineImage : virtualMachineOSImagelist) {
            if ((virtualMachineImage.getName().contains(args))) {
                sourceImageName = virtualMachineImage.getName();
                break;
            }
        }
        return sourceImageName;
    }

    private void waitOperationToComplete(String requestId, long waitTimeBetweenTriesInSeconds, int maximumNumberOfTries) {
        boolean operationCompleted = false;
        int tryCount =0;
        while ((!operationCompleted)&&(tryCount<maximumNumberOfTries))
        {
            OperationStatusResponse operationStatus = null;
            try {
                operationStatus = computeManagementClient.getOperationStatus(requestId);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServiceException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }

            if ((operationStatus.getStatus() == OperationStatus.Failed) || (operationStatus.getStatus() == OperationStatus.Succeeded))
            {
                operationCompleted = true;
            }else{
                try {
                    Thread.sleep(waitTimeBetweenTriesInSeconds * 1000);
                    tryCount ++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<VirtualMachineOSImageListResponse.VirtualMachineOSImage> getVirtualMachineOSImageList() throws Exception {
        List<VirtualMachineOSImageListResponse.VirtualMachineOSImage> imageList = new ArrayList<VirtualMachineOSImageListResponse.VirtualMachineOSImage>();

        VirtualMachineOSImageListResponse response = computeManagementClient.getVirtualMachineOSImagesOperations().list();

        ArrayList<VirtualMachineOSImageListResponse.VirtualMachineOSImage> osImages = response.getImages();

        for (VirtualMachineOSImageListResponse.VirtualMachineOSImage image: osImages) {
            imageList.add(image);
        }
        return imageList;
    }


    public String getVirtualMachineStatus(String name) throws Exception {
        String status = "";
        ArrayList<RoleInstance> roleInstances = computeManagementClient.getDeploymentsOperations().getBySlot(name, DeploymentSlot.Production).getRoleInstances();
        for (RoleInstance instance : roleInstances) {
            if (instance.getRoleName().equals(name)) {
                status = instance.getInstanceStatus();
                break;
            }
        }
        return status;
    }

}
