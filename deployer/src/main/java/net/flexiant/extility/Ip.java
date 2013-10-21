
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ip complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ip">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="auto" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="ipAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nicUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subnetUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://extility.flexiant.net}ipType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ip", propOrder = {
    "auto",
    "ipAddress",
    "nicUUID",
    "subnetUUID",
    "type"
})
public class Ip {

    protected boolean auto;
    protected String ipAddress;
    protected String nicUUID;
    protected String subnetUUID;
    protected IpType type;

    /**
     * Gets the value of the auto property.
     * 
     */
    public boolean isAuto() {
        return auto;
    }

    /**
     * Sets the value of the auto property.
     * 
     */
    public void setAuto(boolean value) {
        this.auto = value;
    }

    /**
     * Gets the value of the ipAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Sets the value of the ipAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpAddress(String value) {
        this.ipAddress = value;
    }

    /**
     * Gets the value of the nicUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNicUUID() {
        return nicUUID;
    }

    /**
     * Sets the value of the nicUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNicUUID(String value) {
        this.nicUUID = value;
    }

    /**
     * Gets the value of the subnetUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubnetUUID() {
        return subnetUUID;
    }

    /**
     * Sets the value of the subnetUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubnetUUID(String value) {
        this.subnetUUID = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link IpType }
     *     
     */
    public IpType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link IpType }
     *     
     */
    public void setType(IpType value) {
        this.type = value;
    }

}
