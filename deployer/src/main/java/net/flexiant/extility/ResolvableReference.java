
package net.flexiant.extility;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for resolvableReference complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resolvableReference">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="referenceResource" type="{http://extility.flexiant.net}resource" minOccurs="0"/>
 *         &lt;element name="missingType" type="{http://extility.flexiant.net}resourceType" minOccurs="0"/>
 *         &lt;element name="missingUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="alternateReferences" type="{http://extility.flexiant.net}resource" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resolvableReference", propOrder = {
    "referenceResource",
    "missingType",
    "missingUUID",
    "alternateReferences"
})
public class ResolvableReference {

    protected Resource referenceResource;
    protected ResourceType missingType;
    protected String missingUUID;
    protected List<Resource> alternateReferences;

    /**
     * Gets the value of the referenceResource property.
     * 
     * @return
     *     possible object is
     *     {@link Resource }
     *     
     */
    public Resource getReferenceResource() {
        return referenceResource;
    }

    /**
     * Sets the value of the referenceResource property.
     * 
     * @param value
     *     allowed object is
     *     {@link Resource }
     *     
     */
    public void setReferenceResource(Resource value) {
        this.referenceResource = value;
    }

    /**
     * Gets the value of the missingType property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceType }
     *     
     */
    public ResourceType getMissingType() {
        return missingType;
    }

    /**
     * Sets the value of the missingType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceType }
     *     
     */
    public void setMissingType(ResourceType value) {
        this.missingType = value;
    }

    /**
     * Gets the value of the missingUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMissingUUID() {
        return missingUUID;
    }

    /**
     * Sets the value of the missingUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMissingUUID(String value) {
        this.missingUUID = value;
    }

    /**
     * Gets the value of the alternateReferences property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the alternateReferences property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAlternateReferences().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Resource }
     * 
     * 
     */
    public List<Resource> getAlternateReferences() {
        if (alternateReferences == null) {
            alternateReferences = new ArrayList<Resource>();
        }
        return this.alternateReferences;
    }

}
