
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for referralPromoCode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="referralPromoCode">
 *   &lt;complexContent>
 *     &lt;extension base="{http://extility.flexiant.net}promoCode">
 *       &lt;sequence>
 *         &lt;element name="emailAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="expiryDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="inviteeCustomerUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="invitorCustomerUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="status" type="{http://extility.flexiant.net}referralStatus" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "referralPromoCode", propOrder = {
    "emailAddress",
    "expiryDate",
    "inviteeCustomerUUID",
    "invitorCustomerUUID",
    "status"
})
public class ReferralPromoCode
    extends PromoCode
{

    protected String emailAddress;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar expiryDate;
    protected String inviteeCustomerUUID;
    protected String invitorCustomerUUID;
    protected ReferralStatus status;

    /**
     * Gets the value of the emailAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the value of the emailAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailAddress(String value) {
        this.emailAddress = value;
    }

    /**
     * Gets the value of the expiryDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets the value of the expiryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setExpiryDate(XMLGregorianCalendar value) {
        this.expiryDate = value;
    }

    /**
     * Gets the value of the inviteeCustomerUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInviteeCustomerUUID() {
        return inviteeCustomerUUID;
    }

    /**
     * Sets the value of the inviteeCustomerUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInviteeCustomerUUID(String value) {
        this.inviteeCustomerUUID = value;
    }

    /**
     * Gets the value of the invitorCustomerUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvitorCustomerUUID() {
        return invitorCustomerUUID;
    }

    /**
     * Sets the value of the invitorCustomerUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvitorCustomerUUID(String value) {
        this.invitorCustomerUUID = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link ReferralStatus }
     *     
     */
    public ReferralStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReferralStatus }
     *     
     */
    public void setStatus(ReferralStatus value) {
        this.status = value;
    }

}
