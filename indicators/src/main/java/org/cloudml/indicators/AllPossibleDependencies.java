package org.cloudml.indicators;

import eu.diversify.trio.core.requirements.Requirement;
import java.util.LinkedList;
import java.util.List;
import org.cloudml.core.*;

import static eu.diversify.trio.core.requirements.Factory.nothing;
import static eu.diversify.trio.core.requirements.Factory.require;

/**
 * Compute the all the "possible" dependencies of a given component.
 *
 * Possible dependencies include, for each required port instance, the
 * disjunction between all the existing compliant provided ports in the model.
 */
public class AllPossibleDependencies implements DependencyExtractor {

    @Override
    public Requirement from(ComponentInstance<?> component) {
        requireValidComponent(component);

        Requirement requirements = nothing();

        if (component.isInternal()) {
            final InternalComponentInstance internalInstance = component.asInternal();
            for (RequiredPortInstance eachDependency: internalInstance.getRequiredPorts()) {
                requirements = requirements.and(anyMatchingProvider(eachDependency));
            }

            requirements = requirements.and(anyMatchingHost(internalInstance));
        }

        return requirements;
    }

    private void requireValidComponent(ComponentInstance<?> componentInstance) throws IllegalArgumentException {
        if (componentInstance == null) {
            throw new IllegalArgumentException("Unable to extract requirement from 'null'");
        }
    }

    /**
     * Compute the disjunction of all possible host that matches the given
     * component
     *
     * @param component the internal component whose candidate hosts are needed.
     *
     * @return a disjunction (OR) between all candidates hosts
     */
    private Requirement anyMatchingHost(final InternalComponentInstance component) {
        assert component != null:
                "Unable to find hosts for 'null'";

        Requirement disjunction = null;
        for (ComponentInstance<?> eachHost: findAllCandidateHosts(component)) {
            if (disjunction == null) {
                disjunction = require(eachHost.getName());

            } else {
                disjunction = disjunction.or(require(eachHost.getName()));
            }
        }

        return disjunction;
    }

    /**
     * Build a disjunction between all the providers that can meet the
     * requirements of the given requiredPort
     *
     * @param dependency the dependency that must satisfied
     * @return a disjunction (i.e., logical or) between all the service
     * providers that satisfies the given dependencies.
     */
    private Requirement anyMatchingProvider(RequiredPortInstance dependency) {
        assert dependency != null:
                "Unable to find service providers for 'null'";

        Requirement disjunction = null;
        for (ComponentInstance<?> eachProvider: findAllCandidateProviders(dependency)) {
            if (disjunction == null) {
                disjunction = require(eachProvider.getName());

            } else {
                disjunction = disjunction.or(require(eachProvider.getName()));
            }
        }
        return disjunction;
    }

    /**
     * Find all the component that can be used as service providers for the
     * given required port
     *
     * @param requiredPort the port for which a provider is needed
     * @return the list of candidates service providers
     */
    private List<ComponentInstance<?>> findAllCandidateProviders(RequiredPortInstance requiredPort) {
        assert requiredPort != null:
                "Unable to find service providers for 'null'";

        final List<ComponentInstance<?>> candidates = new LinkedList<ComponentInstance<?>>();

        final Deployment deployment = requiredPort.getDeployment();
        for (Relationship eachRelationship: deployment.getRelationships()) {
            if (eachRelationship.getRequiredEnd().equals(requiredPort.getType())) {
                final ProvidedPort serverType = eachRelationship.getProvidedEnd();
                for (ComponentInstance<?> eachComponent: deployment.getComponentInstances()) {
                    if (eachComponent.getType().canProvide(serverType)) {
                        candidates.add(eachComponent);
                    }
                }
            }
        }

        return candidates;
    }

    /**
     * Find all the components that can possibly host the given internal
     * component instance
     *
     * @param component the component that needs a host
     * @return the list of components that can be host
     */
    private List<ComponentInstance<?>> findAllCandidateHosts(InternalComponentInstance component) {
        assert component != null:
                "Unable to find host for 'null'";

        final List<ComponentInstance<?>> candidates = new LinkedList<ComponentInstance<?>>();

        for (ComponentInstance<?> eachComponent: component.getDeployment().getComponentInstances()) {
            if (eachComponent.canHost(component.getType())) {
                candidates.add(eachComponent);
            }
        }

        return candidates;
    }

}
