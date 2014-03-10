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

    public DeploymentModelBuilder getTypes() {
        return aDeployment()
                .withProvider(aProvider().named("Amazon EC2"))
                .withNodeType(aNode()
                    .named("Linux Ubuntu 12.04")
                    .providedBy("Amazon EC2"))
                .withNodeType(aNode()
                    .named("Windows 7")
                    .providedBy("Amazon EC2"))
                .withArtefact(anArtefact()
                    .named("Client Application")
                    .withClientPort(aClientPort()
                        .named("SSH client")
                        .remote()))
                .withArtefact(anArtefact()
                    .named("Server Application")
                    .withServerPort(aServerPort()
                        .named("SSH server")
                        .remote()))
                .withBinding(aBinding()
                    .named("SSH connection")
                    .from("Client Application", "SSH client")
                    .to("Server Application", "SSH server"));
    }

    public DeploymentModelBuilder getOneClientConnectedToOneServer() {
        return getTypes()
                .named("1 client connected to one server by ")
                .withNodeInstance(aNodeInstance()
                    .named("VM no. 1")
                    .ofType("Linux Ubuntu 12.04"))
                .withNodeInstance(aNodeInstance()
                    .named("VM no. 2")
                    .ofType("Windows 7"))
                .withArtefactInstance(anArtefactInstance()
                    .named("client")
                    .ofType("Client Application")
                    .hostedBy("VM no. 1"))
                .withArtefactInstance(anArtefactInstance()
                    .named("server")
                    .ofType("Server Application")
                    .hostedBy("VM no. 2"))
                .withBindingInstance(aBindingInstance()
                    .named("the SSH connection")
                    .ofType("SSH connection")
                    .from("client", "SSH client")
                    .to("server", "SSH server"));

    }
}
