
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for resourceMetadata complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resourceMetadata">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="privateMetadata" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="publicMetadata" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resourceUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="restrictedMetadata" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resourceMetadata", propOrder = {
    "privateMetadata",
    "publicMetadata",
    "resourceUUID",
    "restrictedMetadata"
})
public class ResourceMetadata {

    protected String privateMetadata;
    protected String publicMetadata;
    protected String resourceUUID;
    protected String restrictedMetadata;

    /**
     * Gets the value of the privateMetadata property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrivateMetadata() {
        return privateMetadata;
    }

    /**
     * Sets the value of the privateMetadata property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrivateMetadata(String value) {
        this.privateMetadata = value;
    }

    /**
     * Gets the value of the publicMetadata property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPublicMetadata() {
        return publicMetadata;
    }

    /**
     * Sets the value of the publicMetadata property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPublicMetadata(String value) {
        this.publicMetadata = value;
    }

    /**
     * Gets the value of the resourceUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResourceUUID() {
        return resourceUUID;
    }

    /**
     * Sets the value of the resourceUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResourceUUID(String value) {
        this.resourceUUID = value;
    }

    /**
     * Gets the value of the restrictedMetadata property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRestrictedMetadata() {
        return restrictedMetadata;
    }

    /**
     * Sets the value of the restrictedMetadata property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRestrictedMetadata(String value) {
        this.restrictedMetadata = value;
    }

}
