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
package org.cloudml.codecs.library;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.codecs.XmiCodec;
import org.cloudml.codecs.commons.Codec;
import org.cloudml.core.CloudMLModel;

/**
 * Created by Nicolas Ferry & Franck Chauvel on 25.02.14.
 */
public class CodecsLibrary {

    
    private final HashMap<String, Codec> codecs;
    
    
    public CodecsLibrary() {
        codecs = new HashMap<String, Codec>();
        codecs.put(".json", new JsonCodec());
        codecs.put(".xmi", new XmiCodec());
    }
  
    public void saveAs(CloudMLModel model, String fileName) throws FileNotFoundException{
        checkParameters(model, fileName);
        String extension = getExtension(fileName);
        Codec codec = this.codecs.get(extension);
        if (codec == null) {
            throw new IllegalArgumentException("Unknown file extension '" + extension + "'");
        }
        codec.save(model, new FileOutputStream(fileName));
    }

    public String getExtension(String fileName) {
        String extension = "";
        Pattern pattern = Pattern.compile("(\\.\\w+)$");
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            extension = matcher.group(1);
        } else {
            throw new IllegalArgumentException("Cannot select codec on file without extension");
        }
        return extension;
    }

    private void checkParameters(CloudMLModel model, String fileName) throws IllegalArgumentException {
        if (model==null) {
            throw new IllegalArgumentException("Cannot serialize a 'null' model");
        }
        if (fileName == null) {
            throw new IllegalArgumentException("Cannot serialize in a non-existing file");
        }
    }

}
