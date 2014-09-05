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
