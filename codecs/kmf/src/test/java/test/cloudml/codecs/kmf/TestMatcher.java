/*
 */
package test.cloudml.codecs.kmf;

import junit.framework.TestCase;
import static junit.framework.TestCase.assertFalse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestMatcher extends TestCase {

    // Provider
    @Test
    public void testMatchProviderWithNullKmf() {
        KmfSamplesBuilder kmf = new KmfSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.match(null, cloudml.getSampleProviderA());
        assertFalse(result);
    }

    @Test
    public void testMatchProviderWithNullCloudMLObject() {
        KmfSamplesBuilder kmf = new KmfSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.match(kmf.getSampleProviderA(), null);
        assertFalse(result);
    }

    @Test
    public void testMatchProviderWhenProviderMatch() {
        KmfSamplesBuilder kmf = new KmfSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.match(kmf.getSampleProviderB(), cloudml.getSampleProviderB());
        assertTrue(result);
    }

    @Test
    public void testMatchProviderWhenProviderDoNotMatch() {
        KmfSamplesBuilder kmf = new KmfSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.match(kmf.getSampleProviderA(), cloudml.getSampleProviderB());
        assertFalse(result);

    }
    // TODO: test VM instances
    // TODO: test Relationship
    // TODO: test RelationshipInstance
    // TODO: test InternalComponent
    // TODO: test InternalComponentInstance
}