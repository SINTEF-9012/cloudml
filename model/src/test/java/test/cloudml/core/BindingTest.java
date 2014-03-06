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
import org.cloudml.core.Binding;
import org.cloudml.core.ClientPort;
import org.cloudml.core.ServerPort;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import test.cloudml.core.builder.ArtefactBuilder;
import test.cloudml.core.builder.Builder;

@RunWith(JUnit4.class)
public class BindingTest extends TestCase {

    public static final boolean REMOTE = true;
    public static final boolean MANDATORY = true;
    public static final boolean WITHOUT_WARNING = false;
    public static final boolean LOCAL = false;

    @Test
    public void validationPassWhenValid() {
        Builder builder = new Builder();
        ArtefactBuilder client = builder.createArtefactType("Client Type");
        ClientPort clientPort = client.createClientPort("ssh", REMOTE, MANDATORY);
        ArtefactBuilder server = builder.createArtefactType("Server Type");
        ServerPort serverPort = server.createServerPort("ssh", REMOTE);
        Binding binding = builder.createBindingType("ssh connection", clientPort, serverPort);

        assertTrue(binding.validate().pass(WITHOUT_WARNING));
    }

    @Test
    public void validationReportsMissingClientEnd() {
        Builder builder = new Builder();

        ArtefactBuilder server = builder.createArtefactType("Server Type");
        ServerPort serverPort = server.createServerPort("ssh", REMOTE);

        Binding binding = new Binding(null, serverPort);

        assertTrue(binding.validate().hasErrorAbout("client end"));
    }

    @Test
    public void validationReportsMissingServerEnd() {
        Builder builder = new Builder();

        ArtefactBuilder client = builder.createArtefactType("Client Type");
        ClientPort clientPort = client.createClientPort("ssh", REMOTE, MANDATORY);
        ArtefactBuilder server = builder.createArtefactType("Server Type");

        Binding binding = new Binding(clientPort, null);

        assertTrue(binding.validate().hasErrorAbout("server end"));
    }
    
    
    @Test
    public void validationReportsLocalClientsConnectedToRemoteServers() {
        Builder builder = new Builder();
        ArtefactBuilder client = builder.createArtefactType("Client Type");
        ClientPort clientPort = client.createClientPort("ssh", LOCAL, MANDATORY);
        ArtefactBuilder server = builder.createArtefactType("Server Type");
        ServerPort serverPort = server.createServerPort("ssh", REMOTE);
        Binding binding = builder.createBindingType("ssh connection", clientPort, serverPort);

        assertTrue(binding.validate().hasErrorAbout("local client", "remote server"));
    }
    
    @Test
    public void validationReportsRemoteClientsConnectedToLocalServers() {
        Builder builder = new Builder();
        ArtefactBuilder client = builder.createArtefactType("Client Type");
        ClientPort clientPort = client.createClientPort("ssh", REMOTE, MANDATORY);
        ArtefactBuilder server = builder.createArtefactType("Server Type");
        ServerPort serverPort = server.createServerPort("ssh", LOCAL);
        Binding binding = builder.createBindingType("ssh connection", clientPort, serverPort);

        assertTrue(binding.validate().hasErrorAbout("remote client", "local server"));
    }
        
    
}
