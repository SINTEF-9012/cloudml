/*
 */
package test.cloudml.core;

import org.cloudml.core.ArtefactPort;
import static org.cloudml.core.ArtefactPort.LOCAL;
import org.cloudml.core.ServerPort;
import test.cloudml.core.builder.ArtefactBuilder;
import test.cloudml.core.builder.Builder;

public class ServerPortTest extends ArtefactPortTest {

    @Override
    public ArtefactPort getPortWithoutOwner() {
        return new ServerPort(null, null, true);
    }

    @Override
    public ArtefactPort getValidPort() {
        Builder builder = new Builder();
        ArtefactBuilder artefact = builder.createArtefactType("My App type");
        return artefact.createServerPort("ssh", LOCAL);
    }

    @Override
    public ArtefactPort getPortWithoutName() {
        return new ServerPort(null, null, true);
    }
}
