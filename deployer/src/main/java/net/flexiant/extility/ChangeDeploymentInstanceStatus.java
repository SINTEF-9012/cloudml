
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for changeDeploymentInstanceStatus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="changeDeploymentInstanceStatus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="deploymentInstanceUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="newStatus" type="{http://extility.flexiant.net}deploymentInstanceStatus" minOccurs="0"/>
 *         &lt;element name="safe" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="runtimeMetadata" type="{http://extility.flexiant.net}resourceMetadata" minOccurs="0"/>
 *         &lt;element name="when" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "changeDeploymentInstanceStatus", propOrder = {
    "deploymentInstanceUUID",
    "newStatus",
    "safe",
    "runtimeMetadata",
    "when"
})
public class ChangeDeploymentInstanceStatus {

    protected String deploymentInstanceUUID;
    protected DeploymentInstanceStatus newStatus;
    protected boolean safe;
    protected ResourceMetadata runtimeMetadata;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar when;

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
     * Gets the value of the newStatus property.
     * 
     * @return
     *     possible object is
     *     {@link DeploymentInstanceStatus }
     *     
     */
    public DeploymentInstanceStatus getNewStatus() {
        return newStatus;
    }

    /**
     * Sets the value of the newStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeploymentInstanceStatus }
     *     
     */
    public void setNewStatus(DeploymentInstanceStatus value) {
        this.newStatus = value;
    }

    /**
     * Gets the value of the safe property.
     * 
     */
    public boolean isSafe() {
        return safe;
    }

    /**
     * Sets the value of the safe property.
     * 
     */
    public void setSafe(boolean value) {
        this.safe = value;
    }

    /**
     * Gets the value of the runtimeMetadata property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceMetadata }
     *     
     */
    public ResourceMetadata getRuntimeMetadata() {
        return runtimeMetadata;
    }

    /**
     * Sets the value of the runtimeMetadata property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceMetadata }
     *     
     */
    public void setRuntimeMetadata(ResourceMetadata value) {
        this.runtimeMetadata = value;
    }

    /**
     * Gets the value of the when property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getWhen() {
        return when;
    }

    /**
     * Sets the value of the when property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setWhen(XMLGregorianCalendar value) {
        this.when = value;
    }

}
