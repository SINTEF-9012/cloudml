
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for unitTransaction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="unitTransaction">
 *   &lt;complexContent>
 *     &lt;extension base="{http://extility.flexiant.net}pseudoResource">
 *       &lt;sequence>
 *         &lt;element name="billingEntityUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="chargedFromDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="chargedToDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="closingBalance" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="customerUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descriptionDetail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="linkedPurchase" type="{http://extility.flexiant.net}productPurchase" minOccurs="0"/>
 *         &lt;element name="linkedResourceName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="linkedResourceUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="linkedServerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="linkedServerUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="openingBalance" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="transactionAmount" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="transactionDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="unitsType" type="{http://extility.flexiant.net}unitType" minOccurs="0"/>
 *         &lt;element name="vdcUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "unitTransaction", propOrder = {
    "billingEntityUUID",
    "chargedFromDate",
    "chargedToDate",
    "closingBalance",
    "customerUUID",
    "description",
    "descriptionDetail",
    "linkedPurchase",
    "linkedResourceName",
    "linkedResourceUUID",
    "linkedServerName",
    "linkedServerUUID",
    "openingBalance",
    "transactionAmount",
    "transactionDate",
    "unitsType",
    "vdcUUID"
})
public class UnitTransaction
    extends PseudoResource
{

    protected String billingEntityUUID;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar chargedFromDate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar chargedToDate;
    protected double closingBalance;
    protected String customerUUID;
    protected String description;
    protected String descriptionDetail;
    protected ProductPurchase linkedPurchase;
    protected String linkedResourceName;
    protected String linkedResourceUUID;
    protected String linkedServerName;
    protected String linkedServerUUID;
    protected double openingBalance;
    protected double transactionAmount;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar transactionDate;
    protected UnitType unitsType;
    protected String vdcUUID;

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
     * Gets the value of the chargedFromDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getChargedFromDate() {
        return chargedFromDate;
    }

    /**
     * Sets the value of the chargedFromDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setChargedFromDate(XMLGregorianCalendar value) {
        this.chargedFromDate = value;
    }

    /**
     * Gets the value of the chargedToDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getChargedToDate() {
        return chargedToDate;
    }

    /**
     * Sets the value of the chargedToDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setChargedToDate(XMLGregorianCalendar value) {
        this.chargedToDate = value;
    }

    /**
     * Gets the value of the closingBalance property.
     * 
     */
    public double getClosingBalance() {
        return closingBalance;
    }

    /**
     * Sets the value of the closingBalance property.
     * 
     */
    public void setClosingBalance(double value) {
        this.closingBalance = value;
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
     * Gets the value of the descriptionDetail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescriptionDetail() {
        return descriptionDetail;
    }

    /**
     * Sets the value of the descriptionDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescriptionDetail(String value) {
        this.descriptionDetail = value;
    }

    /**
     * Gets the value of the linkedPurchase property.
     * 
     * @return
     *     possible object is
     *     {@link ProductPurchase }
     *     
     */
    public ProductPurchase getLinkedPurchase() {
        return linkedPurchase;
    }

    /**
     * Sets the value of the linkedPurchase property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProductPurchase }
     *     
     */
    public void setLinkedPurchase(ProductPurchase value) {
        this.linkedPurchase = value;
    }

    /**
     * Gets the value of the linkedResourceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkedResourceName() {
        return linkedResourceName;
    }

    /**
     * Sets the value of the linkedResourceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkedResourceName(String value) {
        this.linkedResourceName = value;
    }

    /**
     * Gets the value of the linkedResourceUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkedResourceUUID() {
        return linkedResourceUUID;
    }

    /**
     * Sets the value of the linkedResourceUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkedResourceUUID(String value) {
        this.linkedResourceUUID = value;
    }

    /**
     * Gets the value of the linkedServerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkedServerName() {
        return linkedServerName;
    }

    /**
     * Sets the value of the linkedServerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkedServerName(String value) {
        this.linkedServerName = value;
    }

    /**
     * Gets the value of the linkedServerUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkedServerUUID() {
        return linkedServerUUID;
    }

    /**
     * Sets the value of the linkedServerUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkedServerUUID(String value) {
        this.linkedServerUUID = value;
    }

    /**
     * Gets the value of the openingBalance property.
     * 
     */
    public double getOpeningBalance() {
        return openingBalance;
    }

    /**
     * Sets the value of the openingBalance property.
     * 
     */
    public void setOpeningBalance(double value) {
        this.openingBalance = value;
    }

    /**
     * Gets the value of the transactionAmount property.
     * 
     */
    public double getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * Sets the value of the transactionAmount property.
     * 
     */
    public void setTransactionAmount(double value) {
        this.transactionAmount = value;
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
     * Gets the value of the unitsType property.
     * 
     * @return
     *     possible object is
     *     {@link UnitType }
     *     
     */
    public UnitType getUnitsType() {
        return unitsType;
    }

    /**
     * Sets the value of the unitsType property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnitType }
     *     
     */
    public void setUnitsType(UnitType value) {
        this.unitsType = value;
    }

    /**
     * Gets the value of the vdcUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVdcUUID() {
        return vdcUUID;
    }

    /**
     * Sets the value of the vdcUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVdcUUID(String value) {
        this.vdcUUID = value;
    }

}
