
package org.cloudml.indicators;

import eu.diversify.trio.core.requirements.Requirement;
import org.cloudml.core.ComponentInstance;

/**
 * Extract the requirement of a single CloudML component.
 *
 * This a strategy object, used to configure the TrioExporter. For the record, a
 * requirement in TRIO is a logical expression that describe the condition under
 * which a given component fails, when failure occur in its direct environment.
 */
public interface DependencyExtractor {

    /**
     * Compute the TRIO requirement associated with a single CloudML component
     * instance.
     *
     * @param componentInstance the component instance whose requirement are
     * needed.
     *
     * @return the associated requirement object.
     */
    Requirement from(ComponentInstance<?> componentInstance);

}
