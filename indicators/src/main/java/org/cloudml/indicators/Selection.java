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
package org.cloudml.indicators;

import eu.diversify.trio.filter.All;
import eu.diversify.trio.filter.Filter;
import eu.diversify.trio.filter.TaggedAs;

/**
 * Represent the TRIO tags that can be generated from any CloudML models:
 * internal, external and VM
 */
public enum Selection {

    INTERNAL("internal", new TaggedAs("internal")),
    EXTERNAL("external", new TaggedAs("external")),
    VM("vm", new TaggedAs("vm")),
    SERVICE("service", new TaggedAs("service")),
    NOT_SERVICE("not service", new TaggedAs("service").not()),
    ALL("all", All.getInstance());

    private final String label;
    private final Filter trioFilter;

    private Selection(String label, Filter trioFilter) {
        this.label = label;
        this.trioFilter = trioFilter;
    }

    /**
     * @return the label associated with this tag
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * @return the associated trio filter
     */
    public Filter asTrioFilter() {
        return trioFilter;
    }

    /**
     * Build a tag from a given text.
     *
     * @param text the text to be parsed
     * @return the related tag
     */
    public static Selection readFrom(String text) {
        requireValid(text);

        final String escapedText = text.toLowerCase().trim();
        for (Selection anyTag: Selection.values()) {
            if (anyTag.getLabel().equals(escapedText)) {
                return anyTag;
            }
        }

        final String errorMessage = String.format("There is no tag matching the text '%s'", text);
        throw new IllegalArgumentException(errorMessage);
    }

    private static void requireValid(String text) throws IllegalArgumentException {
        if (text == null) {
            throw new IllegalArgumentException("No tag can be read from 'null'");
        }
        if (text.isEmpty()) {
            throw new IllegalArgumentException("No tag can be read from an empty string");
        }
    }

}
