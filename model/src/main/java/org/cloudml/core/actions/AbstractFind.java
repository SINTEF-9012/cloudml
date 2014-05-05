/**
 * This file is part of CloudML [ http://cloudml.org ]
 *
 * Copyright (C) 2012 - SINTEF ICT
 * Contact: Franck Chauvel <franck.chauvel@sintef.no>
 *
 * Module: root
 *
 * CloudML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * CloudML is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with CloudML. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.cloudml.core.actions;


import java.util.List;
import java.util.Random;
import org.cloudml.core.Deployment;


public abstract class AbstractFind<T> extends AbstractAction<T> {

    public AbstractFind(StandardLibrary library) {
        super(library);
    }
       
    @Override
    public final T applyTo(Deployment deployment) {
        final List<T> candidates = collectCandidates(deployment);
        if (candidates.isEmpty()) {
            handleLackOfCandidate(deployment, candidates);
        }
        return chooseOnlyOne(deployment, candidates);
    }

    protected abstract List<T> collectCandidates(Deployment deployment);

    protected abstract void handleLackOfCandidate(Deployment deployment, List<T> candidates);

    protected T chooseOnlyOne(Deployment deployment, List<T> candidates) {
        final int index = new Random().nextInt(candidates.size());
        return candidates.get(index);
    }

    
    
}
