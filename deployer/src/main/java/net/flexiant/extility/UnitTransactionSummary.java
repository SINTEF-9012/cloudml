
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for unitTransactionSummary complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="unitTransactionSummary">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="billingEntityUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="freeUnits" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="transactionDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="unitCredits" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="unitDebits" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "unitTransactionSummary", propOrder = {
    "billingEntityUUID",
    "customerUUID",
    "freeUnits",
    "transactionDate",
    "unitCredits",
    "unitDebits"
})
public class UnitTransactionSummary {

    protected String billingEntityUUID;
    protected String customerUUID;
    protected double freeUnits;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar transactionDate;
    protected double unitCredits;
    protected double unitDebits;

    /**
     * Gets the value of the billingEntityUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingEntityUUID() {
        return billingEntityUUID;
    }

    /**
     * Sets the value of the billingEntityUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingEntityUUID(String value) {
        this.billingEntityUUID = value;
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
     * Gets the value of the freeUnits property.
     * 
     */
    public double getFreeUnits() {
        return freeUnits;
    }

    /**
     * Sets the value of the freeUnits property.
     * 
     */
    public void setFreeUnits(double value) {
        this.freeUnits = value;
    }

    /**
     * Gets the value of the transactionDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTransactionDate() {
        return transactionDate;
    }

    /**
     * Sets the value of the transactionDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTransactionDate(XMLGregorianCalendar value) {
        this.transactionDate = value;
    }

    /**
     * Gets the value of the unitCredits property.
     * 
     */
    public double getUnitCredits() {
        return unitCredits;
    }

    /**
     * Sets the value of the unitCredits property.
     * 
     */
    public void setUnitCredits(double value) {
        this.unitCredits = value;
    }

    /**
     * Gets the value of the unitDebits property.
     * 
     */
    public double getUnitDebits() {
        return unitDebits;
    }

    /**
     * Sets the value of the unitDebits property.
     * 
     */
    public void setUnitDebits(double value) {
        this.unitDebits = value;
    }

}
