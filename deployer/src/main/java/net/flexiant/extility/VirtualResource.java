
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for virtualResource complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="virtualResource">
 *   &lt;complexContent>
 *     &lt;extension base="{http://extility.flexiant.net}resource">
 *       &lt;sequence>
 *         &lt;element name="billingEntityName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="clusterName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="clusterUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deploymentInstanceName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deploymentInstanceUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productOfferName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productOfferUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vdcName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vdcUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "virtualResource", propOrder = {
    "billingEntityName",
    "clusterName",
    "clusterUUID",
    "customerName",
    "customerUUID",
    "deploymentInstanceName",
    "deploymentInstanceUUID",
    "productOfferName",
    "productOfferUUID",
    "vdcName",
    "vdcUUID"
})
@XmlSeeAlso({
    Nic.class,
    DeploymentTemplate.class,
    Server.class,
    Image.class,
    Vdc.class,
    SshKey.class,
    Job.class,
    FirewallTemplate.class,
    Snapshot.class,
    Disk.class,
    Network.class,
    Subnet.class,
    Firewall.class,
    Group.class
})
public class VirtualResource
    extends Resource
{

    protected String billingEntityName;
    protected String clusterName;
    protected String clusterUUID;
    protected String customerName;
    protected String customerUUID;
    protected String deploymentInstanceName;
    protected String deploymentInstanceUUID;
    protected String productOfferName;
    protected String productOfferUUID;
    protected String vdcName;
    protected String vdcUUID;

    /**
     * Gets the value of the billingEntityName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingEntityName() {
        return billingEntityName;
    }

    /**
     * Sets the value of the billingEntityName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingEntityName(String value) {
        this.billingEntityName = value;
    }

    /**
     * Gets the value of the clusterName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClusterName() {
        return clusterName;
    }

    /**
     * Sets the value of the clusterName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClusterName(String value) {
        this.clusterName = value;
    }

    /**
     * Gets the value of the clusterUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClusterUUID() {
        return clusterUUID;
    }

    /**
     * Sets the value of the clusterUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClusterUUID(String value) {
        this.clusterUUID = value;
    }

    /**
     * Gets the value of the customerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the value of the customerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerName(String value) {
        this.customerName = value;
    }

    /**
     * Gets the value of the customerUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerUUID() {
        return customerUUID;
    }

    /**
     * Sets the value of the customerUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerUUID(String value) {
        this.customerUUID = value;
    }

    /**
     * Gets the value of the deploymentInstanceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeploymentInstanceName() {
        return deploymentInstanceName;
    }

    /**
     * Sets the value of the deploymentInstanceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeploymentInstanceName(String value) {
        this.deploymentInstanceName = value;
    }

    /**
     * Gets the value of the deploymentInstanceUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeploymentInstanceUUID() {
        return deploymentInstanceUUID;
    }

    /**
     * Sets the value of the deploymentInstanceUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeploymentInstanceUUID(String value) {
        this.deploymentInstanceUUID = value;
    }

    /**
     * Gets the value of the productOfferName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductOfferName() {
        return productOfferName;
    }

    /**
     * Sets the value of the productOfferName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductOfferName(String value) {
        this.productOfferName = value;
    }

    /**
     * Gets the value of the productOfferUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductOfferUUID() {
        return productOfferUUID;
    }

    /**
     * Sets the value of the productOfferUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductOfferUUID(String value) {
        this.productOfferUUID = value;
    }

    /**
     * Gets the value of the vdcName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVdcName() {
        return vdcName;
    }

    /**
     * Sets the value of the vdcName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVdcName(String value) {
        this.vdcName = value;
    }

    /**
     * Gets the value of the vdcUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVdcUUID() {
        return vdcUUID;
    }

    /**
     * Sets the value of the vdcUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVdcUUID(String value) {
        this.vdcUUID = value;
    }

}
