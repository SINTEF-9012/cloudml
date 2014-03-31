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
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudml.codecs.commons.Codec;
import org.cloudml.core.CloudMLElement;
import org.cloudml.core.*;

public class DotCodec implements Codec {

    @Override
    public CloudMLElement load(InputStream content) {
        throw new UnsupportedOperationException("The DOT codec does not support extracting CloudML model from DOT file.");
    }

    @Override
    public void save(CloudMLElement model, OutputStream content) {
        try {
            content.write(new DotPrinter().print((DeploymentModel) model).getBytes());
        
        } catch (IOException ex) {
            Logger.getLogger(DotCodec.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
