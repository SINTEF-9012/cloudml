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
package test.cloudml.core;

import junit.framework.TestCase;
import static junit.framework.TestCase.assertTrue;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.ArtefactPort;
import org.cloudml.core.Binding;
import org.cloudml.core.BindingInstance;
import org.cloudml.core.ClientPort;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.Provider;
import org.cloudml.core.ServerPort;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import test.cloudml.core.builder.ArtefactBuilder;
import test.cloudml.core.builder.Builder;

@RunWith(JUnit4.class)
public class BindingInstanceTest extends TestCase {

    @Test
    public void validationPassWithoutWarningWhenValid() {
        BindingInstance instance = prepareBindingInstance();
        Report report = instance.validate();
        System.out.println(report);
        assertTrue(report.pass(Report.WITHOUT_WARNING));
    }

    @Test
    public void validationReportsNullName() {
        BindingInstance instance = prepareBindingInstance();
        instance.setName(null);

        Report report = instance.validate();

        assertTrue(report.hasErrorAbout("name", "null"));
    }

    @Test
    public void validationReportsEmptyName() {
        BindingInstance instance = prepareBindingInstance();
        instance.setName("");

        Report report = instance.validate();

        assertTrue(report.hasErrorAbout("name", "empty"));
    }

    @Test
    public void validationReportsNullType() {
        BindingInstance instance = prepareBindingInstance();
        instance.setType(null);

        Report report = instance.validate();

        assertTrue(report.hasErrorAbout("type", "null"));
    }

    @Test
    public void validationReportsNullClient() {
        BindingInstance instance = prepareBindingInstance();
        instance.setClient(null);

        Report report = instance.validate();

        assertTrue(report.hasErrorAbout("client end", "null"));
    }

    @Test
    public void validationReportsNullServer() {
        BindingInstance instance = prepareBindingInstance();
        instance.setServer(null);

        Report report = instance.validate();

        assertTrue(report.hasErrorAbout("server end", "null"));
    }

    @Test
    public void validateReportsIllegalServerSide() {
        BindingInstance instance = prepareBindingInstance();
        instance.getServer().setType(new ServerPort("Foo", null, true));

        Report report = instance.validate();
        System.out.println(report);

        assertTrue(report.hasErrorAbout("illegal", "binding", "instance", "type", "server"));
    }

    @Test
    public void validateReportsIllegalClientSide() {
        BindingInstance instance = prepareBindingInstance();
        instance.getClient().setType(new ClientPort("Foo", null, true));

        Report report = instance.validate();
        System.out.println(report);

        assertTrue(report.hasErrorAbout("illegal", "binding", "instance", "type", "client"));
    }

    // TODO: remove duplication with the code in test visitor
    private BindingInstance prepareBindingInstance() {
        final Builder builder = new Builder();

        Provider amazon = builder.createProvider("Amazon EC2");

        Node linux = builder.createNodeType("Linux Ubuntu", amazon);
        Node windows = builder.createNodeType("Windows 7", amazon);

        ArtefactBuilder clientBuilder = builder.createArtefactType("Client Application");
        ClientPort client = clientBuilder.createClientPort("SSH client", ArtefactPort.REMOTE, ClientPort.MANDATORY);

        ArtefactBuilder serverBuilder = builder.createArtefactType("Server Application");
        ServerPort server = serverBuilder.createServerPort("SSH server", ArtefactPort.REMOTE);

        Binding sshConnection = builder.createBindingType("SSH connection", client, server);

        NodeInstance windowsHost = builder.provision(windows, "Windows Server");
        ArtefactInstance clientApp = builder.install(clientBuilder.getResult(), "client", windowsHost);

        NodeInstance linuxHost = builder.provision(linux, "Windows Server");
        ArtefactInstance serverApp = builder.install(serverBuilder.getResult(), "server", linuxHost);

        return builder.connect("ssh connection", sshConnection, clientApp.getRequired().get(0), serverApp.getProvided().get(0));
    }
}
