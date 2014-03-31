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
import org.cloudml.core.DeploymentModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.cloudml.core.builders.Commons.*;
import org.cloudml.core.validation.Report;

@RunWith(JUnit4.class)
public class BindingTest extends TestCase {

    @Test
    public void validationPassWhenValid() {
        DeploymentModel model = createTestModel();

        Binding binding = model.findBindingByName("SSH connection");

        assertTrue(binding.validate().pass(Report.WITHOUT_WARNING));
    }

    @Test
    public void validationReportsMissingClientEnd() {
        DeploymentModel model = createTestModel();
        Binding binding = model.findBindingByName("SSH connection");
        binding.setClient(null);
        
        assertTrue(binding.validate().hasErrorAbout("client end"));
    }

    @Test
    public void validationReportsMissingServerEnd() {
        DeploymentModel model = createTestModel();
        Binding binding = model.findBindingByName("SSH connection");
        binding.setServer(null);

        assertTrue(binding.validate().hasErrorAbout("server end"));
    }

    @Test
    public void validationReportsLocalClientsConnectedToRemoteServers() {
        DeploymentModel model = createTestModel();
        model.getArtefactTypes().named("Client").findRequiredPortByName("ssh").setRemote(false);
          
        Binding binding = model.findBindingByName("SSH connection");
        assertTrue(binding.validate().hasErrorAbout("local client", "remote server"));
    }

    @Test
    public void validationReportsRemoteClientsConnectedToLocalServers() {
        DeploymentModel model = createTestModel();
        model.getArtefactTypes().named("Server").findProvidedPortByName("ssh").setRemote(false);
  
        Binding binding = model.findBindingByName("SSH connection");
        assertTrue(binding.validate().hasErrorAbout("remote client", "local server"));
    }
    
    
     private DeploymentModel createTestModel() {
         return aDeployment()
                .withArtefact(anArtefact()
                    .named("Client")
                    .withClientPort(aClientPort()
                        .named("ssh")
                        .remote()
                        .mandatory()))
                .withArtefact(anArtefact()
                    .named("Server")
                    .withServerPort(aServerPort()
                        .named("ssh")
                        .remote()))
                .withBinding(aBinding()
                    .named("SSH connection")
                    .from("Client", "ssh")
                    .to("Server", "ssh"))
                .build();
    }
}
