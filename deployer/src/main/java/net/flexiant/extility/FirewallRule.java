
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for firewallRule complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="firewallRule">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="action" type="{http://extility.flexiant.net}firewallRuleAction" minOccurs="0"/>
 *         &lt;element name="connState" type="{http://extility.flexiant.net}firewallConnectionState" minOccurs="0"/>
 *         &lt;element name="icmpParam" type="{http://extility.flexiant.net}icmpParam" minOccurs="0"/>
 *         &lt;element name="ipAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ipCIDRMask" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="localPort" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="protocol" type="{http://extility.flexiant.net}firewallProtocol" minOccurs="0"/>
 *         &lt;element name="remotePort" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "firewallRule", propOrder = {
    "action",
    "connState",
    "icmpParam",
    "ipAddress",
    "ipCIDRMask",
    "localPort",
    "name",
    "protocol",
    "remotePort"
})
public class FirewallRule {

    protected FirewallRuleAction action;
    protected FirewallConnectionState connState;
    protected IcmpParam icmpParam;
    protected String ipAddress;
    protected int ipCIDRMask;
    protected int localPort;
    protected String name;
    protected FirewallProtocol protocol;
    protected int remotePort;

    /**
     * Gets the value of the action property.
     * 
     * @return
     *     possible object is
     *     {@link FirewallRuleAction }
     *     
     */
    public FirewallRuleAction getAction() {
        return action;
    }

    /**
     * Sets the value of the action property.
     * 
     * @param value
     *     allowed object is
     *     {@link FirewallRuleAction }
     *     
     */
    public void setAction(FirewallRuleAction value) {
        this.action = value;
    }

    /**
     * Gets the value of the connState property.
     * 
     * @return
     *     possible object is
     *     {@link FirewallConnectionState }
     *     
     */
    public FirewallConnectionState getConnState() {
        return connState;
    }

    /**
     * Sets the value of the connState property.
     * 
     * @param value
     *     allowed object is
     *     {@link FirewallConnectionState }
     *     
     */
    public void setConnState(FirewallConnectionState value) {
        this.connState = value;
    }

    /**
     * Gets the value of the icmpParam property.
     * 
     * @return
     *     possible object is
     *     {@link IcmpParam }
     *     
     */
    public IcmpParam getIcmpParam() {
        return icmpParam;
    }

    /**
     * Sets the value of the icmpParam property.
     * 
     * @param value
     *     allowed object is
     *     {@link IcmpParam }
     *     
     */
    public void setIcmpParam(IcmpParam value) {
        this.icmpParam = value;
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
     * Gets the value of the ipCIDRMask property.
     * 
     */
    public int getIpCIDRMask() {
        return ipCIDRMask;
    }

    /**
     * Sets the value of the ipCIDRMask property.
     * 
     */
    public void setIpCIDRMask(int value) {
        this.ipCIDRMask = value;
    }

    /**
     * Gets the value of the localPort property.
     * 
     */
    public int getLocalPort() {
        return localPort;
    }

    /**
     * Sets the value of the localPort property.
     * 
     */
    public void setLocalPort(int value) {
        this.localPort = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the protocol property.
     * 
     * @return
     *     possible object is
     *     {@link FirewallProtocol }
     *     
     */
    public FirewallProtocol getProtocol() {
        return protocol;
    }

    /**
     * Sets the value of the protocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link FirewallProtocol }
     *     
     */
    public void setProtocol(FirewallProtocol value) {
        this.protocol = value;
    }

    /**
     * Gets the value of the remotePort property.
     * 
     */
    public int getRemotePort() {
        return remotePort;
    }

    /**
     * Sets the value of the remotePort property.
     * 
     */
    public void setRemotePort(int value) {
        this.remotePort = value;
    }

}
