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
package test.cloudml.codecs;

import junit.framework.TestCase;
import org.cloudml.codecs.DotPrinter;
import org.cloudml.codecs.SymbolTable;
import org.cloudml.core.Deployment;
import org.cloudml.core.ExecuteInstance;
import org.cloudml.core.InternalComponentInstance;
import org.cloudml.core.RelationshipInstance;
import org.cloudml.core.VMInstance;
import org.cloudml.core.samples.SshClientServer;
import org.cloudml.core.visitors.Visitable;
import org.cloudml.core.visitors.Visitor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * Specification of the Dot printer behaviour
 */
@RunWith(JUnit4.class)
public class DotPrinterTest extends TestCase {

    // should return "" empty string by default ??
    @Test
    public void shouldFormatInternalComponentsAsDotNodes() {
        final String dotText = getDotSnippet(aSampleInternalComponentInstance());

        assertThat("number of DOT node in '" + dotText + "'", DotText.nodeCount(dotText), is(equalTo(1)));
    }

    @Test
    public void shouldFormatVMAsDotNodes() {
        final String dotText = getDotSnippet(aSampleVMInstance());

        assertThat("number of DOT node in '" + dotText + "'", DotText.nodeCount(dotText), is(equalTo(1)));
    }

    // print a relationship instance
    @Test
    public void shouldFormatARelationshipInstanceAsADotEdge() {
        final SymbolTable symbols = new SymbolTable();

        final RelationshipInstance relationship = aSampleRelationshipInstance();
        symbols.initialise(relationship.getClientComponent());
        symbols.initialise(relationship.getServerComponent());

        final String dotText = getDotSnippet(relationship, symbols);

        assertThat("number of DOT edge in '" + dotText + "'", DotText.edgeCount(dotText), is(equalTo(1)));
    }

    @Test
    public void shouldFormatExecuteInstanceAsADotEdge() {
        final SymbolTable symbols = new SymbolTable();

        final ExecuteInstance execute = aSampleExecuteInstance();
        symbols.initialise(execute.getHost());
        symbols.initialise(execute.getSubject());

        final String dotText = getDotSnippet(execute, symbols);

        assertThat("number of DOT edge in '" + dotText + "'", DotText.edgeCount(dotText), is(equalTo(1)));
    }

    @Test
    public void shouldFormatADeploymentAsADigraph() {
        final String dotText = getDotSnippet(new Deployment());

        assertThat("number of graph in  '" + dotText + "'", DotText.digraphCount(dotText), is(equalTo(1)));
    }

    @Test
    public void shouldFormatClientServerAsACorrectGraph() {
        final String dotText = getDotSnippet(SshClientServer.getOneClientConnectedToOneServer().build());

        assertThat("number of graph in  '" + dotText + "'", DotText.digraphCount(dotText), is(equalTo(1)));
        assertThat("number of DOT node in '" + dotText + "'", DotText.nodeCount(dotText), is(equalTo(4)));
        assertThat("number of DOT edge in '" + dotText + "'", DotText.edgeCount(dotText), is(equalTo(3)));
    }

    /*
     * Helpers
     */
    private String getDotSnippet(Visitable component) {
        final DotPrinter sut = new DotPrinter();
        component.accept(new Visitor(sut));
        return sut.getDotText();
    }

    private String getDotSnippet(Visitable component, SymbolTable symbols) {
        final DotPrinter sut = new DotPrinter(symbols);
        component.accept(new Visitor(sut));
        return sut.getDotText();
    }

    private InternalComponentInstance aSampleInternalComponentInstance() {
        final Deployment model = getSampleDeployment();

        return model.getComponentInstances()
                .onlyInternals()
                .firstNamed(SshClientServer.CLIENT_1);
    }

    private VMInstance aSampleVMInstance() {
        final Deployment model = getSampleDeployment();

        return model.getComponentInstances()
                .onlyVMs()
                .firstNamed(SshClientServer.VM_OF_CLIENT_1);
    }

    private RelationshipInstance aSampleRelationshipInstance() {
        final Deployment model = getSampleDeployment();

        return model.getRelationshipInstances().toList().get(0);
    }

    private ExecuteInstance aSampleExecuteInstance() {
        final Deployment model = getSampleDeployment();

        return model.getExecuteInstances().toList().get(0);
    }

    private Deployment getSampleDeployment() {
        return SshClientServer.getOneClientConnectedToOneServer().build();
    }

}
