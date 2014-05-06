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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Set;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.codecs.XmiCodec;
import org.cloudml.codecs.commons.Codec;
import org.cloudml.core.Deployment;

/**
 * Hold a set of preloaded codecs, and select the relevant one according to the
 * file extension.
 */
public class CodecsLibrary {

    private final HashMap<String, Codec> codecsByExtension;

    public Set<String> getExtensions() {
        return codecsByExtension.keySet();
    }

    public CodecsLibrary() {
        codecsByExtension = new HashMap<String, Codec>();
        codecsByExtension.put(".json", new JsonCodec());
        codecsByExtension.put(".xmi", new XmiCodec());
    }

    /**
     * Save a model into a the given file, based on the extension of the file
     *
     * @param model the model to serialise
     * @param pathToFile the path to the file
     * @throws FileNotFoundException if the path is not valid on disc
     */
    public void saveAs(Deployment model, String pathToFile) throws FileNotFoundException {
        failIfNotValid(model);
        failIfNotValid(pathToFile);
        final Codec codec = getCodec(Utils.getFileExtension(pathToFile));
        codec.save(model, new FileOutputStream(pathToFile));
    }

    private void failIfNotValid(Deployment model) throws IllegalArgumentException {
        if (model == null) {
            throw new IllegalArgumentException("Cannot serialize a 'null' model");
        }
    }

    private Codec getCodec(String extension) throws IllegalArgumentException {
        final Codec codec = this.codecsByExtension.get(extension);
        if (codec == null) {
            throw new IllegalArgumentException("Unsupported file format '*" + extension + "' (supported formats are " + getExtensions() + ")");
        }
        return codec;
    }

    /**
     * Read a file and build the related model selecting the appropriate codec
     * based on the file extension.
     *
     * @param pathToFile the path to the file
     * @return the related Deployment object
     * @throws FileNotFoundException if the given path is invalid
     */
    public Deployment load(String pathToFile) throws FileNotFoundException {
        failIfNotValid(pathToFile);
        final Codec codec = getCodec(Utils.getFileExtension(pathToFile));
        Deployment model = (Deployment) codec.load(new FileInputStream(pathToFile));
        if (model == null) {
            model = new Deployment();
        }
        return model;
    }

    private void failIfNotValid(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("Cannot serialize in a non-existing file");
        }
        if (fileName.equals("")) {
            throw new IllegalArgumentException("Cannot find file named ''");
        }
    }
}
