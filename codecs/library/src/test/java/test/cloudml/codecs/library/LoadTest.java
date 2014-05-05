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

import java.io.FileNotFoundException;
import junit.framework.TestCase;
import org.cloudml.codecs.library.CodecsLibrary;
import org.cloudml.core.Deployment;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@Ignore 
@RunWith(JUnit4.class)
public class LoadTest extends TestCase {

    private static final String TEST_RESOURCE_PATH="src/test/resources/";

    @Test(expected = FileNotFoundException.class)
    public void testLoadUnexistingFile() throws FileNotFoundException {
        CodecsLibrary library = new CodecsLibrary();
        String fileName = "plop.json";
        library.load(fileName);
    }

    @Test
    public void testLoadUpperCaseFileName() throws FileNotFoundException {
        CodecsLibrary library = new CodecsLibrary();
        String fileName = TEST_RESOURCE_PATH+"sensapp2.JSON";
        Deployment model=library.load(fileName);
        assertFalse(model.getComponentInstances().isEmpty());
        assertFalse(model.getComponents().isEmpty());
        assertFalse(model.getComponentInstances().onlyExternals().isEmpty());
        assertFalse(model.getComponents().onlyExternals().isEmpty());
        assertTrue(model.getClouds().isEmpty());
        assertFalse(model.getRelationshipInstances().isEmpty());
        assertFalse(model.getRelationships().isEmpty());
        assertFalse(model.getProviders().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadEmptyFileName() throws FileNotFoundException {
        CodecsLibrary library = new CodecsLibrary();
        String fileName = "";
        library.load(fileName);
    }

    @Test
    public void testLoadEmptyFile() throws FileNotFoundException {
        CodecsLibrary library = new CodecsLibrary();
        String fileName = TEST_RESOURCE_PATH+"empty.json";
        Deployment model = library.load(fileName);
        assertTrue(model.getComponentInstances().isEmpty());
        assertTrue(model.getComponents().isEmpty());
        assertTrue(model.getComponentInstances().onlyExternals().isEmpty());
        assertTrue(model.getComponents().onlyExternals().isEmpty());
        assertTrue(model.getClouds().isEmpty());
        assertTrue(model.getRelationshipInstances().isEmpty());
        assertTrue(model.getRelationships().isEmpty());
        assertTrue(model.getProviders().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadUnknownEnstension() throws FileNotFoundException {
        CodecsLibrary library = new CodecsLibrary();
        String fileName = "plop.cc";
        library.load(fileName);
    }

}
