
package net.flexiant.extility;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for image complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="image">
 *   &lt;complexContent>
 *     &lt;extension base="{http://extility.flexiant.net}virtualResource">
 *       &lt;sequence>
 *         &lt;element name="size" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="defaultUser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="genPassword" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="imageType" type="{http://extility.flexiant.net}imageType" minOccurs="0"/>
 *         &lt;element name="baseUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="snapshotUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="baseName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userPermission" type="{http://extility.flexiant.net}imagePermission" minOccurs="0"/>
 *         &lt;element name="ownerPermission" type="{http://extility.flexiant.net}imagePermission" minOccurs="0"/>
 *         &lt;element name="customerValidString" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="publishedTo" type="{http://extility.flexiant.net}publishTo" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="osChargeProductOfferUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="osChargeProductOfferName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "image", propOrder = {
    "size",
    "defaultUser",
    "genPassword",
    "imageType",
    "baseUUID",
    "snapshotUUID",
    "baseName",
    "userPermission",
    "ownerPermission",
    "customerValidString",
    "publishedTo",
    "osChargeProductOfferUUID",
    "osChargeProductOfferName"
})
public class Image
    extends VirtualResource
{

    protected long size;
    protected String defaultUser;
    protected boolean genPassword;
    protected ImageType imageType;
    protected String baseUUID;
    protected String snapshotUUID;
    protected String baseName;
    protected ImagePermission userPermission;
    protected ImagePermission ownerPermission;
    protected String customerValidString;
    @XmlElement(nillable = true)
    protected List<PublishTo> publishedTo;
    protected String osChargeProductOfferUUID;
    protected String osChargeProductOfferName;

    /**
     * Gets the value of the size property.
     * 
     */
    public long getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     */
    public void setSize(long value) {
        this.size = value;
    }

    /**
     * Gets the value of the defaultUser property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultUser() {
        return defaultUser;
    }

    /**
     * Sets the value of the defaultUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultUser(String value) {
        this.defaultUser = value;
    }

    /**
     * Gets the value of the genPassword property.
     * 
     */
    public boolean isGenPassword() {
        return genPassword;
    }

    /**
     * Sets the value of the genPassword property.
     * 
     */
    public void setGenPassword(boolean value) {
        this.genPassword = value;
    }

    /**
     * Gets the value of the imageType property.
     * 
     * @return
     *     possible object is
     *     {@link ImageType }
     *     
     */
    public ImageType getImageType() {
        return imageType;
    }

    /**
     * Sets the value of the imageType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageType }
     *     
     */
    public void setImageType(ImageType value) {
        this.imageType = value;
    }

    /**
     * Gets the value of the baseUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseUUID() {
        return baseUUID;
    }

    /**
     * Sets the value of the baseUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseUUID(String value) {
        this.baseUUID = value;
    }

    /**
     * Gets the value of the snapshotUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSnapshotUUID() {
        return snapshotUUID;
    }

    /**
     * Sets the value of the snapshotUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSnapshotUUID(String value) {
        this.snapshotUUID = value;
    }

    /**
     * Gets the value of the baseName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseName() {
        return baseName;
    }

    /**
     * Sets the value of the baseName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseName(String value) {
        this.baseName = value;
    }

    /**
     * Gets the value of the userPermission property.
     * 
     * @return
     *     possible object is
     *     {@link ImagePermission }
     *     
     */
    public ImagePermission getUserPermission() {
        return userPermission;
    }

    /**
     * Sets the value of the userPermission property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImagePermission }
     *     
     */
    public void setUserPermission(ImagePermission value) {
        this.userPermission = value;
    }

    /**
     * Gets the value of the ownerPermission property.
     * 
     * @return
     *     possible object is
     *     {@link ImagePermission }
     *     
     */
    public ImagePermission getOwnerPermission() {
        return ownerPermission;
    }

    /**
     * Sets the value of the ownerPermission property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImagePermission }
     *     
     */
    public void setOwnerPermission(ImagePermission value) {
        this.ownerPermission = value;
    }

    /**
     * Gets the value of the customerValidString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerValidString() {
        return customerValidString;
    }

    /**
     * Sets the value of the customerValidString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerValidString(String value) {
        this.customerValidString = value;
    }

    /**
     * Gets the value of the publishedTo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the publishedTo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPublishedTo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PublishTo }
     * 
     * 
     */
    public List<PublishTo> getPublishedTo() {
        if (publishedTo == null) {
            publishedTo = new ArrayList<PublishTo>();
        }
        return this.publishedTo;
    }

    /**
     * Gets the value of the osChargeProductOfferUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOsChargeProductOfferUUID() {
        return osChargeProductOfferUUID;
    }

    /**
     * Sets the value of the osChargeProductOfferUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOsChargeProductOfferUUID(String value) {
        this.osChargeProductOfferUUID = value;
    }

    /**
     * Gets the value of the osChargeProductOfferName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOsChargeProductOfferName() {
        return osChargeProductOfferName;
    }

    /**
     * Sets the value of the osChargeProductOfferName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOsChargeProductOfferName(String value) {
        this.osChargeProductOfferName = value;
    }

}
