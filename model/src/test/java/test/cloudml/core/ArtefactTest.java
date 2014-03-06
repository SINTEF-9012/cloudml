package test.cloudml.core;

import junit.framework.TestCase;
import static junit.framework.TestCase.assertTrue;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactPort;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import test.cloudml.core.builder.ArtefactBuilder;

@RunWith(JUnit4.class)
public class ArtefactTest extends TestCase {

    @Test
    public void passValidationWhenValid() {
        ArtefactBuilder builder = new ArtefactBuilder("My Artefact");
        builder.createClientPort("port", ArtefactPort.REMOTE, ArtefactPort.LOCAL);
        Artefact artefact = builder.getResult();

        assertTrue(artefact.validate().pass(false));
    }

    @Test
    public void validationReportsNullName() {
        Artefact artefact = new Artefact();
        assertTrue(artefact.validate().hasErrorAbout("name"));
    }

    @Test
    public void validationReportsEmptyName() {
        Artefact artefact = new Artefact("");
        assertTrue(artefact.validate().hasErrorAbout("name"));
    }
    
    @Test
    public void validationReportsNoPorts() {
        Artefact artefact = new Artefact("my Artefact");
        assertTrue(artefact.validate().hasWarningAbout("port"));
    }
}
