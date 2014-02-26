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
package test.cloudml.codecs.kmf;

import junit.framework.TestCase;

import org.cloudml.codecs.KMFBridge;
import org.cloudml.core.CloudMLModel;
import org.cloudml.core.Provider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BridgeToKmfTest extends TestCase {




    @Test
    public void testWithOneProvider(){
        Provider amazon = new Provider("aws-ec2", "./credentials");
        CloudMLModel model = new CloudMLModel("SensAppAdmnin");
        model.getProviders().add(amazon);

        KMFBridge bridge=new KMFBridge();
        net.cloudml.core.CloudMLModel kmodel=bridge.toKMF(model);
        assertTrue(kmodel.getProviders().size()==1);
    }

    @Test
    public void testWithManyProviders(){
        Provider amazon = new Provider("aws-ec2", "./credentials");
        Provider flexiant = new Provider("flexiant", "./credentials");
        Provider minimoi = new Provider("mini-cloud", "./credentials");
        CloudMLModel model = new CloudMLModel("SensAppAdmnin");
        model.getProviders().add(amazon);
        model.getProviders().add(flexiant);
        model.getProviders().add(minimoi);

        KMFBridge bridge=new KMFBridge();
        net.cloudml.core.CloudMLModel kmodel=bridge.toKMF(model);
        assertTrue(kmodel.getProviders().size()==3);
    }

}
