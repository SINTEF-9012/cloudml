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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cloudml.core.*;

/*
 * A bridge to translate CloudML POJOs into a KMF representation (which then
 * offers XMI and JSON serialization for free, with no deps) This stupid code
 * could go away if we decide to base the metamodel on KMF generated classes...
 */
public class KMFBridge {

    private BridgeToCloudML toPojo;
    private BridgeToKmf toKmf;

    public KMFBridge() {
        toPojo=new BridgeToCloudML();
        toKmf=new BridgeToKmf();
    }

    public CloudMLElement toPOJO(net.cloudml.core.CloudMLModel kDeploy) {
        return toPojo.toPOJO(kDeploy);
    }

    public net.cloudml.core.CloudMLModel toKMF(CloudMLModel deploy) {
        return toKmf.toKMF(deploy);
    }


    /**
     * Convenience procedure to convert JSON into XMI. Should be removed (or
     * moved) not to introduce a dependency to JSON here
     *
     * @param args
     */
    /*
     * public static void main(String[] args) { KMFBridge xmiCodec = new
     * KMFBridge(); JsonCodec jsonCodec = new JsonCodec();
     *
     * try { FilenameFilter filter = new FilenameFilter() {
     *
     * public boolean accept(File dir, String name) { return
     * name.endsWith(".json"); } }; File inputDirectory = new
     * File(xmiCodec.getClass().getResources("/").toURI()); for (File input :
     * inputDirectory.listFiles(filter)) { System.out.println("loading " +
     * input.getAbsolutePath() + ", " + jsonCodec); CloudMLModel model =
     * (CloudMLModel) jsonCodec.load(new FileInputStream(input));
     * xmiCodec.save(model, new FileOutputStream(new File(input.getParentFile(),
     * input.getName() + ".xmi"))); } } catch (FileNotFoundException ex) {
     * Logger.getLogger(KMFBridge.class.getName()).log(Level.SEVERE, null, ex);
     * } catch (URISyntaxException ex) {
     * Logger.getLogger(KMFBridge.class.getName()).log(Level.SEVERE, null, ex);
     * }
     *
     * }
     */
}
