
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for paymentCard complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="paymentCard">
 *   &lt;complexContent>
 *     &lt;extension base="{http://extility.flexiant.net}resource">
 *       &lt;sequence>
 *         &lt;element name="reference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="authcode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DSChecked" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="DSPassed" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="avsPassed" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="avsDetail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="scheme" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="expiryDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="registrationTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="declinedTransaction" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="cardDefault" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="hash" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paymentMethodUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paymentCard", propOrder = {
    "reference",
    "authcode",
    "dsChecked",
    "dsPassed",
    "avsPassed",
    "avsDetail",
    "scheme",
    "expiryDate",
    "registrationTime",
    "declinedTransaction",
    "cardDefault",
    "hash",
    "customerUUID",
    "paymentMethodUUID"
})
public class PaymentCard
    extends Resource
{

    protected String reference;
    protected String authcode;
    @XmlElement(name = "DSChecked")
    protected boolean dsChecked;
    @XmlElement(name = "DSPassed")
    protected boolean dsPassed;
    protected boolean avsPassed;
    protected String avsDetail;
    protected String scheme;
    protected String expiryDate;
    protected String registrationTime;
    protected int declinedTransaction;
    protected boolean cardDefault;
    protected String hash;
    protected String customerUUID;
    protected String paymentMethodUUID;

    /**
     * Gets the value of the reference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets the value of the reference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReference(String value) {
        this.reference = value;
    }

    /**
     * Gets the value of the authcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthcode() {
        return authcode;
    }

    /**
     * Sets the value of the authcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthcode(String value) {
        this.authcode = value;
    }

    /**
     * Gets the value of the dsChecked property.
     * 
     */
    public boolean isDSChecked() {
        return dsChecked;
    }

    /**
     * Sets the value of the dsChecked property.
     * 
     */
    public void setDSChecked(boolean value) {
        this.dsChecked = value;
    }

    /**
     * Gets the value of the dsPassed property.
     * 
     */
    public boolean isDSPassed() {
        return dsPassed;
    }

    /**
     * Sets the value of the dsPassed property.
     * 
     */
    public void setDSPassed(boolean value) {
        this.dsPassed = value;
    }

    /**
     * Gets the value of the avsPassed property.
     * 
     */
    public boolean isAvsPassed() {
        return avsPassed;
    }

    /**
     * Sets the value of the avsPassed property.
     * 
     */
    public void setAvsPassed(boolean value) {
        this.avsPassed = value;
    }

    /**
     * Gets the value of the avsDetail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvsDetail() {
        return avsDetail;
    }

    /**
     * Sets the value of the avsDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvsDetail(String value) {
        this.avsDetail = value;
    }

    /**
     * Gets the value of the scheme property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScheme() {
        return scheme;
    }

    /**
     * Sets the value of the scheme property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScheme(String value) {
        this.scheme = value;
    }

    /**
     * Gets the value of the expiryDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets the value of the expiryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpiryDate(String value) {
        this.expiryDate = value;
    }

    /**
     * Gets the value of the registrationTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegistrationTime() {
        return registrationTime;
    }

    /**
     * Sets the value of the registrationTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegistrationTime(String value) {
        this.registrationTime = value;
    }

    /**
     * Gets the value of the declinedTransaction property.
     * 
     */
    public int getDeclinedTransaction() {
        return declinedTransaction;
    }

    /**
     * Sets the value of the declinedTransaction property.
     * 
     */
    public void setDeclinedTransaction(int value) {
        this.declinedTransaction = value;
    }

    /**
     * Gets the value of the cardDefault property.
     * 
     */
    public boolean isCardDefault() {
        return cardDefault;
    }

    /**
     * Sets the value of the cardDefault property.
     * 
     */
    public void setCardDefault(boolean value) {
        this.cardDefault = value;
    }

    /**
     * Gets the value of the hash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHash() {
        return hash;
    }

    /**
     * Sets the value of the hash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHash(String value) {
        this.hash = value;
    }

    /**
     * Gets the value of the customerUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerUUID() {
        return customerUUID;
    }

    /**
     * Sets the value of the customerUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerUUID(String value) {
        this.customerUUID = value;
    }

    /**
     * Gets the value of the paymentMethodUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentMethodUUID() {
        return paymentMethodUUID;
    }

    /**
     * Sets the value of the paymentMethodUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentMethodUUID(String value) {
        this.paymentMethodUUID = value;
    }

}
