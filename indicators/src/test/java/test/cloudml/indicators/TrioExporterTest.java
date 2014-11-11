/*
 */

package test.cloudml.indicators;

import org.cloudml.core.ComponentInstance;
import org.cloudml.core.Deployment;
import org.cloudml.core.samples.SshClientServer;
import org.cloudml.indicators.OnlyExplicitDependencies;
import org.cloudml.indicators.TrioExporter;

import static org.hamcrest.Matchers.*;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test the conversion of CloudML models into TRIO systems.
 */
public class TrioExporterTest {

     @Test
    public void convertOneClientOneServer() {
        final Deployment cloudml = SshClientServer.getOneClientConnectedToOneServer().build();

        final TrioExporter export = new TrioExporter(new OnlyExplicitDependencies());
        final eu.diversify.trio.core.System trioModel = export.asTrioSystem(cloudml);

        assertThat(trioModel, is(not(nullValue())));

        assertEquivalence(cloudml, trioModel);
        assertTags(cloudml, trioModel);
    }

    /**
     * Check that the given cloudML model and the given Trio System are related.
     * Raise an assertion error as soon as a discrepancy is detected.
     */
    private void assertEquivalence(Deployment cloudml, eu.diversify.trio.core.System trioModel) {
        assertThat("Wrong number of TRIO components",
                   trioModel.getComponentNames().size(),
                   is(equalTo(cloudml.getComponentInstances().size())));

        for (ComponentInstance each: cloudml.getComponentInstances()) {
            assertThat("missing instance '" + each.getName() + "'",
                       trioModel.hasComponentNamed(each.getName()));
        }
    }

    /**
     * Check that the proper tags were added on the TRIO model (i.e., internal,
     * versus external).
     */
    private void assertTags(Deployment cloudml, eu.diversify.trio.core.System trioModel) {
        int tagCount
                = trioModel.taggedAs("internal").size()
                + trioModel.taggedAs("external").size();

        assertThat("some components were not tagged",
                   tagCount,
                   is(equalTo(cloudml.getComponentInstances().size())));
    }
    
}
