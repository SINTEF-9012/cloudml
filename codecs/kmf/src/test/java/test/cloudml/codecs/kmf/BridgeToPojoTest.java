package test.cloudml.codecs.kmf;

import junit.framework.TestCase;
import net.cloudml.core.Provider;
import org.cloudml.codecs.BridgeToPojo;
import org.cloudml.codecs.KMFBridge;
import org.cloudml.core.CloudMLModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicolasf on 26.02.14.
 */

@RunWith(JUnit4.class)
public class BridgeToPojoTest extends TestCase {

    @Test
    public void testFromEmptyModel(){
        CloudMLModel model=new CloudMLModel();
        KMFBridge bridge=new KMFBridge();
        net.cloudml.core.CloudMLModel kmodel=bridge.toKMF(model);
        assertTrue(kmodel.getClouds().isEmpty());
        assertTrue(kmodel.getComponentInstances().isEmpty());
        assertTrue(kmodel.getComponents().isEmpty());
        assertTrue(kmodel.getClouds().isEmpty());
        assertTrue(kmodel.getRelationshipInstances().isEmpty());
        assertTrue(kmodel.getRelationships().isEmpty());
        assertTrue(kmodel.getProviders().isEmpty());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testProviderToPojoWithNull(){
        CloudMLModel model=new CloudMLModel();
        BridgeToPojo bridge=new BridgeToPojo();
        bridge.providersToPOJO(null);
    }

    @Test
    public void testProviderToPojoEmpty(){
        CloudMLModel model=new CloudMLModel();
        BridgeToPojo bridge=new BridgeToPojo();
        bridge.providersToPOJO(new ArrayList<net.cloudml.core.Provider>());
        assertTrue(bridge.getCloudMLModel().getProviders().isEmpty());
    }

    @Test
    public void testProviderToPojoWithElements(){
        CloudMLModel model=new CloudMLModel();
        BridgeToPojo bridge=new BridgeToPojo();
        List<net.cloudml.core.Provider> providers=new ArrayList<net.cloudml.core.Provider>();

        providers.add(createProvider("aws","./credentials"));
        providers.add(createProvider("flexiant","./credentials"));
        bridge.providersToPOJO(providers);
        assertEquals(2,bridge.getCloudMLModel().getProviders().size());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testExternalComponentToPojoWithNull(){
        CloudMLModel model=new CloudMLModel();
        BridgeToPojo bridge=new BridgeToPojo();
        bridge.externalComponentToPOJO(null);
    }


    @Test
    public void testExternalComponentToPojoWithElements(){
        CloudMLModel model=new CloudMLModel();
        BridgeToPojo bridge=new BridgeToPojo();
        net.cloudml.core.ExternalComponent kExternalComponent= createVM("vm1","chauvel");
        bridge.externalComponentToPOJO(kExternalComponent);
        assertTrue(compare(kExternalComponent,model.getExternalComponentInstances().get(0)));
    }


    public net.cloudml.core.ExternalComponent createVM(String name, String endPoint){
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.ExternalComponent kcomp=factory.createVM();
        //TODO: to be completed
        kcomp.setName(name);
        kcomp.setEndPoint(endPoint);
        return kcomp;
    }

    public net.cloudml.core.Provider createProvider(String name, String credentials){
        net.cloudml.core.CoreFactory factory = new net.cloudml.factory.MainFactory().getCoreFactory();
        net.cloudml.core.Provider kProvider = factory.createProvider();
        kProvider.setName(name);
        kProvider.setCredentials(credentials);
        return kProvider;
    }
}
