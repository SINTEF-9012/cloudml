/*
 */
package test.cloudml.core;

import org.cloudml.core.ArtefactPort;
import org.cloudml.core.ClientPort;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import test.cloudml.core.builder.ArtefactBuilder;
import test.cloudml.core.builder.Builder;
import static org.cloudml.core.ClientPort.*;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class ClientPortTest extends ArtefactPortTest {
   
    @Override
    public ArtefactPort getPortWithoutOwner() {
        return new ClientPort(null, null, true);
    }

    @Override
    public ArtefactPort getValidPort() {
        Builder builder = new Builder();
        ArtefactBuilder artefact = builder.createArtefactType("My App type");
        return artefact.createClientPort("ssh", LOCAL, MANDATORY); 
    }

    @Override
    public ArtefactPort getPortWithoutName() {
         return new ClientPort(null, null, true);
    }

}
