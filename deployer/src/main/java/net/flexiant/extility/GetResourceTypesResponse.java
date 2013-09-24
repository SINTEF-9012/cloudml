
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getResourceTypesResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getResourceTypesResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="resourceTypeMap" type="{http://extility.flexiant.net}mapHolder" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getResourceTypesResponse", propOrder = {
    "resourceTypeMap"
})
public class GetResourceTypesResponse {

    protected MapHolder resourceTypeMap;

    /**
     * Gets the value of the resourceTypeMap property.
     * 
     * @return
     *     possible object is
     *     {@link MapHolder }
     *     
     */
    public MapHolder getResourceTypeMap() {
        return resourceTypeMap;
    }

    /**
     * Sets the value of the resourceTypeMap property.
     * 
     * @param value
     *     allowed object is
     *     {@link MapHolder }
     *     
     */
    public void setResourceTypeMap(MapHolder value) {
        this.resourceTypeMap = value;
    }

}
