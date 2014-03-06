/*
 */
package test.cloudml.core.builder;

import org.cloudml.core.Artefact;
import org.cloudml.core.ClientPort;
import org.cloudml.core.ServerPort;

/**
 * Simplify the construction of complex artefact types
 */
public class ArtefactBuilder {

    private final Artefact inProgress;

    public ArtefactBuilder(String artefactName) {
        this.inProgress = new Artefact(artefactName);
    }

    public ClientPort createClientPort(String portName, boolean isRemote, boolean isMandatory) {
        ClientPort port = new ClientPort(portName, inProgress, isRemote, isMandatory);
        inProgress.getRequired().add(port);
        return port;
    }
    
    public ServerPort createServerPort(String portName, boolean isRemote) {
        ServerPort port = new ServerPort(portName, inProgress, isRemote);
        inProgress.getProvided().add(port);
        return port;
    }

    public Artefact getResult() {
        return this.inProgress;
    }
}
