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
 */
package test.cloudml.core.validation;

import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import org.cloudml.core.validation.Level;
import org.cloudml.core.validation.Message;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class ReportTest extends TestCase {

    @Test(expected = IllegalArgumentException.class)
    public void testAddErrorWithNullAsDescription() {
        Report report = new Report();
        report.addError(null);
    }

    @Test
    public void testMessage() {
        final String errorText = "This is an error";
        Message message = new Message(Level.ERROR, errorText);
        assertEquals(Level.ERROR, message.getLevel());
        assertEquals(errorText, message.getMessage());
    }

    @Test
    public void testMessageFormatting() {
        final String advice = "This is the associated advice";
        final String message = "This is an error";
        Message error = new Message(Level.ERROR, message, advice);
        String expected = String.format("%s: %s \n => %s", Level.ERROR.getLabel(), message, advice);
        assertEquals(expected, error.toString());
    }

    @Test
    public void testMessageFormattingWithoutAdvice() {
        final String message = "This is an error";
        Message error = new Message(Level.ERROR, message);
        String expected = String.format("%s: %s \n => No given advice", Level.ERROR.getLabel(), message);
        assertEquals(expected, error.toString());
    }
    
}
