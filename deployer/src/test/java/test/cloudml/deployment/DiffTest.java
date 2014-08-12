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
import org.cloudml.core.InternalComponent;
import org.cloudml.core.InternalComponentInstance;
import org.cloudml.core.RelationshipInstance;
import org.cloudml.deployer.CloudMLModelComparator;

/**
 *
 * @author Hui Song
 */
public class DiffTest extends TestCase{
    
    public static void main(String[] args) throws FileNotFoundException{
        new DiffTest().testComparator();
    }
    
    public Deployment current = null;
    public Deployment target = null;
    
    @Override
    public void setUp() throws FileNotFoundException{
        Codec codec = new JsonCodec();
        Codec codec2 = new JsonCodec();
        InputStream stream = null;
        stream = new FileInputStream(new File("c:\\temp\\sensapp-v2.json"));
        current = (Deployment) codec.load(stream);
        stream = new FileInputStream("c:\\temp\\sensapp-v2.json");
        target = (Deployment) codec2.load(stream);

    }
    
    /**
     * Change the name of the Internal Component Instance sensApp1
     * Since *name* works as the id of the instance, this change should be regarded
     * as "removing one component instance and adding a new one". Therefore, the
     * relationship instances and execution instances are changed as well.
     * @throws FileNotFoundException 
     */
    public void testNameOfOneInCompChanged(){
        
        String nameBeforeChange = "sensApp1";
        String nameToChange = "sensApp-modified-1";
        
        InternalComponentInstance ici = target.getComponentInstances().onlyInternals().firstNamed(nameBeforeChange);
        ici.setName(nameToChange);

        CloudMLModelComparator cmc = new CloudMLModelComparator(current, target);
        cmc.compareCloudMLModel();
        checkOneComponentInstanceChanged(cmc, nameBeforeChange, nameToChange);            
        
    }
    
    public void testTypeOfOneInCompChanged() {
        String nameBeforeChange = "sensapp";
        String nameAfterChange = "sensapp-modified";
        String instanceName = "sensApp1";
        InternalComponent ic = target.getComponents().onlyInternals().firstNamed(nameBeforeChange);
        ic.setName(nameAfterChange);
        CloudMLModelComparator cmc = new CloudMLModelComparator(current, target);
        cmc.compareCloudMLModel();
        checkOneComponentInstanceChanged(cmc, instanceName, instanceName);
    }

    private void checkOneComponentInstanceChanged(CloudMLModelComparator cmc, String nameBeforeChange, String nameToChange) {
        assertEquals(0, cmc.getAddedECs().size());
        assertEquals(0, cmc.getRemovedECs().size());
        assertEquals(1, cmc.getRemovedComponents().size());
        assertEquals(nameBeforeChange, cmc.getRemovedComponents().get(0).getName());
        assertEquals(1, cmc.getAddedComponents().size());
        assertEquals(nameToChange, cmc.getAddedComponents().get(0).getName());
        
        assertEquals(2, cmc.getRemovedRelationships().size());
        assertTrue(
                nameBeforeChange.equals(cmc.getRemovedRelationships().get(0).getServerComponent().getName()) ||
                        nameBeforeChange.equals(cmc.getRemovedRelationships().get(1).getServerComponent().getName())
        );
        assertEquals(2, cmc.getAddedRelationships().size());
        for(RelationshipInstance ri : cmc.getAddedRelationships()){
            assertTrue(
                    (
                            nameToChange.equals(ri.getServerComponent().getName()) &&
                                    ri.getClientComponent().getOwner().getValue() == current
                            )
                            ||
                            (
                                    nameToChange.equals(ri.getClientComponent().getName()) &&
                                            ri.getServerComponent().getOwner().getValue() == current
                                    )
            );
        }
    }
}
