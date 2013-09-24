
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
 * <p>Java class for invoice complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="invoice">
 *   &lt;complexContent>
 *     &lt;extension base="{http://extility.flexiant.net}resource">
 *       &lt;sequence>
 *         &lt;element name="invoiceTotalInc" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="invoiceDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="invoiceTaxAmt" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="invoiceTotalExc" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="paidDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="invoiceNo" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="invoiceItems" type="{http://extility.flexiant.net}invoiceItem" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="customerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="testMode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="customerAddress" type="{http://extility.flexiant.net}address" minOccurs="0"/>
 *         &lt;element name="billingAddress" type="{http://extility.flexiant.net}address" minOccurs="0"/>
 *         &lt;element name="customerVatNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billingEntityName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billingEntityVatNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="currencyId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="taxRate" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="pdfRef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="invoiceSource" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paymentMethodUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paymentProvider" type="{http://extility.flexiant.net}paymentProvider" minOccurs="0"/>
 *         &lt;element name="paymentProviderRef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="authCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="usingStoredCard" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="cardUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="status" type="{http://extility.flexiant.net}invoiceStatus" minOccurs="0"/>
 *         &lt;element name="dueDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "invoice", propOrder = {
    "invoiceTotalInc",
    "invoiceDate",
    "invoiceTaxAmt",
    "invoiceTotalExc",
    "paidDate",
    "invoiceNo",
    "invoiceItems",
    "customerName",
    "testMode",
    "customerAddress",
    "billingAddress",
    "customerVatNo",
    "billingEntityName",
    "billingEntityVatNo",
    "currencyId",
    "taxRate",
    "pdfRef",
    "invoiceSource",
    "paymentMethodUUID",
    "paymentProvider",
    "paymentProviderRef",
    "authCode",
    "usingStoredCard",
    "cardUUID",
    "customerUUID",
    "status",
    "dueDate"
})
public class Invoice
    extends Resource
{

    protected double invoiceTotalInc;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar invoiceDate;
    protected double invoiceTaxAmt;
    protected double invoiceTotalExc;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar paidDate;
    protected long invoiceNo;
    @XmlElement(nillable = true)
    protected List<InvoiceItem> invoiceItems;
    protected String customerName;
    protected boolean testMode;
    protected Address customerAddress;
    protected Address billingAddress;
    protected String customerVatNo;
    protected String billingEntityName;
    protected String billingEntityVatNo;
    protected long currencyId;
    protected double taxRate;
    protected String pdfRef;
    protected String invoiceSource;
    protected String paymentMethodUUID;
    protected PaymentProvider paymentProvider;
    protected String paymentProviderRef;
    protected String authCode;
    protected boolean usingStoredCard;
    protected String cardUUID;
    protected String customerUUID;
    protected InvoiceStatus status;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dueDate;

    /**
     * Gets the value of the invoiceTotalInc property.
     * 
     */
    public double getInvoiceTotalInc() {
        return invoiceTotalInc;
    }

    /**
     * Sets the value of the invoiceTotalInc property.
     * 
     */
    public void setInvoiceTotalInc(double value) {
        this.invoiceTotalInc = value;
    }

    /**
     * Gets the value of the invoiceDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getInvoiceDate() {
        return invoiceDate;
    }

    /**
     * Sets the value of the invoiceDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setInvoiceDate(XMLGregorianCalendar value) {
        this.invoiceDate = value;
    }

    /**
     * Gets the value of the invoiceTaxAmt property.
     * 
     */
    public double getInvoiceTaxAmt() {
        return invoiceTaxAmt;
    }

    /**
     * Sets the value of the invoiceTaxAmt property.
     * 
     */
    public void setInvoiceTaxAmt(double value) {
        this.invoiceTaxAmt = value;
    }

    /**
     * Gets the value of the invoiceTotalExc property.
     * 
     */
    public double getInvoiceTotalExc() {
        return invoiceTotalExc;
    }

    /**
     * Sets the value of the invoiceTotalExc property.
     * 
     */
    public void setInvoiceTotalExc(double value) {
        this.invoiceTotalExc = value;
    }

    /**
     * Gets the value of the paidDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPaidDate() {
        return paidDate;
    }

    /**
     * Sets the value of the paidDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPaidDate(XMLGregorianCalendar value) {
        this.paidDate = value;
    }

    /**
     * Gets the value of the invoiceNo property.
     * 
     */
    public long getInvoiceNo() {
        return invoiceNo;
    }

    /**
     * Sets the value of the invoiceNo property.
     * 
     */
    public void setInvoiceNo(long value) {
        this.invoiceNo = value;
    }

    /**
     * Gets the value of the invoiceItems property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the invoiceItems property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInvoiceItems().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InvoiceItem }
     * 
     * 
     */
    public List<InvoiceItem> getInvoiceItems() {
        if (invoiceItems == null) {
            invoiceItems = new ArrayList<InvoiceItem>();
        }
        return this.invoiceItems;
    }

    /**
     * Gets the value of the customerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the value of the customerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerName(String value) {
        this.customerName = value;
    }

    /**
     * Gets the value of the testMode property.
     * 
     */
    public boolean isTestMode() {
        return testMode;
    }

    /**
     * Sets the value of the testMode property.
     * 
     */
    public void setTestMode(boolean value) {
        this.testMode = value;
    }

    /**
     * Gets the value of the customerAddress property.
     * 
     * @return
     *     possible object is
     *     {@link Address }
     *     
     */
    public Address getCustomerAddress() {
        return customerAddress;
    }

    /**
     * Sets the value of the customerAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link Address }
     *     
     */
    public void setCustomerAddress(Address value) {
        this.customerAddress = value;
    }

    /**
     * Gets the value of the billingAddress property.
     * 
     * @return
     *     possible object is
     *     {@link Address }
     *     
     */
    public Address getBillingAddress() {
        return billingAddress;
    }

    /**
     * Sets the value of the billingAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link Address }
     *     
     */
    public void setBillingAddress(Address value) {
        this.billingAddress = value;
    }

    /**
     * Gets the value of the customerVatNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerVatNo() {
        return customerVatNo;
    }

    /**
     * Sets the value of the customerVatNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerVatNo(String value) {
        this.customerVatNo = value;
    }

    /**
     * Gets the value of the billingEntityName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingEntityName() {
        return billingEntityName;
    }

    /**
     * Sets the value of the billingEntityName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingEntityName(String value) {
        this.billingEntityName = value;
    }

    /**
     * Gets the value of the billingEntityVatNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingEntityVatNo() {
        return billingEntityVatNo;
    }

    /**
     * Sets the value of the billingEntityVatNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingEntityVatNo(String value) {
        this.billingEntityVatNo = value;
    }

    /**
     * Gets the value of the currencyId property.
     * 
     */
    public long getCurrencyId() {
        return currencyId;
    }

    /**
     * Sets the value of the currencyId property.
     * 
     */
    public void setCurrencyId(long value) {
        this.currencyId = value;
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
     * Gets the value of the pdfRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPdfRef() {
        return pdfRef;
    }

    /**
     * Sets the value of the pdfRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPdfRef(String value) {
        this.pdfRef = value;
    }

    /**
     * Gets the value of the invoiceSource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvoiceSource() {
        return invoiceSource;
    }

    /**
     * Sets the value of the invoiceSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvoiceSource(String value) {
        this.invoiceSource = value;
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

    /**
     * Gets the value of the paymentProvider property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentProvider }
     *     
     */
    public PaymentProvider getPaymentProvider() {
        return paymentProvider;
    }

    /**
     * Sets the value of the paymentProvider property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentProvider }
     *     
     */
    public void setPaymentProvider(PaymentProvider value) {
        this.paymentProvider = value;
    }

    /**
     * Gets the value of the paymentProviderRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentProviderRef() {
        return paymentProviderRef;
    }

    /**
     * Sets the value of the paymentProviderRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentProviderRef(String value) {
        this.paymentProviderRef = value;
    }

    /**
     * Gets the value of the authCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthCode() {
        return authCode;
    }

    /**
     * Sets the value of the authCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthCode(String value) {
        this.authCode = value;
    }

    /**
     * Gets the value of the usingStoredCard property.
     * 
     */
    public boolean isUsingStoredCard() {
        return usingStoredCard;
    }

    /**
     * Sets the value of the usingStoredCard property.
     * 
     */
    public void setUsingStoredCard(boolean value) {
        this.usingStoredCard = value;
    }

    /**
     * Gets the value of the cardUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardUUID() {
        return cardUUID;
    }

    /**
     * Sets the value of the cardUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardUUID(String value) {
        this.cardUUID = value;
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
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link InvoiceStatus }
     *     
     */
    public InvoiceStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link InvoiceStatus }
     *     
     */
    public void setStatus(InvoiceStatus value) {
        this.status = value;
    }

    /**
     * Gets the value of the dueDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDueDate() {
        return dueDate;
    }

    /**
     * Sets the value of the dueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDueDate(XMLGregorianCalendar value) {
        this.dueDate = value;
    }

}
