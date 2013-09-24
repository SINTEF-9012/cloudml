
package net.flexiant.extility;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for billingEntityDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="billingEntityDetails">
 *   &lt;complexContent>
 *     &lt;extension base="{http://extility.flexiant.net}billingEntity">
 *       &lt;sequence>
 *         &lt;element name="address" type="{http://extility.flexiant.net}address" minOccurs="0"/>
 *         &lt;element name="vatNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="controlPanelURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="brand" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="currencyId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="no3ds28DaySpendLimit" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="overall28DaySpendLimit" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="enableUserEditing" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="adminControlPanelURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="standardEmailSettings">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="key" type="{http://extility.flexiant.net}billingEntityVAR" minOccurs="0"/>
 *                             &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="emailTemplates">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="key" type="{http://extility.flexiant.net}emailType" minOccurs="0"/>
 *                             &lt;element name="value" type="{http://extility.flexiant.net}emailTemplate" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "billingEntityDetails", propOrder = {
    "address",
    "vatNumber",
    "controlPanelURL",
    "description",
    "brand",
    "currencyId",
    "no3Ds28DaySpendLimit",
    "overall28DaySpendLimit",
    "enableUserEditing",
    "adminControlPanelURL",
    "standardEmailSettings",
    "emailTemplates"
})
public class BillingEntityDetails
    extends BillingEntity
{

    protected Address address;
    protected String vatNumber;
    protected String controlPanelURL;
    protected String description;
    protected long brand;
    protected int currencyId;
    @XmlElement(name = "no3ds28DaySpendLimit")
    protected double no3Ds28DaySpendLimit;
    protected double overall28DaySpendLimit;
    protected boolean enableUserEditing;
    protected String adminControlPanelURL;
    @XmlElement(required = true)
    protected BillingEntityDetails.StandardEmailSettings standardEmailSettings;
    @XmlElement(required = true)
    protected BillingEntityDetails.EmailTemplates emailTemplates;

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
     * Gets the value of the controlPanelURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getControlPanelURL() {
        return controlPanelURL;
    }

    /**
     * Sets the value of the controlPanelURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setControlPanelURL(String value) {
        this.controlPanelURL = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the brand property.
     * 
     */
    public long getBrand() {
        return brand;
    }

    /**
     * Sets the value of the brand property.
     * 
     */
    public void setBrand(long value) {
        this.brand = value;
    }

    /**
     * Gets the value of the currencyId property.
     * 
     */
    public int getCurrencyId() {
        return currencyId;
    }

    /**
     * Sets the value of the currencyId property.
     * 
     */
    public void setCurrencyId(int value) {
        this.currencyId = value;
    }

    /**
     * Gets the value of the no3Ds28DaySpendLimit property.
     * 
     */
    public double getNo3Ds28DaySpendLimit() {
        return no3Ds28DaySpendLimit;
    }

    /**
     * Sets the value of the no3Ds28DaySpendLimit property.
     * 
     */
    public void setNo3Ds28DaySpendLimit(double value) {
        this.no3Ds28DaySpendLimit = value;
    }

    /**
     * Gets the value of the overall28DaySpendLimit property.
     * 
     */
    public double getOverall28DaySpendLimit() {
        return overall28DaySpendLimit;
    }

    /**
     * Sets the value of the overall28DaySpendLimit property.
     * 
     */
    public void setOverall28DaySpendLimit(double value) {
        this.overall28DaySpendLimit = value;
    }

    /**
     * Gets the value of the enableUserEditing property.
     * 
     */
    public boolean isEnableUserEditing() {
        return enableUserEditing;
    }

    /**
     * Sets the value of the enableUserEditing property.
     * 
     */
    public void setEnableUserEditing(boolean value) {
        this.enableUserEditing = value;
    }

    /**
     * Gets the value of the adminControlPanelURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdminControlPanelURL() {
        return adminControlPanelURL;
    }

    /**
     * Sets the value of the adminControlPanelURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdminControlPanelURL(String value) {
        this.adminControlPanelURL = value;
    }

    /**
     * Gets the value of the standardEmailSettings property.
     * 
     * @return
     *     possible object is
     *     {@link BillingEntityDetails.StandardEmailSettings }
     *     
     */
    public BillingEntityDetails.StandardEmailSettings getStandardEmailSettings() {
        return standardEmailSettings;
    }

    /**
     * Sets the value of the standardEmailSettings property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillingEntityDetails.StandardEmailSettings }
     *     
     */
    public void setStandardEmailSettings(BillingEntityDetails.StandardEmailSettings value) {
        this.standardEmailSettings = value;
    }

    /**
     * Gets the value of the emailTemplates property.
     * 
     * @return
     *     possible object is
     *     {@link BillingEntityDetails.EmailTemplates }
     *     
     */
    public BillingEntityDetails.EmailTemplates getEmailTemplates() {
        return emailTemplates;
    }

    /**
     * Sets the value of the emailTemplates property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillingEntityDetails.EmailTemplates }
     *     
     */
    public void setEmailTemplates(BillingEntityDetails.EmailTemplates value) {
        this.emailTemplates = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="key" type="{http://extility.flexiant.net}emailType" minOccurs="0"/>
     *                   &lt;element name="value" type="{http://extility.flexiant.net}emailTemplate" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "entry"
    })
    public static class EmailTemplates {

        protected List<BillingEntityDetails.EmailTemplates.Entry> entry;

        /**
         * Gets the value of the entry property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the entry property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getEntry().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link BillingEntityDetails.EmailTemplates.Entry }
         * 
         * 
         */
        public List<BillingEntityDetails.EmailTemplates.Entry> getEntry() {
            if (entry == null) {
                entry = new ArrayList<BillingEntityDetails.EmailTemplates.Entry>();
            }
            return this.entry;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="key" type="{http://extility.flexiant.net}emailType" minOccurs="0"/>
         *         &lt;element name="value" type="{http://extility.flexiant.net}emailTemplate" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "key",
            "value"
        })
        public static class Entry {

            protected EmailType key;
            protected EmailTemplate value;

            /**
             * Gets the value of the key property.
             * 
             * @return
             *     possible object is
             *     {@link EmailType }
             *     
             */
            public EmailType getKey() {
                return key;
            }

            /**
             * Sets the value of the key property.
             * 
             * @param value
             *     allowed object is
             *     {@link EmailType }
             *     
             */
            public void setKey(EmailType value) {
                this.key = value;
            }

            /**
             * Gets the value of the value property.
             * 
             * @return
             *     possible object is
             *     {@link EmailTemplate }
             *     
             */
            public EmailTemplate getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             * 
             * @param value
             *     allowed object is
             *     {@link EmailTemplate }
             *     
             */
            public void setValue(EmailTemplate value) {
                this.value = value;
            }

        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="key" type="{http://extility.flexiant.net}billingEntityVAR" minOccurs="0"/>
     *                   &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "entry"
    })
    public static class StandardEmailSettings {

        protected List<BillingEntityDetails.StandardEmailSettings.Entry> entry;

        /**
         * Gets the value of the entry property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the entry property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getEntry().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link BillingEntityDetails.StandardEmailSettings.Entry }
         * 
         * 
         */
        public List<BillingEntityDetails.StandardEmailSettings.Entry> getEntry() {
            if (entry == null) {
                entry = new ArrayList<BillingEntityDetails.StandardEmailSettings.Entry>();
            }
            return this.entry;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="key" type="{http://extility.flexiant.net}billingEntityVAR" minOccurs="0"/>
         *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "key",
            "value"
        })
        public static class Entry {

            protected BillingEntityVAR key;
            protected String value;

            /**
             * Gets the value of the key property.
             * 
             * @return
             *     possible object is
             *     {@link BillingEntityVAR }
             *     
             */
            public BillingEntityVAR getKey() {
                return key;
            }

            /**
             * Sets the value of the key property.
             * 
             * @param value
             *     allowed object is
             *     {@link BillingEntityVAR }
             *     
             */
            public void setKey(BillingEntityVAR value) {
                this.key = value;
            }

            /**
             * Gets the value of the value property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setValue(String value) {
                this.value = value;
            }

        }

    }

}
