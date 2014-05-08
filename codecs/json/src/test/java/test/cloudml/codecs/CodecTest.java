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

import java.io.FileNotFoundException;
import java.io.OutputStream;

import org.cloudml.codecs.JsonCodec;
import org.cloudml.codecs.commons.Codec;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.cloudml.core.*;


import static org.cloudml.core.samples.SensApp.*;
import static org.cloudml.core.samples.PaasCloudBees.*;

/**
 * Unit test for simple App.
 */
public class CodecTest
        extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CodecTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( CodecTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        Deployment model = completeSensApp().build();
        Deployment model2 = completeCloudBeesPaaS().build();
        try{
            Codec jsonCodec=new JsonCodec();
            OutputStream streamResult=new java.io.FileOutputStream("sensappTEST.json");
            jsonCodec.save(model,streamResult);
            streamResult=new java.io.FileOutputStream("PaaS.json");
            //jsonCodec.save(model2, streamResult);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }
}
