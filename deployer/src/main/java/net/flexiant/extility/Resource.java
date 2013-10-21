
package net.flexiant.extility;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for resource complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resource">
 *   &lt;complexContent>
 *     &lt;extension base="{http://extility.flexiant.net}pseudoResource">
 *       &lt;sequence>
 *         &lt;element name="billingEntityUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resourceCreateDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="resourceKey" type="{http://extility.flexiant.net}resourceKey" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="resourceMetadata" type="{http://extility.flexiant.net}resourceMetadata" minOccurs="0"/>
 *         &lt;element name="resourceName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resourceState" type="{http://extility.flexiant.net}resourceState" minOccurs="0"/>
 *         &lt;element name="resourceUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resource", propOrder = {
    "billingEntityUUID",
    "resourceCreateDate",
    "resourceKey",
    "resourceMetadata",
    "resourceName",
    "resourceState",
    "resourceUUID"
})
@XmlSeeAlso({
    ProductOffer.class,
    Customer.class,
    Promotion.class,
    BillingEntity.class,
    Invoice.class,
    PaymentCard.class,
    Cluster.class,
    VirtualResource.class,
    User.class
})
public class Resource
    extends PseudoResource
{

    protected String billingEntityUUID;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar resourceCreateDate;
    @XmlElement(nillable = true)
    protected List<ResourceKey> resourceKey;
    protected ResourceMetadata resourceMetadata;
    protected String resourceName;
    protected ResourceState resourceState;
    protected String resourceUUID;

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
     * Gets the value of the resourceCreateDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getResourceCreateDate() {
        return resourceCreateDate;
    }

    /**
     * Sets the value of the resourceCreateDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setResourceCreateDate(XMLGregorianCalendar value) {
        this.resourceCreateDate = value;
    }

    /**
     * Gets the value of the resourceKey property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceKey property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceKey().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceKey }
     * 
     * 
     */
    public List<ResourceKey> getResourceKey() {
        if (resourceKey == null) {
            resourceKey = new ArrayList<ResourceKey>();
        }
        return this.resourceKey;
    }

    /**
     * Gets the value of the resourceMetadata property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceMetadata }
     *     
     */
    public ResourceMetadata getResourceMetadata() {
        return resourceMetadata;
    }

    /**
     * Sets the value of the resourceMetadata property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceMetadata }
     *     
     */
    public void setResourceMetadata(ResourceMetadata value) {
        this.resourceMetadata = value;
    }

    /**
     * Gets the value of the resourceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * Sets the value of the resourceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResourceName(String value) {
        this.resourceName = value;
    }

    /**
     * Gets the value of the resourceState property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceState }
     *     
     */
    public ResourceState getResourceState() {
        return resourceState;
    }

    /**
     * Sets the value of the resourceState property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceState }
     *     
     */
    public void setResourceState(ResourceState value) {
        this.resourceState = value;
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

}
