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
package test.cloudml.codecs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import org.cloudml.codecs.DotCodec;
import org.cloudml.core.Deployment;
import org.cloudml.core.samples.SshClientServer;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * Specification of the Dot Codec
 */
@RunWith(JUnit4.class)
public class DotCodecTest extends TestCase {

    private static final String OUTPUT_FILE = "target/anyfile.dot";

    @After
    public void removeGeneratedFiles() {
        File file = new File(OUTPUT_FILE);
        file.delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNullAsModel() throws FileNotFoundException {
        aDotCodec().save(null, new FileOutputStream(OUTPUT_FILE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNullAsOutput() {
        aDotCodec().save(SshClientServer.getOneClientConnectedToOneServer().build(), null);
    }

    @Test
    public void shouldCreateANonEmptyFileForSshClientServerExample() throws FileNotFoundException, IOException {
        final Deployment example = SshClientServer.getOneClientConnectedToOneServer().build();
        
        final FileOutputStream output = new FileOutputStream(OUTPUT_FILE);
        aDotCodec().save(example, output);
        output.close();

        File file = new File(OUTPUT_FILE);
        assertThat("not empty", file.length(), is(greaterThan(200L)));

    }

    private DotCodec aDotCodec() {
        return new DotCodec();
    }

}
