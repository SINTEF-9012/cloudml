
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for createFirewallTemplate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createFirewallTemplate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="skeletonFirewallTemplate" type="{http://extility.flexiant.net}firewallTemplate" minOccurs="0"/>
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
@XmlType(name = "createFirewallTemplate", propOrder = {
    "skeletonFirewallTemplate",
    "when"
})
public class CreateFirewallTemplate {

    protected FirewallTemplate skeletonFirewallTemplate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar when;

    /**
     * Gets the value of the skeletonFirewallTemplate property.
     * 
     * @return
     *     possible object is
     *     {@link FirewallTemplate }
     *     
     */
    public FirewallTemplate getSkeletonFirewallTemplate() {
        return skeletonFirewallTemplate;
    }

    /**
     * Sets the value of the skeletonFirewallTemplate property.
     * 
     * @param value
     *     allowed object is
     *     {@link FirewallTemplate }
     *     
     */
    public void setSkeletonFirewallTemplate(FirewallTemplate value) {
        this.skeletonFirewallTemplate = value;
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
