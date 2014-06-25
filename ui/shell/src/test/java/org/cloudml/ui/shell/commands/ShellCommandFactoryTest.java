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
package org.cloudml.ui.shell.commands;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Specification of the commands
 */
@RunWith(JUnit4.class)
public class ShellCommandFactoryTest extends TestCase {

    @Test
    public void fromTextShouldBuildExit() {
        final String command = "exit";

        ShellCommand actual = ShellCommand.fromText(command);

        assertThat(actual, is(equalTo(ShellCommand.exit())));
    }

    @Test
    public void fromTextShouldBuildQuit() {
        final String command = "quit";

        ShellCommand actual = ShellCommand.fromText(command);

        assertThat(actual, is(equalTo(ShellCommand.exit())));
    }

    @Test
    public void fromTextShouldBuildVersion() {
        final String command = "version";

        ShellCommand actual = ShellCommand.fromText(command);

        assertThat(actual, is(equalTo(ShellCommand.version())));
    }

    @Test
    public void fromTextShouldBuildHelp() {
        final String command = "help";

        ShellCommand actual = ShellCommand.fromText(command);

        assertThat(actual, is(equalTo(ShellCommand.help())));
    }

    @Test
    public void fromTextShouldBuildHelpWithASubject() {
        final String command = "help \"foo\"";

        ShellCommand actual = ShellCommand.fromText(command);

        assertThat(actual, is(equalTo(ShellCommand.help("foo"))));
    }

    @Test
    public void fromTextShouldBuildHistory() {
        final String command = "history";

        ShellCommand actual = ShellCommand.fromText(command);

        assertThat(actual, is(equalTo(ShellCommand.history())));
    }

    @Test
    public void fromTextShouldBuildHistoryWithDepth() {
        final String command = "history 23";

        ShellCommand actual = ShellCommand.fromText(command);

        assertThat(actual, is(equalTo(ShellCommand.history(23))));
    }

    @Test
    public void fromTextShouldBuildDumpWithAWindowsFile() {
        final String command = "dump to c:\\temp\\myscript.txt";

        ShellCommand actual = ShellCommand.fromText(command);

        assertThat(actual, is(equalTo(ShellCommand.dumpTo("c:\\temp\\myscript.txt"))));
    }

    @Test
    public void fromTextShouldBuildDumpWithALinuxFile() {
        final String command = "dump to /home/foo/script.txt";

        ShellCommand actual = ShellCommand.fromText(command);

        assertThat(actual, is(equalTo(ShellCommand.dumpTo("/home/foo/script.txt"))));
    }

    @Test
    public void fromTextShouldBuildDumpWithDepth() {
        final String command = "dump 23 to /home/foo/script.txt";

        ShellCommand actual = ShellCommand.fromText(command);

        assertThat(actual, is(equalTo(ShellCommand.dumpTo(23, "/home/foo/script.txt"))));
    }

    @Test
    public void fromTextShouldBuildReplay() {
        ShellCommand actual = ShellCommand.fromText("replay /home/foo/script.txt");

        assertThat(actual, is(equalTo(ShellCommand.replay("/home/foo/script.txt"))));
    }

    @Test
    public void fromTextShouldBuildMessages() {
        ShellCommand actual = ShellCommand.fromText("messages");

        assertThat(actual, is(equalTo(ShellCommand.showMessages())));
    }

    @Test
    public void fromTextShouldBuildMessagesWithDepth() {
        ShellCommand actual = ShellCommand.fromText("messages 543");

        assertThat(actual, is(equalTo(ShellCommand.showMessages(543))));
    }

}
