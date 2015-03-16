package dsl;

import java.util.HashMap;

/**
 * Created by Maksym on 16.03.2015.
 */
abstract public class Element {
    public static final String DEFAULT_NAME = "empty";

    private String name;
    private String elementID;
    private HashMap<String,String> properties = new HashMap<String, String>();

    public Element(){
        setName(DEFAULT_NAME);
    }

    public Element(String name){
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getElementID() {
        return elementID;
    }

    public void setElementID(String elementID) {
        this.elementID = elementID;
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, String> properties) {
        this.properties = properties;
    }
}
