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
package test.cloudml.facade;

import junit.framework.TestCase;
import org.cloudml.facade.*;
import org.cloudml.facade.commands.*;
import org.cloudml.facade.events.*;
import org.cloudml.facade.util.WSClient;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


/**
 * A simple test to check whether the behaviour of the facade is correct,
 * regarding message exchange and triggering of events
 */
public class TestFacade extends TestCase implements EventHandler {

    /**
     * Check that the facade does invoke the event handlers which are
     * registered.
     *
     * We retrieve an instance of the CloudML facade, we register basic handler
     * for success and failure, which both sets a boolean value showing that a
     * command was processed. Then we create a command an we wait for the
     * command to be processed, which must be done within 10 s, otherwise we
     * fail the test.
     */
    @Test
    public void testTriggerHandler() {
        // Retrieve CloudML
        CloudML cloudml = Factory.getInstance().getCloudML();
        cloudml.register(this);

        CommandFactory factory = new CommandFactory();

        // Send a dummy command
        ArrayList<String> vms=new ArrayList<String>();
        vms.add("foo");
        CloudMlCommand command = factory.startComponent(vms);
        final Execution execution = cloudml.fireAndWait(command);
        assertTrue(execution.isCompleted());

    }

    @Test
    public void testRemoteFacade(){
        //CloudML cloudml=Factory.getInstance().getCloudML("ws://127.0.0.1:9000");
        //cloudml.fireAndWait(new LoadDeployment("C:\\Users\\nicolasf\\Desktop\\cloudml2.0\\flexiantStorm.json"));
        //cloudml.fireAndWait(new Deploy());
    }

    /*@Test
    public void testComparisonInFacade(){
        try {
            Deployment d2 =(Deployment) new JsonCodec().load(new FileInputStream("C:\\Users\\nicolasf\\Desktop\\cloudml2.0\\v2.json"));
            Deployment d =(Deployment) new JsonCodec().load(new FileInputStream("C:\\Users\\nicolasf\\Desktop\\cloudml2.0\\v4.json"));

            CloudMLModelComparator diff = new CloudMLModelComparator(d,d2);
            diff.compareCloudMLModel();

            d.getComponents().addAll(d2.getComponents());
            d.getRelationships().addAll(d2.getRelationships());

            d.getRelationshipInstances().removeAll(diff.getRemovedRelationships());
            d.getExecuteInstances().removeAll(diff.getRemovedExecutes());
            d.getComponentInstances().removeAll(diff.getRemovedECs());
            d.getComponentInstances().removeAll(diff.getRemovedComponents());

            d.getRelationshipInstances().replaceAll(diff.getAddedRelationships());
            d.getComponentInstances().replaceAll(diff.getAddedECs());
            d.getExecuteInstances().replaceAll(diff.getAddedExecutes());
            d.getComponentInstances().replaceAll(diff.getAddedComponents());


            System.out.println(d.getComponents().toList().toString());
            System.out.println(d.getComponentInstances().toList().toString());
            System.out.println(d.getExecuteInstances().toList().toString());
            System.out.println(d.getRelationshipInstances().toList().toString());
            System.out.println(d.getRelationships().toList().toString());

            new JsonCodec().save(d, new FileOutputStream("C:\\Users\\nicolasf\\Desktop\\cloudml2.0\\v3.json"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }*/

    public boolean canHandle(Event event) {
        return true;
    }

    @Override
    public void handle(Event event) {
        System.out.println(event);
    }

    @Override
    public void handle(Message message) {
        this.handle((Event) message);
    }

    @Override
    public void handle(ComponentList artefacts) {
        handle((Event) artefacts);
    }

    @Override
    public void handle(Data data) {
        handle((Event) data);
    }

    @Override
    public void handle(ComponentInstanceList artefacts) {
        this.handle((Event) artefacts);
    }

    @Override
    public void handle(ComponentInstanceData artefact) {
        this.handle((Event) artefact);
    }

    @Override
    public void handle(ComponentData type) {
        this.handle((Event) type);
    }
}
