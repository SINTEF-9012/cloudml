package org.cloudml.deployer2.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Maksym on 20.03.2015.
 */
public class RouteParts {
    private Class cl;
    private static final Class[] paramString = {String.class};
    private Method  method;
    private RouteBuilder builder;

    public RouteParts(RouteBuilder builder){
        setBuilder(builder);
    }

    public RouteDefinition from(String uri) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        cl = Class.forName("org.apache.camel.builder.RouteBuilder");
        method = cl.getDeclaredMethod("from", paramString);
        return (RouteDefinition) (method.invoke(getBuilder(),uri));
    }

    public RouteBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(RouteBuilder builder) {
        this.builder = builder;
    }
}
