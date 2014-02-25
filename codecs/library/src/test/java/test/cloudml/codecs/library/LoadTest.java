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
package test.cloudml.codecs.library;

import java.io.FileNotFoundException;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertTrue;
import org.cloudml.codecs.library.CodecsLibrary;
import org.cloudml.core.CloudMLModel;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class LoadTest extends TestCase {

    @Test(expected = FileNotFoundException.class)
    public void testLoadUnexistingFile() throws FileNotFoundException {
        CodecsLibrary library = new CodecsLibrary();
        String fileName = "plop.json";
        library.load(fileName);
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
        String fileName = "src/test/resources/empty.json";
        CloudMLModel model = library.load(fileName);
        assertTrue(model.getComponentInstances().isEmpty());
        assertTrue(model.getComponents().isEmpty());
        assertTrue(model.getExternalComponentInstances().isEmpty());
        assertTrue(model.getExternalComponents().isEmpty());
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

    // TODO: we shall be able to compare two models
    @Ignore
    @Test
    public void testCompareSensApp() throws FileNotFoundException {
        CodecsLibrary library = new CodecsLibrary();
        String fileName = "src/test/resources/sensapp.json";
        CloudMLModel sensapp = library.load(fileName);
        CloudMLModel reference = new SampleModels().buildSensApp();
        assertEquals(reference, sensapp);
    }
}
