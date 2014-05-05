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

package test.cloudml.codecs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cloudml.codecs.DotCodec;
import org.cloudml.codecs.DotPrinter;
import org.cloudml.core.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import static org.cloudml.core.samples.SshClientServer.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(JUnit4.class)
public class TestDotCodec {

    @Test
    public void testSaveAs() throws FileNotFoundException {
        Deployment model = getTwoClientsConnectedToOneServer()
                .build();
        final String fileName = "target/test.dot";

        new DotCodec().save(model, new FileOutputStream(fileName)); 
        
        verifyThatFileExistsOnDisk(fileName);
    }

    @Test
    public void testNodeAreTransformedIntoCluster() {
        Deployment model = getTwoClientsConnectedToOneServer()
                .build();

        String dotText = new DotPrinter().print(model);

        verifyNumberOfDigraphs(model, dotText);
        verifyNumberOfClusters(model, dotText);
        verifyNumberOfDotNodes(model, dotText);
        verifyNumberOfDotEdges(model, dotText);
    }

    private void verifyNumberOfDigraphs(Deployment model, String dotText) {
        int count = countOccurences(dotText, "digraph");
        assertThat("number of directed graphs", count, is(equalTo(1)));
    }

    private void verifyNumberOfClusters(Deployment model, String dotText) {
        final int nodeInstancesCount = model.getComponentInstances().onlyVMs().size();
        final int clusterCount = countOccurences(dotText, "subgraph cluster_");
        assertThat("cluster count", clusterCount, is(equalTo(nodeInstancesCount)));
    }

    private void verifyNumberOfDotNodes(Deployment model, String dotText) {
        final int dotNodeCount = countOccurences(dotText, "(?!->)\\s+node_\\d+_\\d+\\s+(?!->)");
        final int artefactInstancesCount = model.getComponentInstances().onlyInternals().size();
        assertThat("dot nodes count", dotNodeCount, is(equalTo(artefactInstancesCount)));
    }

    private void verifyNumberOfDotEdges(Deployment model, String dotText) {
        final int dotNodeCount = countOccurences(dotText, "node_\\d+_\\d+ -> node_\\d+_\\d+");
        final int bindingInstancesCount = model.getRelationshipInstances().size();
        assertThat("dot edges count", dotNodeCount, is(equalTo(bindingInstancesCount)));
    }

    private int countOccurences(String dotText, String label) {
        Pattern pattern = Pattern.compile(label);
        Matcher matcher = pattern.matcher(dotText);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private void verifyThatFileExistsOnDisk(String fileName) {
        assertThat("dot file created", new File(fileName).exists(), is(true));
    }
}