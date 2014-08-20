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
import java.util.Collection;
import java.util.List;
import junit.framework.TestCase;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.codecs.commons.Codec;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.ComponentInstance.State;
import org.cloudml.core.Deployment;
import org.cloudml.core.InternalComponent;
import org.cloudml.core.InternalComponentInstance;
import org.cloudml.core.RelationshipInstance;
import org.cloudml.core.VMInstance;
import org.cloudml.core.samples.SensApp;
import org.cloudml.deployer.CloudMLElementComparator;
import org.cloudml.deployer.CloudMLElementComparator.ElementUpdate;
import org.cloudml.deployer.CloudMLModelComparator;

/**
 *
 * I have block the test case so that it won't slow down the building process
 * To test it particularly, just change or the "ttest" to "test"...
 * 
 * @author Hui Song
 */
public class DiffTest extends TestCase{
    
    
    
    public Deployment current = null;
    public Deployment target = null;
    
    @Override
    public void setUp() throws FileNotFoundException{
        Codec codec = new JsonCodec();
        Codec codec2 = new JsonCodec();
        InputStream stream = null;
        stream = new FileInputStream(new File("sensapp-v2.json"));
        current = (Deployment) codec.load(stream);
        stream = new FileInputStream("sensapp-v2.json");
        target = (Deployment) codec2.load(stream);

    }
    
    public void testNothing(){
        
    }
    
    /**
     * Change the name of the Internal Component Instance sensApp1
     * Since *name* works as the id of the instance, this change should be regarded
     * as "removing one component instance and adding a new one". Therefore, the
     * relationship instances and execution instances are changed as well.
     * @throws FileNotFoundException 
     */
    public void ttestNameOfOneInCompChanged(){
        
        String nameBeforeChange = "sensApp1";
        String nameToChange = "sensApp-modified-1";
        
        InternalComponentInstance ici = target.getComponentInstances()
                .onlyInternals().firstNamed(nameBeforeChange);
        ici.setName(nameToChange);

        CloudMLModelComparator cmc = new CloudMLModelComparator(current, target);
        cmc.compareCloudMLModel();
        checkOneComponentInstanceChanged(cmc, nameBeforeChange, nameToChange);            
        
    }
    
    /**
     * Change the name of a component's type. Now the component is not considered to 
     * be the same one as before (not sure if this the most reasonable behaviour), 
     * and the comparing result should be the same as only changing the instance name.
     */
    public void ttestTypeOfOneInCompChanged() {
        String nameBeforeChange = "sensapp";
        String nameAfterChange = "sensapp-modified";
        String instanceName = "sensApp1";
        InternalComponent ic = target.getComponents()
                .onlyInternals().firstNamed(nameBeforeChange);
        ic.setName(nameAfterChange);
        CloudMLModelComparator cmc = new CloudMLModelComparator(current, target);
        cmc.compareCloudMLModel();
        checkOneComponentInstanceChanged(cmc, instanceName, instanceName);
    }

    /**
     * Common checking method after changing an internal component's name or its 
     * type name.
     * @param cmc
     * @param nameBeforeChange
     * @param nameToChange 
     */
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
    
    public void ttestElementCompareInSensApp(){
        String vmName = "sensapp-sl1";
        VMInstance ici = target.getComponentInstances()
                .onlyVMs().firstNamed(vmName);
        
        ici.setProperty("newProp", "aValue");
        ici.getType().setOs("windows");
        
        CloudMLModelComparator cmc = new CloudMLModelComparator(current, target);
        cmc.compareCloudMLModel();
        
        List<ElementUpdate> li = cmc.getAllEcUpdates();
        
        assertEquals(2, li.size());
        
        assertEquals(ici, li.iterator().next().element);
        assertEquals("type/os", li.iterator().next().path);

        
    }
    
    /**
     * Check the changes between matched components, relations, and execute
     * instances
     */
    public void ttestSignleElementCompare(){
        Deployment dm = SensApp.completeSensApp().build();
        VMInstance vmi = dm.getComponentInstances()
                .onlyVMs().firstNamed(SensApp.SENSAPP_VM);
        vmi.setProperty("testprop", "some value");
        
        Deployment dm2 = SensApp.completeSensApp().build();
        VMInstance vmi2 = dm2.getComponentInstances()
                .onlyVMs().firstNamed(SensApp.SENSAPP_VM);
        
        vmi2.getType().setOs("windows");
        vmi2.setStatus(ComponentInstance.State.ERROR);
        vmi2.setProperty("test", "someValue");
        
        CloudMLElementComparator cec = new CloudMLElementComparator(vmi, vmi2);
        
       
        Collection<CloudMLElementComparator.ElementUpdate> updates = cec.compare();
        
        assertEquals(4, updates.size());
        for(CloudMLElementComparator.ElementUpdate u : updates){
            if("type/os".equals(u.path)){
                assertEquals("windows", u.newValue);
                continue;
            }
            else if("status".equals(u.path)){
                assertEquals(State.STOPPED, u.oldValue);
                continue;
            }
            else if("properties/test".equals(u.path)){
                assertNull(u.oldValue);
                continue;
            }
            else if("properties/testprop".equals(u.path)){
                assertTrue(vmi == u.element);
                continue;
            }
            fail(String.format("%s should not be here", u));
        }
    }
}
