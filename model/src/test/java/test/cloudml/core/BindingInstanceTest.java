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
import org.cloudml.core.BindingInstance;
import org.cloudml.core.ClientPort;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.ServerPort;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.cloudml.core.samples.SshClientServer;

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
      
        assertTrue(report.hasErrorAbout("illegal", "binding", "instance", "type", "server"));
    }

    @Test
    public void validateReportsIllegalClientSide() {
        BindingInstance instance = prepareBindingInstance();
        instance.getClient().setType(new ClientPort("Foo", null, true));

        Report report = instance.validate();
      
        assertTrue(report.hasErrorAbout("illegal", "binding", "instance", "type", "client"));
    }

    private BindingInstance prepareBindingInstance() {
        DeploymentModel model = new SshClientServer()
                .getOneClientConnectedToOneServer()
                .build();
        
        return model.findBindingInstanceByName(SshClientServer.BINDING_NO_1);
    }
}
