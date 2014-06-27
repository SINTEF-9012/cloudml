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

import org.cloudml.ui.shell.commands.ShellCommand;
import static org.cloudml.ui.shell.commands.ShellCommand.*;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * Specification of the History
 */
@RunWith(JUnit4.class)
public class HistoryTest extends TestCase {

    @Test
    public void historyShouldRecordCommands() {
        final History history = new History(null, null);

        history.record(ShellCommand.exit());

        assertThat(history.length(), is(equalTo(1)));
    }

    @Test
    public void historyShouldAllowSelectingTheLastNCommands() {
        final History history = new History(null, null);

        history.record(version());
        history.record(history());
        history.record(dumpTo("C:\\Users\\franckc"));
        history.record(exit());

        assertThat(history.selectLast(1).size(), is(equalTo(1)));
        assertThat(history.selectLast(2).size(), is(equalTo(2)));
        assertThat(history.selectLast(2), contains(exit(), dumpTo("C:\\Users\\franckc")));
        
    }
    
    @Test
    public void selectLastShouldReturnTheCompleteHistoryWhenGivenANegativeCounts() {
        final History history = new History(null, null);
        history.record(version());
        history.record(history());
        history.record(dumpTo("C:\\Users\\franckc"));
        history.record(exit());

        assertThat(history.selectLast(-4),  
                   contains(
                           exit(), 
                           dumpTo("C:\\Users\\franckc"),
                           history(),
                           version()));
    }

}
