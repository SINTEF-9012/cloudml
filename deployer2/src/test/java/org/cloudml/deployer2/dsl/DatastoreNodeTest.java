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


import org.junit.Test;


import java.util.ArrayList;

import static org.junit.Assert.*;

public class DatastoreNodeTest {

    @Test
    public void noDuplicates() throws Exception {
        DatastoreNode store = new DatastoreNode("store");

        String object1 = "o1";
        String object2 = "o2";
        String object3 = "o1";
        ArrayList<Object> objects = new ArrayList<Object>();
        objects.add(object1);
        objects.add(object2);
        objects.add(object3);

        store.setObjects(objects);
        ArrayList<String> datastoreObjects = (ArrayList<String>) store.getObjects();

        //datastore node deletes all duplicates
        assertEquals(datastoreObjects.indexOf("o1"), datastoreObjects.lastIndexOf("o1"));
    }
}