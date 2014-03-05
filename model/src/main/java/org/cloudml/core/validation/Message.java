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
}