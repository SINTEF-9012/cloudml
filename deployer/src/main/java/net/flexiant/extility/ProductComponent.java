
package net.flexiant.extility;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for productComponent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="productComponent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lastBillingTime" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="billingConfiguredValues" type="{http://extility.flexiant.net}value" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="billingMethodUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="componentTypeUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productConfiguredValues" type="{http://extility.flexiant.net}value" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "productComponent", propOrder = {
    "lastBillingTime",
    "billingConfiguredValues",
    "billingMethodUUID",
    "componentTypeUUID",
    "productConfiguredValues"
})
public class ProductComponent {

    protected Long lastBillingTime;
    @XmlElement(nillable = true)
    protected List<Value> billingConfiguredValues;
    protected String billingMethodUUID;
    protected String componentTypeUUID;
    @XmlElement(nillable = true)
    protected List<Value> productConfiguredValues;

    /**
     * Gets the value of the lastBillingTime property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLastBillingTime() {
        return lastBillingTime;
    }

    /**
     * Sets the value of the lastBillingTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLastBillingTime(Long value) {
        this.lastBillingTime = value;
    }

    /**
     * Gets the value of the billingConfiguredValues property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the billingConfiguredValues property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBillingConfiguredValues().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Value }
     * 
     * 
     */
    public List<Value> getBillingConfiguredValues() {
        if (billingConfiguredValues == null) {
            billingConfiguredValues = new ArrayList<Value>();
        }
        return this.billingConfiguredValues;
    }

    /**
     * Gets the value of the billingMethodUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingMethodUUID() {
        return billingMethodUUID;
    }

    /**
     * Sets the value of the billingMethodUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingMethodUUID(String value) {
        this.billingMethodUUID = value;
    }

    /**
     * Gets the value of the componentTypeUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComponentTypeUUID() {
        return componentTypeUUID;
    }

    /**
     * Sets the value of the componentTypeUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComponentTypeUUID(String value) {
        this.componentTypeUUID = value;
    }

    /**
     * Gets the value of the productConfiguredValues property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the productConfiguredValues property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProductConfiguredValues().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Value }
     * 
     * 
     */
    public List<Value> getProductConfiguredValues() {
        if (productConfiguredValues == null) {
            productConfiguredValues = new ArrayList<Value>();
        }
        return this.productConfiguredValues;
    }

}
