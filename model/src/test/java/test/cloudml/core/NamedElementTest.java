/*
 */
package test.cloudml.core;

import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import org.cloudml.core.NamedElement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class NamedElementTest extends TestCase {
    
    @Test(expected = IllegalArgumentException.class)
    public void emptyStringAsNameIsRejectedByConstructor() {
        new NamedElement("") {
        };
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void nullAsNameIsRejectedByConstructor() {
        new NamedElement(null) {
        };
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void setNameRejectsNull() {
        NamedElement namedElement = new NamedElement("name") {
        };
        namedElement.setName(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void setNameRejectsEmptyString() {
        NamedElement namedElement = new NamedElement("name") {
        };
        namedElement.setName("");
    }
    
    @Test
    public void constructorUsedTheDefaultName() {
        NamedElement namedElement = new NamedElement() {
        };
        assertEquals("name", NamedElement.DEFAULT_NAME, namedElement.getName());
    }
    
    @Test
    public void nameCanBeSetInConstructorAndRetrived() {
        final String name = "my name";
        NamedElement namedElement = new NamedElement(name) {
        };
        assertEquals("name", name, namedElement.getName());
    }
    
    @Test
    public void nameCanBeSetAndRetrived() {
        final String name = "my name";
        NamedElement namedElement = new NamedElement() {
        };
        namedElement.setName(name);
        assertEquals("name", name, namedElement.getName());
    }
}
