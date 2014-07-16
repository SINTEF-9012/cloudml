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

package org.cloudml.ui.shell;

import org.cloudml.ui.shell.terminal.Scenario;
import org.cloudml.ui.shell.terminal.Recorder;
import java.util.ArrayList;
import java.util.Collection;
import org.cloudml.facade.CloudML;
import org.cloudml.facade.commands.CloudMlCommand;
import org.cloudml.facade.commands.*;
import org.cloudml.ui.shell.commands.ShellCommand;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.jmock.Expectations.any;

/**
 * Check that CloudML actions are properly delegated to the facade
 */
@RunWith(Parameterized.class)
public class AsynchronousCloudMLCommandTest {

    private final Mockery context = new JUnit4Mockery();

    private final String description;
    private final CloudMlCommand command;

    public AsynchronousCloudMLCommandTest(String description, CloudMlCommand command) {
        this.description = description;
        this.command = command;
    }
    
    
    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> testCases() {
        Collection<Object[]> testCases = new ArrayList<Object[]>();
        
        testCases.add(new Object[]{"deploy", new Deploy()});
        testCases.add(new Object[]{"connect", new Attach("foo", "bar")}); 
        testCases.add(new Object[]{"disconnect", new Detach("foo", "bar")}); 
        testCases.add(new Object[]{"start", new StartComponent("foo")}); 
        testCases.add(new Object[]{"stop", new StopComponent("foo")}); 
        testCases.add(new Object[]{"install", new Install("foo", "bar")}); 
        testCases.add(new Object[]{"uninstall", new Uninstall("foo", "bar")}); 
        testCases.add(new Object[]{"load deployment", new LoadDeployment("foo.json")}); 
        testCases.add(new Object[]{"store deployment", new StoreDeployment("foo.json")}); 
        testCases.add(new Object[]{"load credentials", new LoadCredentials("foo.credentials")}); 
        testCases.add(new Object[]{"store credentials", new StoreCredentials("foo.credentials")}); 
        testCases.add(new Object[]{"list types", new ListComponents()}); 
        testCases.add(new Object[]{"list instances", new ListComponentInstances()}); 
        testCases.add(new Object[]{"view type", new ViewComponent("foo")}); 
        testCases.add(new Object[]{"view instance", new ViewComponentInstance("foo")}); 
        testCases.add(new Object[]{"destory", new Destroy("foo")}); 
        testCases.add(new Object[]{"instantiate", new Instantiate("foo", "foo1")}); 
        testCases.add(new Object[]{"snapshot", new Snapshot("foo.png")}); 
        testCases.add(new Object[]{"upload", new Upload("foo", "afile.sh", "home/afile.sh")}); 
    
        return testCases;
    }
    
        
    @Test
    public void shellShouldDelegateToCloudML() {
        final Scenario input = new Scenario(ShellCommand.delegate(command, true));
        final Recorder output = new Recorder();

        final CloudML proxy = context.mock(CloudML.class);

        context.checking(new Expectations() {
            {
                oneOf(proxy).register(with(any(Mailbox.EventHandler.class)));
                oneOf(proxy).fireAndForget(with(any(CloudMlCommand.class)));
                never(proxy).fireAndWait(with(any(CloudMlCommand.class)));
                oneOf(proxy).terminate();
            }
        });

        final Shell shell = new Shell(input, output, proxy);
        shell.start();

        context.assertIsSatisfied();

        assertThat(output.record(), not(containsString("Error")));
        assertThat(output.record(), not(containsString("Exception")));
    }
}
