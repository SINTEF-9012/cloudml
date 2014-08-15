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

package org.cloudml.core.util;


public class Optional<O> {
   
    private final O NONE = null;
    private O value;
    
    public Optional() { 
        this.value = NONE;
    }
   
    public final O get() {
        if (!isDefined()) {
            final String message = String.format("%s has is not yet defined", getClass().getName());
            throw new IllegalStateException(message);
        }
        return value; 
    }
    
    public final O getValue(){
        return get();
    }

    public final void set(O value) {
        if (value == NONE) {
            final String message = "'null' is not a valid owner";
            throw new IllegalArgumentException(message);
        }
        this.value = value;
    }

    public void discard() {
        this.value = NONE;
    }

    public final boolean isDefined() {
        return this.value != NONE;
    }
    
    public final boolean isUndefined() {
        return !isDefined();
    }
    @Override
    public boolean equals(Object other){
        if (this == other)
            return true;
        
        if(other instanceof Optional){
            Optional otherOptional = (Optional) other;
            if(this.isDefined() && otherOptional.isDefined())
                return this.get().equals(otherOptional.get());
            else if(this.isUndefined() && otherOptional.isUndefined())
                return true;
            else
                return false;
        }
        else
            return false;
            
    }
    
}



