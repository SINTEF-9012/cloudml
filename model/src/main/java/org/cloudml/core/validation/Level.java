/*
 */

package org.cloudml.core.validation;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public enum Level {
    ERROR("Error"), 
    WARNING("Warning");
    
    private final String label;
    
    private Level(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
    
}
