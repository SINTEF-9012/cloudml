
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for isPermittedResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="isPermittedResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="permitted" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "isPermittedResponse", propOrder = {
    "permitted"
})
public class IsPermittedResponse {

    protected boolean permitted;

    /**
     * Gets the value of the permitted property.
     * 
     */
    public boolean isPermitted() {
        return permitted;
    }

    /**
     * Sets the value of the permitted property.
     * 
     */
    public void setPermitted(boolean value) {
        this.permitted = value;
    }

}
