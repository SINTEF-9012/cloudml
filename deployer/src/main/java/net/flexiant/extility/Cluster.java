
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cluster complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cluster">
 *   &lt;complexContent>
 *     &lt;extension base="{http://extility.flexiant.net}resource">
 *       &lt;sequence>
 *         &lt;element name="systemCapabilities" type="{http://extility.flexiant.net}systemCapabilitySet" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cluster", propOrder = {
    "systemCapabilities"
})
public class Cluster
    extends Resource
{

    protected SystemCapabilitySet systemCapabilities;

    /**
     * Gets the value of the systemCapabilities property.
     * 
     * @return
     *     possible object is
     *     {@link SystemCapabilitySet }
     *     
     */
    public SystemCapabilitySet getSystemCapabilities() {
        return systemCapabilities;
    }

    /**
     * Sets the value of the systemCapabilities property.
     * 
     * @param value
     *     allowed object is
     *     {@link SystemCapabilitySet }
     *     
     */
    public void setSystemCapabilities(SystemCapabilitySet value) {
        this.systemCapabilities = value;
    }

}
