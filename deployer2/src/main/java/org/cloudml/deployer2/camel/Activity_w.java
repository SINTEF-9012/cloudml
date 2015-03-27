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

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultProducerTemplate;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by Maksym on 06.03.2015.
 */
public class Activity_w extends RouteBuilder {
    static Logger LOG = LoggerFactory.getLogger(Activity_w.class);
    ArrayList<String> tasks;

    public Activity_w(ArrayList<String> tasks) {
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

        final ProducerTemplate template = this.getContext().createProducerTemplate();




//        from("timer://runOnce?repeatCount=1&delay=1000").id("init").startupOrder(1).beanRef("Start").beanRef("Start:OUT").
//                beanRef("Fork from:Start").multicast().parallelProcessing().to("bean:provisionAVM 1:controlIN","bean:provisionAVM 2:controlIN","bean:provisionAVM 3:controlIN").end().
//                process(new Processor() {
//                    @Override
//                    public void process(final Exchange exchange) throws Exception {
//                        String init = template.requestBody("controlbus:route?routeId=init&action=status", null, String.class);
//                        System.out.println(init);
//                        exchange.getContext().startAllRoutes();
////                        exchange.getContext().startRoute("pro1");
////                        exchange.getContext().startRoute("pro2");
////                        exchange.getContext().startRoute("pro3");
//                    }
//                });
//        from("timer://runOnce?repeatCount=1&delay=1000").id("pro1").autoStartup(false).to("bean:provisionAVM 1").delay(1000).multicast().parallelProcessing().to("bean:provisionAVM 1:dataOUT", "bean:provisionAVM 1:controlOUT");
//        from("timer://runOnce?repeatCount=1&delay=1000").id("pro2").autoStartup(false).to("bean:provisionAVM 2").delay(2000).multicast().parallelProcessing().to("bean:provisionAVM 2:dataOUT", "bean:provisionAVM 2:controlOUT");
//        from("timer://runOnce?repeatCount=1&delay=1000").id("pro3").autoStartup(false).to("bean:provisionAVM 3").delay(3000).multicast().parallelProcessing().to("bean:provisionAVM 3:dataOUT", "bean:provisionAVM 3:controlOUT");


        from("timer://runOnce?repeatCount=1&delay=1000").beanRef("Start").beanRef("Start:OUT").beanRef("Fork from:Start").
                multicast().parallelProcessing().to("bean:provisionAVM 1:controlIN","bean:provisionAVM 2:controlIN","bean:provisionAVM 3:controlIN").end().
                multicast().parallelProcessing().to("bean:provisionAVM 1","bean:provisionAVM 2","bean:provisionAVM 3").end().
                multicast().parallelProcessing().to("bean:provisionAVM 1:dataOUT", "bean:provisionAVM 1:controlOUT", "bean:provisionAVM 2:dataOUT", "bean:provisionAVM 2:controlOUT", "bean:provisionAVM 3:dataOUT", "bean:provisionAVM 3:controlOUT").end().
                multicast().parallelProcessing().to("bean:Join to:Public Addresses", "bean:Join to:Stop").end().
                multicast().parallelProcessing().to("bean:Public Addresses:dataIN", "bean:Stop:IN").end().
                beanRef("Public Addresses").beanRef("Stop");

    }


}
