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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudml.codecs.commons.Codec;
import org.cloudml.core.CloudMLElement;
import org.cloudml.core.CloudMLModel;

/*
 * An coder to visualize CloudML models
 */
public class JungCodec implements Codec {

    public JungCodec() {
    }

    public CloudMLElement load(InputStream content) {
        throw new UnsupportedOperationException("Jung codec is actually just a coder (i.e. a visulizer)");
    }

    public void save(CloudMLElement model, OutputStream content) {
        if (model instanceof CloudMLModel) {
            ObjectOutputStream serializer = null;
            try {
                //Display model
                DrawnIconVertexDemo g = new DrawnIconVertexDemo((CloudMLModel)model);
                ArrayList<Vertex> v = g.drawVerticesFromDeploymentModel((CloudMLModel) model);
                g.drawEdgesFromDeploymentModel((CloudMLModel) model, v);
                
                //Serializes it
                serializer = new ObjectOutputStream(content);
                serializer.writeObject(g);
                serializer.close();
            } 
            catch (IOException ex) {
                Logger.getLogger(JungCodec.class.getName()).log(Level.SEVERE, null, ex);
            }  finally {
                try {
                    content.close();
                } catch (IOException ex) {
                    Logger.getLogger(JungCodec.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    serializer.close();
                } catch (IOException ex) {
                    Logger.getLogger(JungCodec.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
