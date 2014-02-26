/*
 */
package test.cloudml.codecs.kmf;

import org.cloudml.core.Provider;

/**
 * Create sample CloudML objects used during testing
 */
class CloudMLSamplesBuilder {

    public Provider getSampleProviderA() {
        return createProvider("Provider A");
    }

    public Provider getSampleProviderB() {
        return createProvider("Provider B");
    }
    
    private Provider createProvider(String name) {
        Provider provider = new Provider();
        provider.setName(name);
        return provider;
    }
}
