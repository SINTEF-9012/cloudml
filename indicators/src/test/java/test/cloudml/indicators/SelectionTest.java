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
package test.cloudml.indicators;

import org.cloudml.indicators.Selection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Test of the tag list
 */
@RunWith(JUnit4.class)
public class SelectionTest {

    @Test
    public void shouldBeParsedFromAString() {
        final int TEXT = 0;
        final int EXPECTED_TAG = 1;

        final Object[][] examples = new Object[][]{
            new Object[]{"internal", Selection.INTERNAL},
            new Object[]{"INTERNAL", Selection.INTERNAL},
            new Object[]{"inTERNAl", Selection.INTERNAL},
            new Object[]{"  intERnal  ", Selection.INTERNAL},
            new Object[]{"external", Selection.EXTERNAL},
            new Object[]{"EXTERNAL", Selection.EXTERNAL},
            new Object[]{"exTERNAl", Selection.EXTERNAL},
            new Object[]{"  extERnal  ", Selection.EXTERNAL},
            new Object[]{"vm", Selection.VM},
            new Object[]{"VM", Selection.VM},
            new Object[]{"vM", Selection.VM},
            new Object[]{" Vm  ", Selection.VM},};

        for (Object[] eachExample: examples) {
            final Selection tag = Selection.readFrom((String) eachExample[TEXT]);
            assertThat("Internal not properly parsed!",
                       tag, is(equalTo(eachExample[EXPECTED_TAG]))
            );
        }
    }

}
