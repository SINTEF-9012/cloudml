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
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.cloudml.deployment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import junit.framework.TestCase;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.codecs.commons.Codec;
import org.cloudml.core.Deployment;
import org.cloudml.core.InternalComponentInstance;
import org.cloudml.deployer.CloudMLModelComparator;

/**
 *
 * @author huis
 */
public class DiffTest extends TestCase{
    
    public static void main(String[] args) throws FileNotFoundException{
        new DiffTest().testComparator();
    }
    
    public void testComparator() throws FileNotFoundException{
        Codec codec = new JsonCodec();
        Codec codec2 = new JsonCodec();
        InputStream stream = null;
        stream = new FileInputStream(new File("c:\\temp\\sensapp-v2.json"));
        Deployment current = (Deployment) codec.load(stream);
        stream = new FileInputStream("c:\\temp\\sensapp-v2.json");
        Deployment target = (Deployment) codec2.load(stream);

        InternalComponentInstance ici = target.getComponentInstances().onlyInternals().firstNamed("sensApp1");

        ici.setName("sensApp-modified-1");

        CloudMLModelComparator cmc = new CloudMLModelComparator(current, target);
        cmc.compareCloudMLModel();
            
        
    }
}
