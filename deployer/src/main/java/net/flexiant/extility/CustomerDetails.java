
package net.flexiant.extility;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for customerDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="customerDetails">
 *   &lt;complexContent>
 *     &lt;extension base="{http://extility.flexiant.net}customer">
 *       &lt;sequence>
 *         &lt;element name="organisationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vatNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerCardCheck" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="warningLevel" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="warningSent" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="timeZone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vatExempt" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="users" type="{http://extility.flexiant.net}userDetails" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="address" type="{http://extility.flexiant.net}address" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customerDetails", propOrder = {
    "organisationName",
    "vatNumber",
    "customerCardCheck",
    "warningLevel",
    "warningSent",
    "timeZone",
    "vatExempt",
    "users",
    "email",
    "address"
})
public class CustomerDetails
    extends Customer
{

    protected String organisationName;
    protected String vatNumber;
    protected boolean customerCardCheck;
    protected double warningLevel;
    protected boolean warningSent;
    protected String timeZone;
    protected boolean vatExempt;
    @XmlElement(nillable = true)
    protected List<UserDetails> users;
    protected String email;
    protected Address address;

    /**
     * Gets the value of the organisationName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganisationName() {
        return organisationName;
    }

    /**
     * Sets the value of the organisationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganisationName(String value) {
        this.organisationName = value;
    }

    /**
     * Gets the value of the vatNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVatNumber() {
        return vatNumber;
    }

    /**
     * Sets the value of the vatNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVatNumber(String value) {
        this.vatNumber = value;
    }

    /**
     * Gets the value of the customerCardCheck property.
     * 
     */
    public boolean isCustomerCardCheck() {
        return customerCardCheck;
    }

    /**
     * Sets the value of the customerCardCheck property.
     * 
     */
    public void setCustomerCardCheck(boolean value) {
        this.customerCardCheck = value;
    }

    /**
     * Gets the value of the warningLevel property.
     * 
     */
    public double getWarningLevel() {
        return warningLevel;
    }

    /**
     * Sets the value of the warningLevel property.
     * 
     */
    public void setWarningLevel(double value) {
        this.warningLevel = value;
    }

    /**
     * Gets the value of the warningSent property.
     * 
     */
    public boolean isWarningSent() {
        return warningSent;
    }

    /**
     * Sets the value of the warningSent property.
     * 
     */
    public void setWarningSent(boolean value) {
        this.warningSent = value;
    }

    /**
     * Gets the value of the timeZone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * Sets the value of the timeZone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeZone(String value) {
        this.timeZone = value;
    }

    /**
     * Gets the value of the vatExempt property.
     * 
     */
    public boolean isVatExempt() {
        return vatExempt;
    }

    /**
     * Sets the value of the vatExempt property.
     * 
     */
    public void setVatExempt(boolean value) {
        this.vatExempt = value;
    }

    /**
     * Gets the value of the users property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the users property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUsers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UserDetails }
     * 
     * 
     */
    public List<UserDetails> getUsers() {
        if (users == null) {
            users = new ArrayList<UserDetails>();
        }
        return this.users;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link Address }
     *     
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link Address }
     *     
     */
    public void setAddress(Address value) {
        this.address = value;
    }

}
