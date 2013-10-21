
package net.flexiant.extility;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for deploymentTemplate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="deploymentTemplate">
 *   &lt;complexContent>
 *     &lt;extension base="{http://extility.flexiant.net}virtualResource">
 *       &lt;sequence>
 *         &lt;element name="server" type="{http://extility.flexiant.net}server" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="network" type="{http://extility.flexiant.net}network" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="vdc" type="{http://extility.flexiant.net}vdc" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="firewallTemplate" type="{http://extility.flexiant.net}firewallTemplate" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="firewall" type="{http://extility.flexiant.net}firewall" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="sshkey" type="{http://extility.flexiant.net}sshKey" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="disk" type="{http://extility.flexiant.net}disk" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="deploymentTemplateUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="publishedTo" type="{http://extility.flexiant.net}publishTo" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="userPermission" type="{http://extility.flexiant.net}templateProtectionPermission" minOccurs="0"/>
 *         &lt;element name="ownerPermission" type="{http://extility.flexiant.net}templateProtectionPermission" minOccurs="0"/>
 *         &lt;element name="questions" type="{http://extility.flexiant.net}question" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="customer" type="{http://extility.flexiant.net}customer" minOccurs="0"/>
 *         &lt;element name="billing" type="{http://extility.flexiant.net}billingEntity" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deploymentTemplate", propOrder = {
    "server",
    "network",
    "vdc",
    "firewallTemplate",
    "firewall",
    "sshkey",
    "disk",
    "deploymentTemplateUUID",
    "publishedTo",
    "userPermission",
    "ownerPermission",
    "questions",
    "customer",
    "billing"
})
@XmlSeeAlso({
    DeploymentInstance.class
})
public class DeploymentTemplate
    extends VirtualResource
{

    @XmlElement(nillable = true)
    protected List<Server> server;
    @XmlElement(nillable = true)
    protected List<Network> network;
    @XmlElement(nillable = true)
    protected List<Vdc> vdc;
    @XmlElement(nillable = true)
    protected List<FirewallTemplate> firewallTemplate;
    @XmlElement(nillable = true)
    protected List<Firewall> firewall;
    @XmlElement(nillable = true)
    protected List<SshKey> sshkey;
    @XmlElement(nillable = true)
    protected List<Disk> disk;
    protected String deploymentTemplateUUID;
    @XmlElement(nillable = true)
    protected List<PublishTo> publishedTo;
    protected TemplateProtectionPermission userPermission;
    protected TemplateProtectionPermission ownerPermission;
    @XmlElement(nillable = true)
    protected List<Question> questions;
    protected Customer customer;
    protected BillingEntity billing;

    /**
     * Gets the value of the server property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the server property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Server }
     * 
     * 
     */
    public List<Server> getServer() {
        if (server == null) {
            server = new ArrayList<Server>();
        }
        return this.server;
    }

    /**
     * Gets the value of the network property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the network property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNetwork().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Network }
     * 
     * 
     */
    public List<Network> getNetwork() {
        if (network == null) {
            network = new ArrayList<Network>();
        }
        return this.network;
    }

    /**
     * Gets the value of the vdc property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vdc property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVdc().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Vdc }
     * 
     * 
     */
    public List<Vdc> getVdc() {
        if (vdc == null) {
            vdc = new ArrayList<Vdc>();
        }
        return this.vdc;
    }

    /**
     * Gets the value of the firewallTemplate property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the firewallTemplate property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFirewallTemplate().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FirewallTemplate }
     * 
     * 
     */
    public List<FirewallTemplate> getFirewallTemplate() {
        if (firewallTemplate == null) {
            firewallTemplate = new ArrayList<FirewallTemplate>();
        }
        return this.firewallTemplate;
    }

    /**
     * Gets the value of the firewall property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the firewall property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFirewall().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Firewall }
     * 
     * 
     */
    public List<Firewall> getFirewall() {
        if (firewall == null) {
            firewall = new ArrayList<Firewall>();
        }
        return this.firewall;
    }

    /**
     * Gets the value of the sshkey property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sshkey property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSshkey().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SshKey }
     * 
     * 
     */
    public List<SshKey> getSshkey() {
        if (sshkey == null) {
            sshkey = new ArrayList<SshKey>();
        }
        return this.sshkey;
    }

    /**
     * Gets the value of the disk property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the disk property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDisk().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Disk }
     * 
     * 
     */
    public List<Disk> getDisk() {
        if (disk == null) {
            disk = new ArrayList<Disk>();
        }
        return this.disk;
    }

    /**
     * Gets the value of the deploymentTemplateUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeploymentTemplateUUID() {
        return deploymentTemplateUUID;
    }

    /**
     * Sets the value of the deploymentTemplateUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeploymentTemplateUUID(String value) {
        this.deploymentTemplateUUID = value;
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
     * Gets the value of the userPermission property.
     * 
     * @return
     *     possible object is
     *     {@link TemplateProtectionPermission }
     *     
     */
    public TemplateProtectionPermission getUserPermission() {
        return userPermission;
    }

    /**
     * Sets the value of the userPermission property.
     * 
     * @param value
     *     allowed object is
     *     {@link TemplateProtectionPermission }
     *     
     */
    public void setUserPermission(TemplateProtectionPermission value) {
        this.userPermission = value;
    }

    /**
     * Gets the value of the ownerPermission property.
     * 
     * @return
     *     possible object is
     *     {@link TemplateProtectionPermission }
     *     
     */
    public TemplateProtectionPermission getOwnerPermission() {
        return ownerPermission;
    }

    /**
     * Sets the value of the ownerPermission property.
     * 
     * @param value
     *     allowed object is
     *     {@link TemplateProtectionPermission }
     *     
     */
    public void setOwnerPermission(TemplateProtectionPermission value) {
        this.ownerPermission = value;
    }

    /**
     * Gets the value of the questions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the questions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQuestions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Question }
     * 
     * 
     */
    public List<Question> getQuestions() {
        if (questions == null) {
            questions = new ArrayList<Question>();
        }
        return this.questions;
    }

    /**
     * Gets the value of the customer property.
     * 
     * @return
     *     possible object is
     *     {@link Customer }
     *     
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Sets the value of the customer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Customer }
     *     
     */
    public void setCustomer(Customer value) {
        this.customer = value;
    }

    /**
     * Gets the value of the billing property.
     * 
     * @return
     *     possible object is
     *     {@link BillingEntity }
     *     
     */
    public BillingEntity getBilling() {
        return billing;
    }

    /**
     * Sets the value of the billing property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillingEntity }
     *     
     */
    public void setBilling(BillingEntity value) {
        this.billing = value;
    }

}
