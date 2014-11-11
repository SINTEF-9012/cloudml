package test.cloudml.indicators;

import eu.diversify.trio.core.requirements.Requirement;
import eu.diversify.trio.core.requirements.stats.VariableCount;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.Deployment;
import org.cloudml.core.samples.SshClientServer;
import org.cloudml.indicators.DependencyExtractor;
import org.cloudml.indicators.OnlyExplicitDependencies;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.*;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static eu.diversify.trio.core.Evaluation.evaluate;
import static org.hamcrest.Matchers.*;

/**
 * Test the computation of the logical expression that reflects the conditions
 * under which a given component fail, when failure occurs in its direct
 * environment.
 */
@RunWith(JUnit4.class)
public class OnlyExplicitDependenciesTest {

    @Test
    public void sshClientShouldHaveOnlyTwoDependencies() {
        final Deployment deployment = SshClientServer
                .getOneClientConnectedToOneServer()
                .build();

        final ComponentInstance<?> subject = deployment
                .getComponentInstances()
                .firstNamed(SshClientServer.CLIENT_1);

        final DependencyExtractor extractRequirement = new OnlyExplicitDependencies();
        final Requirement requirement = extractRequirement.from(subject);

        final VariableCount dependencyCount = new VariableCount();
        evaluate(dependencyCount).on(requirement);

        assertThat("SSH client should have only two dependencies",
                   dependencyCount.get(),
                   is(equalTo(2)));

    }

}
