/*
 */
package test.cloudml.core;

import junit.framework.TestCase;
import static junit.framework.TestCase.assertTrue;
import org.cloudml.core.ArtefactPort;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public abstract class ArtefactPortTest extends TestCase {
    public static final boolean WITHOUT_WARNING = false;

    public abstract ArtefactPort getPortWithoutName();
    
    public abstract ArtefactPort getPortWithoutOwner();

    public abstract ArtefactPort getValidPort();
    
    @Test
    public void testValidationReportsMissingOwner() {
        ArtefactPort port = getPortWithoutOwner();

        assertTrue(port.validate().hasErrorAbout("owner"));
    }
    
    @Test
    public void testValidationReportsMissingName() {
        ArtefactPort port = getPortWithoutName();

        assertTrue(port.validate().hasErrorAbout("name"));
    }

    @Test
    public void testThatValidationsPassWhenPortIsValid() {
        ArtefactPort port = getValidPort();
        
        assertTrue(port.validate().pass(WITHOUT_WARNING));
    }
}
