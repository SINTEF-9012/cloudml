/**
 * This file is part of CloudML [ http://cloudml.org ]
 *
 * Copyright (C) 2012 - SINTEF ICT Contact: Franck Chauvel
 * <franck.chauvel@sintef.no>
 *
 * Module: root
 *
 * CloudML is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * CloudML is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CloudML. If not, see
 * <http://www.gnu.org/licenses/>.
 */
/*
 */
package org.cloudml.core.validation;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Message {

    public static final String NO_GIVEN_ADVICE = "No given advice";
    private final Level level;
    private final String message;
    private final String advice;

    public Message(Level level, String message) {
        abortIfInvalidMessage(message);
        this.level = level;
        this.message = message;
        this.advice = NO_GIVEN_ADVICE;
    }

    public Message(Level level, String message, String advice) {
        abortIfInvalidMessage(message);
        abortIfInvalidAdvice(advice);
        this.level = level;
        this.message = message;
        this.advice = advice;
    }

    public Level getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public String getAdvice() {
        return advice;
    }

    public boolean isError() {
        return this.level == Level.ERROR;
    }

    @Override
    public String toString() {
        return String.format("%s: %s \n => %s", level.getLabel(), message, advice);
    }

    private void abortIfInvalidMessage(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Error description shall not be null");
        }
        if (message.isEmpty()) {
            throw new IllegalArgumentException("Error description shall not be empty");
        }
    }

    private void abortIfInvalidAdvice(String advice) {
        if (advice == null) {
            throw new IllegalArgumentException("When given, advice shall not be null");
        }
        if (advice.isEmpty()) {
            throw new IllegalArgumentException("When given, advice shall not be empty");
        }
    }

    public boolean containsAll(String[] keywords) {
        for (String keyword : keywords) {
            if (!this.message.contains(keyword)) {
                return false;
            }
        }
        return true;
    }
}