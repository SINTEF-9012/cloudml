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

import org.cloudml.codecs.library.CodecsLibrary;
import org.cloudml.core.Deployment;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import junit.framework.TestCase;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;

import static org.cloudml.core.samples.SensApp.completeSensApp;

/**
 * Created by Nicolas Ferry & Franck Chauvel on 25.02.14.
 */
@Ignore
@RunWith(value = Parameterized.class)
public class AcceptanceTest extends TestCase {

    private String extension;

    public AcceptanceTest(String extension) {
        this.extension = extension;
    }

    @Parameterized.Parameters
    public static Collection<String[]> data() {
        Collection<String[]> result = new ArrayList<String[]>();
        CodecsLibrary lib = new CodecsLibrary();
        for (String s : lib.getExtensions()) {
            result.add(new String[]{s});
        }
        return result;
    }

    @Test
    public void testSaveAsAndLoadForEachCodec() throws FileNotFoundException {
        CodecsLibrary library = new CodecsLibrary();
        Deployment reference = completeSensApp().build();
        String fileName = "sensapp" + extension;
        library.saveAs(reference, fileName);
        Deployment sensapp = library.load(fileName);
        assertEquals(reference, sensapp);
    }

    @After
    public void cleanDirectory() {
        File sensapp = new File("sensapp" + extension);
        sensapp.delete();
    }
}
