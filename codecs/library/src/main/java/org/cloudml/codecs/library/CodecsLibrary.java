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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.codecs.XmiCodec;
import org.cloudml.codecs.commons.Codec;
import org.cloudml.core.Deployment;

/**
 * Created by Nicolas Ferry & Franck Chauvel on 25.02.14.
 */
public class CodecsLibrary {

    private final HashMap<String, Codec> codecsByExtension;

    public Set<String> getExtensions(){
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
        checkModel(model);
        checkPath(pathToFile);
        String extension = getFileExtension(pathToFile);
        Codec codec = this.codecsByExtension.get(extension);
        checkCodec(codec, extension);
        codec.save(model, new FileOutputStream(pathToFile));
    }

    public String getFileExtension(String fileName) {
        String extension = "";
        Pattern pattern = Pattern.compile("(\\.\\w+)$");
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            extension = matcher.group(1);
        }
        else {
            throw new IllegalArgumentException("Cannot select codec on file without extension");
        }
        return extension.toLowerCase();
    }

    private void checkModel(Deployment model) throws IllegalArgumentException {
        if (model == null) {
            throw new IllegalArgumentException("Cannot serialize a 'null' model");
        }
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
        checkPath(pathToFile);
        String extension = getFileExtension(pathToFile);
        Codec codec = this.codecsByExtension.get(extension);
        checkCodec(codec, extension);
        Deployment model = (Deployment) codec.load(new FileInputStream(pathToFile));
        if (model == null) {
            model = new Deployment();
        }
        return model;
    }

    private void checkCodec(Codec codec, String extension) throws IllegalArgumentException {
        if (codec == null) {
            throw new IllegalArgumentException("Unknown file extension '" + extension + "'");
        }
    }

    private void checkPath(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("Cannot serialize in a non-existing file");
        }
        if (fileName.equals("")) {
            throw new IllegalArgumentException("Cannot find file named ''");
        }
    }
}
