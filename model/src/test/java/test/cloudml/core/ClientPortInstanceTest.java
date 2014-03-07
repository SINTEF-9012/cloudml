/*
 */

package test.cloudml.core;

import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.ArtefactPortInstance;
import org.cloudml.core.ClientPort;
import org.cloudml.core.ClientPortInstance;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class ClientPortInstanceTest extends ArtefactPortInstanceTest {

    @Override
    public ArtefactPortInstance getValidInstance() {
        return new ClientPortInstance("name", new ClientPort(null, null, true), new ArtefactInstance());
    }
        
}
