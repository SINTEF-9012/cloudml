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
package org.cloudml.deployer2.camel;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by Maksym on 06.03.2015.
 */
public class ParallelFlow extends RouteBuilder {
    static Logger LOG = LoggerFactory.getLogger(ParallelFlow.class);
    ArrayList<String> tasks;

    public ParallelFlow(ArrayList<String> tasks) {
        this.tasks = tasks;
    }

    @Override
    public void configure() throws Exception {
//        // call methods at run-time http://www.mkyong.com/java/how-to-use-reflection-to-call-java-method-at-runtime/
//        MulticastDefinition multi = from("timer://runOnce?repeatCount=1&delay=2000").multicast().parallelProcessing();
//
//        Class cls = Class.forName("org.apache.camel.model.ProcessorDefinition");
//        Class[] paramString = new Class[1];
//        paramString[0] = String.class;
//        Method method = cls.getDeclaredMethod("inOut", paramString);
//
//        for (String task:tasks){
//            method.invoke(multi, "bean:" + task);
//        }
        String[] list = new String[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            list[i] = "bean:" + tasks.get(i);
        }
        from("timer://runOnce?repeatCount=1&delay=4000").multicast().parallelProcessing().to(list);

    }


}
