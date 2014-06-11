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
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cloudml.mrt.cmd.abstracts;

/**
 *
 * @author huis
 */
public class Listener {
    
    public String id;
    public boolean cancel = false;
    public Object root = null;
    
    public String getID(){
        return id;
    }
    
    public boolean careFor(Change change){
        return _careFor(change);
    }
    
    protected boolean _careFor(Change change){
        return false;
    }
    
    protected Object queryXPathFromRoot(XPath xpath){
        return xpath.query(root);
    }
    
        @Override
    public boolean equals(Object other){
        try{
            return getID().equals(((Listener) other).getID());
        }
        catch(Exception e){
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getID().hashCode();
    }
    
}
