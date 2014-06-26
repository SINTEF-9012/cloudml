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
import org.cloudml.facade.commands.Deploy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

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
        results.add(new Object[]{"dump to c:\\temp\\myscript.txt", dumpTo("c:\\temp\\myscript.txt")});
        results.add(new Object[]{"dump to /home/foo/script.txt", dumpTo("/home/foo/script.txt")});
        results.add(new Object[]{"dump 23 to /home/foo/script.txt", dumpTo(23, "/home/foo/script.txt")});
        results.add(new Object[]{"replay /home/foo/script.txt", replay("/home/foo/script.txt")});
        results.add(new Object[]{"messages", showMessages()});
        results.add(new Object[]{"messages 24", showMessages(24)});
        results.add(new Object[]{"history version exit", script(history(), version(), exit())});
        results.add(new Object[]{"deploy &", delegate(new Deploy(), true)});
        results.add(new Object[]{"deploy", delegate(new Deploy(), false)});  
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
