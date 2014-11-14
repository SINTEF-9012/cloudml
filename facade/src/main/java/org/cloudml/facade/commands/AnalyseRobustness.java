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
package org.cloudml.facade.commands;

import org.cloudml.indicators.Selection;

/**
 * Compute the robustness of the internal components, with respect to failures
 * in the external ones.
 *
 */
public class AnalyseRobustness extends CloudMlCommand {

    private final Selection toObserve;
    private final Selection toControl;
    
    public AnalyseRobustness() {
        this(Selection.INTERNAL.getLabel(), Selection.EXTERNAL.getLabel()); 
    }
    
    public AnalyseRobustness(String toObserve, String toControl) {
        this.toObserve = Selection.readFrom(toObserve);
        this.toControl = Selection.readFrom(toControl);
    }

    public Selection getToObserve() {
        return toObserve;
    }

    public Selection getToControl() {
        return toControl;
    }
    
    @Override
    public void execute(CommandHandler target) {
        target.handle(this);
    }

    @Override
    public String toString() {
        return String.format("analyse robustness");
    }

}
