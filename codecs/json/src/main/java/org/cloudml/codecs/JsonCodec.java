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
import java.util.logging.Level;
import java.util.logging.Logger;
import net.cloudml.loader.JSONModelLoader;
import net.cloudml.loader.ModelLoader;
import net.cloudml.serializer.JSONModelSerializer;
import net.cloudml.serializer.ModelSerializer;
import org.cloudml.codecs.commons.Codec;
import org.cloudml.core.NamedElement;
import org.cloudml.core.Deployment;


/*
 * A JSON codec, as its name might suggest...
 * @author Brice MORIN
 */
public class JsonCodec implements Codec {
    
    private static final Logger journal = Logger.getLogger(JsonCodec.class.getName());
    
    KMFBridge bridge = new KMFBridge();
    
    static {
        extensions.put("json", new JsonCodec());
    }

    public JsonCodec() {
    }
    
    public static void init(){};

    public NamedElement load(InputStream content) {
        ModelLoader loader = new JSONModelLoader();
        try {
            net.cloudml.core.CloudMLModel kDeploy = (net.cloudml.core.CloudMLModel) loader.loadModelFromStream(content).get(0);//beware of this cast...
            return bridge.toPOJO(kDeploy);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getLocalizedMessage());
            journal.log(Level.SEVERE, e.getLocalizedMessage());
        }
        return null;
    }

    public void save(NamedElement model, OutputStream content) {
        ModelSerializer serializer = new JSONModelSerializer();
        try {
            serializer.serialize(bridge.toKMF((Deployment)model), content);
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            journal.log(Level.SEVERE, e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }
}
