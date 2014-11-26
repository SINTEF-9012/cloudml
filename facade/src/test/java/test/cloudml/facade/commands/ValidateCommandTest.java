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


package test.cloudml.facade.commands;

import org.cloudml.facade.commands.CommandHandler;
import org.cloudml.facade.commands.ValidateCommand;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test the proper behaviour of the validate command
 */
@RunWith(JUnit4.class)
public class ValidateCommandTest {

    private final Mockery context = new JUnit4Mockery();
    
    @Test
    public void facadeShouldBeProperlyInvoked() {
        final CommandHandler handler = context.mock(CommandHandler.class);
        
        final ValidateCommand validation = new ValidateCommand();
        
        context.checking(new Expectations(){{
            exactly(1).of(handler).handle(validation);
        }});
        
        validation.execute(handler);
        
        context.assertIsSatisfied();
    }
    
}
