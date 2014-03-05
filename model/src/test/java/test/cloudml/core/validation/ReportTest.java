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
