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
package org.cloudml.codecs;

import java.io.InputStream;
import java.io.OutputStream;
import net.cloudml.loader.XMIModelLoader;
import net.cloudml.serializer.XMIModelSerializer;
import org.cloudml.codecs.commons.Codec;
import org.cloudml.core.CloudMLElement;
import org.cloudml.core.CloudMLModel;

/*
 * An XMI codec, as its name might suggest... @author Brice MORIN
 */
public class XmiCodec implements Codec {

    KMFBridge bridge = new KMFBridge();

    static {
        extensions.put("xmi", new XmiCodec());
    }

    public XmiCodec() {
    }

    public static void init() {
    }

    ;

    public CloudMLElement load(InputStream content) {
        XMIModelLoader loader = new XMIModelLoader();
        net.cloudml.core.DeploymentModel kDeploy = (net.cloudml.core.DeploymentModel) loader.loadModelFromStream(content).get(0);

        /*net.cloudml.core.Resource resource = (net.cloudml.core.Resource) kDeploy.findByPath("artefactTypes[Dome]/resource[domewar]");
        if (resource != null) {
            System.out.println("Found! " + resource);
        }

        net.cloudml.core.PortInstance pi = (net.cloudml.core.PortInstance) kDeploy.findByPath("artefactInstances[jboss1]/provided[wc1]");
        if (resource != null) {
            System.out.println("Found! " + pi);
        }*/

        return bridge.toPOJO(kDeploy);
    }

    public void save(CloudMLElement model, OutputStream content) {
        XMIModelSerializer serializer = new XMIModelSerializer();
        serializer.serialize(bridge.toKMF((CloudMLModel) model), content);
    }
}
