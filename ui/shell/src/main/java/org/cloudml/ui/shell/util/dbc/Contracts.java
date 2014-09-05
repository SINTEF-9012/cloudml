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
package org.cloudml.ui.shell.util.dbc;

import org.hamcrest.Description; 
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription; 

/**
 *
 */
public class Contracts {


    public static <E> void require(E e, Matcher<E> matcher) {
        require(Scope.ARGUMENT, e, matcher);
    }

    public static <E> void require(Scope scope, E e, Matcher<E> matcher) {
        if (!matcher.matches(e)) {
            final Description description = new StringDescription();
            matcher.describeMismatch(e, description);
            final String message = "Precondition violated! " + description.toString();
            switch (scope) {
                case ARGUMENT:
                    throw new IllegalArgumentException(message);
                case STATE:
                    throw new IllegalStateException(message);
                default:
                    throw new RuntimeException("Unexpected scope value: '" + scope.name() + "'");
            }
        }
    }
    
    public static <E> void ensure(E e, Matcher<E> matcher) {
        final Description description = new StringDescription();
        matcher.describeMismatch(e, description);
        assert matcher.matches(e): "Postcondition violated: " + description.toString();
    }
}
