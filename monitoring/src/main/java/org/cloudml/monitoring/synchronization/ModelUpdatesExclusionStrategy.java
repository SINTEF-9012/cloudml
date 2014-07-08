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
import org.slf4j.Logger;

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
/* example of using the gson serialization
    public static void main(String[] args) {

        List<VM> vms = new ArrayList<VM>();
        List<Component> components = new ArrayList<Component>();
        List<ExternalComponent> externalComponents = new ArrayList<ExternalComponent>();

        for(int i = 0; i<1;i++){
            VM vm = new VM();
            vm.setId(Integer.toString(i));
            vm.setNumberOfCpus(i);
            vm.setUrl("abcd");
            vms.add(vm);
        }

        for(int i = 0; i<2;i++){
            Component component = new Component();
            component.setId(Integer.toString(i)+"comp");
            component.setStarted(true);
            component.setUrl("abcd-compo");
            components.add(component);
        }

        for(int i = 0; i<3;i++){
            ExternalComponent externalComponent = new ExternalComponent();
            externalComponent.setId(Integer.toString(i)+"extcomp");
            externalComponent.setStarted(false);
            externalComponent.setUrl("abcd-extcompo");
            externalComponent.setCloudProvider("me");
            externalComponents.add(externalComponent);
        }

        ModelUpdates model = new ModelUpdates(vms,components,externalComponents);

        Gson gson = new GsonBuilder().setExclusionStrategies(new ModelUpdatesExclusionStrategy()).serializeNulls().create();

        String json = gson.toJson(model);

        System.out.println(json);

    }
    */


}