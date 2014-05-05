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
package org.cloudml.codecs.commons;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.cloudml.core.NamedElement;

/**
 * Codec provides service to serialize/deserialize models into/from streams,
 * which define the content of serialized models (and not locations where to
 * access them)
 *
 * IMPORTANT: Storing serialized versions of the models is the responsibility of
 * the client calling these services
 *
 * @author Franck CHAUVEL, Brice MORIN
 */
public interface Codec {

    /**
     * Lists of extension supported by codecs
     */
    static final Map<String, Codec> extensions = new HashMap<String, Codec>();
    
    /**
     * @param content, the stream from which we can read the serialized version
     * of the model
     * @return the in-memory version of the model
     */
    public NamedElement load(InputStream content);

    /**
     * @param model, the in-memory version of the model
     * @param content, the stream where the serialized model will be pushed
     */
    public void save(NamedElement model, OutputStream content);
}
