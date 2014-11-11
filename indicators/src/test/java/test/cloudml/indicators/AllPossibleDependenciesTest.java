package test.cloudml.indicators;

import eu.diversify.trio.core.requirements.Requirement;
import eu.diversify.trio.core.requirements.stats.VariableCount;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.Deployment;
import org.cloudml.core.samples.SshClientServer;
import org.cloudml.indicators.AllPossibleDependencies;
import org.cloudml.indicators.DependencyExtractor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static eu.diversify.trio.core.Evaluation.evaluate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Test the extraction of all the possible dependencies of a given component.
 * 
 * The resulting requirements should capture a disjunction between all possible
 * provider of all the required services.
 */
@RunWith(JUnit4.class)
public class AllPossibleDependenciesTest {

    @Test
    public void sshClientShouldHaveThreePossibleDependencies() {
         final Deployment deployment = SshClientServer
                .getOneClientConnectedToOneOfTwoServers()
                .build();

        final ComponentInstance<?> subject = deployment
                .getComponentInstances()
                .firstNamed(SshClientServer.CLIENT_1);

        final DependencyExtractor extractRequirement = new AllPossibleDependencies();
        final Requirement requirement = extractRequirement.from(subject);

        final VariableCount dependencyCount = new VariableCount();
        evaluate(dependencyCount).on(requirement);

        assertThat("SSH client should have only two dependencies",
                   dependencyCount.get(),
                   is(equalTo(3)));
    }
    
    
}
