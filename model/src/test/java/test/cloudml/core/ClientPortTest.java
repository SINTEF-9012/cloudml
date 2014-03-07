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
package test.cloudml.core;

import org.cloudml.core.ArtefactPort;
import org.cloudml.core.ClientPort;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import test.cloudml.core.builder.ArtefactBuilder;
import test.cloudml.core.builder.Builder;
import static org.cloudml.core.ClientPort.*;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class ClientPortTest extends ArtefactPortTest {
   
    @Override
    public ArtefactPort getPortWithoutOwner() {
        return new ClientPort("cp", null, true);
    }

    @Override
    public ArtefactPort getValidPort() {
        Builder builder = new Builder();
        ArtefactBuilder artefact = builder.createArtefactType("My App type");
        return artefact.createClientPort("ssh", LOCAL, MANDATORY); 
    }

    @Override
    public ArtefactPort getPortWithoutName() {
         return new ClientPort("cp", null, true);
    }

}
