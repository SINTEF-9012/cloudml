
package net.flexiant.extility;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for productPurchase complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="productPurchase">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="active" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="billingEntityUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="donotRebill" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="endDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="lastBillPeriod" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="nextBillPeriod" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="productID" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="productOfferUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="purchaseDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="purchasedConfig" type="{http://extility.flexiant.net}productComponent" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="referenceUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resourceUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="vdcUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "productPurchase", propOrder = {
    "active",
    "billingEntityUUID",
    "customerUUID",
    "description",
    "donotRebill",
    "endDate",
    "lastBillPeriod",
    "nextBillPeriod",
    "productID",
    "productOfferUUID",
    "purchaseDate",
    "purchasedConfig",
    "referenceUUID",
    "resourceUUID",
    "startDate",
    "vdcUUID"
})
public class ProductPurchase {

    protected boolean active;
    protected String billingEntityUUID;
    protected String customerUUID;
    protected String description;
    protected boolean donotRebill;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar endDate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastBillPeriod;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar nextBillPeriod;
    protected long productID;
    protected String productOfferUUID;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar purchaseDate;
    @XmlElement(nillable = true)
    protected List<ProductComponent> purchasedConfig;
    protected String referenceUUID;
    protected String resourceUUID;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar startDate;
    protected String vdcUUID;

    /**
     * Gets the value of the active property.
     * 
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the value of the active property.
     * 
     */
    public void setActive(boolean value) {
        this.active = value;
    }

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
     * Gets the value of the donotRebill property.
     * 
     */
    public boolean isDonotRebill() {
        return donotRebill;
    }

    /**
     * Sets the value of the donotRebill property.
     * 
     */
    public void setDonotRebill(boolean value) {
        this.donotRebill = value;
    }

    /**
     * Gets the value of the endDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndDate(XMLGregorianCalendar value) {
        this.endDate = value;
    }

    /**
     * Gets the value of the lastBillPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastBillPeriod() {
        return lastBillPeriod;
    }

    /**
     * Sets the value of the lastBillPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastBillPeriod(XMLGregorianCalendar value) {
        this.lastBillPeriod = value;
    }

    /**
     * Gets the value of the nextBillPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getNextBillPeriod() {
        return nextBillPeriod;
    }

    /**
     * Sets the value of the nextBillPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setNextBillPeriod(XMLGregorianCalendar value) {
        this.nextBillPeriod = value;
    }

    /**
     * Gets the value of the productID property.
     * 
     */
    public long getProductID() {
        return productID;
    }

    /**
     * Sets the value of the productID property.
     * 
     */
    public void setProductID(long value) {
        this.productID = value;
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
     * Gets the value of the purchaseDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * Sets the value of the purchaseDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPurchaseDate(XMLGregorianCalendar value) {
        this.purchaseDate = value;
    }

    /**
     * Gets the value of the purchasedConfig property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the purchasedConfig property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPurchasedConfig().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProductComponent }
     * 
     * 
     */
    public List<ProductComponent> getPurchasedConfig() {
        if (purchasedConfig == null) {
            purchasedConfig = new ArrayList<ProductComponent>();
        }
        return this.purchasedConfig;
    }

    /**
     * Gets the value of the referenceUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenceUUID() {
        return referenceUUID;
    }

    /**
     * Sets the value of the referenceUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenceUUID(String value) {
        this.referenceUUID = value;
    }

    /**
     * Gets the value of the resourceUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResourceUUID() {
        return resourceUUID;
    }

    /**
     * Sets the value of the resourceUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResourceUUID(String value) {
        this.resourceUUID = value;
    }

    /**
     * Gets the value of the startDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStartDate(XMLGregorianCalendar value) {
        this.startDate = value;
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
