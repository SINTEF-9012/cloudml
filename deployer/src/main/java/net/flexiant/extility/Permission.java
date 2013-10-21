
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for permission complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="permission">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="permittedResource" type="{http://extility.flexiant.net}resource" minOccurs="0"/>
 *         &lt;element name="permittedTo" type="{http://extility.flexiant.net}simpleResource" minOccurs="0"/>
 *         &lt;element name="capability" type="{http://extility.flexiant.net}capability" minOccurs="0"/>
 *         &lt;element name="resourceType" type="{http://extility.flexiant.net}resourceType" minOccurs="0"/>
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
@XmlType(name = "permission", propOrder = {
    "permittedResource",
    "permittedTo",
    "capability",
    "resourceType",
    "permitted"
})
public class Permission {

    protected Resource permittedResource;
    protected SimpleResource permittedTo;
    protected Capability capability;
    protected ResourceType resourceType;
    protected boolean permitted;

    /**
     * Gets the value of the permittedResource property.
     * 
     * @return
     *     possible object is
     *     {@link Resource }
     *     
     */
    public Resource getPermittedResource() {
        return permittedResource;
    }

    /**
     * Sets the value of the permittedResource property.
     * 
     * @param value
     *     allowed object is
     *     {@link Resource }
     *     
     */
    public void setPermittedResource(Resource value) {
        this.permittedResource = value;
    }

    /**
     * Gets the value of the permittedTo property.
     * 
     * @return
     *     possible object is
     *     {@link SimpleResource }
     *     
     */
    public SimpleResource getPermittedTo() {
        return permittedTo;
    }

    /**
     * Sets the value of the permittedTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link SimpleResource }
     *     
     */
    public void setPermittedTo(SimpleResource value) {
        this.permittedTo = value;
    }

    /**
     * Gets the value of the capability property.
     * 
     * @return
     *     possible object is
     *     {@link Capability }
     *     
     */
    public Capability getCapability() {
        return capability;
    }

    /**
     * Sets the value of the capability property.
     * 
     * @param value
     *     allowed object is
     *     {@link Capability }
     *     
     */
    public void setCapability(Capability value) {
        this.capability = value;
    }

    /**
     * Gets the value of the resourceType property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceType }
     *     
     */
    public ResourceType getResourceType() {
        return resourceType;
    }

    /**
     * Sets the value of the resourceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceType }
     *     
     */
    public void setResourceType(ResourceType value) {
        this.resourceType = value;
    }

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
