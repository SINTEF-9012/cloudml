/*
 */
package test.cloudml.codecs.kmf;

import net.cloudml.core.*;
import net.cloudml.factory.*;

/**
 * Provide samples of KMF/POJO objects used during testing
 */
class KmfSamplesBuilder {

    public Provider getSampleProviderA() {
        return createProvider("Provider A");
    }
    
    public Provider getSampleProviderB() {
        return createProvider("Provider B");
    }

    private Provider createProvider(String name) {
        CoreFactory factory = new MainFactory().getCoreFactory();
        Provider provider = factory.createProvider();
        provider.setName(name);
        provider.setCredentials("./credentials");
        return provider;
    }
}
