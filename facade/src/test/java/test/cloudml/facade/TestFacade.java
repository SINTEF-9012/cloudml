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
import org.junit.Test;

/**
 * A simple test to check whether the behaviour of the facade is correct,
 * regarding message exchange and triggering of events
 *
 * @author Franck Chauvel
 * @author Brice Morin
 * @since 1.0
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
        CloudMlCommand command = factory.startComponent("foo");
        final Execution execution = cloudml.fireAndWait(command);
        assertTrue(execution.isCompleted());


    }

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
