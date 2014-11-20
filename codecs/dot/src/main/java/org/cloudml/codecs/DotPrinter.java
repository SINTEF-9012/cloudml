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
package org.cloudml.codecs;

import org.cloudml.core.*;
import org.cloudml.core.visitors.AbstractVisitListener;

/**
 * Traverse the deployment model a output DOT code for visualisation
 */
public class DotPrinter extends AbstractVisitListener {

    private final SymbolTable symbols;
    private StringBuilder dotText;

    public DotPrinter() {
        this.symbols = new SymbolTable();
    }

    public DotPrinter(SymbolTable symbols) {
        this.symbols = symbols;
    }

    @Override
    public void onDeploymentEntry(Deployment subject) {
        ensureBufferIsReady();
        dotText.append("digraph Deployment {\n");
    }

    @Override
    public void onDeploymentExit(Deployment subject) {
        dotText.append("}\n");
    }

    @Override
    public void onExternalComponentInstanceEntry(ExternalComponentInstance subject) {
        ensureBufferIsReady();
        dotText.append("\t")
                .append(symbols.initialise(subject))
                .append(" [")
                .append(componentFormatting(subject)).append("];\n");
    }

    @Override
    public void onVMInstanceEntry(VMInstance subject) {
        ensureBufferIsReady();
        dotText.append("\t")
                .append(symbols.initialise(subject))
                .append(" [")
                .append(vmFormatting(subject)).append("];\n");
    }

    private String vmFormatting(ComponentInstance<? extends Component> subject) {
        return "label=\"" + subject.getName() + "\", style=\"filled\"";
    }

    private String componentFormatting(ComponentInstance<? extends Component> subject) {
        return "label=\"" + subject.getName() + "\"";
    }

    @Override
    public void onInternalComponentInstanceEntry(InternalComponentInstance subject) {
        ensureBufferIsReady();
        dotText.append("\t")
                .append(symbols.initialise(subject))
                .append(" [")
                .append(componentFormatting(subject)).append("];\n");
    }

    @Override
    public void onRelationshipInstanceEntry(RelationshipInstance subject) {
        ensureBufferIsReady();
        dotText.append("\t")
                .append(symbols.get(subject.getClientComponent()))
                .append(" -> ")
                .append(symbols.get(subject.getServerComponent()))
                .append(" [")
                .append(relationshipFormating(subject))
                .append(" ];\n");
    }

    private String relationshipFormating(RelationshipInstance subject) {
        return "label=\"" + subject.getName() + "\"";
    }

    @Override
    public void onExecuteInstanceEntry(ExecuteInstance subject) {
        ensureBufferIsReady();
        dotText.append("\t")
                .append(symbols.get(subject.getSubject()))
                .append(" -> ")
                .append(symbols.get(subject.getHost()))
                .append(" [")
                .append(executeOnFormatting(subject))
                .append(" ];\n");
    }

    private String executeOnFormatting(ExecuteInstance subject) {
        return "style=\"dashed\"";
    }

    private void ensureBufferIsReady() {
        if (dotText == null) {
            dotText = new StringBuilder();
        }
    }

    public String getDotText() {
        ensureBufferIsReady();
        return dotText.toString();
    }

}
