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
/*
 */
package test.cloudml.codecs.kmf;

import junit.framework.TestCase;
import static junit.framework.TestCase.assertFalse;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@Ignore
@RunWith(JUnit4.class)
public class TestMatcher extends TestCase {

    // Provider
    @Test
    public void testMatchProviderWithNullKmf() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.match(null, cloudml.getProviderA());
        assertFalse(result);
    }

    @Test
    public void testMatchProviderWithNullCloudMLObject() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.match(kmf.getProviderA(), null);
        assertFalse(result);
    }

    @Test
    public void testMatchProviderWhenMatch() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.match(kmf.getProviderB(), cloudml.getSampleProviderB());
        assertTrue(result);
    }

    @Test
    public void testMatchProviderWhenDoNotMatch() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.match(kmf.getProviderA(), cloudml.getSampleProviderB());
        assertFalse(result);

    }

    //VM Instance
    @Test
    public void testMatchVMInstanceWithNullKmf() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchVMInstance(null, cloudml.getVMInstanceA());
        assertFalse(result);
    }

    @Test
    public void testMatchVMInstanceWithNullCloudMLObject() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchVMInstance(kmf.getVMInstanceA(), null);
        assertFalse(result);
    }

    @Test
    public void testMatchVMInstanceWhenMatch() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchVMInstance(kmf.getVMInstanceA(), cloudml.getVMInstanceA());
        assertTrue(result);
    }

    @Test
    public void testMatchVMInstanceWhenDoNotMatch() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchVMInstance(kmf.getVMInstanceA(), cloudml.getSampleVMInstanceB());
        assertFalse(result);

    }

    //VM
    @Test
    public void testMatchVMWithNullKmf() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchVM(null, cloudml.getVMA());
        assertFalse(result);
    }

    @Test
    public void testMatchVMWithNullCloudMLObject() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchVM(kmf.getVMA(), null);
        assertFalse(result);
    }

    @Test
    public void testMatchVMWhenVMMatch() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchVM(kmf.getVMA(), cloudml.getVMA());
        assertTrue(result);
    }

    @Test
    public void testMatchVMWhenVMDoNotMatch() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchVM(kmf.getVMA(), cloudml.getSampleVMB());
        assertFalse(result);

    }


    //InternalComponent
    @Test
    public void testMatchInternalComponentWithNullKmf() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchIC(null, cloudml.getInternalComponentA());
        assertFalse(result);
    }

    @Test
    public void testMatchInternalComponentWithNullCloudMLObject() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchIC(kmf.getInternalComponentA(), null);
        assertFalse(result);
    }

    @Test
    public void testMatchInternalComponentWhenMatch() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchIC(kmf.getInternalComponentA(), cloudml.getInternalComponentA());
        assertTrue(result);
    }

    @Test
    public void testMatchInternalComponentDoNotMatch() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchIC(kmf.getInternalComponentA(), cloudml.getSampleInternalComponentB());
        assertFalse(result);
    }

    //InternalComponentInstance
    @Test
    public void testMatchInternalComponentInstanceWithNullKmf() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchICI(null, cloudml.getInternalComponentInstanceA());
        assertFalse(result);
    }

    @Test
    public void testMatchInternalComponentInstanceWithNullCloudMLObject() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchICI(kmf.getInternalComponentInstanceA(), null);
        assertFalse(result);
    }

    @Test
    public void testMatchInternalComponentInstanceWhenMatch() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchICI(kmf.getInternalComponentInstanceA(), cloudml.getInternalComponentInstanceA());
        assertTrue(result);
    }

    @Test
    public void testMatchInternalComponentInstanceWhenDoNotMatch() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchICI(kmf.getInternalComponentInstanceA(), cloudml.getInternalComponentInstanceB());
        assertFalse(result);
    }

    //Relationship
    @Test
    public void testMatchRelationshipWithNullKmf() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchRelationship(null, cloudml.getRelationshipA());
        assertFalse(result);
    }

    @Test
    public void testMatchRelationshipWithNullCloudMLObject() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchRelationship(kmf.getRelationshipA(), null);
        assertFalse(result);
    }

    @Test
    public void testMatchRelationshipWhenMatch() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchRelationship(kmf.getRelationshipA(), cloudml.getRelationshipA());
        assertTrue(result);
    }

    @Test
    public void testMatchRelationshipWhenDoNotMatch() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchRelationship(kmf.getRelationshipA(), cloudml.getSampleRelationshipB());
        assertFalse(result);
    }

    // Test RelationshipInstance
    @Test
    public void testMatchRelationshipInstanceWithNullKmf() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchRelationshipInstance(null, cloudml.getRelationshipInstanceA());
        assertFalse(result);
    }

    @Test
    public void testMatchRelationshipInstanceWithNullCloudMLObject() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchRelationshipInstance(kmf.getRelationshipInstanceA(), null);
        assertFalse(result);
    }

    @Test
    public void testMatchRelationshipInstanceWhenMatch() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchRelationshipInstance(kmf.getRelationshipInstanceA(), cloudml.getRelationshipInstanceA());
        assertTrue(result);
    }

    @Test
    public void testMatchRelationshipInstanceWhenDoNotMatch() {
        KMFSamplesBuilder kmf = new KMFSamplesBuilder();
        CloudMLSamplesBuilder cloudml = new CloudMLSamplesBuilder();
        Matcher matcher = new Matcher();
        boolean result = matcher.matchRelationshipInstance(kmf.getRelationshipInstanceA(), cloudml.getSampleRelationshipInstanceB());
        assertFalse(result);
    }
}