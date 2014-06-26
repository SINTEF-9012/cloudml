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
package org.cloudml.ui.shell2;

import org.cloudml.ui.shell.ShellMode;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * Specification of the execution modes of the CloudML shell
 */
@RunWith(JUnit4.class)
public class ShellModeTest extends TestCase {

    
    @Test
    public void batchModeShouldBeTriggeredByShortFlag() {
        ShellMode mode = ShellMode.from("-b", "replay", "c:\\Users\\franckc\\script.txt");
        assertThat(mode, is(equalTo(ShellMode.batch("replay c:\\Users\\franckc\\script.txt"))));
    }

    @Test
    public void batchModeShouldBeTriggeredByFullFlag() {
        ShellMode mode = ShellMode.from("--batch", "replay", "c:\\Users\\franckc\\script.txt");
        assertThat(mode, is(equalTo(ShellMode.batch("replay c:\\Users\\franckc\\script.txt"))));
    }

    @Test
    public void interactiveModeShouldBeTheTriggerByTheFullFlag() {
        ShellMode mode = ShellMode.from("--interactive");
        assertThat(mode, is(equalTo(ShellMode.interactive())));
    }

    @Test
    public void interactiveModeShouldBeTheTriggerByTheShortFlag() {
        ShellMode mode = ShellMode.from("-i");
        assertThat(mode, is(equalTo(ShellMode.interactive())));
    }

    @Test
    public void interactiveModeShouldBeTheDefaultOne() {
        ShellMode mode = ShellMode.from(new String[]{});
        assertThat(mode, is(equalTo(ShellMode.interactive())));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void illegalFlagsShouldBeDetected() {
        ShellMode mode = ShellMode.from("--interactive --foo");        
    }

}
