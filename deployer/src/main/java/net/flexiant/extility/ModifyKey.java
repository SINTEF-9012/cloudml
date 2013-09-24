
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for modifyKey complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="modifyKey">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="resourceUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resourceKey" type="{http://extility.flexiant.net}resourceKey" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "modifyKey", propOrder = {
    "resourceUUID",
    "resourceKey"
})
public class ModifyKey {

    protected String resourceUUID;
    protected ResourceKey resourceKey;

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
     * Gets the value of the resourceKey property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceKey }
     *     
     */
    public ResourceKey getResourceKey() {
        return resourceKey;
    }

    /**
     * Sets the value of the resourceKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceKey }
     *     
     */
    public void setResourceKey(ResourceKey value) {
        this.resourceKey = value;
    }

}
