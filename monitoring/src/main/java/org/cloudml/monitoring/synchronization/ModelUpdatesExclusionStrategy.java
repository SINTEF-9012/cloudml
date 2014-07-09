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
package org.cloudml.monitoring.synchronization;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.modaclouds.qos_models.monitoring_ontology.Component;
import it.polimi.modaclouds.qos_models.monitoring_ontology.ExternalComponent;
import it.polimi.modaclouds.qos_models.monitoring_ontology.VM;

import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lorenzo Cianciaruso on 07.07.14.
 */
public class ModelUpdatesExclusionStrategy implements ExclusionStrategy {


    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return
                f.getName().equals("shortURI")||f.getName().equals("uri");

    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {

        return ( clazz == Logger.class );
    }

}