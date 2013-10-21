
package net.flexiant.extility;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for firewallTemplate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="firewallTemplate">
 *   &lt;complexContent>
 *     &lt;extension base="{http://extility.flexiant.net}virtualResource">
 *       &lt;sequence>
 *         &lt;element name="defaultInAction" type="{http://extility.flexiant.net}firewallRuleAction" minOccurs="0"/>
 *         &lt;element name="defaultOutAction" type="{http://extility.flexiant.net}firewallRuleAction" minOccurs="0"/>
 *         &lt;element name="firewallInRuleList" type="{http://extility.flexiant.net}firewallRule" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="firewallOutRuleList" type="{http://extility.flexiant.net}firewallRule" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="serviceNetworkUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://extility.flexiant.net}ipType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "firewallTemplate", propOrder = {
    "defaultInAction",
    "defaultOutAction",
    "firewallInRuleList",
    "firewallOutRuleList",
    "serviceNetworkUUID",
    "type"
})
public class FirewallTemplate
    extends VirtualResource
{

    protected FirewallRuleAction defaultInAction;
    protected FirewallRuleAction defaultOutAction;
    @XmlElement(nillable = true)
    protected List<FirewallRule> firewallInRuleList;
    @XmlElement(nillable = true)
    protected List<FirewallRule> firewallOutRuleList;
    protected String serviceNetworkUUID;
    protected IpType type;

    /**
     * Gets the value of the defaultInAction property.
     * 
     * @return
     *     possible object is
     *     {@link FirewallRuleAction }
     *     
     */
    public FirewallRuleAction getDefaultInAction() {
        return defaultInAction;
    }

    /**
     * Sets the value of the defaultInAction property.
     * 
     * @param value
     *     allowed object is
     *     {@link FirewallRuleAction }
     *     
     */
    public void setDefaultInAction(FirewallRuleAction value) {
        this.defaultInAction = value;
    }

    /**
     * Gets the value of the defaultOutAction property.
     * 
     * @return
     *     possible object is
     *     {@link FirewallRuleAction }
     *     
     */
    public FirewallRuleAction getDefaultOutAction() {
        return defaultOutAction;
    }

    /**
     * Sets the value of the defaultOutAction property.
     * 
     * @param value
     *     allowed object is
     *     {@link FirewallRuleAction }
     *     
     */
    public void setDefaultOutAction(FirewallRuleAction value) {
        this.defaultOutAction = value;
    }

    /**
     * Gets the value of the firewallInRuleList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the firewallInRuleList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFirewallInRuleList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FirewallRule }
     * 
     * 
     */
    public List<FirewallRule> getFirewallInRuleList() {
        if (firewallInRuleList == null) {
            firewallInRuleList = new ArrayList<FirewallRule>();
        }
        return this.firewallInRuleList;
    }

    /**
     * Gets the value of the firewallOutRuleList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the firewallOutRuleList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFirewallOutRuleList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FirewallRule }
     * 
     * 
     */
    public List<FirewallRule> getFirewallOutRuleList() {
        if (firewallOutRuleList == null) {
            firewallOutRuleList = new ArrayList<FirewallRule>();
        }
        return this.firewallOutRuleList;
    }

    /**
     * Gets the value of the serviceNetworkUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceNetworkUUID() {
        return serviceNetworkUUID;
    }

    /**
     * Sets the value of the serviceNetworkUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceNetworkUUID(String value) {
        this.serviceNetworkUUID = value;
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
