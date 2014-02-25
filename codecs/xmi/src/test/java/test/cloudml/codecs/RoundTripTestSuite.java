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

import java.io.File;
import java.io.FilenameFilter;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestSuite;
import org.cloudml.codecs.commons.Codec;

/**
 *
 * @author bmori
 */
public abstract class RoundTripTestSuite extends TestSuite {
    Codec codec;
    FilenameFilter filter;

    public Codec getCodec() {
        return codec;
    }

    public void setCodec(Codec codec) {
        this.codec = codec;
    }

    public FilenameFilter getFilter() {
        return filter;
    }

    public void setFilter(FilenameFilter filter) {
        this.filter = filter;
    }  
    
    public RoundTripTestSuite() {
        super("Testing roundtrip serialization");
    }
    
    protected void init() {
        try {
            File inputDirectory = new File(this.getClass().getResource("/").toURI());
            for(File input : inputDirectory.listFiles(filter)) {
                System.out.println("add test case to suite" + input.getAbsolutePath() + ", " + codec);
                RoundTripTest test = new RoundTripTest("testRoundTrip", input, codec);
                addTest(test);
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(RoundTripTestSuite.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
