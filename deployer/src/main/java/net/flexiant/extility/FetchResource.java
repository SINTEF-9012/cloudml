
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for fetchResource complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fetchResource">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="skeletonResource" type="{http://extility.flexiant.net}virtualResource" minOccurs="0"/>
 *         &lt;element name="fetchParameters" type="{http://extility.flexiant.net}fetchParameters" minOccurs="0"/>
 *         &lt;element name="when" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fetchResource", propOrder = {
    "skeletonResource",
    "fetchParameters",
    "when"
})
public class FetchResource {

    protected VirtualResource skeletonResource;
    protected FetchParameters fetchParameters;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar when;

    /**
     * Gets the value of the skeletonResource property.
     * 
     * @return
     *     possible object is
     *     {@link VirtualResource }
     *     
     */
    public VirtualResource getSkeletonResource() {
        return skeletonResource;
    }

    /**
     * Sets the value of the skeletonResource property.
     * 
     * @param value
     *     allowed object is
     *     {@link VirtualResource }
     *     
     */
    public void setSkeletonResource(VirtualResource value) {
        this.skeletonResource = value;
    }

    /**
     * Gets the value of the fetchParameters property.
     * 
     * @return
     *     possible object is
     *     {@link FetchParameters }
     *     
     */
    public FetchParameters getFetchParameters() {
        return fetchParameters;
    }

    /**
     * Sets the value of the fetchParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link FetchParameters }
     *     
     */
    public void setFetchParameters(FetchParameters value) {
        this.fetchParameters = value;
    }

    /**
     * Gets the value of the when property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getWhen() {
        return when;
    }

    /**
     * Sets the value of the when property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setWhen(XMLGregorianCalendar value) {
        this.when = value;
    }

}
