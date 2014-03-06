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
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.Provider;
import org.cloudml.core.ServerPort;
import org.cloudml.core.ServerPortInstance;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import test.cloudml.core.builder.ArtefactBuilder;
import test.cloudml.core.builder.Builder;

@RunWith(JUnit4.class)
public class ArtefactInstanceTest extends TestCase {

    @Test
    public void validationPassWithoutWarningWhenValid() {
        ArtefactInstance instance = prepareInstance();
        assertTrue(instance.validate().pass(Report.WITHOUT_WARNING));
    }

    @Test
    public void validationReportsNullName() {
        ArtefactInstance instance = prepareInstance();
        instance.setName(null);
        assertTrue(instance.validate().hasErrorAbout("null", "name"));
    }

    @Test
    public void validationReportsEmptyName() {
        ArtefactInstance instance = prepareInstance();
        instance.setName("");
        assertTrue(instance.validate().hasErrorAbout("empty", "name"));
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
        System.out.println(report);
        
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
        Builder builder = new Builder();
        Provider providers = builder.createProvider("EC2");
        Node linux = builder.createNodeType("Linux", providers);
        NodeInstance host1 = builder.provision(linux, "host1");
        ArtefactBuilder appBuilder = builder.createArtefactType("My App");
        appBuilder.createClientPort("rp#1", ClientPort.LOCAL, ClientPort.MANDATORY);
        appBuilder.createClientPort("rp#2", ClientPort.REMOTE, ClientPort.MANDATORY);
        appBuilder.createServerPort("pp#1", ServerPort.LOCAL);
        appBuilder.createServerPort("pp#2", ServerPort.REMOTE);
        return builder.install(appBuilder.getResult(), "app 1", host1);
    }
}
