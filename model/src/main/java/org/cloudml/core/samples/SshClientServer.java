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

package org.cloudml.core.samples;

import static org.cloudml.core.builders.Commons.*;
import org.cloudml.core.builders.DeploymentModelBuilder;

/**
 * A sample CloudML model, where a client application, deployed on Linux host
 * communicate through SSH with a server deployed on a Windows 7 host.
 * 
 */
public class SshClientServer {
    
    public static final String PROVIDER = "Amazon EC2";
    public static final String LINUX_TYPE = "Linux Ubuntu 12.04";
    public static final String WINDOWS_TYPE = "Windows 7";
    public static final String CLIENT_TYPE = "Client Application";
    public static final String CLIENT_PORT = "SSH client";
    public static final String SERVER_TYPE = "Server Application";
    public static final String SERVER_PORT = "SSH server";
    public static final String BINDING_TYPE = "SSH connection";
    public static final String HOST_3 = "VM no. 3";
    public static final String CLIENT_NO_2 = "client no. 2";
    public static final String BINDING_NO_2 = "SSH connection no. 2";
    public static final String SERVER_NO_1 = "server no. 1";
    public static final String CLIENT_NO_1 = "client no. 1";
    public static final String HOST_NO_1 = "VM no. 1";
    public static final String HOST_NO_2 = "VM no. 2";
    public static final String BINDING_NO_1 = "SSH connection no. 1";

    public DeploymentModelBuilder getTypes() {
        return aDeployment()
                .withProvider(aProvider().named(PROVIDER))
                .withNodeType(aNode()
                    .named(LINUX_TYPE)
                    .providedBy(PROVIDER))
                .withNodeType(aNode()
                    .named(WINDOWS_TYPE)
                    .providedBy(PROVIDER))
                .withArtefact(anArtefact()
                    .named(CLIENT_TYPE)
                    .withClientPort(aClientPort()
                        .named(CLIENT_PORT)
                        .remote()))
                .withArtefact(anArtefact()
                    .named(SERVER_TYPE)
                    .withServerPort(aServerPort()
                        .named(SERVER_PORT)
                        .remote()))
                .withBinding(aBinding()
                    .named(BINDING_TYPE)
                    .from(CLIENT_TYPE, CLIENT_PORT)
                    .to(SERVER_TYPE, SERVER_PORT));
    }

    public DeploymentModelBuilder getOneClientConnectedToOneServer() {
        return getTypes()
                .named("1 client connected to one server by SSH")
                .withNodeInstance(aNodeInstance()
                    .named(HOST_NO_1)
                    .ofType(LINUX_TYPE))
                .withNodeInstance(aNodeInstance()
                    .named(HOST_NO_2)
                    .ofType(WINDOWS_TYPE))
                .withArtefactInstance(anArtefactInstance()
                    .named(CLIENT_NO_1)
                    .ofType(CLIENT_TYPE)
                    .hostedBy(HOST_NO_1))
                .withArtefactInstance(anArtefactInstance()
                    .named(SERVER_NO_1)
                    .ofType(SERVER_TYPE)
                    .hostedBy(HOST_NO_2))
                .withBindingInstance(aBindingInstance()
                    .named(BINDING_NO_1)
                    .ofType(BINDING_TYPE)
                    .from(CLIENT_NO_1, CLIENT_PORT)
                    .to(SERVER_NO_1, SERVER_PORT));
    }
    
    
    public DeploymentModelBuilder getTwoClientsConnectedToOneServer() {
        return getOneClientConnectedToOneServer()
                .named("2 clients connected to one server by SSH")
                .withNodeInstance(aNodeInstance()
                    .named(HOST_3)
                    .ofType(LINUX_TYPE))
                .withArtefactInstance(anArtefactInstance()
                    .named(CLIENT_NO_2)
                    .ofType(CLIENT_TYPE)
                    .hostedBy(HOST_3))
                .withBindingInstance(aBindingInstance()
                    .named(BINDING_NO_2)
                    .ofType(BINDING_TYPE)
                    .from(CLIENT_NO_2, CLIENT_PORT)
                    .to(SERVER_NO_1, SERVER_PORT));
    }
}
