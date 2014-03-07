/*
 */

package test.cloudml.core;

import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.ArtefactPortInstance;
import org.cloudml.core.ServerPort;
import org.cloudml.core.ServerPortInstance;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ServerPortInstanceTest extends ArtefactPortInstanceTest {

    @Override
    public ArtefactPortInstance getValidInstance() {
        return new ServerPortInstance("spi", new ServerPort(null, null, true), new ArtefactInstance());
    }
    
    
}
