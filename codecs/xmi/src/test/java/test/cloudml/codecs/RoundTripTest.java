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
package test.cloudml.codecs;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestCase;
import org.cloudml.codecs.commons.Codec;
import org.cloudml.core.*;

/**
 * Unit test for rountrip testing of CloudML models.
 */
public class RoundTripTest extends TestCase {

    File input;
    Codec codec;

    public RoundTripTest(String testName) {
        super(testName);
    }

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public RoundTripTest(String testName, File input, Codec codec) {
        this(testName);
        this.input = input;
        this.codec = codec;       
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new XMIRoundTripTestSuite();
    }

    /**
     * Rigourous Test :-)
     */
    public void testRoundTrip() {
        long start = System.currentTimeMillis();
        if (input != null && codec != null) {
            InputStream stream = null;
            try {
                stream = new FileInputStream(input);
                
            } catch (IOException ex) {
                Logger.getLogger(RoundTripTest.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                System.out.println("took " + (System.currentTimeMillis() - start) + " ms" );
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(RoundTripTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            System.out.println("Dummy test: no codec and/or no input file");
        }
    }
}
