package test.cloudml.core;

import junit.framework.TestCase;
import static junit.framework.TestCase.assertTrue;
import org.cloudml.core.Binding;
import org.cloudml.core.ClientPort;
import org.cloudml.core.ServerPort;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import test.cloudml.core.builder.ArtefactBuilder;
import test.cloudml.core.builder.Builder;

@RunWith(JUnit4.class)
public class BindingTest extends TestCase {

    public static final boolean REMOTE = true;
    public static final boolean MANDATORY = true;
    public static final boolean WITHOUT_WARNING = false;
    public static final boolean LOCAL = false;

    @Test
    public void validationPassWhenValid() {
        Builder builder = new Builder();
        ArtefactBuilder client = builder.createArtefactType("Client Type");
        ClientPort clientPort = client.createClientPort("ssh", REMOTE, MANDATORY);
        ArtefactBuilder server = builder.createArtefactType("Server Type");
        ServerPort serverPort = server.createServerPort("ssh", REMOTE);
        Binding binding = builder.createBindingType("ssh connection", clientPort, serverPort);

        assertTrue(binding.validate().pass(WITHOUT_WARNING));
    }

    @Test
    public void validationReportsMissingClientEnd() {
        Builder builder = new Builder();

        ArtefactBuilder server = builder.createArtefactType("Server Type");
        ServerPort serverPort = server.createServerPort("ssh", REMOTE);

        Binding binding = new Binding(null, serverPort);

        assertTrue(binding.validate().hasErrorAbout("client end"));
    }

    @Test
    public void validationReportsMissingServerEnd() {
        Builder builder = new Builder();

        ArtefactBuilder client = builder.createArtefactType("Client Type");
        ClientPort clientPort = client.createClientPort("ssh", REMOTE, MANDATORY);
        ArtefactBuilder server = builder.createArtefactType("Server Type");

        Binding binding = new Binding(clientPort, null);

        assertTrue(binding.validate().hasErrorAbout("server end"));
    }
    
    
    @Test
    public void validationReportsLocalClientsConnectedToRemoteServers() {
        Builder builder = new Builder();
        ArtefactBuilder client = builder.createArtefactType("Client Type");
        ClientPort clientPort = client.createClientPort("ssh", LOCAL, MANDATORY);
        ArtefactBuilder server = builder.createArtefactType("Server Type");
        ServerPort serverPort = server.createServerPort("ssh", REMOTE);
        Binding binding = builder.createBindingType("ssh connection", clientPort, serverPort);

        assertTrue(binding.validate().hasErrorAbout("local client", "remote server"));
    }
    
    @Test
    public void validationReportsRemoteClientsConnectedToLocalServers() {
        Builder builder = new Builder();
        ArtefactBuilder client = builder.createArtefactType("Client Type");
        ClientPort clientPort = client.createClientPort("ssh", REMOTE, MANDATORY);
        ArtefactBuilder server = builder.createArtefactType("Server Type");
        ServerPort serverPort = server.createServerPort("ssh", LOCAL);
        Binding binding = builder.createBindingType("ssh connection", clientPort, serverPort);

        assertTrue(binding.validate().hasErrorAbout("remote client", "local server"));
    }
        
    
}
