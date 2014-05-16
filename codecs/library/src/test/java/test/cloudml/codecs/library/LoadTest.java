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
package test.cloudml.codecs.library;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import junit.framework.TestCase;
import org.cloudml.codecs.DotCodec;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.codecs.commons.Codec;
import org.cloudml.codecs.library.CodecsLibrary;
import org.cloudml.core.Deployment;
import org.cloudml.core.ExternalComponent;
import org.cloudml.core.samples.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author Nicolas Ferry and Franck Chauvel
 */

@RunWith(JUnit4.class)
public class LoadTest extends TestCase {

    private static final String TEST_RESOURCE_PATH = "src/test/resources/";

    private static CodecsLibrary aCodecLibrary() {
        return new CodecsLibrary();
    }

    @Test(expected = FileNotFoundException.class)
    public void loadShouldRejectFilesThatDoNotExist() throws FileNotFoundException {
        aCodecLibrary().load("a/file/that/does/not/exist.json");
    }

    @Test
    public void loadShouldAcceptSensapp() throws FileNotFoundException {
        Codec json= new JsonCodec();
        json.save(SensApp.completeSensApp().build(), new FileOutputStream(testFile("sensapp.json")));
        final Deployment model = aCodecLibrary().load(testFile("sensapp.json"));
        Codec c= new DotCodec();
        //c.save(SensApp.completeSensApp().build(), new FileOutputStream("sensapp2.dot"));
        assertNotNull(model.getRelationships().firstNamed("SensAppAdminSensApp").getClientResource());
        c.save(model, new FileOutputStream(testFile("sensapp.dot")));
        assertModelMatchesSensApp(model);
    }

    @Test
    public void loadShouldAcceptPaaS() throws FileNotFoundException {
        Codec json2= new JsonCodec();
        Deployment dm=PaasCloudBees.completeCloudBeesPaaS().build();
        ExternalComponent ec = dm.getComponents().onlyExternals().firstNamed("cbdb");
        ec.setLogin("sintef");
        ec.setPasswd("password123");
        json2.save(dm, new FileOutputStream(testFile("paas.json")));

        Codec c= new DotCodec();
        //c.save(model, new FileOutputStream(testFile("paas.dot")));
        c.save(PaasCloudBees.completeCloudBeesPaaS().build(), new FileOutputStream(testFile("paas.dot")));

        final Deployment model = aCodecLibrary().load(testFile("paas.json"));

        assertNotNull(model.getRelationships().firstNamed("dbrel"));
        assertModelMatchesPaaS(model);
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadShouldRejectEmptyFileNames() throws FileNotFoundException {
        aCodecLibrary().load("");
    }

    @Test
    public void loadShouldBuildAnEmptyModelWhenTheFileIsEmpty() throws FileNotFoundException {
        final Deployment model = aCodecLibrary().load(testFile("empty.json"));
        assertModelIsEmpty(model);
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadShouldRejectFilesWhoseFormatIsNotSupported() throws FileNotFoundException {
        aCodecLibrary().load("a_file_with_unknown_extension.foo");
    }

    //
    // ---- Helper methods
    //
    private String testFile(final String testFile) {
        return TEST_RESOURCE_PATH + testFile;
    }

    private void assertModelIsEmpty(Deployment model) {
        assertThat("no provider", model.getProviders(), is(empty()));
        assertThat("no component instance", model.getComponentInstances(), is(empty()));
        assertThat("no component type", model.getComponents(), is(empty()));
        assertThat("no relationship type", model.getRelationships(), is(empty()));
        assertThat("no relationship instance", model.getRelationshipInstances(), is(empty()));
        assertThat("no cloud", model.getClouds(), is(empty()));
    }

    private void assertModelMatchesPaaS(Deployment model){
        assertThat("1 providers", model.getProviders(), hasSize(1));
        assertThat("3 component instances", model.getComponentInstances(), hasSize(3));
        assertThat("3 component types", model.getComponents(), hasSize(3));
        assertThat("1 relationship types", model.getRelationships(), hasSize(1));
        assertThat("1 relationship instances", model.getRelationshipInstances(), hasSize(1));
    }


    private void assertModelMatchesSensApp(Deployment model) {
        assertThat("3 providers", model.getProviders(), hasSize(3));
        assertThat("7 component instances", model.getComponentInstances(), hasSize(7));
        assertThat("5 component types", model.getComponents(), hasSize(6));
        assertThat("2 relationship types", model.getRelationships(), hasSize(2));
        assertThat("2 relationship instances", model.getRelationshipInstances(), hasSize(2));
        assertThat("0 cloud", model.getClouds(), is(empty()));
    }

}
