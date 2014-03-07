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
import org.cloudml.core.ArtefactPort;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public abstract class ArtefactPortTest extends TestCase {
    public static final boolean WITHOUT_WARNING = false;

    public abstract ArtefactPort getPortWithoutName();
    
    public abstract ArtefactPort getPortWithoutOwner();

    public abstract ArtefactPort getValidPort();
    
    @Test
    public void testValidationReportsMissingOwner() {
        ArtefactPort port = getPortWithoutOwner();

        assertTrue(port.validate().hasErrorAbout("owner"));
    }
    
    @Test
    public void testThatValidationsPassWhenPortIsValid() {
        ArtefactPort port = getValidPort();
        
        assertTrue(port.validate().pass(WITHOUT_WARNING));
    }
}
