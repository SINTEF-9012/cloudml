/*
 */
package test.cloudml.core;

import junit.framework.TestCase;
import static junit.framework.TestCase.assertTrue;
import org.cloudml.core.ArtefactPortInstance;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public abstract class ArtefactPortInstanceTest extends TestCase {

    public abstract ArtefactPortInstance getValidInstance();

    @Test
    public void testPassValidationWithoutWarningWhenValid() {
        ArtefactPortInstance instance = getValidInstance();

        Report validation = instance.validate();

        assertTrue(validation.pass(Report.WITHOUT_WARNING));
    }

    @Test
    public void validationReportsNullName() {
        ArtefactPortInstance instance = getValidInstance();
        instance.setName(null);

        Report validation = instance.validate();

        assertTrue(validation.hasErrorAbout("name", "null"));
    }

    @Test
    public void validationReportsNullType() {
        ArtefactPortInstance instance = getValidInstance();
        instance.setType(null);

        Report validation = instance.validate();

        assertTrue(validation.hasErrorAbout("type", "null"));
    }

    @Test
    public void validationReportsNullOwner() {
        ArtefactPortInstance instance = getValidInstance();
        instance.setOwner(null);

        Report validation = instance.validate();

        assertTrue(validation.hasErrorAbout("owner", "null"));
    }
}
