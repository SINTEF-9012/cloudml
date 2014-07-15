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
package org.cloudml.ui.shell;

import org.cloudml.ui.shell.terminal.Scenario;
import org.cloudml.ui.shell.terminal.Recorder;
import java.util.ArrayList;
import java.util.Collection;
import junit.framework.TestCase;
import org.cloudml.facade.CloudML;
import org.cloudml.facade.commands.*;
import org.cloudml.ui.shell.commands.ShellCommand;

import static org.cloudml.ui.shell.commands.ShellCommand.*;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Parameterized;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.jmock.Expectations.any;

/**
 * Behavioural specification of the Shell. Check that it propagates messages
 * properly
 */
@RunWith(Parameterized.class)
public class LocalShellCommandTest extends TestCase {

    private final Mockery context = new JUnit4Mockery();

    private final String description;
    private final ShellCommand command;

    public LocalShellCommandTest(String description, ShellCommand command) {
        this.description = description;
        this.command = command;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> testCases() {
        Collection<Object[]> testCases = new ArrayList<Object[]>();

        testCases.add(new Object[]{"version", version()});
        testCases.add(new Object[]{"history", history()});
        testCases.add(new Object[]{"history 3", history(2)});
        testCases.add(new Object[]{"messages", showMessages()});
        testCases.add(new Object[]{"messages 3", showMessages(3)});
        testCases.add(new Object[]{"exit", exit()});
        testCases.add(new Object[]{"dump", script(version(), history(), dumpTo("test.txt"))});
        testCases.add(new Object[]{"replay", replay("test.txt")});
        testCases.add(new Object[]{"nothing", empty()}); 
        return testCases;
    }

    @Test
    public void localShellCommandsShouldNotBeDelegatedToTheFacade() {
        final Scenario input = new Scenario(command);
        final Recorder output = new Recorder();

        final CloudML proxy = context.mock(CloudML.class);

        context.checking(new Expectations() {
            {
                oneOf(proxy).register(with(any(Mailbox.EventHandler.class)));
                never(proxy).fireAndForget(with(any(CloudMlCommand.class)));
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


    private static ShellCommand empty() {
        return new ShellCommand() {

            @Override
            public String toString() {
                return "";
            }

        };
    }

}
