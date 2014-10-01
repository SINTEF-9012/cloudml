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

import java.io.File;
import java.io.FileNotFoundException;

import static org.cloudml.ui.shell.commands.ShellCommand.*;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 *
 */
@RunWith(JUnit4.class)
public class ScriptTest extends TestCase {

    @Test
    public void toFileShouldCreateANonEmptyFile() throws FileNotFoundException {
        final ShellCommand script = script(history(), version(), exit());
        final String destination = "test.txt";

        script.toFile(destination);

        File file = new File(destination);

        assertThat("no file generated!", file.exists());
        assertThat("empty file generated!", file.length(), is(greaterThan(0L)));

        file.delete();
    }

    @Test
    public void generatedScriptsShouldBeReadble() throws Exception {
        final String destination = "test.txt";
        final ShellCommand script = script(history(), version(), exit());
        
        try {
            script.toFile(destination);
            final ShellCommand script2 = fromFile(destination);

            assertThat("Different files", script2, is(equalTo(script)));

        } catch (Exception e) {
            throw e; 

        } finally {
            File file = new File(destination);
            file.delete();
        
        }
    }

}
