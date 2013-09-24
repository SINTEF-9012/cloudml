
package net.flexiant.extility;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for network complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="network">
 *   &lt;complexContent>
 *     &lt;extension base="{http://extility.flexiant.net}virtualResource">
 *       &lt;sequence>
 *         &lt;element name="networkType" type="{http://extility.flexiant.net}networkType" minOccurs="0"/>
 *         &lt;element name="subnets" type="{http://extility.flexiant.net}subnet" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ipv6RoutingEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "network", propOrder = {
    "networkType",
    "subnets",
    "ipv6RoutingEnabled"
})
public class Network
    extends VirtualResource
{

    protected NetworkType networkType;
    @XmlElement(nillable = true)
    protected List<Subnet> subnets;
    protected boolean ipv6RoutingEnabled;

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
     * Gets the value of the subnets property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subnets property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubnets().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Subnet }
     * 
     * 
     */
    public List<Subnet> getSubnets() {
        if (subnets == null) {
            subnets = new ArrayList<Subnet>();
        }
        return this.subnets;
    }

    /**
     * Gets the value of the ipv6RoutingEnabled property.
     * 
     */
    public boolean isIpv6RoutingEnabled() {
        return ipv6RoutingEnabled;
    }

    /**
     * Sets the value of the ipv6RoutingEnabled property.
     * 
     */
    public void setIpv6RoutingEnabled(boolean value) {
        this.ipv6RoutingEnabled = value;
    }

}
