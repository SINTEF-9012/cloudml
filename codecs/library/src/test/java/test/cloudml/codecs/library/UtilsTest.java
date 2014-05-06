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
package test.cloudml.codecs.library;

import org.cloudml.codecs.library.Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Test of the helper methods
 */
@RunWith(JUnit4.class)
public class UtilsTest {

    @Test
    public void getExtensionShouldReturnTheLastExtension() {
        final String extension = Utils.getFileExtension("test.pouet.toto");
        assertThat("wong extension", extension, is(equalTo(".toto")));
    }

    @Test
    public void getExtenstionShouldAcceptSequenceOfDots() {
        final String extension = Utils.getFileExtension("test....pouet");
        assertThat("wong extension", extension, is(equalTo(".pouet")));
    }

    @Test
    public void getExtensionShouldAcceptUppercaseExtension() {
        final String extension = Utils.getFileExtension("test.JSON");
        assertThat("wong extension", extension, is(equalTo(".json")));
    }

    @Test
    public void getExtensionShouldAcceptPath() {
        final String extension = Utils.getFileExtension("this/is/a/longer/path.json");
        assertThat("wong extension", extension, is(equalTo(".json")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getExtensionShouldRejectFileWithoutExtension() {
        Utils.getFileExtension("test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getExtensionShouldRejectFileIllformedExtension() {
        Utils.getFileExtension("file.xx$%");
    }
}
