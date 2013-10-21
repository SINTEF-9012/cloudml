
package net.flexiant.extility;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for subnet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="subnet">
 *   &lt;complexContent>
 *     &lt;extension base="{http://extility.flexiant.net}virtualResource">
 *       &lt;sequence>
 *         &lt;element name="networkUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="networkAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="broadcastAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="defaultGatewayAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="firstUsableAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mask" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="subnetType" type="{http://extility.flexiant.net}ipType" minOccurs="0"/>
 *         &lt;element name="systemAllocated" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="networkName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="networkType" type="{http://extility.flexiant.net}networkType" minOccurs="0"/>
 *         &lt;element name="useableIps" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "subnet", propOrder = {
    "networkUUID",
    "networkAddress",
    "broadcastAddress",
    "defaultGatewayAddress",
    "firstUsableAddress",
    "mask",
    "subnetType",
    "systemAllocated",
    "networkName",
    "networkType",
    "useableIps"
})
public class Subnet
    extends VirtualResource
{

    protected String networkUUID;
    protected String networkAddress;
    protected String broadcastAddress;
    protected String defaultGatewayAddress;
    protected String firstUsableAddress;
    protected int mask;
    protected IpType subnetType;
    protected Boolean systemAllocated;
    protected String networkName;
    protected NetworkType networkType;
    @XmlElement(nillable = true)
    protected List<String> useableIps;

    /**
     * Gets the value of the networkUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetworkUUID() {
        return networkUUID;
    }

    /**
     * Sets the value of the networkUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetworkUUID(String value) {
        this.networkUUID = value;
    }

    /**
     * Gets the value of the networkAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetworkAddress() {
        return networkAddress;
    }

    /**
     * Sets the value of the networkAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetworkAddress(String value) {
        this.networkAddress = value;
    }

    /**
     * Gets the value of the broadcastAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBroadcastAddress() {
        return broadcastAddress;
    }

    /**
     * Sets the value of the broadcastAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBroadcastAddress(String value) {
        this.broadcastAddress = value;
    }

    /**
     * Gets the value of the defaultGatewayAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultGatewayAddress() {
        return defaultGatewayAddress;
    }

    /**
     * Sets the value of the defaultGatewayAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultGatewayAddress(String value) {
        this.defaultGatewayAddress = value;
    }

    /**
     * Gets the value of the firstUsableAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstUsableAddress() {
        return firstUsableAddress;
    }

    /**
     * Sets the value of the firstUsableAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstUsableAddress(String value) {
        this.firstUsableAddress = value;
    }

    /**
     * Gets the value of the mask property.
     * 
     */
    public int getMask() {
        return mask;
    }

    /**
     * Sets the value of the mask property.
     * 
     */
    public void setMask(int value) {
        this.mask = value;
    }

    /**
     * Gets the value of the subnetType property.
     * 
     * @return
     *     possible object is
     *     {@link IpType }
     *     
     */
    public IpType getSubnetType() {
        return subnetType;
    }

    /**
     * Sets the value of the subnetType property.
     * 
     * @param value
     *     allowed object is
     *     {@link IpType }
     *     
     */
    public void setSubnetType(IpType value) {
        this.subnetType = value;
    }

    /**
     * Gets the value of the systemAllocated property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSystemAllocated() {
        return systemAllocated;
    }

    /**
     * Sets the value of the systemAllocated property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSystemAllocated(Boolean value) {
        this.systemAllocated = value;
    }

    /**
     * Gets the value of the networkName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetworkName() {
        return networkName;
    }

    /**
     * Sets the value of the networkName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetworkName(String value) {
        this.networkName = value;
    }

    /**
     * Gets the value of the networkType property.
     * 
     * @return
     *     possible object is
     *     {@link NetworkType }
     *     
     */
    public NetworkType getNetworkType() {
        return networkType;
    }

    /**
     * Sets the value of the networkType property.
     * 
     * @param value
     *     allowed object is
     *     {@link NetworkType }
     *     
     */
    public void setNetworkType(NetworkType value) {
        this.networkType = value;
    }

    /**
     * Gets the value of the useableIps property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the useableIps property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUseableIps().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getUseableIps() {
        if (useableIps == null) {
            useableIps = new ArrayList<String>();
        }
        return this.useableIps;
    }

}
