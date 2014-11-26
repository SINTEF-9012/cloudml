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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import junit.framework.TestCase;
import org.cloudml.facade.commands.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.cloudml.facade.commands.ValidateCommand.REPORT_ONLY_ERRORS;
import static org.cloudml.facade.commands.ValidateCommand.REPORT_WARNINGS_AND_ERRORS;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import static org.cloudml.ui.shell.commands.ShellCommand.*;

/**
 * Specification of the commands accepted by the parser
 */
@RunWith(Parameterized.class)
public class ShellCommandFactoryTest extends TestCase {

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> samples() {
        final List<Object[]> results = new ArrayList<Object[]>();
        
        results.add(new Object[]{"exit", exit()});
        results.add(new Object[]{"quit", exit()});
        results.add(new Object[]{"version", version()});
        results.add(new Object[]{"help", help()});
        results.add(new Object[]{"help \"foo\"", help("foo")});
        results.add(new Object[]{"history", history()});
        results.add(new Object[]{"history 23", history(23)});
        results.add(new Object[]{"history 3", history(3)});
        results.add(new Object[]{"dump to c:\\temp\\myscript.txt", dumpTo("c:\\temp\\myscript.txt")});
        results.add(new Object[]{"dump to /home/foo/script.txt", dumpTo("/home/foo/script.txt")});
        results.add(new Object[]{"dump 23 to /home/foo/script.txt", dumpTo(23, "/home/foo/script.txt")});
        results.add(new Object[]{"replay /home/foo/script.txt", replay("/home/foo/script.txt")});
        results.add(new Object[]{"messages", showMessages()});
        results.add(new Object[]{"messages 4", showMessages(4)});
        results.add(new Object[]{"messages 24", showMessages(24)});
        results.add(new Object[]{"history version exit", script(history(), version(), exit())});
        results.add(new Object[]{"deploy &", delegate(new Deploy(), true)});
        results.add(new Object[]{"deploy", delegate(new Deploy(), false)});  
        results.add(new Object[]{"connect foo to bar", delegate(new Attach("foo", "bar"), false)});
        results.add(new Object[]{"connect @8f1d4fae-7dec-11d0-a765-00a0c91e6bf6 to bar", delegate(new Attach("8f1d4fae-7dec-11d0-a765-00a0c91e6bf6", "bar"), false)});
        results.add(new Object[]{"disconnect foo from bar", delegate(new Detach("foo", "bar"), false)});
        results.add(new Object[]{"destroy foo", delegate(new Destroy("foo"), false)});
        results.add(new Object[]{"install foo on bar", delegate(new Install("foo", "bar"), false)});
        results.add(new Object[]{"uninstall foo from bar", delegate(new Uninstall("bar", "foo"), false)});
        results.add(new Object[]{"instantiate foo as bar", delegate(new Instantiate("foo", "bar"), false)});
        results.add(new Object[]{"list types", delegate(new ListComponents(), false)});
        results.add(new Object[]{"list instances", delegate(new ListComponentInstances(), false)});
        results.add(new Object[]{"view type foo", delegate(new ViewComponent("foo"), false)});
        results.add(new Object[]{"view instance foo", delegate(new ViewComponentInstance("foo"), false)});
        results.add(new Object[]{"load credentials from c:\\Users\\franckc\\credentials", delegate(new LoadCredentials("c:\\Users\\franckc\\credentials"), false)});
        results.add(new Object[]{"load deployment from c:\\Users\\franckc\\sensapp.json", delegate(new LoadDeployment("c:\\Users\\franckc\\sensapp.json"), false)});
        results.add(new Object[]{"view instance foo", delegate(new ViewComponentInstance("foo"), false)});
        results.add(new Object[]{"store credentials to c:\\Users\\franckc\\credentials", delegate(new StoreCredentials("c:\\Users\\franckc\\credentials"), false)});
        results.add(new Object[]{"store deployment to c:\\Users\\franckc\\sensapp.json", delegate(new StoreDeployment("c:\\Users\\franckc\\sensapp.json"), false)});
        results.add(new Object[]{"upload c:\\sensapp.json on foo at /home/sensapp.json", delegate(new Upload("c:\\sensapp.json", "foo", "/home/sensapp.json"), false)});
        results.add(new Object[]{"shot to /home/sensapp.json", delegate(new ShotImage("/home/sensapp.json"), false)});
        results.add(new Object[]{"scale out @8f1d4fae-7dec-11d0-a765-00a0c91e6bf6", delegate(new ScaleOut("8f1d4fae-7dec-11d0-a765-00a0c91e6bf6"), false)});
        results.add(new Object[]{"validate", delegate(new ValidateCommand(REPORT_WARNINGS_AND_ERRORS), false)});
        results.add(new Object[]{"validate no warnings", delegate(new ValidateCommand(REPORT_ONLY_ERRORS), false)});
           
        return results;
    }
    
    
    private final String text;
    private final ShellCommand expectation;
    
    public ShellCommandFactoryTest(String text, ShellCommand expectation) {
        this.text = text;
        this.expectation = expectation;
    }
    
    
    @Test
    public void builderShouldAccept() {
        ShellCommand actual = ShellCommand.fromText(text);
        assertThat(actual, is(equalTo(expectation)));
    }



}
