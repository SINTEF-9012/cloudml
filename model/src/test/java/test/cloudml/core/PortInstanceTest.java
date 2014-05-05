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

import org.cloudml.core.Port;
import org.cloudml.core.PortInstance;
import org.cloudml.core.WithResources;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.*;

@RunWith(JUnit4.class) 
public abstract class PortInstanceTest extends WithResourcesTest {

    @Override
    public final WithResources aSampleWithResources(String name) {
        return aSamplePortInstance(name);
    }

    public abstract PortInstance<? extends Port> aSamplePortInstance(String name);
    
    
    public final PortInstance<? extends Port> aSamplePortInstance() {
        return aSamplePortInstance("sut");
    }

    @Test
    public void testValidationPassesWithoutWarningWhenValid() {
        final PortInstance<? extends Port> instance = aSamplePortInstance();

        final Report validation = instance.validate();

        assertThat("valid ports shall have no issues",
                   validation.pass(Report.WITHOUT_WARNING));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullIsRejectedAsPortType() {
        final PortInstance<? extends Port> instance = aSamplePortInstance();
        instance.setType(null);
    }

    @Test
    public void validationReportsMissingOwner() {
        final PortInstance<? extends Port> instance = aSamplePortInstance();
        instance.getOwner().discard();

        final Report validation = instance.validate();

        assertThat("null owner shall be detected",
                   validation.hasErrorAbout("no", "owner"));
    }
}
