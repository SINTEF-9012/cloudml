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
import org.cloudml.core.ClientPort;
import org.cloudml.core.ClientPortInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.ServerPort;
import org.cloudml.core.ServerPortInstance;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.cloudml.core.builders.Commons.*;

@RunWith(JUnit4.class)
public class ArtefactInstanceTest extends TestCase {

    @Test
    public void validationPassWithoutWarningWhenValid() {
        ArtefactInstance instance = prepareInstance();
        assertTrue(instance.validate().pass(Report.WITHOUT_WARNING));
    }

    @Test
    public void validationReportsNullType() {
        ArtefactInstance instance = prepareInstance();
        instance.setType(null);
        assertTrue(instance.validate().hasErrorAbout("null", "type"));
    }

    @Test
    public void validationReportsMissingClientPort() {
        ArtefactInstance instance = prepareInstance();
        instance.getRequired().remove(0);

        Report report = instance.validate();

        assertTrue(report.hasErrorAbout("Missing", "port", "client"));
    }

    @Test
    public void validationReportsExtraClientPort() {
        ArtefactInstance instance = prepareInstance();
        instance.getRequired().add(new ClientPortInstance("rp#3", new ClientPort("extra port", null, true), instance));

        Report report = instance.validate();
        
        assertTrue(report.hasErrorAbout("extra", "port", "client"));
    }

    @Test
    public void validationReportsMissingServerPort() {
        ArtefactInstance instance = prepareInstance();
        instance.getProvided().remove(0);

        Report report = instance.validate();

        assertTrue(report.hasErrorAbout("Missing", "port", "server"));
    }
    
    @Test
    public void validationReportsExtraServerPort() {
        ArtefactInstance instance = prepareInstance();
        instance.getProvided().add(new ServerPortInstance("pp#3", new ServerPort("extra port", null, true), instance));

        Report report = instance.validate();
        
        assertTrue(report.hasErrorAbout("extra", "port", "server"));
    }

    private ArtefactInstance prepareInstance() {
        DeploymentModel model = aDeployment()
                .withProvider(aProvider().named("EC2"))
                .withNodeType(aNode()
                    .named("Linux")
                    .providedBy("EC2"))
                .withNodeInstance(aNodeInstance()
                    .named("host 1")
                    .ofType("Linux"))
                .withArtefact(anArtefact()
                    .named("My App")
                    .withClientPort(aClientPort().named("rp#1").remote().mandatory())
                    .withClientPort(aClientPort().named("rp#2").remote().mandatory())
                    .withServerPort(aServerPort().named("pp#1").remote())
                    .withServerPort(aServerPort().named("pp#2").remote()))
                .withArtefactInstance(anArtefactInstance()
                    .named("app 1")
                    .ofType("My App")
                    .hostedBy("host 1"))
                .build();
                
        return model.findArtefactInstanceByName("app 1");
    }
}
