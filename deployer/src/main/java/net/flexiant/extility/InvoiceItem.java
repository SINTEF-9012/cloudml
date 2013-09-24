
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for invoiceItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="invoiceItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="invoiceItemTaxAmt" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="invoiceItemTotalExc" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="invoiceItemTotalInc" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="invoiceUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="taxRate" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="unitPrice" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "invoiceItem", propOrder = {
    "description",
    "invoiceItemTaxAmt",
    "invoiceItemTotalExc",
    "invoiceItemTotalInc",
    "invoiceUUID",
    "quantity",
    "taxRate",
    "unitPrice"
})
public class InvoiceItem {

    protected String description;
    protected double invoiceItemTaxAmt;
    protected double invoiceItemTotalExc;
    protected double invoiceItemTotalInc;
    protected String invoiceUUID;
    protected long quantity;
    protected double taxRate;
    protected double unitPrice;

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
     * Gets the value of the invoiceItemTaxAmt property.
     * 
     */
    public double getInvoiceItemTaxAmt() {
        return invoiceItemTaxAmt;
    }

    /**
     * Sets the value of the invoiceItemTaxAmt property.
     * 
     */
    public void setInvoiceItemTaxAmt(double value) {
        this.invoiceItemTaxAmt = value;
    }

    /**
     * Gets the value of the invoiceItemTotalExc property.
     * 
     */
    public double getInvoiceItemTotalExc() {
        return invoiceItemTotalExc;
    }

    /**
     * Sets the value of the invoiceItemTotalExc property.
     * 
     */
    public void setInvoiceItemTotalExc(double value) {
        this.invoiceItemTotalExc = value;
    }

    /**
     * Gets the value of the invoiceItemTotalInc property.
     * 
     */
    public double getInvoiceItemTotalInc() {
        return invoiceItemTotalInc;
    }

    /**
     * Sets the value of the invoiceItemTotalInc property.
     * 
     */
    public void setInvoiceItemTotalInc(double value) {
        this.invoiceItemTotalInc = value;
    }

    /**
     * Gets the value of the invoiceUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvoiceUUID() {
        return invoiceUUID;
    }

    /**
     * Sets the value of the invoiceUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvoiceUUID(String value) {
        this.invoiceUUID = value;
    }

    /**
     * Gets the value of the quantity property.
     * 
     */
    public long getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     */
    public void setQuantity(long value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the taxRate property.
     * 
     */
    public double getTaxRate() {
        return taxRate;
    }

    /**
     * Sets the value of the taxRate property.
     * 
     */
    public void setTaxRate(double value) {
        this.taxRate = value;
    }

    /**
     * Gets the value of the unitPrice property.
     * 
     */
    public double getUnitPrice() {
        return unitPrice;
    }

    /**
     * Sets the value of the unitPrice property.
     * 
     */
    public void setUnitPrice(double value) {
        this.unitPrice = value;
    }

}
