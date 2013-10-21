
package net.flexiant.extility;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for productOffer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="productOffer">
 *   &lt;complexContent>
 *     &lt;extension base="{http://extility.flexiant.net}resource">
 *       &lt;sequence>
 *         &lt;element name="productUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="componentConfig" type="{http://extility.flexiant.net}productComponent" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="billingPeriod" type="{http://extility.flexiant.net}billingPeriod" minOccurs="0"/>
 *         &lt;element name="unitType" type="{http://extility.flexiant.net}unitType" minOccurs="0"/>
 *         &lt;element name="clusters" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="inUse" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isEditable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="productName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "productOffer", propOrder = {
    "productUUID",
    "componentConfig",
    "billingPeriod",
    "unitType",
    "clusters",
    "inUse",
    "isEditable",
    "productName"
})
public class ProductOffer
    extends Resource
{

    protected String productUUID;
    @XmlElement(nillable = true)
    protected List<ProductComponent> componentConfig;
    protected BillingPeriod billingPeriod;
    protected UnitType unitType;
    @XmlElement(nillable = true)
    protected List<String> clusters;
    protected Boolean inUse;
    protected Boolean isEditable;
    protected String productName;

    /**
     * Gets the value of the productUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductUUID() {
        return productUUID;
    }

    /**
     * Sets the value of the productUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductUUID(String value) {
        this.productUUID = value;
    }

    /**
     * Gets the value of the componentConfig property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the componentConfig property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComponentConfig().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProductComponent }
     * 
     * 
     */
    public List<ProductComponent> getComponentConfig() {
        if (componentConfig == null) {
            componentConfig = new ArrayList<ProductComponent>();
        }
        return this.componentConfig;
    }

    /**
     * Gets the value of the billingPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link BillingPeriod }
     *     
     */
    public BillingPeriod getBillingPeriod() {
        return billingPeriod;
    }

    /**
     * Sets the value of the billingPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillingPeriod }
     *     
     */
    public void setBillingPeriod(BillingPeriod value) {
        this.billingPeriod = value;
    }

    /**
     * Gets the value of the unitType property.
     * 
     * @return
     *     possible object is
     *     {@link UnitType }
     *     
     */
    public UnitType getUnitType() {
        return unitType;
    }

    /**
     * Sets the value of the unitType property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnitType }
     *     
     */
    public void setUnitType(UnitType value) {
        this.unitType = value;
    }

    /**
     * Gets the value of the clusters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the clusters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClusters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getClusters() {
        if (clusters == null) {
            clusters = new ArrayList<String>();
        }
        return this.clusters;
    }

    /**
     * Gets the value of the inUse property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInUse() {
        return inUse;
    }

    /**
     * Sets the value of the inUse property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInUse(Boolean value) {
        this.inUse = value;
    }

    /**
     * Gets the value of the isEditable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsEditable() {
        return isEditable;
    }

    /**
     * Sets the value of the isEditable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsEditable(Boolean value) {
        this.isEditable = value;
    }

    /**
     * Gets the value of the productName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the value of the productName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductName(String value) {
        this.productName = value;
    }

}
