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
package test.cloudml.core;

import junit.framework.TestCase;
import static junit.framework.TestCase.assertTrue;
import org.cloudml.core.ArtefactPortInstance;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public abstract class ArtefactPortInstanceTest extends TestCase {

    public abstract ArtefactPortInstance getValidInstance();

    @Test
    public void testPassValidationWithoutWarningWhenValid() {
        ArtefactPortInstance instance = getValidInstance();

        Report validation = instance.validate();

        assertTrue(validation.pass(Report.WITHOUT_WARNING));
    }

    @Test
    public void validationReportsNullType() {
        ArtefactPortInstance instance = getValidInstance();
        instance.setType(null);

        Report validation = instance.validate();

        assertTrue(validation.hasErrorAbout("type", "null"));
    }

    @Test
    public void validationReportsNullOwner() {
        ArtefactPortInstance instance = getValidInstance();
        instance.setOwner(null);

        Report validation = instance.validate();

        assertTrue(validation.hasErrorAbout("owner", "null"));
    }
}
