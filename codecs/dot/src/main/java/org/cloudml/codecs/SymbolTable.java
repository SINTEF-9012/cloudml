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
/*
 */

package org.cloudml.codecs;

import java.util.HashMap;
import org.cloudml.core.InternalComponentInstance;
import org.cloudml.core.NamedElement;

/**
 * Handle the symbols created during the generation get DOT code
 */
public class SymbolTable {

    private final HashMap<NamedElement, DotSymbol> symbols;

    public SymbolTable() {
        symbols = new HashMap<NamedElement, DotSymbol>();
    }
    
    public DotSymbol get(NamedElement subject) {
        final DotSymbol symbol = symbols.get(subject);
        if (symbol == null) {
            throw new RuntimeException("Unable to find a symbol for " + subject + ". It should have been already register. Check traversal order");
        }
        return symbol;
    }
    
    public DotSymbol initialise(NamedElement subject) {
        final DotSymbol symbol = new DotSymbol(symbols.size(), subject);
        symbols.put(subject, symbol);
        return symbol;
    }

}
