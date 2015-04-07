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
package org.cloudml.deployer2.dsl;

import java.util.ArrayList;

/**
 * Created by Maksym on 16.03.2015.
 */
public class ExpansionNode extends ObjectNode {

    public ExpansionNode(String name){
        super(name);
    }

    public ExpansionNode(String name, ArrayList<Object> collection) throws Exception {
        super(name);
        setObjects(collection);
    }

    private void inputIsCollection (ArrayList<? extends Object> input) throws Exception {
        if (input.size() < 2){
            throw new Exception("ExpansionNode expects collection of size two as minimum as input");
        }
    }

    @Override
    public void setObjects(ArrayList<Object> collection) throws Exception {
        inputIsCollection(collection);
        this.objects = collection;
    }

}
