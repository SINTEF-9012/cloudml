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

/*
 */
import java.io.File;
import java.io.FileNotFoundException;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import org.cloudml.codecs.library.CodecsLibrary;
import org.cloudml.core.CloudMLModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SaveAsTest extends TestCase {

    @Test(expected = IllegalArgumentException.class)
    public void testSaveAsWithNullAsModel() throws FileNotFoundException {
        String fileName = "test.json";
        CloudMLModel model = null;
        CodecsLibrary library = new CodecsLibrary();
        library.saveAs(model, fileName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveAsWithEmptyStringAsFileName() throws FileNotFoundException {
        String fileName = "";
        CloudMLModel model = new CloudMLModel();
        CodecsLibrary library = new CodecsLibrary();
        library.saveAs(model, fileName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveAsWithNullAsFileName() throws FileNotFoundException {
        String fileName = null;
        CloudMLModel model = new CloudMLModel();
        CodecsLibrary library = new CodecsLibrary();
        library.saveAs(model, fileName);
    }
    public static final long EMPTY_FILE_SIZE = 0L;

    @Test
    public void testSaveAsWithEmptyDeploymentModel() throws FileNotFoundException {
        String fileName = "test.json";
        CloudMLModel model = new CloudMLModel();
        CodecsLibrary library = new CodecsLibrary();
        library.saveAs(model, fileName);
        File file = new File(fileName);
        assertTrue(file.exists());
        assertEquals(EMPTY_FILE_SIZE, file.length());
        cleanDirectory();
    }

    @Test
    public void testSaveAsWithSensApp() throws FileNotFoundException {
        String fileName = "sensapp.json";
        CloudMLModel model = new SampleModels().buildSensApp();
        CodecsLibrary library = new CodecsLibrary();
        library.saveAs(model, fileName);
        File file = new File(fileName);
        assertTrue(file.exists());
        assertTrue(1000L < file.length());
        cleanDirectory();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveAsWithUnknownException() throws FileNotFoundException {
        String fileName = "test.pouet";
        CloudMLModel model = new CloudMLModel();
        CodecsLibrary library = new CodecsLibrary();
        library.saveAs(model, fileName);
    }

    @Test
    public void testSaveAsWithUpperCase() throws FileNotFoundException {
        String fileName = "test.JSON";
        CloudMLModel model = new CloudMLModel();
        CodecsLibrary library = new CodecsLibrary();
        library.saveAs(model, fileName);
        File file = new File(fileName);
        assertTrue(file.exists());
        assertEquals(EMPTY_FILE_SIZE, file.length());
    }

    @Test
    public void testGetExtensionWithMultipleExtensions() {
        CodecsLibrary library = new CodecsLibrary();
        String extension = library.getFileExtension("test.pouet.toto");
        assertEquals(".toto", extension);
    }

    @Test
    public void testGetExtensionWithMultipleDots() {
        CodecsLibrary library = new CodecsLibrary();
        String extension = library.getFileExtension("test....pouet");
        assertEquals(".pouet", extension);
    }

    @Test
    public void testGetExtensionWithUpperCase() {
        CodecsLibrary library = new CodecsLibrary();
        String extension = library.getFileExtension("test.JSON");
        assertEquals(".json", extension);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetExtensionWithoutExtension() {
        CodecsLibrary library = new CodecsLibrary();
        String extension = library.getFileExtension("test");
    }

    private void cleanDirectory() {
        File test = new File("test.json");
        test.delete();
        File sensapp = new File("sensapp.json");
        sensapp.delete();
    }
}
