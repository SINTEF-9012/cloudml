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
 */
package org.cloudml.core.samples;

import org.cloudml.core.builders.*;

import static org.cloudml.core.builders.Commons.*;

/**
 * A sample deployment model, used for testing purpose
 */
public class SshClientServer {

    public static final String AMAZON_EC2 = "Amazon EC2";
    public static final String EC2_LARGE_LINUX = "Large Linux";
    public static final String UBUNTU_1204 = "Ubuntu 12.04";
    public static final String SSH_CLIENT = "Client SSH Application";
    public static final String SSH_SERVER = "Server SSH Application";
    public static final String WINDOWS_7 = "Windows 7";
    public static final String SSH_CONNECTION = "SSH connection";
    public static final String EC2_XLARGE_WINDOWS_7 = "xLarge Windows 7";
    public static final String CLIENT_PORT = "client";
    public static final String SERVER_PORT = "server";
    public static final String OPERATING_SYSTEM = "OS";
    public static final String REQUIRED_PLATFORM = "run on";
    public static final String PROVIDED_PLATFORM = "running";
    public static final String VM_OF_CLIENT_1 = "client's host";
    public static final String VM_OF_SERVER_1 = "server's host";
    public static final String SERVER_1 = "server";
    public static final String CLIENT_1 = "client";
    public static final String CLIENT_2 = "another client";
    public static final String VM_OF_CLIENT_2 = "vm of client 2";
    public static final String VM_OF_SERVER_2 = "vm of server 2";
    public static final String SERVER_2 = "server 2";

    
    public static ProviderBuilder amazonEc2() {
        return aProvider()
                .named(AMAZON_EC2);
    }
       
    public static VMBuilder ec2LargeLinux() {
        return aVM()
                .named(EC2_LARGE_LINUX)
                .providedBy(AMAZON_EC2)
                .with(aProvidedExecutionPlatform()
                    .named(PROVIDED_PLATFORM)
                    .offering(OPERATING_SYSTEM, UBUNTU_1204));
    }
    
    public static VMBuilder ec2XLargeWindows7() {
        return aVM()
                .named(EC2_XLARGE_WINDOWS_7)
                .providedBy(AMAZON_EC2)
                .with(aProvidedExecutionPlatform()
                    .named("running")
                    .offering(OPERATING_SYSTEM, WINDOWS_7));
                
    }
    
    public static InternalComponentBuilder sshClient() {
        return anInternalComponent()
                .named(SSH_CLIENT)
                .with(aRequiredExecutionPlatform()
                    .named("run on")
                    .demanding(OPERATING_SYSTEM, UBUNTU_1204))
                .with(sshClientPort());
    }
    
    
    public static InternalComponentBuilder sshServer() {
        return anInternalComponent()
                .named(SSH_SERVER)
                .with(aRequiredExecutionPlatform()
                    .named(REQUIRED_PLATFORM)
                    .demanding(OPERATING_SYSTEM, WINDOWS_7))
                .with(sshServerPort());
    }
    
    public static RelationshipBuilder sshConnection() {
        return aRelationship()
                .named(SSH_CONNECTION)
                .from(SSH_CLIENT, CLIENT_PORT)
                .to(SSH_SERVER, SERVER_PORT);
    }

    public static ProvidedPortBuilder sshServerPort() {
        return aProvidedPort()
        .named(SERVER_PORT)
        .remote();
    }

    public static RequiredPortBuilder sshClientPort() {
        return aRequiredPort()
        .named(CLIENT_PORT)
        .mandatory()
        .remote();
    }
    
    public static DeploymentBuilder getSshTypes() {
        return aDeployment()
                .with(amazonEc2())
                .with(ec2LargeLinux())
                .with(ec2XLargeWindows7())
                .with(sshClient())
                .with(sshServer())
                .with(sshConnection());
    }
    
    public static DeploymentBuilder getOneClientConnectedToOneServer() {
        return getSshTypes()
                .with(vmOfClient1())
                .with(client1())
                .with(vmOfServer1())
                .with(server1())
                .with(aRelationshipInstance()
                    .named("ssh")
                    .ofType(SSH_CONNECTION)
                    .from(CLIENT_1, CLIENT_PORT)
                    .to(SERVER_1, SERVER_PORT));
    }
    
    public static DeploymentBuilder getOneClientConnectedToOneOfTwoServers() {
        return getOneClientConnectedToOneServer()
                .with(vmOfServer2())
                .with(server2());
    }
    
    
    public static DeploymentBuilder getTwoClientsConnectedToOneServer() {
        return getOneClientConnectedToOneServer()
                .with(vmOfClient2())
                .with(client2())
                .with(aRelationshipInstance()
                    .named("ssh 2")
                    .ofType(SSH_CONNECTION)
                    .from(CLIENT_2, CLIENT_PORT)
                    .to(SERVER_1, SERVER_PORT));
    }
    
    public static DeploymentBuilder getTwoClientsConnectedToTwoServers() {
        return getOneClientConnectedToOneServer()
                .with(vmOfClient2())
                .with(client2())
                .with(vmOfServer2())
                .with(server2())
                .with(aRelationshipInstance()
                    .named("ssh 2")
                    .ofType(SSH_CONNECTION)
                    .from(CLIENT_2, CLIENT_PORT)
                    .to(SERVER_2, SERVER_PORT));
    }

    private static InternalComponentInstanceBuilder client2() {
        return anInternalComponentInstance()
             .named(CLIENT_2)
             .ofType(SSH_CLIENT)
             .hostedBy(VM_OF_CLIENT_2);
    }

    private static VMInstanceBuilder vmOfClient2() {
        return aVMInstance()
             .named(VM_OF_CLIENT_2)
             .ofType(EC2_LARGE_LINUX);
    }

    private static VMInstanceBuilder vmOfClient1() {
        return aVMInstance()
             .named(VM_OF_CLIENT_1)
             .ofType(EC2_LARGE_LINUX);
    }

    private static InternalComponentInstanceBuilder client1() {
        return anInternalComponentInstance()
             .named(CLIENT_1)
             .ofType(SSH_CLIENT)
             .hostedBy(VM_OF_CLIENT_1);
    }

    private static VMInstanceBuilder vmOfServer1() {
        return aVMInstance()
             .named(VM_OF_SERVER_1)
             .ofType(EC2_XLARGE_WINDOWS_7);
    }

    private static InternalComponentInstanceBuilder server1() {
        return anInternalComponentInstance()
             .named(SERVER_1)
             .ofType(SSH_SERVER)
             .hostedBy(VM_OF_SERVER_1);
    }


    private static VMInstanceBuilder vmOfServer2() {
        return aVMInstance()
             .named(VM_OF_SERVER_2)
             .ofType(EC2_XLARGE_WINDOWS_7);
    }

    private static InternalComponentInstanceBuilder server2() {
        return anInternalComponentInstance()
             .named(SERVER_2)
             .ofType(SSH_SERVER)
             .hostedBy(VM_OF_SERVER_2);
    }
}
