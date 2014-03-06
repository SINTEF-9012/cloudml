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

import junit.framework.TestCase;
import static junit.framework.TestCase.assertTrue;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactPort;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import test.cloudml.core.builder.ArtefactBuilder;

@RunWith(JUnit4.class)
public class ArtefactTest extends TestCase {

    @Test
    public void passValidationWhenValid() {
        ArtefactBuilder builder = new ArtefactBuilder("My Artefact");
        builder.createClientPort("port", ArtefactPort.REMOTE, ArtefactPort.LOCAL);
        Artefact artefact = builder.getResult();

        assertTrue(artefact.validate().pass(false));
    }

    @Test
    public void validationReportsNullName() {
        Artefact artefact = new Artefact();
        assertTrue(artefact.validate().hasErrorAbout("name"));
    }

    @Test
    public void validationReportsEmptyName() {
        Artefact artefact = new Artefact("");
        assertTrue(artefact.validate().hasErrorAbout("name"));
    }
    
    @Test
    public void validationReportsNoPorts() {
        Artefact artefact = new Artefact("my Artefact");
        assertTrue(artefact.validate().hasWarningAbout("port"));
    }
}
