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

package org.cloudml.mrt;

import java.util.Collection;
import org.cloudml.core.Deployment;
import org.cloudml.core.samples.SensApp;

/**
 *
 * @author huis
 */
public class SimpleModelRepo implements ModelRepo{
    
    Deployment root = null;
    
    public SimpleModelRepo(){
        root = new Deployment();
    }
    
    public SimpleModelRepo(Deployment root){
        this.root = root;
    }

    @Override
    public Deployment getRoot() {
        return root;
    }


    @Override
    public Object handle(String name, Collection<String> parameter) {
        if("LoadDeployment".equals(name) && parameter.size()>0){
            if("sample://sensapp".equals(parameter.iterator().next().toLowerCase())){
                root = SensApp.completeSensApp().build();                          
                return "Loaded";
            }
        }
        throw new UnsupportedOperationException(String.format("Extended command %s Not supported yet.", name)); 
    }
    
}
