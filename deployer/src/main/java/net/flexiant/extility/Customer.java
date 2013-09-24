
package net.flexiant.extility;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for customer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="customer">
 *   &lt;complexContent>
 *     &lt;extension base="{http://extility.flexiant.net}resource">
 *       &lt;sequence>
 *         &lt;element name="status" type="{http://extility.flexiant.net}status" minOccurs="0"/>
 *         &lt;element name="limitsMap">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="key" type="{http://extility.flexiant.net}limits" minOccurs="0"/>
 *                             &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
 *         &lt;element name="carryOverBalance" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="nonCarryOverBalance" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="customerUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="validatedString" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="creditCustomer" type="{http://extility.flexiant.net}paymentType" minOccurs="0"/>
 *         &lt;element name="productOfferUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customer", propOrder = {
    "status",
    "limitsMap",
    "carryOverBalance",
    "nonCarryOverBalance",
    "customerUUID",
    "validatedString",
    "creditCustomer",
    "productOfferUUID"
})
@XmlSeeAlso({
    CustomerDetails.class
})
public class Customer
    extends Resource
{

    protected Status status;
    @XmlElement(required = true)
    protected Customer.LimitsMap limitsMap;
    protected double carryOverBalance;
    protected double nonCarryOverBalance;
    protected String customerUUID;
    protected String validatedString;
    protected PaymentType creditCustomer;
    protected String productOfferUUID;

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link Status }
     *     
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link Status }
     *     
     */
    public void setStatus(Status value) {
        this.status = value;
    }

    /**
     * Gets the value of the limitsMap property.
     * 
     * @return
     *     possible object is
     *     {@link Customer.LimitsMap }
     *     
     */
    public Customer.LimitsMap getLimitsMap() {
        return limitsMap;
    }

    /**
     * Sets the value of the limitsMap property.
     * 
     * @param value
     *     allowed object is
     *     {@link Customer.LimitsMap }
     *     
     */
    public void setLimitsMap(Customer.LimitsMap value) {
        this.limitsMap = value;
    }

    /**
     * Gets the value of the carryOverBalance property.
     * 
     */
    public double getCarryOverBalance() {
        return carryOverBalance;
    }

    /**
     * Sets the value of the carryOverBalance property.
     * 
     */
    public void setCarryOverBalance(double value) {
        this.carryOverBalance = value;
    }

    /**
     * Gets the value of the nonCarryOverBalance property.
     * 
     */
    public double getNonCarryOverBalance() {
        return nonCarryOverBalance;
    }

    /**
     * Sets the value of the nonCarryOverBalance property.
     * 
     */
    public void setNonCarryOverBalance(double value) {
        this.nonCarryOverBalance = value;
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
     * Gets the value of the validatedString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValidatedString() {
        return validatedString;
    }

    /**
     * Sets the value of the validatedString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidatedString(String value) {
        this.validatedString = value;
    }

    /**
     * Gets the value of the creditCustomer property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentType }
     *     
     */
    public PaymentType getCreditCustomer() {
        return creditCustomer;
    }

    /**
     * Sets the value of the creditCustomer property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentType }
     *     
     */
    public void setCreditCustomer(PaymentType value) {
        this.creditCustomer = value;
    }

    /**
     * Gets the value of the productOfferUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductOfferUUID() {
        return productOfferUUID;
    }

    /**
     * Sets the value of the productOfferUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductOfferUUID(String value) {
        this.productOfferUUID = value;
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
     *                   &lt;element name="key" type="{http://extility.flexiant.net}limits" minOccurs="0"/>
     *                   &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
    public static class LimitsMap {

        protected List<Customer.LimitsMap.Entry> entry;

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
         * {@link Customer.LimitsMap.Entry }
         * 
         * 
         */
        public List<Customer.LimitsMap.Entry> getEntry() {
            if (entry == null) {
                entry = new ArrayList<Customer.LimitsMap.Entry>();
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
         *         &lt;element name="key" type="{http://extility.flexiant.net}limits" minOccurs="0"/>
         *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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

            protected Limits key;
            protected Integer value;

            /**
             * Gets the value of the key property.
             * 
             * @return
             *     possible object is
             *     {@link Limits }
             *     
             */
            public Limits getKey() {
                return key;
            }

            /**
             * Sets the value of the key property.
             * 
             * @param value
             *     allowed object is
             *     {@link Limits }
             *     
             */
            public void setKey(Limits value) {
                this.key = value;
            }

            /**
             * Gets the value of the value property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setValue(Integer value) {
                this.value = value;
            }

        }

    }

}
