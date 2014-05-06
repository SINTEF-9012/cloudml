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

import java.io.File;
import java.io.FileNotFoundException;
import junit.framework.TestCase;
import org.cloudml.codecs.library.CodecsLibrary;
import org.cloudml.core.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.cloudml.core.samples.SensApp.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@RunWith(JUnit4.class)
public class SaveAsTest extends TestCase {

    @Test(expected = IllegalArgumentException.class)
    public void saveAsShouldRejectNullAsDeploymentModel() throws FileNotFoundException {
        aCodecLibrary().saveAs(null, "any_file.json");
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveAsShouldRejectEmptyStringAsFileName() throws FileNotFoundException {
        aCodecLibrary().saveAs(new Deployment(), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveAsShouldRejectNullAsFileName() throws FileNotFoundException {
        aCodecLibrary().saveAs(new Deployment(), null);
    }

    @Test
    public void saveAsShouldProduceASmallFileForAnEmptyDeployment() throws FileNotFoundException {
        final String fileName = "test.json";

        aCodecLibrary().saveAs(new Deployment(), fileName);

        assertThatFileIsNearlyEmpty(fileName);

        cleanDirectory();
    }

    @Test
    public void testSaveAsShouldProduceALargeFileForSensApp() throws FileNotFoundException {
        final String fileName = "sensapp.json";
 
        aCodecLibrary().saveAs(completeSensApp().build(), fileName);

        final long sizeInBytes = 1000L;
        assertThatFileSizeIsAtLeast(fileName, sizeInBytes);

        cleanDirectory();
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveAsShouldRejectFileWhoseFormatIsNotSupported() throws FileNotFoundException {
        aCodecLibrary().saveAs(new Deployment(), "test.pouet");
    }

    @Test
    public void saveAsShouldAcceptFilesThatAreSupportedRegardlessOfTheCase() throws FileNotFoundException {
        String aFileWithUppercaseExtension = "test.JSON"; 

        aCodecLibrary().saveAs(new Deployment(), aFileWithUppercaseExtension);

        assertThatFileIsNearlyEmpty(aFileWithUppercaseExtension);

        cleanDirectory();
    }

    // Helpers methods
    private CodecsLibrary aCodecLibrary() {
        return new CodecsLibrary();
    }

    private File assertThatFileExists(String path) {
        final File file = new File(path);
        assertThat("missing file '" + path + "'", file.exists());
        return file;
    }

    private File assertThatFileIsNearlyEmpty(String path) {
        final long minimumSize = 70L; // The empty deployment is 66 bytes
        final File file = assertThatFileExists(path);
        assertThat("file '" + file + "' is too large", file.length(), is(lessThan(minimumSize)));
        return file;
    }

    private File assertThatFileSizeIsAtLeast(String path, long maxSize) {
        final File file = assertThatFileExists(path);
        assertThat("file '" + file + "' is too small", file.length(), is(greaterThan(maxSize)));
        return file;
    }

    private void cleanDirectory() {
        File test = new File("test.json");
        test.delete();
        File sensapp = new File("sensapp.json");
        sensapp.delete();
    }
}
