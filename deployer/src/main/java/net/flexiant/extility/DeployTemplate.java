
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for deployTemplate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="deployTemplate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="skeletonDeploymentInstance" type="{http://extility.flexiant.net}deploymentInstance" minOccurs="0"/>
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
@XmlType(name = "deployTemplate", propOrder = {
    "skeletonDeploymentInstance",
    "when"
})
public class DeployTemplate {

    protected DeploymentInstance skeletonDeploymentInstance;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar when;

    /**
     * Gets the value of the skeletonDeploymentInstance property.
     * 
     * @return
     *     possible object is
     *     {@link DeploymentInstance }
     *     
     */
    public DeploymentInstance getSkeletonDeploymentInstance() {
        return skeletonDeploymentInstance;
    }

    /**
     * Sets the value of the skeletonDeploymentInstance property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeploymentInstance }
     *     
     */
    public void setSkeletonDeploymentInstance(DeploymentInstance value) {
        this.skeletonDeploymentInstance = value;
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
