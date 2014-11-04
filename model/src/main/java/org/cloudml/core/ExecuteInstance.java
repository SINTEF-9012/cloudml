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
package org.cloudml.core;

import org.cloudml.core.collections.PropertyGroup;
import org.cloudml.core.util.OwnedBy;
import org.cloudml.core.validation.Report;
import org.cloudml.core.visitors.Visitor;

public class ExecuteInstance extends WithResources implements DeploymentElement, OwnedBy<Deployment> {

    private final OptionalOwner<Deployment> owner;
    private ProvidedExecutionPlatformInstance providedEnd;
    private RequiredExecutionPlatformInstance requiredEnd;

    public ExecuteInstance(String name, InternalComponentInstance instance, ProvidedExecutionPlatformInstance platform) {
        this(name, instance.getRequiredExecutionPlatform(), platform);
    }

    public ExecuteInstance(String name, RequiredExecutionPlatformInstance requiredEnd, ProvidedExecutionPlatformInstance providedEnd) {
        super(name);
        this.owner = new OptionalOwner<Deployment>();
        unlessExpectationsAreMet(providedEnd, requiredEnd);
        setProvidedEnd(providedEnd);
        setRequiredEnd(requiredEnd);
    }

    @Override
    public void validate(Report report) {
        final PropertyGroup demands = getRequiredEnd().getType().getDemands();
        for (Property demand : demands) {
            final PropertyGroup offers = getProvidedEnd().getType().getOffers();
            if (!offers.isDefined(demand.getName())) {
                final String error = String.format("Mismatch between demands and offers! Demanded: %s, but %s is not offered", demand, demand.getName());
                report.addError(error);
            }
            else {
                if (!offers.contains(demand)) {
                    final Property offer = offers.get(demand.getName());
                    final String error = String.format("Mismatch between demands and offers!! Demanded: %s, but offered %s", demand, offer);
                    report.addError(error);
                }
            }
        }
    }

    public boolean hasSubject(InternalComponentInstance component) {
        return getRequiredEnd().getOwner().get().getName().equals(component.getName());
    }

    public boolean isHostedBy(ComponentInstance<? extends Component> host) {
        return getProvidedEnd().getOwner().get().equals(host);
    }

    public ComponentInstance<? extends Component> getHost() {
        return getProvidedEnd().getOwner().get();
    }

    public InternalComponentInstance getSubject() {
        return (InternalComponentInstance) getRequiredEnd().getOwner().get();
    }

    public boolean isBetween(String demanderName, String demandName, String providerName, String providedName) {
        return this.getRequiredEnd().getOwner().get().isNamed(demanderName)
                && this.getRequiredEnd().isNamed(demandName)
                && this.getProvidedEnd().getOwner().get().isNamed(providerName)
                && this.getProvidedEnd().isNamed(providedName);
    }

    //TODO: to be refined (we should not only match if values are strictly equals)
    private void unlessExpectationsAreMet(ProvidedExecutionPlatformInstance provided, RequiredExecutionPlatformInstance required) {
        for (Property demand : required.getType().getDemands()) {
            Property offer = provided.getType().getOffers().get(demand.getName());
            if (offer == null) {
                throw new IllegalArgumentException("Missing expectations: " + demand.getName());
            }
            if (!demand.getValue().equals(offer.getValue())) {
                throw new IllegalArgumentException("Unmet expectation '" + demand.getName() + "' (expected + '" + demand.getValue() + "' but found: '" + offer.getValue() + "')");
            }
        }
    }

    @Override
    public Deployment getDeployment() {
        return getOwner().get();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitExecuteInstance(this);
    }

    @Override
    public String getQualifiedName() {
        return String.format("%s%s%s", getOwner().getName(), CONTAINED, getName());
    }

    @Override
    public OptionalOwner<Deployment> getOwner() {
        return owner;
    }

    public RequiredExecutionPlatformInstance getRequiredEnd() {
        return requiredEnd;
    }

    public final void setRequiredEnd(RequiredExecutionPlatformInstance end) {
        if (end == null) {
            final String error = String.format("'null' is not a valid required end for '%s'", getQualifiedName());
            throw new IllegalArgumentException(error);
        }
        this.requiredEnd = end;
    }

    public ProvidedExecutionPlatformInstance getProvidedEnd() {
        return providedEnd;
    }

    public final void setProvidedEnd(ProvidedExecutionPlatformInstance end) {
        if (end == null) {
            final String error = String.format("'null' is not a valid provided end for '%s'", getQualifiedName());
            throw new IllegalArgumentException(error);
        }
        this.providedEnd = end;
    }
    
    @Override
    public boolean equals(Object other){
        if(this == other)
            return true;
        if(other == null || !(other instanceof ExecuteInstance))
            return false;
        ExecuteInstance otherEI = (ExecuteInstance) other;
        
        boolean reqMatch = false;
        if(this.getRequiredEnd() == null && otherEI.getRequiredEnd()==null)
            reqMatch = true;
        else if(this.getRequiredEnd()!=null && otherEI.getRequiredEnd()!=null)
            reqMatch = this.getRequiredEnd().equals(otherEI.getRequiredEnd());
        
        boolean prvMatch = false;
        if(this.getProvidedEnd() == null && otherEI.getProvidedEnd() == null)
            prvMatch = true;
        else if(this.getProvidedEnd()!=null && otherEI.getProvidedEnd()!=null)
            prvMatch = this.getProvidedEnd().equals(otherEI.getProvidedEnd());
        
        return reqMatch && prvMatch;
      
    }
}
