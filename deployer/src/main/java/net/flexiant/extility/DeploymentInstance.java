
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for deploymentInstance complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="deploymentInstance">
 *   &lt;complexContent>
 *     &lt;extension base="{http://extility.flexiant.net}deploymentTemplate">
 *       &lt;sequence>
 *         &lt;element name="status" type="{http://extility.flexiant.net}deploymentInstanceStatus" minOccurs="0"/>
 *         &lt;element name="deploymentTemplateName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="templateKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deploymentInstance", propOrder = {
    "status",
    "deploymentTemplateName",
    "templateKey"
})
public class DeploymentInstance
    extends DeploymentTemplate
{

    protected DeploymentInstanceStatus status;
    protected String deploymentTemplateName;
    protected String templateKey;

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link DeploymentInstanceStatus }
     *     
     */
    public DeploymentInstanceStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeploymentInstanceStatus }
     *     
     */
    public void setStatus(DeploymentInstanceStatus value) {
        this.status = value;
    }

    /**
     * Gets the value of the deploymentTemplateName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeploymentTemplateName() {
        return deploymentTemplateName;
    }

    /**
     * Sets the value of the deploymentTemplateName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeploymentTemplateName(String value) {
        this.deploymentTemplateName = value;
    }

    /**
     * Gets the value of the templateKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTemplateKey() {
        return templateKey;
    }

    /**
     * Sets the value of the templateKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTemplateKey(String value) {
        this.templateKey = value;
    }

}
